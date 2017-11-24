package bjdodo.ie_city_bus.controller.data;

import java.util.List;

import bjdodo.ie_city_bus.model.crud.Route;

public class GuiConfigurationData {

	public Double getGuiOverviewMapLatitude() {
		return guiOverviewMapLatitude;
	}

	public void setGuiOverviewMapLatitude(Double guiOverviewMapLatitude) {
		this.guiOverviewMapLatitude = guiOverviewMapLatitude;
	}

	public Double getGuiOverviewMapLongitude() {
		return guiOverviewMapLongitude;
	}

	public void setGuiOverviewMapLongitude(Double guiOverviewMapLongitude) {
		this.guiOverviewMapLongitude = guiOverviewMapLongitude;
	}

	public Long getGuiOverviewMapZoom() {
		return guiOverviewMapZoom;
	}

	public void setGuiOverviewMapZoom(Long guiOverviewMapZoom) {
		this.guiOverviewMapZoom = guiOverviewMapZoom;
	}

	public List<Route> getMonitoredRoutes() {
		return monitoredRoutes;
	}

	public void setMonitoredRoutes(List<Route> monitoredRoutes) {
		this.monitoredRoutes = monitoredRoutes;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	private Double guiOverviewMapLatitude;

	private Double guiOverviewMapLongitude;

	private Long guiOverviewMapZoom;

	private List<Route> monitoredRoutes;

	private String buildVersion;



}
