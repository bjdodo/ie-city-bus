package bjdodo.ie_city_bus.htmlonly;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

@RestController
@RequestMapping("/htmlonly")
public class ActiveTripsHtmlController {

	private static final Logger log = LoggerFactory.getLogger(ActiveTripsHtmlController.class);

	@Autowired
	private ActiveTripRepository activeTripsRepository;
	
	// http://localhost:8090/htmlonly
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String get() {
		
		log.info("ActiveTripsHtmlController.get all");
		
		List<ActiveTrip> activeTrips = activeTripsRepository.getActiveTrips();
		return getFormattedHtml(activeTrips);
	}

	// http://localhost:8090/htmlonly?route=405 - with request param
	// http://localhost:8090/htmlonly/405
	@RequestMapping(value = "/{route}", method = RequestMethod.GET)
	public String get(@PathVariable String route) {
		
		log.info("ActiveTripsHtmlController.get " + route);
		
		List<ActiveTrip> activeTrips = activeTripsRepository.getRouteActiveTrips(route);
		return getFormattedHtml(activeTrips);
	}

	private String getFormattedHtml(List<ActiveTrip> activeTrips) {
		
		DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
	            .withZone(ZoneId.systemDefault());
		DateTimeFormatter dtFormatterShort = DateTimeFormatter.ofPattern("HH:mm")
	            .withZone(ZoneId.systemDefault());

		StringBuilder sb = new StringBuilder();
		activeTrips.forEach(trip -> {
				
			sb.append("<a href=\"/htmlonly/trip/" + trip.getTripId() + "\">" + trip.getRouteShortName() + "</a> ");
			sb.append("<b>" + dtFormatterShort.format(trip.getScheduledStart()) + "</b><br>");
			sb.append("From: " + trip.getOriginStopName() + " @ " + dtFormatter.format(trip.getActualStart()) +  "<br>");
			sb.append("To: " + trip.getDestinationStopName() + ", expected: @ " + dtFormatter.format(trip.getActualFinish()) + "<br>");

			sb.append("Near: " + trip.getNearestStopPointName() + "<br>");
			long delaySeconds = trip.getActualFinish().getEpochSecond() - trip.getScheduledFinish().getEpochSecond();
			long delayMin = Math.floorDiv(delaySeconds, 60);
			sb.append("Delay: " + delayMin + " min<br>");

			sb.append("<br>");
		});
		
		return "<html>" + sb.toString() + "</html>";
	}
	
	private String getFormattedTable(List<ActiveTrip> activeTrips) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<tr>");
		sb.append("<th>" + "Route" + "</th>");
		sb.append("<th>" + "Origin" + "</th>");
		sb.append("<th>" + "Nearest Stop" + "</th>");
		sb.append("<th>" + "Direction" + "</th>");
		sb.append("<th>" + "Destination" + "</th>");
		sb.append("<th>" + "Sch. Start" + "</th>");
		sb.append("<th>" + "Actual Start" + "</th>");
		sb.append("</tr>");
		
		activeTrips.forEach(trip -> {
				sb.append("<tr>");
				
				sb.append("<td>" + trip.getRouteShortName() + "</td>");
				sb.append("<td>" + trip.getOriginStopName() + "</td>");
				sb.append("<td>" + trip.getNearestStopPointName() + "</td>");
				sb.append("<td>" + trip.getTripDirection() + "</td>");
				sb.append("<td>" + trip.getDestinationStopName() + "</td>");
				sb.append("<td>" + trip.getScheduledStart() + "</td>");
				sb.append("<td>" + trip.getActualStart() + "</td>");
				
				sb.append("</tr>");
				
			});
		
		return "<html><table>" + sb.toString() + "</table></html>";
	}
}