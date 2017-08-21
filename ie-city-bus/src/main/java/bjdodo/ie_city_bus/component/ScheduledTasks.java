package bjdodo.ie_city_bus.component;



import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bjdodo.ie_city_bus.model.Route;
import bjdodo.ie_city_bus.model.StopPassage;
import bjdodo.ie_city_bus.model.StopPoint;
import bjdodo.ie_city_bus.model.Vehicle;
import bjdodo.ie_city_bus.service.DataDownloaderService;

@Component
public class ScheduledTasks {
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	DataDownloaderService dataDownloaderService;
	// private static final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("HH:mm:ss");
    
    @Scheduled(fixedRate = 60000)
	public void downloadSlowChangingData() {
        try
        {
			Map<String, Route> routes = dataDownloaderService.downloadRoutes();
			Map<String, StopPoint> stopPoints = dataDownloaderService.downloadStopPoints();
			Map<String, Vehicle> vehicles = dataDownloaderService.downloadVehicles();
			for (Vehicle v : vehicles.values()) {
				Map<String, StopPassage> stopPassages = dataDownloaderService.downloadStopPassages(v.getTripDuid());
			}
			// Map<String, StopPassage> stopPassages =
			// dataDownloaderService.downloadBusStopPoints()();

        }
		catch (Exception ex) {
			log.error(ex.getMessage());
		}
    }

}
