package bjdodo.ie_city_bus.model;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import bjdodo.ie_city_bus.utils.ModelUtils;

public class StopPassage {

	public StopPassage() {
	}

	private StopPassage(JSONObject obj) throws JSONException {

		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.isDeleted = obj.getBoolean("is_deleted");
		this.routeDuid = ModelUtils.getDuid(obj, "route_duid");
		this.tripDuid = ModelUtils.getDuid(obj, "trip_duid");
		this.stopPointDuid = ModelUtils.getDuid(obj, "stop_point_duid");
		this.vehicleDuid = ModelUtils.getDuid(obj, "vehicle_duid");
		this.patternDuid = ModelUtils.getDuid(obj, "pattern_duid");
		this.direction = obj.getInt("direction");
		this.arrivalData = new Date(obj.getLong("arrival_data"));
		this.departureData = new Date(obj.getLong("departure_data"));

	}

	public static StopPassage fromBuseireannJson(JSONObject obj) throws JSONException {
		return new StopPassage(obj);
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

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Date getArrivalData() {
		return arrivalData;
	}

	public void setArrivalData(Date arrivalData) {
		this.arrivalData = arrivalData;
	}

	public Date getDepartureData() {
		return departureData;
	}

	public void setDepartureData(Date departureData) {
		this.departureData = departureData;
	}

	public int getRouteId() {
		return routeId;
	}

	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}

	public int getTripId() {
		return tripId;
	}

	public void setTripId(int tripId) {
		this.tripId = tripId;
	}

	public int getStopPointId() {
		return stopPointId;
	}

	public void setStopPointId(int stopPointId) {
		this.stopPointId = stopPointId;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
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
	private int direction;
	private Date arrivalData;
	private Date departureData;

	private int routeId;
	private int tripId;
	private int stopPointId;
	private int vehicleId;
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