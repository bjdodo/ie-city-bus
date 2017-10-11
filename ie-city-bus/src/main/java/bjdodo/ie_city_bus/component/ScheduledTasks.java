package bjdodo.ie_city_bus.component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bjdodo.ie_city_bus.model.crud.Route;
import bjdodo.ie_city_bus.model.crud.StopPassage;
import bjdodo.ie_city_bus.model.crud.StopPoint;
import bjdodo.ie_city_bus.model.crud.Trip;
import bjdodo.ie_city_bus.model.crud.Vehicle;
import bjdodo.ie_city_bus.repository.CustomDBStatementCalls;
import bjdodo.ie_city_bus.repository.crud.RouteRepository;
import bjdodo.ie_city_bus.repository.crud.StopPassageRepository;
import bjdodo.ie_city_bus.repository.crud.StopPointRepository;
import bjdodo.ie_city_bus.repository.crud.TripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;
import bjdodo.ie_city_bus.service.DataDownloaderService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	// This is injected from the config file
	@Value("${ie_city_bus.monitoredroutes}")
	private String monitoredRoutes;

	@Autowired
	DataDownloaderService dataDownloaderService;

	@Autowired
	RouteRepository routeRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	@Autowired
	StopPointRepository stopPointRepository;

	@Autowired
	TripRepository tripRepository;

	@Autowired
	StopPassageRepository stopPassageRepository;

	@Autowired
	CustomDBStatementCalls customDBStatementCalls;

	@Scheduled(fixedRate = 60000)
	public void downloadData() {
		
		log.info("ScheduledTasks downloadData() starting...");

		dataDownloaderService.startDownloadBatch();

		List<String> monitoredRoutesLst = Arrays.asList(monitoredRoutes.split(","));
		
		Map<String, Route> routesInDb = new HashMap<>();
		routeRepository.findAll().stream().forEach(item -> routesInDb.put(item.getDuid(), item));

		Map<String, StopPoint> stopPointsInDb = new HashMap<>();
		stopPointRepository.findAll().stream().forEach(item -> stopPointsInDb.put(item.getDuid(), item));

		Map<String, Vehicle> vehiclesInDb = new HashMap<>();
		vehicleRepository.findAll().stream().forEach(item -> vehiclesInDb.put(item.getDuid(), item));

		Map<String, Trip> unfinishedTripsInDb = new HashMap<>();
		tripRepository.getUnfinishedTrips().stream().forEach(item -> unfinishedTripsInDb.put(item.getDuid(), item));

		Map<String, JSONObject> routes = null;
		Map<String, JSONObject> stopPoints = null;
		Map<String, JSONObject> vehicles = null;
		try {
			routes = dataDownloaderService.downloadRoutes();
			stopPoints = dataDownloaderService.downloadStopPoints();
			vehicles = dataDownloaderService.downloadVehicles();
		} catch (JSONException ex) {
			log.error("Downloading data failed", ex);
			return;
		}

		for (JSONObject vehicle : vehicles.values()) {

			boolean vehicleIsNew = false;
			Vehicle vehicleInDb = null;
			try {
				vehicleInDb = vehiclesInDb.get(Vehicle.getJSONDuid(vehicle));
				if (vehicleInDb == null) {
					vehicleIsNew = true;
					vehicleInDb = new Vehicle();
				}

				vehicleInDb.updateFromJson(vehicle);

				// We save the vehicle after we have downloaded the route info
				// because we do not want to save vehicles when they are assigned to
				// routes that we are not interested in
			} catch (JSONException ex) {
				log.error("Saving vehicle to DB failed. JSON:\r\n" + vehicle.toString(), ex);
				continue;
			}

			if (vehicleInDb.getTripDuid() == null || vehicleInDb.getTripDuid().isEmpty()) {

				log.debug("vehicle has no trips. Vehicle duid: " + vehicleInDb.getDuid());
				vehicleInDb.setCurrentTripId(null);
				vehicleInDb = vehicleRepository.saveAndFlush(vehicleInDb);
				vehiclesInDb.put(vehicleInDb.getDuid(), vehicleInDb);
				continue;
			}

			Map<String, JSONObject> stopPassages = null;
			try {
				stopPassages = dataDownloaderService.downloadStopPassages(vehicleInDb.getTripDuid());
			} catch (JSONException ex) {
				log.error("Downloading stop passages failed", ex);
				continue;
			}
			if (stopPassages.isEmpty()) {
				log.debug("trip has no stops? tripduid: " + vehicleInDb.getTripDuid());
				continue;
			}

			JSONObject route = null;
			Route routeInDb = null;
			try {
				String routeDuid = StopPassage.getJSONRouteDuid(stopPassages.values().stream().findFirst().get());
				route = routes.get(routeDuid);
				routeInDb = routesInDb.get(routeDuid);
				if (routeInDb == null) {
					routeInDb = new Route();
				}
				routeInDb.updateFromJson(route);

			} catch (JSONException ex) {
				log.error("Saving route into DB failed. JSON:\r\n" + route.toString(), ex);
				continue;
			}

			if (!monitoredRoutesLst.contains(routeInDb.getShortName())) {
				log.debug("route ignored " + routeInDb.getShortName());
				if (!vehicleIsNew) {
					// If a vehicle is now assigned to a route that we ignore we need to finish the
					// current trip of the vehicle.
					// So we'll set the tripid for the vehicle null.
					vehicleInDb.setTripDuid(null);
					vehicleInDb.setCurrentTripId(null);
					vehicleRepository.saveAndFlush(vehicleInDb);
				}
				continue;
			}

			routeInDb = routeRepository.saveAndFlush(routeInDb);
			routesInDb.put(routeInDb.getDuid(), routeInDb);
			vehicleInDb = vehicleRepository.save(vehicleInDb);
			vehiclesInDb.put(vehicleInDb.getDuid(), vehicleInDb);

			// We need to save the trip early because the stop passage has a foreign key
			// constraint on it. We'll need to update firstStopPassageId, lastStopPassageId
			// and finished later after all the stop passages are saved.
			Trip tripInDb = unfinishedTripsInDb.get(vehicleInDb.getTripDuid());
			if (tripInDb == null) {
				// This is just to be on the safe side. If a trip is closed prematurely and
				// something new is logged against it then we would crash. The trip would not be
				// in the unfinished list so we'd create a new instance with the same duid and
				// then the unique constraint would complain. This extra check is a bit slower
				// but a lot safer.
				tripInDb = tripRepository.findByDuid(vehicleInDb.getTripDuid());
				if (tripInDb == null) {
					tripInDb = new Trip();
				} else {
					tripInDb.setFinished(0);
				}

			}
			tripInDb.setDuid(vehicleInDb.getTripDuid());
			tripInDb.setVehicleId(vehicleInDb.getId());
			tripInDb.setRouteId(routeInDb.getId());
			try {
				tripInDb.setDirection(StopPassage.getJSONDirection(stopPassages.values().stream().findFirst().get()));
			} catch (JSONException ex) {
				log.warn("Could not set direction on trip", ex);
				// we carry on
			}
			try {
			tripInDb = tripRepository.save(tripInDb);
			} catch (Exception ex) {
				log.error("failed to save trip: " + tripInDb);
				throw ex;
			}
			unfinishedTripsInDb.put(tripInDb.getDuid(), tripInDb);

			vehicleInDb.setCurrentTripId(tripInDb.getId());
			vehicleInDb = vehicleRepository.saveAndFlush(vehicleInDb);
			vehiclesInDb.put(vehicleInDb.getDuid(), vehicleInDb);

			Map<String, StopPassage> tripStopPassagesInDb = new HashMap<>();
			stopPassageRepository.findByTripId(tripInDb.getId()).stream()
					.forEach(item -> tripStopPassagesInDb.put(item.getDuid(), item));

			for (JSONObject stopPassage : stopPassages.values()) {

				JSONObject stopPoint = null;
				StopPoint stopPointInDb = null;
				try {
					stopPoint = stopPoints.get(StopPassage.getJSONStopPointDuid(stopPassage));
					stopPointInDb = stopPointsInDb.get(StopPassage.getJSONStopPointDuid(stopPassage));
					if (stopPointInDb == null) {
						stopPointInDb = new StopPoint();
					}
					stopPointInDb.updateFromJson(stopPoint);
					stopPointInDb = stopPointRepository.saveAndFlush(stopPointInDb);
					stopPointsInDb.put(stopPointInDb.getDuid(), stopPointInDb);
				} catch (JSONException ex) {
					log.error("Saving stopPoint into DB failed. JSON:\r\n" + stopPoint.toString(), ex);
					continue;
				}

				StopPassage stopPassageInDb = null;
				try {
					stopPassageInDb = tripStopPassagesInDb.get(StopPassage.getJSONDuid(stopPassage));
					if (stopPassageInDb == null) {
						stopPassageInDb = new StopPassage();
					}
					stopPassageInDb.updateFromJson(stopPassage);
					stopPassageInDb.setTripId(tripInDb.getId());
					stopPassageInDb.setStopPointId(stopPointInDb.getId());
					stopPassageInDb = stopPassageRepository.saveAndFlush(stopPassageInDb);
					tripStopPassagesInDb.put(stopPassageInDb.getDuid(), stopPassageInDb);

					if (stopPassageInDb.getScheduledDeparture() == null && stopPoint != null) {
						tripInDb.setDestinationStopName(stopPointInDb.getName());
					}
					if (stopPassageInDb.getScheduledDeparture() == null) {
						tripInDb.setActualFinish(stopPassageInDb.getActualArrival());
						tripInDb.setScheduledFinish(stopPassageInDb.getScheduledArrival());
					}
					if (stopPassageInDb.getScheduledArrival() == null && stopPoint != null) {
						tripInDb.setOriginStopName(stopPointInDb.getName());
					}
					if (stopPassageInDb.getScheduledArrival() == null) {
						tripInDb.setActualStart(stopPassageInDb.getActualDeparture());
						tripInDb.setScheduledStart(stopPassageInDb.getScheduledDeparture());
					}

				} catch (JSONException ex) {
					log.error("Saving stopPassage into DB failed. JSON:\r\n" + stopPassage.toString(), ex);
					continue;
				}
			}
			tripInDb = tripRepository.saveAndFlush(tripInDb);
			unfinishedTripsInDb.put(tripInDb.getDuid(), tripInDb);
		}

		customDBStatementCalls.setStaleVehiclesDeleted();
		tripRepository.closeFinishedTrips();
	}


	// Trip setupTripInDb(Map<String, Trip> unfinishedTripsInDb, Vehicle vehicleDb,
	// Route routeDb,
	// Map<String, StopPassage> stopPassages) {
	// Trip trip = unfinishedTripsInDb.get(vehicleDb.getTripDuid());
	// if (trip == null) {
	// trip = new Trip();
	// trip.setDuid(vehicleDb.getTripDuid());
	// trip.setFinish(null);
	//
	// trip.setStart(stopPassages.values().stream().map(StopPassage::getDepartureData).min(Date::compareTo).get());
	// trip.setVehicleId(vehicleDb.getId());
	// trip.setRouteId(routeDb.getId());
	// }
	//
	// // The very first stop would have no arrival date hence we check for 1 or 0
	// if (stopPassages.values().stream().filter(x -> x.getArrivalData() ==
	// null).count() <= 1) {
	// trip.setFinish(stopPassages.values().stream().map(StopPassage::getArrivalData).max(Date::compareTo).get());
	// }
	//
	// return trip;
	// }

	// Trip getTripInDb(Map<String, Trip> unfinishedTripsInDb, Vehicle vehicleDb,
	// Map<String, StopPassage> stopPassages) {
	// Trip trip = unfinishedTripsInDb.get(vehicleDb.getTripDuid());
	// if (trip == null) {
	// trip = new Trip();
	// trip.setDuid(vehicleDb.getTripDuid());
	// trip.setFinish(null);
	//
	// trip.setStart(stopPassages.values().stream().map(StopPassage::getDepartureData).min(Date::compareTo).get());
	// trip.setVehicleId(vehicleDb.getId());
	//
	//
	// }
	//
	// // The very first stop would have no arrival date hence we check for 1 or 0
	// if (stopPassages.values().stream().filter(x -> x.getArrivalData() ==
	// null).count() <= 1) {
	// trip.setFinish(stopPassages.values().stream().map(StopPassage::getArrivalData).max(Date::compareTo).get());
	// }
	//
	// return trip;
	// }
}
