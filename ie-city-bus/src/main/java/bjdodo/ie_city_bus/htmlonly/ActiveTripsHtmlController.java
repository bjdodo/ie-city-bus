package bjdodo.ie_city_bus.htmlonly;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.model.ActiveTrip;
import bjdodo.ie_city_bus.repository.ActiveTripRepository;
import bjdodo.ie_city_bus.utils.HtmlUtils;

@RestController
@RequestMapping("/htmlonly/")
public class ActiveTripsHtmlController {

	private static final Logger log = LoggerFactory.getLogger(ActiveTripsHtmlController.class);

	@Autowired
	private ActiveTripRepository activeTripsRepository;
	
	// http://localhost:8090/htmlonly/
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String get() {
		
		log.info("ActiveTripsHtmlController.get all");
		
		List<ActiveTrip> activeTrips = activeTripsRepository.getActiveTrips();
		return getFormattedHtml(activeTrips);
	}

	// http://localhost:8090/htmlonly/405
	@RequestMapping(value = "{route}", method = RequestMethod.GET)
	public String get(@PathVariable String route) {
		
		log.info("ActiveTripsHtmlController.get " + route);
		
		List<ActiveTrip> activeTrips = activeTripsRepository.getRouteActiveTrips(route);
		return getFormattedHtml(activeTrips);
	}

	
	private String getFormattedHtml(List<ActiveTrip> activeTrips) {
		
		return HtmlUtils.getActiveTripsHtml(activeTrips);
	}
}