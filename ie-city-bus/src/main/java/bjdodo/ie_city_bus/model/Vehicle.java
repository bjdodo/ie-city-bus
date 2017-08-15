package bjdodo.ie_city_bus.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Vehicle {

	// for sample JSON see bottom of file
	private Vehicle(JSONObject obj) throws JSONException {
		super();

		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.last_modification_timestamp = obj.getLong("last_modification_timestamp");
		this.is_deleted = obj.getBoolean("is_deleted");
		this.category = obj.getInt("category");
		this.trip_duid = obj.getJSONObject("trip_duid").getString("duid");
		this.geo_position_status = obj.getInt("geo_position_status");
		this.reference_time = new Date(obj.getLong("reference_time"));
		this.latitude = obj.getDouble("latitude") / 3600000;
		this.longitude = obj.getDouble("longitude") / 3600000;
		this.bearing = obj.getInt("bearing");
		this.is_accessible = obj.getInt("is_accessible") != 0;
		this.pattern_duid = obj.getJSONObject("pattern_duid").getString("duid");
		this.has_bike_rack = obj.getInt("has_bike_rack") != 0;
		this.vehicle_number = obj.getLong("vehicle_number");
		this.operational_number = obj.getLong("operational_number");

	}

	public static Vehicle fromBuseireannJson(JSONObject obj) throws JSONException {
		return new Vehicle(obj);
	}

	public Vehicle(Long id, String duid, long last_modification_timestamp, boolean is_deleted, int category,
			String trip_duid, int geo_position_status, Date reference_time, double latitude, double longitude,
			int bearing, boolean is_accessible, String pattern_duid, boolean has_bike_rack, long vehicle_number,
			long operational_number) {
		super();
		this.id = id;
		this.duid = duid;
		this.last_modification_timestamp = last_modification_timestamp;
		this.is_deleted = is_deleted;
		this.category = category;
		this.trip_duid = trip_duid;
		this.geo_position_status = geo_position_status;
		this.reference_time = reference_time;
		this.latitude = latitude;
		this.longitude = longitude;
		this.bearing = bearing;
		this.is_accessible = is_accessible;
		this.pattern_duid = pattern_duid;
		this.has_bike_rack = has_bike_rack;
		this.vehicle_number = vehicle_number;
		this.operational_number = operational_number;
	}

	public String getDuid() {
		return duid;
	}

	public long getLast_modification_timestamp() {
		return last_modification_timestamp;
	}

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public int getCategory() {
		return category;
	}

	public String getTrip_duid() {
		return trip_duid;
	}

	public int getGeo_position_status() {
		return geo_position_status;
	}

	public Date getReference_time() {
		return reference_time;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getBearing() {
		return bearing;
	}

	public boolean isIs_accessible() {
		return is_accessible;
	}

	public String getPattern_duid() {
		return pattern_duid;
	}

	public boolean isHas_bike_rack() {
		return has_bike_rack;
	}

	public long getVehicle_number() {
		return vehicle_number;
	}

	public long getOperational_number() {
		return operational_number;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	final private Long id;
	final private String duid;
	final private long last_modification_timestamp;
	final private boolean is_deleted;
	final private int category;
	final private String trip_duid;
	final private int geo_position_status;
	final private Date reference_time;
	final private double latitude;
	final private double longitude;
	final private int bearing;
	final private boolean is_accessible;
	final private String pattern_duid;
	final private boolean has_bike_rack;
	final private long vehicle_number;
	final private long operational_number;
}

//{ "vehicleTdi":
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
// }
