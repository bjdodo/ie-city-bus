package bjdodo.ie_city_bus.model.crud;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import bjdodo.ie_city_bus.utils.Utils;

@Entity
public class Vehicle {

	public Vehicle() {
	}

	// for sample JSON see bottom of file
	public void updateFromJson(JSONObject obj) throws JSONException {

		this.duid = obj.getString("duid");
		this.lastModificationTimestamp = Instant.ofEpochMilli(obj.getLong("last_modification_timestamp"));
		this.isDeleted = obj.getBoolean("is_deleted");
		this.tripDuid = Utils.getDuid(obj, "trip_duid");
		this.geoPositionStatus = obj.getInt("geo_position_status");
		this.referenceTime = Instant.ofEpochSecond(obj.getLong("reference_time"));
		this.latLong = Utils.getPointDBString(obj.getDouble("latitude") / 3600000,
				obj.getDouble("longitude") / 3600000);
		this.bearing = obj.getInt("bearing");
		this.isAccessible = obj.getInt("is_accessible") != 0;
		this.patternDuid = Utils.getDuid(obj, "pattern_duid");
		this.bikeRack = obj.getInt("has_bike_rack") != 0;
		this.vehicleNumber = obj.getLong("vehicle_number");
		this.operationalNumber = obj.getLong("operational_number");

	}

	// This is only for unit tests and for symmetry
	/*
	 * public void updateToJson(JSONObject obj) throws JSONException {
	 * obj.put("duid", this.duid); obj.put("last_modification_timestamp",
	 * this.lastModificationTimestamp.toEpochMilli()); obj.put("is_deleted",
	 * this.isDeleted ? 1 : 0); obj.put("category", this.category);
	 * obj.put("trip_duid", this.tripDuid); obj.put("geo_position_status",
	 * this.geoPositionStatus); obj.put("reference_time",
	 * this.referenceTime.getEpochSecond()); obj.put("longitude", 3600000 *
	 * Utils.getPointFromDBPoint(this.latLong).x); obj.put("latitude", 3600000 *
	 * Utils.getPointFromDBPoint(this.latLong).y); obj.put("bearing", this.bearing);
	 * obj.put("is_accessible", this.isAccessible ? 1 : 0); obj.put("pattern_duid",
	 * new JSONObject()); obj.getJSONObject("pattern_guid").put("duid",
	 * this.patternDuid); obj.put("has_bike_rack", this.bikeRack ? 1 : 0);
	 * obj.put("vehicle_number", this.vehicleNumber); obj.put("operational_number",
	 * this.operationalNumber); }
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDuid() {
		return duid;
	}

	public void setDuid(String duid) {
		this.duid = duid;
	}

	public Instant getLastModificationTimestamp() {
		return lastModificationTimestamp;
	}

	public void setLastModificationTimestamp(Instant lastModificationTimestamp) {
		this.lastModificationTimestamp = lastModificationTimestamp;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public String getTripDuid() {
		return tripDuid;
	}

	public void setTripDuid(String tripDuid) {
		this.tripDuid = tripDuid;
	}

	public Long getCurrentTripId() {
		return currentTripId;
	}

	public void setCurrentTripId(Long currentTripId) {
		this.currentTripId = currentTripId;
	}

	public int getGeoPositionStatus() {
		return geoPositionStatus;
	}

	public void setGeoPositionStatus(int geoPositionStatus) {
		this.geoPositionStatus = geoPositionStatus;
	}

	public Instant getReferenceTime() {
		return referenceTime;
	}

	public void setReferenceTime(Instant referenceTime) {
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

	public boolean isBikeRack() {
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
	private long id;
	private String duid;
	private Instant lastModificationTimestamp;
	private boolean isDeleted;
	private String tripDuid;
	private Long currentTripId;
	private int geoPositionStatus;
	private Instant referenceTime;
	private String latLong;
	private int bearing;
	private boolean isAccessible;
	private String patternDuid;
	private boolean bikeRack;
	private long vehicleNumber;
	private long operationalNumber;

	public static String getJSONDuid(JSONObject json) throws JSONException {
		return json.getString("duid");
	}

	public static String getJSONTripDuid(JSONObject json) throws JSONException {
		return Utils.getDuid(json, "trip_duid");
	}
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
