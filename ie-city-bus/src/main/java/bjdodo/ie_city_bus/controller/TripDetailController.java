package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.customquery.TripDetails;
import bjdodo.ie_city_bus.repository.CustomQueries;

@RestController
public class TripDetailController {
	@Autowired
	CustomQueries customQueries;

	@RequestMapping("/activetripdetails/")
	public List<TripDetails> get() {
		return customQueries.getActiveTripDetails();
	}

	@RequestMapping("/routes/{routeShortName}/activetripdetails/")
	public List<TripDetails> getForRoute(@PathVariable String routeShortName) {
		return customQueries.getActiveTripDetails(routeShortName);
	}
}
