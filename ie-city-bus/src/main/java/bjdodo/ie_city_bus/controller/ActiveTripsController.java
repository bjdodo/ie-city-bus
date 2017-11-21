package bjdodo.ie_city_bus.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.controller.data.TripPassageData;
import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.model.TripPassage;
import bjdodo.ie_city_bus.model.crud.Vehicle;
import bjdodo.ie_city_bus.repository.ActiveTripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;
import bjdodo.ie_city_bus.utils.Tuple;
import bjdodo.ie_city_bus.utils.Utils;

@RestController
@RequestMapping("/api/activetrip")
public class ActiveTripsController {

	private static final Logger log = LoggerFactory.getLogger(ActiveTripsController.class);

	@Autowired
	ActiveTripRepository activeTripsRepository;

	@Autowired
	VehicleRepository vehicleRepository;

	// http://localhost:8090/activetrips?tripIds=2,3
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ActiveTrip> getTrip(@RequestParam(required = false) List<Long> tripIds) {
		log.info("ActiveTripsController.getTrip(list)");

		if (tripIds == null || tripIds.isEmpty()) {
			return activeTripsRepository.getActiveTrips();
		} else {
			long[] array = new long[tripIds.size()];
			for (int i = 0; i < tripIds.size(); i++) {
				array[i] = tripIds.get(i);
			}
			return activeTripsRepository.getActiveTripsById(array);
		}

		// URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().
		// path("/{idOfNewResource}").buildAndExpand(idOfNewResource).toUri();
	}

	@RequestMapping(value = "/{tripId}", method = RequestMethod.GET)
	public List<ActiveTrip> getTrip(@PathVariable(required = true) Long tripId) {

		log.info("ActiveTripsController.getTrip");

		if (tripId != null) {
			return activeTripsRepository.getActiveTripsById(new long[] { tripId });
		} else {
			return new ArrayList<>();
		}

	}


	@RequestMapping(value = "/{tripId}/passages", method = RequestMethod.GET)
	public List<TripPassageData> getTripPassages(@PathVariable(required = true) Long tripId) {

		log.info("ActiveTripsController.getTripPassages " + tripId);

		List<TripPassageData> ret = new ArrayList<>();

		List<TripPassage> passages = activeTripsRepository.getTripPassages(tripId);
		List<Vehicle> vehicles = vehicleRepository.findByCurrentTripId(tripId);
		Vehicle vehicle = vehicles.size() == 1 ? vehicles.get(0) : null;
		if (vehicle == null)
		{
			String vehicleIds = "";
			for (Vehicle v : vehicles)
			{
				vehicleIds += v.getDuid() + ",";
			}
			log.warn(String.format("These vehicles have the tripid %s in question: %s", tripId, vehicleIds));
		}

		for (TripPassage tripPassage : passages) {
			TripPassageData tpc = new TripPassageData(tripPassage);

			if (vehicle != null) {
				Tuple<Double, Double> vehicleLatLong = Utils.getPointFromDBPoint(vehicle.getLatLong());
				Tuple<Double, Double> stopLatLong = Utils.getPointFromDBPoint(tpc.getStopLatLong());

				tpc.setMetersFromVehicle((long) Utils.distFrom(
						vehicleLatLong.x, vehicleLatLong.y, stopLatLong.x, stopLatLong.y));
			}
			ret.add(tpc);
		}
		return ret;

	}


	// select stop_point.name, stop_point.lat_long,
	// stop_passage.scheduled_departure, stop_passage.actual_departure,
	// stop_passage.scheduled_arrival, stop_passage.actual_arrival from stop_passage
	// inner join stop_point on stop_point.id=stop_passage.stop_point_id
	// where trip_id=482
	// order by stop_passage.scheduled_arrival
}
