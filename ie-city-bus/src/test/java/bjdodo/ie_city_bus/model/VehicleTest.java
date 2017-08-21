package bjdodo.ie_city_bus.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class VehicleTest {

	@Test
	public void testVehicleJSONObject() throws JSONException {
		String json = " {\"duid\": \"6352185209772835696\","
				+ " \"last_modification_timestamp\": 1502659204867, \"is_deleted\": false," + " \"category\": 5,"
				+ " \"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488880701746\",\"foo\": 0},"
				+ " \"geo_position_status\": 1, \"reference_time\": 1502659204,"
				+ " \"latitude\": 191820703, \"longitude\": -32569902," + " \"bearing\": 19, \"is_accessible\": 0,"
				+ " \"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571043\",\"foo\": 0},"
				+ " \"has_bike_rack\": 0, \"vehicle_number\": 880," + " \"operational_number\": 880, \"foo\": 0}";

		JSONObject jObj = new JSONObject(json);
		Vehicle v = Vehicle.fromBuseireannJson(jObj);
		
		Assert.assertEquals(v.getDuid(), "6352185209772835696");
		Assert.assertEquals(v.getTripDuid(), "6351558488880701746");
		// Assert.assertEquals(v.getLast_modification_timestamp(), new
		// Date(1502659204867L));
		Assert.assertEquals(v.getGeoPositionStatus(), 1);
		// Assert.assertEquals(v.getReference_time(), new Date(1502659204));
		Assert.assertEquals(v.getOperationalNumber(), 880);
		Assert.assertEquals(v.getVehicleNumber(), 880);
		Assert.assertEquals(v.isAccessible(), false);
		Assert.assertEquals(v.getPatternDuid(), "6349931210947571043");
		Assert.assertEquals(v.getBearing(), 19);
		Assert.assertEquals(v.isDeleted(), false);
		Assert.assertEquals(v.getLatLong(), "POINT(53.28352861111111 -9.047195)");
		Assert.assertEquals(v.hasBikeRack(), false);
		Assert.assertEquals(v.getCategory(), 5);

	}

}
