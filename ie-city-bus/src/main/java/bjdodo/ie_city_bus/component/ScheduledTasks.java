package bjdodo.ie_city_bus.component;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
			dataDownloaderService.downloadRoutes();
			dataDownloaderService.downloadBusStopPoints();
			dataDownloaderService.downloadVehicles();

        }
		catch (Exception ex) {
			log.error(ex.getMessage());
		}
    }

}
