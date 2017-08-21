package bjdodo.ie_city_bus.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import bjdodo.ie_city_bus.utils.ModelUtils;

@Entity
public class Vehicle {

	public Vehicle() {
	}
	// for sample JSON see bottom of file
	private Vehicle(JSONObject obj) throws JSONException {
		super();

		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.lastModificationTimestamp = new Date(obj.getLong("last_modification_timestamp"));
		this.isDeleted = obj.getBoolean("is_deleted");
		this.category = obj.getInt("category");
		this.tripDuid = ModelUtils.getDuid(obj, "trip_duid");
		this.geoPositionStatus = obj.getInt("geo_position_status");
		this.referenceTime = new Date(obj.getLong("reference_time") * 1000);
		this.latLong = ModelUtils.getPointDBString(obj.getDouble("latitude") / 3600000,
				obj.getDouble("longitude") / 3600000);
		this.bearing = obj.getInt("bearing");
		this.isAccessible = obj.getInt("is_accessible") != 0;
		this.patternDuid = ModelUtils.getDuid(obj, "pattern_duid");
		this.bikeRack = obj.getInt("has_bike_rack") != 0;
		this.vehicleNumber = obj.getLong("vehicle_number");
		this.operationalNumber = obj.getLong("operational_number");

	}

	public static Vehicle fromBuseireannJson(JSONObject obj) throws JSONException {
		return new Vehicle(obj);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getDuid() {
		return duid;
	}

	public void setDuid(String duid) {
		this.duid = duid;
	}

	public Date getLastModificationTimestamp() {
		return lastModificationTimestamp;
	}

	public void setLastModificationTimestamp(Date lastModificationTimestamp) {
		this.lastModificationTimestamp = lastModificationTimestamp;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public String getTripDuid() {
		return tripDuid;
	}

	public void setTripDuid(String tripDuid) {
		this.tripDuid = tripDuid;
	}

	public int getGeoPositionStatus() {
		return geoPositionStatus;
	}

	public void setGeoPositionStatus(int geoPositionStatus) {
		this.geoPositionStatus = geoPositionStatus;
	}

	public Date getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(Date referenceTime) {
		this.referenceTime = referenceTime;
	}
	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public int getBearing() {
		return bearing;
	}

	public void setBearing(int bearing) {
		this.bearing = bearing;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public String getPatternDuid() {
		return patternDuid;
	}

	public void setPatternDuid(String patternDuid) {
		this.patternDuid = patternDuid;
	}

	public boolean hasBikeRack() {
		return bikeRack;
	}

	public void setBikeRack(boolean bikeRack) {
		this.bikeRack = bikeRack;
	}

	public long getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(long vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public long getOperationalNumber() {
		return operationalNumber;
	}

	public void setOperationalNumber(long operationalNumber) {
		this.operationalNumber = operationalNumber;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String duid;
	private Date lastModificationTimestamp;
	private boolean isDeleted;
	private int category;
	private String tripDuid;
	private int geoPositionStatus;
	private Date referenceTime;
	private String latLong;
	private int bearing;
	private boolean isAccessible;
	private String patternDuid;
	private boolean bikeRack;
	private long vehicleNumber;
	private long operationalNumber;
}

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
// }
