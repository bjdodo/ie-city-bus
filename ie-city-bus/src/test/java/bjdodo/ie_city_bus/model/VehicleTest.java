package bjdodo.ie_city_bus.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import bjdodo.ie_city_bus.model.crud.Vehicle;

public class VehicleTest {

	@Test
	public void testVehicleJSONObject() throws JSONException {
		 String json = " {\"duid\": \"6352185209772835696\","
		 + " \"last_modification_timestamp\": 1502659204867, \"is_deleted\": false," +
		 " \"category\": 5,"
		 + " \"trip_duid\": {\"structTag\": 50471,\"duid\":	\"6351558488880701746\",\"foo\": 0},"
		 + " \"geo_position_status\": 1, \"reference_time\": 1502659204,"
				+ " \"latitude\": 191820703, \"longitude\": -32569902," + " \"bearing\": 19, \"is_accessible\": 0,"
				+ " \"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571043\",\"foo\": 0},"
				+ " \"has_bike_rack\": 0, \"vehicle_number\": 880," + " \"operational_number\": 880, \"foo\": 0}";
		
		JSONObject jObj = new JSONObject(json);
		Vehicle v = new Vehicle();
		v.updateFromJson(jObj);
		
		Assert.assertEquals("6352185209772835696", v.getDuid());
		Assert.assertEquals("6351558488880701746", v.getTripDuid());
		Assert.assertEquals(1502659204867L, v.getLastModificationTimestamp().toEpochMilli());
		Assert.assertEquals(1, v.getGeoPositionStatus());
		Assert.assertEquals(1502659204, v.getReferenceTime().getEpochSecond());
		Assert.assertEquals(880, v.getOperationalNumber());
		Assert.assertEquals(880, v.getVehicleNumber());
		Assert.assertEquals(false, v.isAccessible());
		Assert.assertEquals("6349931210947571043", v.getPatternDuid());
		Assert.assertEquals(19, v.getBearing());
		Assert.assertEquals(false, v.isDeleted());
		Assert.assertEquals("POINT (53.28352861111111 -9.047195)", v.getLatLong());
		// Assert.assertEquals(5, v.getCategory());

	}

}
