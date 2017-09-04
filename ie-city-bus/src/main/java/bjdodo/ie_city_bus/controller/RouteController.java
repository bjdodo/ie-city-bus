package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.customquery.ActiveTrip;
import bjdodo.ie_city_bus.model.customquery.ActiveTripRepository;
import bjdodo.ie_city_bus.repository.RouteRepository;

@RestController
@RequestMapping("/routes")
public class RouteController {

	@Autowired
	ActiveTripRepository activeTripsRepository;

	@Autowired
	RouteRepository routeRepository;

	@RequestMapping("")
	List<Route> get() {
		return routeRepository.findAll();
	}

	@RequestMapping("/{routeShortName}")
	List<Route> get(@PathVariable String routeShortName) {
		return routeRepository.findByShortName(routeShortName);
	}

	@RequestMapping("/{routeShortName}/activetrips")
	public List<ActiveTrip> getTripsForRoute(@PathVariable String routeShortName) {
		return activeTripsRepository.getRouteActiveTrips(routeShortName);
	}
}
