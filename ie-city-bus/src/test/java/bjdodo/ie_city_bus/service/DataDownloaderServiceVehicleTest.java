package bjdodo.ie_city_bus.service;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import bjdodo.ie_city_bus.utils.Utils;

public class DataDownloaderServiceVehicleTest {

	@InjectMocks
	private DataDownloaderServiceImpl dataDownloaderService;

	@Mock
	private HttpService httpService;


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
//		ReflectionTestUtils.setField(dataDownloaderService, "latLongRectangle",
//				"latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484");
	}

	String vehicleUrl = "http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484";

	// @formatter:off
	String jsonVehicle = "{ \"vehicleTdi\":" 
			+ "		 {\"bus_0\":" 
			+ "		 {\"duid\": \"6352185209772835696\","
			+ "		 \"last_modification_timestamp\": 1502659204867," 
			+ "		 \"is_deleted\": false,"
			+ "		 \"category\": 5,"
			+ "		 \"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488880701746\",\"foo\": 0},"
			+ "		 \"geo_position_status\": 1," 
			+ "		 \"reference_time\": 1502659204,"
			+ "		 \"latitude\": 191820703," 
			+ "		 \"longitude\": -32569902," 
			+ "		 \"bearing\": 19,"
			+ "		 \"is_accessible\": 0,"
			+ "		 \"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571043\",\"foo\": 0},"
			+ "		 \"has_bike_rack\": 0," 
			+ "		 \"vehicle_number\": 880," 
			+ "		 \"operational_number\": 880,"
			+ "		 \"foo\": 0}" 
			+ "		 }" 
			+ "		 }";
	// @formatter:on

	@Test
	public void testDownloadVehicles() throws JSONException {

		Mockito.when(httpService.get(vehicleUrl)).thenReturn(jsonVehicle);

		Map<String, JSONObject> vehicles = dataDownloaderService.downloadVehicles();

		Assert.assertEquals("6352185209772835696", vehicles.get("6352185209772835696").get("duid"));
		Assert.assertEquals(5, vehicles.get("6352185209772835696").get("category"));
		Assert.assertEquals(191820703, vehicles.get("6352185209772835696").get("latitude"));
		Assert.assertEquals(-32569902, vehicles.get("6352185209772835696").get("longitude"));
		Assert.assertEquals(19, vehicles.get("6352185209772835696").get("bearing"));
		Assert.assertEquals(0, vehicles.get("6352185209772835696").get("is_accessible"));
		Assert.assertEquals(false, vehicles.get("6352185209772835696").get("is_deleted"));
		Assert.assertEquals("6351558488880701746", Utils.getDuid(vehicles.get("6352185209772835696"), "trip_duid"));
		Assert.assertEquals("6349931210947571043", Utils.getDuid(vehicles.get("6352185209772835696"), "pattern_duid"));
		Assert.assertEquals(0, vehicles.get("6352185209772835696").get("has_bike_rack"));
		Assert.assertEquals(1502659204867L, vehicles.get("6352185209772835696").get("last_modification_timestamp"));
		Assert.assertEquals(880, vehicles.get("6352185209772835696").get("vehicle_number"));
		Assert.assertEquals(880, vehicles.get("6352185209772835696").get("operational_number"));
		Assert.assertEquals(1, vehicles.get("6352185209772835696").get("geo_position_status"));
		Assert.assertEquals(1502659204, vehicles.get("6352185209772835696").get("reference_time"));
	}

}
