package bjdodo.ie_city_bus.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class BusStopPoint {

	public BusStopPoint() {
	}

	private BusStopPoint(JSONObject obj) throws JSONException {
		super();

		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.name = obj.getString("name");
		this.latitude = obj.getDouble("lat");
		this.longitude = obj.getDouble("lng");
		this.number = obj.getInt("num");
	}

	public static BusStopPoint fromBuseireannJson(JSONObject obj) throws JSONException {
		return new BusStopPoint(obj);
	}

	public BusStopPoint(Long id, String duid, String name, double latitude, double longitude, int number) {
		super();
		this.id = id;
		this.duid = duid;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public String getDuid() {
		return duid;
	}

	public String getName() {
		return name;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public int getNumber() {
		return number;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String duid;
	private String name;
	private double latitude;
	private double longitude;
	private int number;
}

// "bus_stop_5159": {"duid": "6350786630982827515","name": "Tirellan Heights
// (Ballinfoyle Rd)","lat": 53.29167,"lng": -9.046,"num": 523771}
