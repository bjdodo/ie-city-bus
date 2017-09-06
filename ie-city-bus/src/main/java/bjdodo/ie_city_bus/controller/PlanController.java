package bjdodo.ie_city_bus.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.component.ScheduledTasks;
import bjdodo.ie_city_bus.model.customquery.CustomQueries;

@RestController
@RequestMapping("/plan")
public class PlanController {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	CustomQueries customQueries;

	// http://localhost:8090/plan/findtrips?originStop=Dublin%20Rd%20(Opp%20GMIT)&destinationStops=Eyre%20Square%20East%20(Stop%20No%208),Eyre%20Square%20East%20(Stop%20No%206),Eyre%20Square%20East%20(Stop%20No%205)
	@RequestMapping(value = "/findtrips", method = RequestMethod.GET)
	public List<Object> findTrips(@RequestParam(required = true) String originStop,
			@RequestParam(required = true) List<String> destinationStops) {
		log.info("findTrips: " + originStop + " | " + destinationStops.size());
		return customQueries.findTrips(originStop, destinationStops.toArray(new String[0]));
	}
}
