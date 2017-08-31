package bjdodo.ie_city_bus.controller;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.customquery.TripDetails;
import bjdodo.ie_city_bus.repository.CustomQueries;

@RestController
@RequestMapping("/trips/")
public class TripDetailController {
	@Autowired
	CustomQueries customQueries;

	@RequestMapping("")
	public List<TripDetails> get() {
		return customQueries.getActiveTripDetails();
	}

	@RequestMapping("{tripid}/stops/")
	public List<TripDetails> getStopsForTrip(@PathVariable String routeShortName,
			@QueryParam("direction") int direction) {
		return customQueries.getRouteTripDetails(routeShortName);
	}
}
