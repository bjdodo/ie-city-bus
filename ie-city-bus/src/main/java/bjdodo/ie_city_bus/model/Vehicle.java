package bjdodo.ie_city_bus.model;

import java.time.Instant;

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
	public void updateFromJson(JSONObject obj) throws JSONException {

		this.duid = obj.getString("duid");
		this.lastModificationTimestamp = Instant.ofEpochMilli(obj.getLong("last_modification_timestamp"));
		this.isDeleted = obj.getBoolean("is_deleted");
		this.category = obj.getInt("category");
		this.tripDuid = ModelUtils.getDuid(obj, "trip_duid");
		this.geoPositionStatus = obj.getInt("geo_position_status");
		this.referenceTime = Instant.ofEpochSecond(obj.getLong("reference_time"));
		this.latLong = ModelUtils.getPointDBString(obj.getDouble("latitude") / 3600000,
				obj.getDouble("longitude") / 3600000);
		this.bearing = obj.getInt("bearing");
		this.isAccessible = obj.getInt("is_accessible") != 0;
		this.patternDuid = ModelUtils.getDuid(obj, "pattern_duid");
		this.bikeRack = obj.getInt("has_bike_rack") != 0;
		this.vehicleNumber = obj.getLong("vehicle_number");
		this.operationalNumber = obj.getLong("operational_number");

	}

	public long getId() {
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

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bearing;
		result = prime * result + (bikeRack ? 1231 : 1237);
		result = prime * result + category;
		result = prime * result + ((duid == null) ? 0 : duid.hashCode());
		result = prime * result + geoPositionStatus;
		result = prime * result + (int) (id ^ (operationalNumber >>> 32));
		result = prime * result + (isAccessible ? 1231 : 1237);
		result = prime * result + (isDeleted ? 1231 : 1237);
		result = prime * result + ((lastModificationTimestamp == null) ? 0 : lastModificationTimestamp.hashCode());
		result = prime * result + ((latLong == null) ? 0 : latLong.hashCode());
		result = prime * result + (int) (operationalNumber ^ (operationalNumber >>> 32));
		result = prime * result + ((patternDuid == null) ? 0 : patternDuid.hashCode());
		result = prime * result + ((referenceTime == null) ? 0 : referenceTime.hashCode());
		result = prime * result + ((tripDuid == null) ? 0 : tripDuid.hashCode());
		result = prime * result + (int) (vehicleNumber ^ (vehicleNumber >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vehicle other = (Vehicle) obj;
		if (bearing != other.bearing)
			return false;
		if (bikeRack != other.bikeRack)
			return false;
		if (category != other.category)
			return false;
		if (duid == null) {
			if (other.duid != null)
				return false;
		} else if (!duid.equals(other.duid))
			return false;
		if (geoPositionStatus != other.geoPositionStatus)
			return false;
		if (id != other.id)
			return false;
		if (isAccessible != other.isAccessible)
			return false;
		if (isDeleted != other.isDeleted)
			return false;
		if (lastModificationTimestamp == null) {
			if (other.lastModificationTimestamp != null)
				return false;
		} else if (!lastModificationTimestamp.equals(other.lastModificationTimestamp))
			return false;
		if (latLong == null) {
			if (other.latLong != null)
				return false;
		} else if (!latLong.equals(other.latLong))
			return false;
		if (operationalNumber != other.operationalNumber)
			return false;
		if (patternDuid == null) {
			if (other.patternDuid != null)
				return false;
		} else if (!patternDuid.equals(other.patternDuid))
			return false;
		if (referenceTime == null) {
			if (other.referenceTime != null)
				return false;
		} else if (!referenceTime.equals(other.referenceTime))
			return false;
		if (tripDuid == null) {
			if (other.tripDuid != null)
				return false;
		} else if (!tripDuid.equals(other.tripDuid))
			return false;
		if (vehicleNumber != other.vehicleNumber)
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private Instant lastModificationTimestamp;
	private boolean isDeleted;
	private int category;
	private String tripDuid;
	private Long tripId;
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
		return ModelUtils.getDuid(json, "trip_duid");
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
