package bjdodo.ie_city_bus.model.crud;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import bjdodo.ie_city_bus.utils.Utils;

@Entity
public class StopPoint {

	public StopPoint() {
	}

	public void updateFromJson(JSONObject obj) throws JSONException {
		this.duid = obj.getString("duid");
		this.name = obj.getString("name");
		this.latLong = Utils.getPointDBString(obj.getDouble("lat"), obj.getDouble("lng"));
		this.number = obj.getInt("num");
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatLong() {
		return latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean contentEquals(StopPoint other) {
		if (duid == null) {
			if (other.duid != null)
				return false;
		} else if (!duid.equals(other.duid))
			return false;
		if (latLong == null) {
			if (other.latLong != null)
				return false;
		} else if (!latLong.equals(other.latLong))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (number != other.number)
			return false;
		return true;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private String name;
	private String latLong;
	private int number;

	public static String getJSONDuid(JSONObject json) throws JSONException {
		return json.getString("duid");
	}
}

// "bus_stop_5159": {"duid": "6350786630982827515","name": "Tirellan Heights
// (Ballinfoyle Rd)","lat": 53.29167,"lng": -9.046,"num": 523771}
