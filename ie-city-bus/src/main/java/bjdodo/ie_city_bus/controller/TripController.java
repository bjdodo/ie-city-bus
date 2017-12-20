package bjdodo.ie_city_bus.controller;

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
import bjdodo.ie_city_bus.controller.data.StopPassageHistoryByStop;
import bjdodo.ie_city_bus.model.TripDetailStopPassage;
import bjdodo.ie_city_bus.model.crud.Vehicle;
import bjdodo.ie_city_bus.repository.crud.TripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;
import bjdodo.ie_city_bus.service.TrafficService;
import bjdodo.ie_city_bus.utils.Pair;
import bjdodo.ie_city_bus.utils.Utils;

@RestController
@RequestMapping("/api/trip")
public class TripController {

	private static final Logger log = LoggerFactory.getLogger(TripController.class);
	
	@Autowired
	TripRepository tripRepository;
	
	@Autowired
	TrafficService trafficService;
	
	@Autowired
	VehicleRepository vehicleRepository;
	
	@RequestMapping(value = "/{tripId}/traffic", method = RequestMethod.GET)
	public List<StopPassageHistoryByStop> getTrafficForTrip(@PathVariable(required = true) Long tripId) {

		List<TripDetailStopPassage> passages = tripRepository.getTripPassages(tripId);

		return trafficService.getRecentPassageHistoryForStops(passages);
	}
	
	@RequestMapping(value = "/{tripId}/passages", method = RequestMethod.GET)
	public List<StopPassageDetail> getTripPassages(@PathVariable(required = true) Long tripId) {

		log.info("ActiveTripsController.getTripPassages " + tripId);

		List<StopPassageDetail> ret = new ArrayList<>();

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
			ret.add(tpc);
		}
		return ret;

	}

}
