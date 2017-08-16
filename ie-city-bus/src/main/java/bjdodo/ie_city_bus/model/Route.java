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

	
	public Route(long id, String duid, Date last_modification_timestamp, boolean is_deleted, String short_name,
			int number, int category) {
		super();
		this.id = id;
		this.duid = duid;
		this.last_modification_timestamp = last_modification_timestamp;
		this.is_deleted = is_deleted;
		this.short_name = short_name;
		this.number = number;
		this.category = category;
	}

	private Route(JSONObject obj) throws JSONException {
		this.id = obj.optLong("id");
		this.duid = obj.getString("duid");
		this.last_modification_timestamp = new Date(obj.getLong("last_modification_timestamp"));
		this.is_deleted = obj.getBoolean("is_deleted");
		this.short_name = obj.getString("short_name");
		this.number = obj.getInt("number");
		this.category = obj.getInt("category");
	}

	public static Route fromBuseireannJson(JSONObject obj) throws JSONException {
		return new Route(obj);
	}

	public Long getId() {
		return id;
	}

	public String getDuid() {
		return duid;
	}

	public Date getLast_modification_timestamp() {
		return last_modification_timestamp;
	}

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public String getShort_name() {
		return short_name;
	}

	public int getNumber() {
		return number;
	}

	public int getCategory() {
		return category;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private Date last_modification_timestamp;
	private boolean is_deleted;
	private String short_name;
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