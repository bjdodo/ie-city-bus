package bjdodo.ie_city_bus.model.crud;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import bjdodo.ie_city_bus.utils.Utils;

@Entity
public class StopPassage {

	public StopPassage() {
	}

	public void updateFromJson(JSONObject obj) throws JSONException {

		this.duid = obj.getString("duid");
		this.isDeleted = obj.getBoolean("is_deleted");
		this.routeDuid = Utils.getDuid(obj, "route_duid");
		this.tripDuid = Utils.getDuid(obj, "trip_duid");
		this.stopPointDuid = Utils.getDuid(obj, "stop_point_duid");
		this.vehicleDuid = Utils.getDuid(obj, "vehicle_duid");
		this.patternDuid = Utils.getDuid(obj, "pattern_duid");

		JSONObject arrivalData = obj.optJSONObject("arrival_data");
		if (arrivalData != null) {
			this.scheduledArrival = Instant.ofEpochSecond(arrivalData.getLong("scheduled_passage_time_utc"));
			Long actual = arrivalData.optLong("actual_passage_time_utc");
			if (actual != null) {
				this.actualArrival = Instant.ofEpochSecond(actual);
			}
		}

		JSONObject departureData = obj.optJSONObject("departure_data");
		if (departureData != null) {
			this.scheduledDeparture = Instant.ofEpochSecond(departureData.getLong("scheduled_passage_time_utc"));
			Long actual = departureData.optLong("actual_passage_time_utc");
			if (actual != null) {
				this.actualDeparture = Instant.ofEpochSecond(actual);
			}
		}
	}

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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRouteDuid() {
		return routeDuid;
	}

	public void setRouteDuid(String routeDuid) {
		this.routeDuid = routeDuid;
	}

	public String getTripDuid() {
		return tripDuid;
	}

	public void setTripDuid(String tripDuid) {
		this.tripDuid = tripDuid;
	}

	public String getStopPointDuid() {
		return stopPointDuid;
	}

	public void setStopPointDuid(String stopPointDuid) {
		this.stopPointDuid = stopPointDuid;
	}

	public String getVehicleDuid() {
		return vehicleDuid;
	}

	public void setVehicleDuid(String vehicleDuid) {
		this.vehicleDuid = vehicleDuid;
	}

	public String getPatternDuid() {
		return patternDuid;
	}

	public void setPatternDuid(String patternDuid) {
		this.patternDuid = patternDuid;
	}

	public Instant getScheduledArrival() {
		return scheduledArrival;
	}

	public void setScheduledArrival(Instant scheduledArrival) {
		this.scheduledArrival = scheduledArrival;
	}

	public Instant getScheduledDeparture() {
		return scheduledDeparture;
	}

	public void setScheduledDeparture(Instant scheduledDeparture) {
		this.scheduledDeparture = scheduledDeparture;
	}

	public Instant getActualArrival() {
		return actualArrival;
	}

	public void setActualArrival(Instant actualArrival) {
		this.actualArrival = actualArrival;
	}

	public Instant getActualDeparture() {
		return actualDeparture;
	}

	public void setActualDepartureData(Instant actualDeparture) {
		this.actualDeparture = actualDeparture;
	}

	public long getTripId() {
		return tripId;
	}

	public void setTripId(long tripId) {
		this.tripId = tripId;
	}

	public Long getStopPointId() {
		return stopPointId;
	}

	public void setStopPointId(Long stopPointId) {
		this.stopPointId = stopPointId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private boolean isDeleted;
	@Transient
	private String routeDuid;
	@Transient
	private String tripDuid;
	@Transient
	private String stopPointDuid;
	@Transient
	private String vehicleDuid;
	@Transient
	private String patternDuid;

	private Instant scheduledArrival;
	private Instant scheduledDeparture;
	private Instant actualArrival;
	private Instant actualDeparture;

	private long tripId;
	private Long stopPointId;
	// private long vehicleId;

	public static String getJSONDuid(JSONObject json) throws JSONException {
		return json.getString("duid");
	}

	public static String getJSONRouteDuid(JSONObject json) throws JSONException {
		return Utils.getDuid(json, "route_duid");
	}

	public static String getJSONStopPointDuid(JSONObject json) throws JSONException {
		return Utils.getDuid(json, "stop_point_duid");
	}

	public static int getJSONDirection(JSONObject json) throws JSONException {
		return json.getInt("direction");
	}
}

// {
//
// "stopPassageTdi": {
// "passage_0": {
// "duid": "-9223372006328457945",
// "last_modification_timestamp": 1503092671492,
// "is_deleted": false,
// "route_duid": {
// "structTag": 50466,
// "duid": "6350571126703259822",
// "foo": 0
// },
// "direction": 1,
// "trip_duid": {
// "structTag": 50471,
// "duid": "6351558488848201275",
// "foo": 0
// },
// "stop_point_duid": {
// "structTag": 50465,
// "duid": "6350786630982825845",
// "foo": 0
// },
// "vehicle_duid": {
// "structTag": 50470,
// "duid": "6356266596935140207",
// "foo": 0
// },
// "arrival_data": {
// "scheduled_passage_time_utc": 1503090480,
// "scheduled_passage_time": "22:08",
// "actual_passage_time_utc": 1503091799,
// "actual_passage_time": "22:29",
// "service_mode": 1,
// "multilingual_direction_text": {
// "defaultValue": "Eyre Square South (Stop No 9)",
// "foo": 0
// },
// "type": 1,
// "foo": 0
// },
// "departure_data": {
// "scheduled_passage_time_utc": 1503090480,
// "scheduled_passage_time": "22:08",
// "actual_passage_time_utc": 1503091799,
// "actual_passage_time": "22:29",
// "service_mode": 1,
// "multilingual_direction_text": {
// "defaultValue": "Dr. Mannix Road",
// "foo": 0
// },
// "type": 1,
// "foo": 0
// },
// "congestion_level": 3,
// "accuracy_level": 3,
// "status": 4,
// "is_accessible": 0,
// "latitude": 191751980,
// "longitude": -32699376,
// "bearing": 74,
// "pattern_duid": {
// "structTag": 50472,
// "duid": "6349931210947318812",
// "foo": 0
// },
// "has_bike_rack": 0,
// "category": 5,
// "foo": 0
// }
// }