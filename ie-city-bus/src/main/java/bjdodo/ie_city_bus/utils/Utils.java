package bjdodo.ie_city_bus.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
	// public static float distFrom(float lat1, float lng1, float lat2, float lng2)
	// {
	// double earthRadius = 6371000; // meters
	// double dLat = Math.toRadians(lat2 - lat1);
	// double dLng = Math.toRadians(lng2 - lng1);
	// double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	// Math.cos(Math.toRadians(lat1))
	// * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
	// double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	// float dist = (float) (earthRadius * c);
	//
	// return dist;
	// }

	public static double distFrom(double d, double e, double f, double g) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(f - d);
		double dLng = Math.toRadians(g - e);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(d))
				* Math.cos(Math.toRadians(f)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (earthRadius * c);

		return dist;
	}

	public static String getDuid(JSONObject obj, String duidName) throws JSONException {

		JSONObject duidObj = obj.optJSONObject(duidName);
		if (duidObj != null) {
			return duidObj.getString("duid");
		} else {
			return "";
		}
	}

	public static String getPointDBString(double lat, double lon) {
		return "POINT (" + lat + " " + lon + ")";
	}

	public static Tuple<Double, Double> getPointFromDBPoint(String dbPoint) {
		if (!dbPoint.startsWith("POINT (") || !dbPoint.endsWith(")")) {
			throw new IllegalArgumentException("Wrong dbPoint " + dbPoint);
		}

		dbPoint = dbPoint.substring("POINT (".length(), dbPoint.length() - 2);

		String[] points = dbPoint.split(" ");
		if (points.length != 2) {
			throw new IllegalArgumentException("Wrong dbPoint " + dbPoint);
		}
		return new Tuple<Double, Double>(Double.parseDouble(points[0]), Double.parseDouble(points[1]));
	}
}
