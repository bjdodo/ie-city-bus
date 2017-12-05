package bjdodo.ie_city_bus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import bjdodo.ie_city_bus.controller.data.GuiConfigurationData;
import bjdodo.ie_city_bus.model.crud.Route;
import bjdodo.ie_city_bus.repository.crud.RouteRepository;
import bjdodo.ie_city_bus.service.ConfigurationService;

@RestController
@RequestMapping("/api/guiconfiguration")
public class GuiConfigurationController {

	@Autowired
	private ConfigurationService configurationService;

	@Autowired
	RouteRepository routeRepository;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public GuiConfigurationData get() {
		GuiConfigurationData ret = new GuiConfigurationData();

		ret.setGuiOverviewMapLatitude(configurationService.getGuiOverviewMapLatitude());
		ret.setGuiOverviewMapLongitude(configurationService.getGuiOverviewMapLongitude());
		ret.setGuiOverviewMapZoom(configurationService.getGuiOverviewMapZoom());
		ret.setGuiSessionExpiryMins(configurationService.getGuiSessionExpiryMins());

		List<Route> configuredRoutes = new ArrayList<>();
		for (Route r : routeRepository.findAll()) {
			if (configurationService.getMonitoredRoutes().contains(r.getShortName())) {
				configuredRoutes.add(r);
			}
		}
		ret.setMonitoredRoutes(configuredRoutes);
		ret.setBuildVersion(configurationService.getBuildVersion());

		return ret;
	}
}
