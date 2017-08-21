package bjdodo.ie_city_bus.model;

import java.util.Date;

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

	private Route(JSONObject obj) throws JSONException {
		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.lastModificationTimestamp = new Date(obj.getLong("last_modification_timestamp"));
		this.isDeleted = obj.getBoolean("is_deleted");
		this.shortName = obj.getString("short_name");
		this.number = obj.getInt("number");
		this.category = obj.getInt("category");
	}

	public static Route fromBuseireannJson(JSONObject obj) throws JSONException {
		return new Route(obj);
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private Date lastModificationTimestamp;
	private boolean isDeleted;
	private String shortName;
	private int number;
	private int category;
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