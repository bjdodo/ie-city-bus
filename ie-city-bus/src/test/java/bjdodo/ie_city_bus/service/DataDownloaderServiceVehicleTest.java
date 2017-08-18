package bjdodo.ie_city_bus.service;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import bjdodo.ie_city_bus.model.Vehicle;
import bjdodo.ie_city_bus.repository.VehicleRepository;

public class DataDownloaderServiceVehicleTest {

	@InjectMocks
	private DataDownloaderServiceImpl dataDownloaderService;

	@Mock
	private HttpService httpService;

	@Mock
	private VehicleRepository vehicleRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	String vehicleUrl = "http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484";

	// @formatter:off
	String jsonVehicle = "{ \"vehicleTdi\":" + "		 {\"bus_0\":" + "		 {\"duid\": \"6352185209772835696\","
			+ "		 \"last_modification_timestamp\": 1502659204867," + "		 \"is_deleted\": false,"
			+ "		 \"category\": 5,"
			+ "		 \"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488880701746\",\"foo\": 0},"
			+ "		 \"geo_position_status\": 1," + "		 \"reference_time\": 1502659204,"
			+ "		 \"latitude\": 191820703," + "		 \"longitude\": -32569902," + "		 \"bearing\": 19,"
			+ "		 \"is_accessible\": 0,"
			+ "		 \"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571043\",\"foo\": 0},"
			+ "		 \"has_bike_rack\": 0," + "		 \"vehicle_number\": 880," + "		 \"operational_number\": 880,"
			+ "		 \"foo\": 0}" + "		 }" + "		 }";
	// @formatter:on

	@Test
	public void testDownloadVehicles() throws JSONException {

		// String url =
		// "http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484";

		Mockito.when(httpService.get(vehicleUrl)).thenReturn(jsonVehicle);

		Mockito.when(vehicleRepository.countByDuid("6352185209772835696")).thenReturn(0);

		dataDownloaderService.downloadVehicles();

		ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
		Mockito.verify(vehicleRepository).saveAndFlush(captor.capture());

		Assert.assertEquals(captor.getValue().getDuid(), "6352185209772835696");
		Assert.assertEquals(captor.getValue().getCategory(), 5);
		Assert.assertEquals(captor.getValue().getLatLong(), "POINT(53.28352861111111 -9.047195)");
		Assert.assertEquals(captor.getValue().getBearing(), 19);
		Assert.assertEquals(captor.getValue().isIs_accessible(), false);
		Assert.assertEquals(captor.getValue().isIs_deleted(), false);
		Assert.assertEquals(captor.getValue().getTrip_duid(), "6351558488880701746");
		Assert.assertEquals(captor.getValue().getPattern_duid(), "6349931210947571043");
		Assert.assertEquals(captor.getValue().isHas_bike_rack(), false);
		// Assert.assertEquals(captor.getValue().getLast_modification_timestamp(),
		// 1502659204867L);
		Assert.assertEquals(captor.getValue().getVehicle_number(), 880L);
		Assert.assertEquals(captor.getValue().getOperational_number(), 880L);
		Assert.assertEquals(captor.getValue().getGeo_position_status(), 1);
		// Assert.assertEquals(captor.getValue().getReference_time(), new
		// Date(1502659204));
	}

	@Test
	public void testDownloadVehiclesAlreadyExists() throws JSONException {

		Mockito.when(httpService.get(vehicleUrl)).thenReturn(jsonVehicle);

		Mockito.when(vehicleRepository.countByDuid("6352185209772835696")).thenReturn(1);

		dataDownloaderService.downloadVehicles();

		Mockito.verify(vehicleRepository, Mockito.never()).save(Mockito.any(Vehicle.class));

	}

}
