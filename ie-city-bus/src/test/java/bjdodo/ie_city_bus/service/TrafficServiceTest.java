package bjdodo.ie_city_bus.service;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TrafficServiceTest {

	@InjectMocks
	private TrafficServiceImpl predictionService;

	@Mock
	private ConfigurationServiceImpl configurationServiceImpl;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		predictionService.init();
	}
}
