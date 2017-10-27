package bjdodo.ie_city_bus.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bjdodo.ie_city_bus.service.DataSavingService;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private DataSavingService dataSavingService;

	@Scheduled(fixedRate = 60000)
	public void downloadAndSaveData() {
		dataSavingService.downloadAndSaveAll();
	}



}
