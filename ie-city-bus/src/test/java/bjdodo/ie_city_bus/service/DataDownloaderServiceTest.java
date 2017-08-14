package bjdodo.ie_city_bus.service;

import java.util.List;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import bjdodo.ie_city_bus.model.Vehicle;

public class DataDownloaderServiceTest {

	@InjectMocks
	private DataDownloaderServiceImpl dataDownloaderService;

	@Mock
	private HttpService httpService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testDownloadVehicles() throws JSONException {

		// @formatter:off
		String resp = "{ \"vehicleTdi\":" + 
				"		 {\"bus_0\":" + 
				"		 {\"duid\": \"6352185209772835696\"," + 
				"		 \"last_modification_timestamp\": 1502659204867," + 
				"		 \"is_deleted\": false," + 
				"		 \"category\": 5," + 
				"		 \"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488880701746\",\"foo\": 0}," + 
				"		 \"geo_position_status\": 1," + 
				"		 \"reference_time\": 1502659204," + 
				"		 \"latitude\": 191820703," + 
				"		 \"longitude\": -32569902," + 
				"		 \"bearing\": 19," + 
				"		 \"is_accessible\": 0," + 
				"		 \"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571043\",\"foo\": 0}," + 
				"		 \"has_bike_rack\": 0," + 
				"		 \"vehicle_number\": 880," + 
				"		 \"operational_number\": 880," + 
				"		 \"foo\": 0}" + 
				"		 }" + 
				"		 }";
		// @formatter:on
		
		String url = "http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484";

		Mockito.when(httpService.get(url)).thenReturn(resp);

		List<Vehicle> vehicles = dataDownloaderService.downloadVehicles();
		Assert.assertEquals(vehicles.size(), 1);

	}
}
