package bjdodo.ie_city_bus.model;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import bjdodo.ie_city_bus.AppTest;
import org.junit.Assert;

public class RouteTest {

	@Test
	public void test() throws JSONException {
		String json = " {\"duid\": \"6350571126703259823\",\"last_modification_timestamp\": 1502532767028,\"is_deleted\": false,\"short_name\": \"402\",\"direction_extensions\": {\"direction\": 1,\"direction_name\": \"\",\"foo\": 0},\"direction_extensions\": {\"direction\": 2,\"direction_name\": \"\",\"foo\": 0},\"number\": 402,\"category\": 5,\"foo\": 0}";

		JSONObject jObj = new JSONObject(json);
		Route r = Route.fromBuseireannJson(jObj);
		
		Assert.assertEquals(r.getLast_modification_timestamp(), 1502532767028L);
		Assert.assertEquals(r.isIs_deleted(), false);
		Assert.assertEquals(r.getShort_name(), "402");
		Assert.assertEquals(r.getCategory(), 5);
		Assert.assertEquals(r.getDuid(), "6350571126703259823");
		Assert.assertEquals(r.getNumber(), 402);
		
	}

}
