package bjdodo.ie_city_bus.service;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import bjdodo.ie_city_bus.repository.crud.RouteRepository;

public class DataDownloaderServiceRouteTest {

	@InjectMocks
	private DataDownloaderServiceImpl dataDownloaderService;

	@Mock
	private HttpService httpService;

	@Mock
	private RouteRepository routeRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	String routeUrl = "http://buseireann.ie/inc/proto/routes.php";

	// @formatter:off
	String jsonRoute = "var obj_routes = {" + 
			"\"routeTdi\": {\"routes_0\": {" + 
			"	\"duid\": \"6350571126703259824\"," + 
			"	\"last_modification_timestamp\": 1502532767028," + 
			"	\"is_deleted\": false," + 
			"	\"short_name\": \"403\"," + 
			"	\"direction_extensions\": {\"direction\": 1,\"direction_name\": \"\",\"foo\": 0}," + 
			"	\"direction_extensions\": {\"direction\": 2,\"direction_name\": \"\",\"foo\": 0}," + 
			"	\"number\": 403," + 
			"	\"category\": 5," + 
			"	\"foo\": 0" + 
			"	}}};";
	// @formatter:on
	
	@Test
	public void testDownloadRoutes() throws JSONException {

		// Mockito.when(httpService.get(routeUrl)).thenReturn(jsonRoute);
		//
		// Mockito.when(routeRepository.countByDuid("6350571126703259824")).thenReturn(0L);
		//
		// dataDownloaderService.downloadRoutes();
		//
		// ArgumentCaptor<Route> captor = ArgumentCaptor.forClass(Route.class);
		// Mockito.verify(routeRepository).saveAndFlush(captor.capture());
		//
		// Assert.assertEquals(captor.getValue().getDuid(), "6350571126703259824");
		// Assert.assertEquals(captor.getValue().getLastModificationTimestamp(),
		// 1502532767028L);
		// Assert.assertEquals(captor.getValue().isDeleted(), false);
		// Assert.assertEquals(captor.getValue().getShortName(), "403");
		// Assert.assertEquals(captor.getValue().getCategory(), 5);
		// Assert.assertEquals(captor.getValue().getNumber(), 403);

	}

}
