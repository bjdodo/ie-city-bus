package bjdodo.ie_city_bus.service;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public interface DataDownloaderService {
	public Map<String, JSONObject> downloadVehicles() throws JSONException;

	public Map<String, JSONObject> downloadRoutes() throws JSONException;

	public Map<String, JSONObject> downloadStopPoints() throws JSONException;

	public Map<String, JSONObject> downloadStopPassages(String tripDuid) throws JSONException;
}