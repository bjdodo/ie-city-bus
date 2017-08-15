package bjdodo.ie_city_bus.service;

import org.json.JSONException;

public interface DataDownloaderService {
	public void downloadVehicles() throws JSONException;

	public void downloadRoutes() throws JSONException;
}