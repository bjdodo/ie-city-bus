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

import bjdodo.ie_city_bus.controller.data.StopPassageDetail;
import bjdodo.ie_city_bus.controller.data.StopPassageHistoryByStop;
import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.model.TripDetailStopPassage;
import bjdodo.ie_city_bus.model.crud.Vehicle;
import bjdodo.ie_city_bus.repository.ActiveTripRepository;
import bjdodo.ie_city_bus.repository.crud.TripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;
import bjdodo.ie_city_bus.service.TrafficService;
import bjdodo.ie_city_bus.utils.Pair;
import bjdodo.ie_city_bus.utils.Utils;

@RestController
@RequestMapping("/api/activetrip")
public class ActiveTripsController {

	private static final Logger log = LoggerFactory.getLogger(ActiveTripsController.class);

	@Autowired
	ActiveTripRepository activeTripsRepository;
	
	@Autowired
	TripRepository tripRepository;

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


	
	


	// select stop_point.name, stop_point.lat_long,
	// stop_passage.scheduled_departure, stop_passage.actual_departure,
	// stop_passage.scheduled_arrival, stop_passage.actual_arrival from stop_passage
	// inner join stop_point on stop_point.id=stop_passage.stop_point_id
	// where trip_id=482
	// order by stop_passage.scheduled_arrival
}
