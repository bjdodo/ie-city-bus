package bjdodo.ie_city_bus.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import bjdodo.ie_city_bus.utils.Pair;
import bjdodo.ie_city_bus.utils.Utils;

@Service
public class DataSavingServiceImpl implements DataSavingService {

	private static final Logger log = LoggerFactory.getLogger(DataSavingServiceImpl.class);

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	private DataDownloaderService dataDownloaderService;

	@Autowired
	private RouteRepository routeRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private StopPointRepository stopPointRepository;

	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private StopPassageRepository stopPassageRepository;

	@Autowired
	private CustomDBStatementCalls customDBStatementCalls;

	@Autowired
	private TrafficService trafficService;

	public void downloadAndSaveAll() {
		log.info("ScheduledTasks downloadData() starting...");

		dataDownloaderService.startDownloadBatch();

		List<String> monitoredRoutesLst = configurationService.getMonitoredRoutes();

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

		log.info("ScheduledTasks downloadData() download finished");

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
				// We only save the vehicle after we have downloaded the trip info
				// because we do not want to save vehicles when they are assigned to
				// trips that we are not interested in
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
				log.info("Downloading stop passages for trip " + vehicleInDb.getTripDuid());
				stopPassages = dataDownloaderService.downloadStopPassages(vehicleInDb.getTripDuid());
				log.info("Downloading stop passages done");
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

			// Sometimes stop_passages simply disappear in the downloaded data
			// I am assuming it means that those are deleted so we set those to deleted here
			cleanupStopPassagesInDb(stopPassages, tripStopPassagesInDb);

			Pair<Double, Double> vehicleLocation = Utils.getPointFromDBPoint(vehicleInDb.getLatLong());
			StopPoint nearestStopPoint = null;
			Double nearestStopPointDistance = null;

			// for diagnostics, trying to catch a bug
			// List<Long> stopPassageIdUpdatedFromJson = new ArrayList<>();

			for (JSONObject stopPassage : stopPassages.values()) {

				JSONObject stopPoint = null;
				StopPoint stopPointInDb = null;
				try {
					String stopPointDuid = StopPassage.getJSONStopPointDuid(stopPassage);
					stopPoint = stopPoints.get(stopPointDuid);
					stopPointInDb = stopPointsInDb.get(stopPointDuid);

					if (stopPoint == null && stopPointInDb == null) {
						log.error("Stop point not known for stop passage " + stopPassage.toString());
					} else {

						if (stopPointInDb == null && stopPoint != null) {
							stopPointInDb = new StopPoint();
						}
						if (stopPoint != null) {
							stopPointInDb.updateFromJson(stopPoint);
							stopPointInDb = stopPointRepository.saveAndFlush(stopPointInDb);
							stopPointsInDb.put(stopPointInDb.getDuid(), stopPointInDb);
						} else {
							log.error("Stop point info not provided by buseireann for stop passage "
									+ stopPassage.toString());
						}
						// get the nearest bus stop
						Pair<Double, Double> stopLocation = Utils.getPointFromDBPoint(stopPointInDb.getLatLong());
						double stopDistance = Utils.distFrom(stopLocation.x, stopLocation.y, vehicleLocation.x,
								vehicleLocation.y);
						if (nearestStopPointDistance == null || nearestStopPointDistance > stopDistance) {
							nearestStopPoint = stopPointInDb;
							nearestStopPointDistance = stopDistance;
						}
					}

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
					stopPassageInDb.setActualEstimated(
							(stopPassageInDb.getActualDeparture() == null ? stopPassageInDb.getActualArrival()
									: stopPassageInDb.getActualDeparture()).isAfter(Instant.now()));

					stopPassageInDb.setTripId(tripInDb.getId());
					stopPassageInDb.setStopPointId(stopPointInDb == null ? null : stopPointInDb.getId());

					// // In theory these should match. In practice they do not.
					// if (!vehicles.containsKey(stopPassageInDb.getVehicleDuid())) {
					// log.warn(String.format(
					// "Skipping stop passage with duid %s, no matching vehicle for vehicle duid",
					// stopPassageInDb.getDuid()));
					// continue;
					// }

					stopPassageInDb.setId(stopPassageRepository.saveAndFlush(stopPassageInDb).getId());
					tripStopPassagesInDb.put(stopPassageInDb.getDuid(), stopPassageInDb);

					if (tripStopPassagesInDb.get(stopPassageInDb.getDuid()).getStopPointDuid() == null) {
						log.error("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
					}

					if (stopPassageInDb.getScheduledDeparture() == null) {
						if (stopPointInDb != null) {
							tripInDb.setDestinationStopName(stopPointInDb.getName());
						} else {
							// Yes this appears to be vice-versa. After some tests this seems (almost)
							// correct.
							tripInDb.setOriginStopName(
									StopPassage.getArrivalMultiLingualDirectionText(stopPassage));
						}

						tripInDb.setActualFinish(stopPassageInDb.getActualArrival());
						tripInDb.setScheduledFinish(stopPassageInDb.getScheduledArrival());
					}
					if (stopPassageInDb.getScheduledArrival() == null) {
						if (stopPointInDb != null) {
							tripInDb.setOriginStopName(stopPointInDb.getName());
						} else {
							// Yes this appears to be vice-versa. After some tests this seems (almost)
							// correct.
							tripInDb.setDestinationStopName(
									StopPassage.getDepartureMultiLingualDirectionText(stopPassage));
						}

						tripInDb.setActualStart(stopPassageInDb.getActualDeparture());
						tripInDb.setScheduledStart(stopPassageInDb.getScheduledDeparture());
					}

				} catch (JSONException ex) {
					log.error("Saving stopPassage into DB failed. JSON:\r\n" + stopPassage.toString(), ex);
					continue;
				}
			}

			if (nearestStopPointDistance != null) {
				tripInDb.setNearestStopPointDistance(nearestStopPointDistance);
				tripInDb.setNearestStopPointId(nearestStopPoint.getId());
			}

			tripInDb = tripRepository.saveAndFlush(tripInDb);
			unfinishedTripsInDb.put(tripInDb.getDuid(), tripInDb);

			trafficServiceRecordTripData(routeInDb, tripInDb, stopPointsInDb,
					new ArrayList<StopPassage>(tripStopPassagesInDb.values()));
		}

		customDBStatementCalls.setStaleVehiclesDeleted();
		tripRepository.closeFinishedTrips();
		stopPassageRepository.closeStopPassagesOfFinishedTrips();
		customDBStatementCalls.deleteOldTrips(configurationService.getMaxTripAgeDays() * 24 * 60 * 60);
		trafficService.cleanupOld();

		log.info("ScheduledTasks downloadData() saving data finished");
	}

