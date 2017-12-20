package bjdodo.ie_city_bus.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import bjdodo.ie_city_bus.service.TrafficService.TripSectionPassage;

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

	@Test
	public void testBasic() {
		
	}
}
