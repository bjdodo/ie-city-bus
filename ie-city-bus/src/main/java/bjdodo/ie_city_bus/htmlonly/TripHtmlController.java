package bjdodo.ie_city_bus.htmlonly;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.controller.data.StopPassageDetail;
import bjdodo.ie_city_bus.model.TripDetailStopPassage;
import bjdodo.ie_city_bus.model.crud.Vehicle;
import bjdodo.ie_city_bus.repository.crud.TripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;
import bjdodo.ie_city_bus.utils.HtmlUtils;
import bjdodo.ie_city_bus.utils.Pair;
import bjdodo.ie_city_bus.utils.Utils;

@RestController
@RequestMapping("/htmlonly/trip")
public class TripHtmlController {

	private static final Logger log = LoggerFactory.getLogger(TripHtmlController.class);
	
	@Autowired
	private TripRepository tripRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@RequestMapping(value = "/{tripId}", method = RequestMethod.GET)
	public String getTripPassages(@PathVariable Long tripId) {

		log.info("TripHtmlController.getTripPassages " + tripId);

		List<StopPassageDetail> stopPassageDetails = new ArrayList<>();

		List<TripDetailStopPassage> passages = tripRepository.getTripPassages(tripId);
		List<Vehicle> vehicles = vehicleRepository.findByCurrentTripId(tripId);
		Vehicle vehicle = vehicles.size() == 1 ? vehicles.get(0) : null;
		if (vehicle == null)
		{
			String vehicleIds = "";
			for (Vehicle v : vehicles)
			{
				vehicleIds += v.getDuid() + ",";
			}
			log.warn(String.format("These vehicles have the tripid %s in question: %s", tripId, vehicleIds));
		}

		for (TripDetailStopPassage tripPassage : passages) {
			StopPassageDetail tpc = new StopPassageDetail(tripPassage);

			if (vehicle != null) {
				Pair<Double, Double> vehicleLatLong = Utils.getPointFromDBPoint(vehicle.getLatLong());
				Pair<Double, Double> stopLatLong = Utils.getPointFromDBPoint(tpc.getStopLatLong());

				tpc.setMetersFromVehicle((long) Utils.distFrom(
						vehicleLatLong.x, vehicleLatLong.y, stopLatLong.x, stopLatLong.y));
			}
			stopPassageDetails.add(tpc);
		}
		
		return getFormattedHtml(stopPassageDetails);
	}
	
	private String getFormattedHtml(List<StopPassageDetail> stopPassageDetails) {
		
		return HtmlUtils.getStopPassageDetailsHtml(stopPassageDetails);
	}
}
