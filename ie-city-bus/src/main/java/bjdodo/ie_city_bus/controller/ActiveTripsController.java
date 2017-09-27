package bjdodo.ie_city_bus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.repository.ActiveTripRepository;

@RestController
@RequestMapping("/api/activetrip")
public class ActiveTripsController {

	@Autowired
	ActiveTripRepository activeTripsRepository;

	// http://localhost:8090/activetrips?tripIds=2,3
	@RequestMapping(value = "", method = RequestMethod.GET)
	public List<ActiveTrip> getTrip(@RequestParam(required = false) List<Long> tripIds) {
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

		if (tripId != null) {
			return activeTripsRepository.getActiveTripsById(new long[] { tripId });
		} else {
			return new ArrayList<>();
		}

	}

	@RequestMapping(value = "/{tripId}/details", method = RequestMethod.GET)
	public List<ActiveTrip> getTripDetails(@PathVariable(required = true) Long tripId) {

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