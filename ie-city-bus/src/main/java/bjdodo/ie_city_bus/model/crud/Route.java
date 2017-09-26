package bjdodo.ie_city_bus.model.crud;


import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Route {

	public Route() {
	}

	public void updateFromJson(JSONObject obj) throws JSONException {
		this.duid = obj.getString("duid");
		this.lastModificationTimestamp = Instant.ofEpochMilli(obj.getLong("last_modification_timestamp"));
		this.isDeleted = obj.getBoolean("is_deleted");
		this.shortName = obj.getString("short_name");
		this.number = obj.getInt("number");
		this.category = obj.getInt("category");
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}


	// public boolean contentEquals(Route other) {
	// if (category != other.category)
	// return false;
	// if (duid == null) {
	// if (other.duid != null)
	// return false;
	// } else if (!duid.equals(other.duid))
	// return false;
	// if (isDeleted != other.isDeleted)
	// return false;
	// if (lastModificationTimestamp == null) {
	// if (other.lastModificationTimestamp != null)
	// return false;
	// } else if
	// (!lastModificationTimestamp.equals(other.lastModificationTimestamp))
	// return false;
	// if (number != other.number)
	// return false;
	// if (shortName == null) {
	// if (other.shortName != null)
	// return false;
	// } else if (!shortName.equals(other.shortName))
	// return false;
	// return true;
	// }

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private Instant lastModificationTimestamp;
	private boolean isDeleted;
	private String shortName;
	private int number;
	private int category;

	public static String getJSONDuid(JSONObject json) throws JSONException {
		return json.getString("duid");
	}
}

// "routes_114":
// {
// "duid": "6350571126703259823",
// "last_modification_timestamp": 1502532767028,
// "is_deleted": false,
// "short_name": "402",
// "direction_extensions": {"direction": 1,"direction_name": "","foo": 0},
// "direction_extensions": {"direction": 2,"direction_name": "","foo": 0},
// "number": 402,
// "category": 5,
// "foo": 0
// }