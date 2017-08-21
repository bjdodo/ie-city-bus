package bjdodo.ie_city_bus.service;

import java.util.Map;

import org.json.JSONException;

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.StopPassage;
import bjdodo.ie_city_bus.model.StopPoint;
import bjdodo.ie_city_bus.model.Vehicle;

public interface DataDownloaderService {
	public Map<String, Vehicle> downloadVehicles() throws JSONException;

	public Map<String, Route> downloadRoutes() throws JSONException;

	public Map<String, StopPoint> downloadStopPoints() throws JSONException;

	public Map<String, StopPassage> downloadStopPassages(String tripDuid) throws JSONException;
}