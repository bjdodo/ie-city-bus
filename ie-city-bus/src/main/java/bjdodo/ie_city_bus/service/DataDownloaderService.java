package bjdodo.ie_city_bus.service;

import java.util.List;

import org.json.JSONException;

import bjdodo.ie_city_bus.model.Vehicle;

public interface DataDownloaderService {
	public List<Vehicle> downloadVehicles() throws JSONException;
}