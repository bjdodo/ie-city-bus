package bjdodo.ie_city_bus.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	private static final Logger log = LoggerFactory.getLogger(HttpServiceImpl.class);

	@Autowired
	HttpService httpService;
	
	@Override
	public List<Vehicle> downloadVehicles() throws JSONException {
		
		String resp = httpService.get("http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484");
		if (resp == null || resp.isEmpty())
		{
			log.info("http get request returned zero vehicles");
			return new ArrayList<Vehicle>();
		}
		JSONObject obj = new JSONObject(resp);

		JSONObject vehicleTdi = obj.getJSONObject("vehicleTdi");

		List<Vehicle> ret = new ArrayList<Vehicle>();
		int cnt = 0;
		while (true) {
			JSONObject bus = vehicleTdi.optJSONObject("bus_" + cnt++);
			if (bus == null) {
				break;
			} else {
				ret.add(Vehicle.fromBuseireannJson(bus));
			}
		}

		return ret;
	}
}
