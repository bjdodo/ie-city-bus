package bjdodo.ie_city_bus.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	public Double getGuiOverviewMapLatitude() {
		return Double.parseDouble(guiOverviewMapLatitude);
	}

	public Double getGuiOverviewMapLongitude() {
		return Double.parseDouble(guiOverviewMapLongitude);
	}

	public Long getGuiOverviewMapZoom() {
		return Long.parseLong(guiOverviewMapZoom);
	}

	public Long getGuiSessionExpiryMins() {
		return Long.parseLong(guiSessionExpiryMins);
	}
	
	public String getLatLongRectangle() {
		return latLongRectangle;
	}

	public String getDownloadedDataSaveDir() {
		return downloadedDataSaveDir;
	}
	
	public List<String> getMonitoredRoutes() {
		return Arrays.asList(monitoredRoutes.split(","));
	}

	public Long getMaxTripAgeDays() {
		return Long.parseLong(maxTripAgeDays);
	}
	
	public Long getPredictionRecentMaxAgeMinutes() {
		return Long.parseLong(predictionRecentMaxAgeMinutes);
	}
	public String getBuildVersion() {
		return buildVersion;
	}

	@Value("${ie_city_bus.latLongRectangle}")
	String latLongRectangle;

	@Value("${ie_city_bus.downloadedDataSaveDir}")
	String downloadedDataSaveDir;

	@Value("${ie_city_bus.monitoredroutes}")
	private String monitoredRoutes;

	@Value("${ie_city_bus.maxtripagedays}")
	private String maxTripAgeDays;
	
	@Value("${ie_city_bus.prediction.recent.maxageminutes}")
	private String predictionRecentMaxAgeMinutes;

	@Value("${build.version}")
	private String buildVersion;
	
	@Value("${ie_city_bus.gui.overviewmap.latitude}")
	private String guiOverviewMapLatitude;

	@Value("${ie_city_bus.gui.overviewmap.longitude}")
	private String guiOverviewMapLongitude;

	@Value("${ie_city_bus.gui.overviewmap.zoom}")
	private String guiOverviewMapZoom;

	@Value("${ie_city_bus.gui.overviewmap.sessionexpirymins}")
	private String guiSessionExpiryMins;
}
