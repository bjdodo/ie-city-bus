package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.customquery.TripDetails;
import bjdodo.ie_city_bus.repository.CustomQueries;
import bjdodo.ie_city_bus.repository.RouteRepository;

@RestController
public class RouteController {

	@Autowired
	CustomQueries customQueries;

	@Autowired
	RouteRepository routeRepository;

	@RequestMapping("/routes/")
	List<Route> get() {
		return routeRepository.findAll();
	}

	@RequestMapping("/routes/{routeShortName}/")
	List<Route> get(@PathVariable String routeShortName) {
		return routeRepository.findByShortName(routeShortName);
	}

	@RequestMapping("/routes/{routeShortName}/trips/")
	public List<TripDetails> getTripsForRoute(@PathVariable String routeShortName) {
		return customQueries.getRouteTripDetails(routeShortName);
	}
}
