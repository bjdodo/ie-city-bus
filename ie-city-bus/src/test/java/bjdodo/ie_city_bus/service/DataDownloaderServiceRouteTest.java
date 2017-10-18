package bjdodo.ie_city_bus.service;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DataDownloaderServiceRouteTest {

	@InjectMocks
	private DataDownloaderServiceImpl dataDownloaderService;

	@Mock
	private HttpService httpService;

	// @Mock
	// private RouteRepository routeRepository;

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


		Mockito.when(httpService.get(routeUrl)).thenReturn(jsonRoute);

		Map<String, JSONObject> routes = dataDownloaderService.downloadRoutes();

		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
		Mockito.verify(httpService).get(captor.capture());
		Assert.assertEquals(routeUrl, captor.getValue());

		Assert.assertEquals("6350571126703259824", routes.get("6350571126703259824").get("duid"));
		Assert.assertEquals(1502532767028L, routes.get("6350571126703259824").get("last_modification_timestamp"));
		Assert.assertEquals(false, routes.get("6350571126703259824").get("is_deleted"));
		Assert.assertEquals(5, routes.get("6350571126703259824").get("category"));
		Assert.assertEquals(403, routes.get("6350571126703259824").get("number"));
	}

}
