package bjdodo.ie_city_bus.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.StopPassage;
import bjdodo.ie_city_bus.model.StopPoint;
import bjdodo.ie_city_bus.model.Vehicle;

@Service
public class DataDownloaderServiceImpl implements DataDownloaderService {

	// { "vehicleTdi":
	// {"bus_0":
	// {"duid": "6352185209772835696",
	// "last_modification_timestamp": 1502659204867,
	// "is_deleted": false,
	// "category": 5,
	// "trip_duid": {"structTag": 50471,"duid": "6351558488880701746","foo": 0},
	// "geo_position_status": 1,
	// "reference_time": 1502659204,
	// "latitude": 191820703,
	// "longitude": -32569902,
	// "bearing": 19,
	// "is_accessible": 0,
	// "pattern_duid": {"structTag": 50472,"duid": "6349931210947571043","foo": 0},
	// "has_bike_rack": 0,
	// "vehicle_number": 880,
	// "operational_number": 880,
	// "foo": 0}
	// }

	private static final Logger log = LoggerFactory.getLogger(DataDownloaderServiceImpl.class);

	@Autowired
	private HttpService httpService;

	// @Autowired
	// private VehicleRepository vehicleRepository;

	@Override
	public Map<String, Vehicle> downloadVehicles() throws JSONException {

		String resp = httpService.get(
				"http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484");
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero vehicles");
			return new HashMap<String, Vehicle>();
		}

		JSONObject obj = new JSONObject(resp);

		JSONObject vehicleTdi = obj.getJSONObject("vehicleTdi");

		Map<String, Vehicle> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject bus = vehicleTdi.optJSONObject("bus_" + cnt++);
			if (bus == null) {
				break;
			} else {
				Vehicle vehicle = Vehicle.fromBuseireannJson(bus);
				ret.put(vehicle.getDuid(), vehicle);
				// if (vehicleRepository.countByDuid(v.getDuid()) == 0) {
				// vehicleRepository.saveAndFlush(v);
				// }
			}
		}

		return ret;
	}

	// @Autowired
	// private RouteRepository routeRepository;

	@Override
	public Map<String, Route> downloadRoutes() throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/routes.php");
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, Route>();
		}

		// This text starts with a javascript variable declaration, we trim that off and
		// the ; from the end
		if (!resp.startsWith("var obj_routes = ") || !resp.endsWith(";")) {
			log.error(String.format("Unexpected string returned for routes '%s...'", resp.substring(0, 100)));
			return new HashMap<String, Route>();
		}
		resp = resp.substring("var obj_routes = ".length());
		resp = resp.substring(0, resp.length() - 1);
		
		// The field direction_extensions appears twice in every entry which is invalid.
		// I need to get rid of it.
		int cnt=0;
		resp = resp.replace("\"direction_extensions\": {\"direction\": 1",
				"\"direction_extensions_1\": {\"direction\": 1");
		resp = resp.replace("\"direction_extensions\": {\"direction\": 2",
				"\"direction_extensions_2\": {\"direction\": 2");

		JSONObject obj = new JSONObject(resp);

		JSONObject routeTdi = obj.getJSONObject("routeTdi");

		Map<String, Route> ret = new HashMap<>();

		while (true) {
			JSONObject route = routeTdi.optJSONObject("routes_" + cnt++);
			if (route == null) {
				break;
			} else {
				Route r = Route.fromBuseireannJson(route);
				ret.put(r.getDuid(), r);
				// if (routeRepository.countByDuid(r.getDuid()) == 0) {
				// routeRepository.saveAndFlush(r);
				// }
			}
		}
		return ret;
	}

	// @Autowired
	// private BusStopPointRepository busStopPointRepository;

	public Map<String, StopPoint> downloadStopPoints() throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/bus_stop_points.php");
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, StopPoint>();
		}

		// This text starts with a javascript variable declaration, we trim that off and
		// the ; from the end
		if (!resp.startsWith("var obj_bus_stop_points = ") || !resp.endsWith(";")) {
			log.error(String.format("Unexpected string returned for bus stops '{0}...'", resp.substring(0, 100)));
			return new HashMap<String, StopPoint>();
		}
		resp = resp.substring("var obj_bus_stop_points = ".length());
		resp = resp.substring(0, resp.length() - 1);

		JSONObject obj = new JSONObject(resp);

		JSONObject bus_stops = obj.getJSONObject("bus_stops");

		Map<String, StopPoint> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject route = bus_stops.optJSONObject("bus_stop_" + cnt++);
			if (route == null) {
				break;
			} else {
				StopPoint busStopPoint = StopPoint.fromBuseireannJson(route);
				ret.put(busStopPoint.getDuid(), busStopPoint);
				// BusStopPoint r = BusStopPoint.fromBuseireannJson(route);
				// if (busStopPointRepository.countByDuid(r.getDuid()) == 0) {
				// busStopPointRepository.saveAndFlush(r);
				// }
			}
		}
		return ret;
	}

	public Map<String, StopPassage> downloadStopPassages(String tripDuid) throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/stopPassageTdi.php?trip=" + tripDuid);
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, StopPassage>();
		}

		JSONObject obj = new JSONObject(resp);

		JSONObject stopPassageTdi = obj.getJSONObject("stopPassageTdi");

		Map<String, StopPassage> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject route = stopPassageTdi.optJSONObject("passage_" + cnt++);
			if (route == null) {
				break;
			} else {
				StopPassage stopPassage = StopPassage.fromBuseireannJson(route);
				ret.put(stopPassage.getDuid(), stopPassage);
				// BusStopPoint r = BusStopPoint.fromBuseireannJson(route);
				// if (busStopPointRepository.countByDuid(r.getDuid()) == 0) {
				// busStopPointRepository.saveAndFlush(r);
				// }
			}
		}
		return ret;
	}
}
