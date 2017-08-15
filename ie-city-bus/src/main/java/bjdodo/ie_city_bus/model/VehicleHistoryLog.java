package bjdodo.ie_city_bus.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class VehicleHistoryLog {

	public VehicleHistoryLog(Long id, long vehicleId, long tripId, long last_modification_timestamp, double latitude,
			double longitude) {
		super();
		this.id = id;
		this.vehicleId = vehicleId;
		this.tripId = tripId;
		this.last_modification_timestamp = last_modification_timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public VehicleHistoryLog(JSONObject vehicleObj, long vehicleId, long tripId) throws JSONException
	{
		this.id = vehicleObj.optLong("id");
		this.last_modification_timestamp = vehicleObj.getLong("last_modification_timestamp");
		this.latitude = vehicleObj.getDouble("latitude") / 3600000;
		this.longitude = vehicleObj.getDouble("longitude") / 3600000;
		
		this.vehicleId = vehicleId;
		this.tripId = tripId;
	}

	public static VehicleHistoryLog fromBuseireannJson(JSONObject obj, long vehicleId, long tripId) throws JSONException {
		return new VehicleHistoryLog(obj, vehicleId, tripId);
	}
	
	public long getVehicleId() {
		return vehicleId;
	}

	public long getTripId() {
		return tripId;
	}

	public long getLast_modification_timestamp() {
		return last_modification_timestamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	final private Long id;
	final private long vehicleId;
	final private long tripId;
	final private long last_modification_timestamp;
	final private double latitude;
	final private double longitude;
	// final private long vehicle_number;
	// final private long operational_number;

}
