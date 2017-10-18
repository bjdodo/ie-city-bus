package bjdodo.ie_city_bus.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import bjdodo.ie_city_bus.model.crud.Route;

public class RouteTest {

	@Test
	public void test() throws JSONException {
		String json = " {\"duid\":	\"6350571126703259823\",\"last_modification_timestamp\": 1502532767028,\"is_deleted\": false,\"short_name\": \"402\",\"direction_extensions\": {\"direction\": 1,\"direction_name\": \"\",\"foo\": 0},\"direction_extensions\": {\"direction\": 2,\"direction_name\": \"\",\"foo\": 0},\"number\": 402,\"category\": 5,\"foo\": 0}";

		JSONObject jObj = new JSONObject(json);
		Route r = new Route();
		r.updateFromJson(jObj);

		Assert.assertEquals(1502532767028L, r.getLastModificationTimestamp().toEpochMilli());
		Assert.assertEquals(false, r.isDeleted());
		Assert.assertEquals("402", r.getShortName());
		Assert.assertEquals(5, r.getCategory());
		Assert.assertEquals("6350571126703259823", r.getDuid());
		Assert.assertEquals(402, r.getNumber());
		
	}

}