	private void cleanupStopPassagesInDb(Map<String, JSONObject> stopPassages,
			Map<String, StopPassage> tripStopPassagesInDb) {

		List<StopPassage> toRemove = new ArrayList<>();
		for (StopPassage sp : tripStopPassagesInDb.values()) {
			if (!stopPassages.containsKey(sp.getDuid())) {

				toRemove.add(sp);
			}
		}

		for (StopPassage sp : toRemove) {
			sp.setDeleted(true);
			stopPassageRepository.saveAndFlush(sp);
			tripStopPassagesInDb.remove(sp.getDuid());
		}
	}

	private void trafficServiceRecordTripData(Route route, Trip trip, Map<String, StopPoint> stopPoints,
			List<StopPassage> stopPassages) {

		if (route == null || trip == null || stopPoints == null || stopPassages == null) {
			log.warn("predictionServiceRecordTripData() called with a null argument");
			return;
		}
		if (stopPassages.isEmpty()) {
			log.warn("predictionServiceRecordTripData() called with empty stopPassages");
			return;
		}

		stopPassages.sort(
				(o1, o2) -> (o1.getScheduledArrival() == null ? o1.getScheduledDeparture() : o1.getScheduledArrival())
						.compareTo(o2.getScheduledArrival() == null ? o2.getScheduledDeparture()
								: o2.getScheduledArrival()));

		List<StopPassage> missedStopPointPassages = new ArrayList<>();
		StopPoint stopPoint1 = null;
		StopPoint stopPoint2 = stopPoints.get(stopPassages.get(0).getStopPointDuid());
		for (int idx = 0; idx < stopPassages.size() - 1; idx++) {

			stopPoint1 = stopPoint2;
			stopPoint2 = stopPoints.get(stopPassages.get(idx + 1).getStopPointDuid());

			if (stopPoint1 == null) {
				missedStopPointPassages.add(stopPassages.get(idx));
				continue;
			}

			if (stopPoint2 == null) {
				missedStopPointPassages.add(stopPassages.get(idx));
				continue;
			}

			if (stopPassages.get(idx).isActualEstimated() || stopPassages.get(idx + 1).isActualEstimated()) {
				continue;
			}
			Instant time1 = stopPassages.get(idx).getActualDeparture() == null
					? stopPassages.get(idx).getActualArrival()
					: stopPassages.get(idx).getActualDeparture();
			Instant time2 = stopPassages.get(idx + 1).getActualDeparture() == null
					? stopPassages.get(idx + 1).getActualArrival()
					: stopPassages.get(idx + 1).getActualDeparture();

			if (time1.isBefore(Instant.now().minusSeconds(2 * 60 * 60)) || time1.isAfter(Instant.now())
					|| time2.isBefore(Instant.now().minusSeconds(2 * 60 * 60)) || time2.isAfter(Instant.now())) {
				continue;
			}
			trafficService.recordRecentSectionPassage(route.getShortName(), trip.getId(), trip.getScheduledStart(),
					stopPoint1.getNumber(), time1, stopPoint2.getNumber(), time2);

		}

		if (!missedStopPointPassages.isEmpty()) {
			log.warn(String.format(
					"predictionServiceRecordTripData() missed %s points out of %s for trip [duid %s id %s] route [shortname %s]",
					missedStopPointPassages.size(), stopPassages.size(), trip.getDuid(), trip.getId(),
					route.getShortName()));

			for (StopPassage sp : missedStopPointPassages) {

				if (stopPoints.values() == null) {
					log.warn(sp.getDuid() + " stopPoints.values() is null");
				} else if (sp.getStopPointId() == null) {
					log.warn(sp.getDuid() + " sp.getStopPointId() is null");
				} else {
					log.warn(sp.getDuid() + " "
							+ stopPoints.values().stream().filter(spnt -> spnt.getId() == sp.getStopPointId())
									.collect(Collectors.toList()).get(0).getName());
				}

			}

		}
	}
}
