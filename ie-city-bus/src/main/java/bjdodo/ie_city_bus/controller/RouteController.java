package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.repository.ActiveTripRepository;
import bjdodo.ie_city_bus.repository.RouteRepository;

@RestController
@RequestMapping("/api/route")
public class RouteController {

	@Autowired
	ActiveTripRepository activeTripsRepository;

	@Autowired
	RouteRepository routeRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	List<Route> get() {
		return routeRepository.findAll();
	}

	@RequestMapping(value = "/{routeShortName}", method = RequestMethod.GET)
	List<Route> get(@PathVariable String routeShortName) {
		return routeRepository.findByShortName(routeShortName);
	}

	@RequestMapping(value = "/{routeShortName}/activetrips", method = RequestMethod.GET)
	public List<ActiveTrip> getTripsForRoute(@PathVariable String routeShortName) {
		return activeTripsRepository.getRouteActiveTrips(routeShortName);
	}

}
