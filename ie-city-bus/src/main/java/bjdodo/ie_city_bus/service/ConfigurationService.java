package bjdodo.ie_city_bus.service;

import java.util.List;

public interface ConfigurationService {

	Double getGuiOverviewMapLatitude();

	Double getGuiOverviewMapLongitude();

	Long getGuiOverviewMapZoom();

	Long getGuiSessionExpiryMins();

	String getLatLongRectangle();

	String getDownloadedDataSaveDir();

	List<String> getMonitoredRoutes();

	Long getMaxTripAgeDays();

	Long getPredictionRecentMaxAgeMinutes();

	String getBuildVersion();

}
