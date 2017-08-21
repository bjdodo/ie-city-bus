package bjdodo.ie_city_bus.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelUtils {
	public static String getDuid(JSONObject obj, String duidName) throws JSONException {

		JSONObject duidObj = obj.optJSONObject(duidName);
		if (duidObj != null) {
			return duidObj.getString("duid");
		} else {
			return "";
		}
	}

	public static String getPointDBString(double lat, double lon) {
		return "POINT(" + lat + " " + lon + ")";
	}

}
