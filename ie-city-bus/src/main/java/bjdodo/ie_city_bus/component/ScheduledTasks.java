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

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.StopPassage;
import bjdodo.ie_city_bus.model.StopPoint;
import bjdodo.ie_city_bus.model.Trip;
import bjdodo.ie_city_bus.model.Vehicle;
import bjdodo.ie_city_bus.repository.RouteRepository;
import bjdodo.ie_city_bus.repository.StopPassageRepository;
import bjdodo.ie_city_bus.repository.StopPointRepository;
import bjdodo.ie_city_bus.repository.TripRepository;
import bjdodo.ie_city_bus.repository.VehicleRepository;
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

	@Scheduled(fixedRate = 60000)
	public void downloadSlowChangingData() {
		
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

			Vehicle vehicleInDb = null;
			try {
				vehicleInDb = vehiclesInDb.get(Vehicle.getJSONDuid(vehicle));
				if (vehicleInDb == null) {
					vehicleInDb = new Vehicle();
				}

				vehicleInDb.updateFromJson(vehicle);

				// We save the vehicle after we have downloaded the route info
				// because we do not want to save vehicles when they are assigned to
				// routes that we are not interesed in
			} catch (JSONException ex) {
				log.error("Saving vehicle to DB failed. JSON:\r\n" + vehicle.toString(), ex);
				continue;
			}

			if (vehicleInDb.getTripDuid() == null || vehicleInDb.getTripDuid().isEmpty()) {

				log.info("vehicle has no trips. Vehicle duid: " + vehicleInDb.getDuid());
				vehicleInDb.setTripId(null);
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
				log.warn("trip has no stops? tripduid: " + vehicleInDb.getTripDuid());
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
				log.info("route ignored " + routeInDb.getShortName());
				continue;
			}

			routeInDb = routeRepository.saveAndFlush(routeInDb);
			routesInDb.put(routeInDb.getDuid(), routeInDb);
			vehicleInDb = vehicleRepository.saveAndFlush(vehicleInDb);
			vehiclesInDb.put(vehicleInDb.getDuid(), vehicleInDb);

			// We need to save the trip early because the stop passage has a foreign key
			// constraint on it. We'll need to update firstStopPassageId, lastStopPassageId
			// and finished later after all the stop passages are saved.
			Trip tripInDb = unfinishedTripsInDb.get(vehicleInDb.getTripDuid());
			if (tripInDb == null) {
				tripInDb = new Trip();
			}
			tripInDb.setDuid(vehicleInDb.getTripDuid());
			tripInDb.setVehicleId(vehicleInDb.getId());
			tripInDb.setRouteId(routeInDb.getId());
			tripInDb = tripRepository.saveAndFlush(tripInDb);
			unfinishedTripsInDb.put(tripInDb.getDuid(), tripInDb);

			vehicleRepository.updateTripId(vehicleInDb.getId(), tripInDb.getId());

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
					stopPassageInDb.setRouteId(routeInDb.getId());
					stopPassageInDb = stopPassageRepository.saveAndFlush(stopPassageInDb);
					tripStopPassagesInDb.put(stopPassageInDb.getDuid(), stopPassageInDb);
				} catch (JSONException ex) {
					log.error("Saving stopPassage into DB failed. JSON:\r\n" + stopPassage.toString(), ex);
					continue;
				}
			}
		}

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
