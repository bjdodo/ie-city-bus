package bjdodo.ie_city_bus.service;

import java.lang.reflect.Method;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import bjdodo.ie_city_bus.repository.crud.RouteRepository;
import bjdodo.ie_city_bus.repository.crud.StopPassageRepository;
import bjdodo.ie_city_bus.repository.crud.StopPointRepository;
import bjdodo.ie_city_bus.repository.crud.TripRepository;
import bjdodo.ie_city_bus.repository.crud.VehicleRepository;

public class DataSavingServiceTest {

	@InjectMocks
	private DataSavingServiceImpl dataSavingServiceImpl;
	@Mock
	private DataDownloaderServiceImpl dataDownloaderServiceImpl;
	@Mock
	private VehicleRepository vehicleRepository;
	@Mock
	private TripRepository tripRepository;
	@Mock
	private RouteRepository routeRepository;
	@Mock
	private StopPointRepository stopPointRepository;
	@Mock
	private StopPassageRepository stopPassageRepository;

	static class MockitoIncrIdArgAnswer<T> implements Answer<T> {

		private static long id = 1000;

		private int argIdx;

		public MockitoIncrIdArgAnswer(int argIdx) {
			this.argIdx = argIdx;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T answer(InvocationOnMock invocation) throws Throwable {

			T ret = (T) invocation.getArguments()[argIdx];
			Method mSetId = ret.getClass().getMethod("setId", long.class);
			Method mGetId = ret.getClass().getMethod("getId");
			if (mSetId != null && mGetId != null) {
				Long currentId = (Long) mGetId.invoke(ret);
				if (currentId == null || currentId == 0) {
					++id;
					mSetId.invoke(ret, id);
				}
			}
			return ret;
		}
	}

	private String vehicle1 = "{\"duid\": \"6352185209772835696\",\"last_modification_timestamp\": 1508426977251,\"is_deleted\": false,\"category\": 5,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488905962536\",\"foo\": 0},\"geo_position_status\": 1,\"reference_time\": 1508426976,\"latitude\": 191837820,\"longitude\": -32574469,\"bearing\": 129,\"is_accessible\": 0,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947571044\",\"foo\": 0},\"has_bike_rack\": 0,\"vehicle_number\": 880,\"operational_number\": 880,\"foo\": 0}";
	private String vehicle2 = "{\"duid\": \"6352185209772835695\",\"last_modification_timestamp\": 1508426975207,\"is_deleted\": false,\"category\": 5,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488901186572\",\"foo\": 0},\"geo_position_status\": 1,\"reference_time\": 1508426974,\"latitude\": 191813317,\"longitude\": -32633593,\"bearing\": 324,\"is_accessible\": 0,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947515429\",\"foo\": 0},\"has_bike_rack\": 0,\"vehicle_number\": 879,\"operational_number\": 879,\"foo\": 0}";

	private String route1 = "{\"duid\": \"6350571126703260049\",\"last_modification_timestamp\": 1508261653275,\"is_deleted\": false,\"short_name\": \"601\",\"direction_extensions\": {\"direction\": 1,\"direction_name\": \"\",\"foo\": 0},\"direction_extensions\": {\"direction\": 2,\"direction_name\": \"\",\"foo\": 0},\"number\": 601,\"category\": 5,\"foo\": 0}";
	private String route2 = "{\"duid\": \"6350571126703260050\",\"last_modification_timestamp\": 1508261653275,\"is_deleted\": false,\"short_name\": \"602\",\"direction_extensions\": {\"direction\": 1,\"direction_name\": \"\",\"foo\": 0},\"direction_extensions\": {\"direction\": 2,\"direction_name\": \"\",\"foo\": 0},\"number\": 602,\"category\": 5,\"foo\": 0}";

	private String stopPassages_trip1 = "{\"passage_0\": {\"duid\": \"-9223372006293325947\",\"last_modification_timestamp\": 1508489433517,\"is_deleted\": false,\"route_duid\": {\"structTag\": 50466,\"duid\": \"6350571126703259823\",\"foo\": 0},\"direction\": 2,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488843998469\",\"foo\": 0},\"stop_point_duid\": {\"structTag\": 50465,\"duid\": \"6350786630982826905\",\"foo\": 0},\"vehicle_duid\": {\"structTag\": 50470,\"duid\": \"6356266596935140008\",\"foo\": 0},\"arrival_data\": {\"scheduled_passage_time_utc\": 1508491680,\"scheduled_passage_time\": \"10:28\",\"actual_passage_time_utc\": 1508491681,\"actual_passage_time\": \"10:28\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Merlin Park\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"departure_data\": {\"scheduled_passage_time_utc\": 1508491680,\"scheduled_passage_time\": \"10:28\",\"actual_passage_time_utc\": 1508491681,\"actual_passage_time\": \"10:28\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Seacrest\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"congestion_level\": 1,\"accuracy_level\": 3,\"status\": 1,\"is_accessible\": 0,\"latitude\": 191813517,\"longitude\": -32517458,\"bearing\": 318,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947384349\",\"foo\": 0},\"has_bike_rack\": 0,\"category\": 5,\"foo\": 0},\"passage_1\": {\"duid\": \"-9223372006293325948\",\"last_modification_timestamp\": 1508489433517,\"is_deleted\": false,\"route_duid\": {\"structTag\": 50466,\"duid\": \"6350571126703259823\",\"foo\": 0},\"direction\": 2,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488843998469\",\"foo\": 0},\"stop_point_duid\": {\"structTag\": 50465,\"duid\": \"6350786630982826895\",\"foo\": 0},\"vehicle_duid\": {\"structTag\": 50470,\"duid\": \"6356266596935140008\",\"foo\": 0},\"arrival_data\": {\"scheduled_passage_time_utc\": 1508491560,\"scheduled_passage_time\": \"10:26\",\"actual_passage_time_utc\": 1508491561,\"actual_passage_time\": \"10:26\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Merlin Park\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"departure_data\": {\"scheduled_passage_time_utc\": 1508491560,\"scheduled_passage_time\": \"10:26\",\"actual_passage_time_utc\": 1508491561,\"actual_passage_time\": \"10:26\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Seacrest\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"congestion_level\": 1,\"accuracy_level\": 3,\"status\": 1,\"is_accessible\": 0,\"latitude\": 191813517,\"longitude\": -32517458,\"bearing\": 318,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947384349\",\"foo\": 0},\"has_bike_rack\": 0,\"category\": 5,\"foo\": 0},\"passage_2\": {\"duid\": \"-9223372006293325946\",\"last_modification_timestamp\": 1508489433517,\"is_deleted\": false,\"route_duid\": {\"structTag\": 50466,\"duid\": \"6350571126703259823\",\"foo\": 0},\"direction\": 2,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488843998469\",\"foo\": 0},\"stop_point_duid\": {\"structTag\": 50465,\"duid\": \"6350786630982826085\",\"foo\": 0},\"vehicle_duid\": {\"structTag\": 50470,\"duid\": \"6356266596935140008\",\"foo\": 0},\"arrival_data\": {\"scheduled_passage_time_utc\": 1508491800,\"scheduled_passage_time\": \"10:30\",\"actual_passage_time_utc\": 1508491801,\"actual_passage_time\": \"10:30\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Merlin Park\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"congestion_level\": 1,\"accuracy_level\": 3,\"status\": 1,\"is_accessible\": 0,\"latitude\": 191813517,\"longitude\": -32517458,\"bearing\": 318,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947384349\",\"foo\": 0},\"has_bike_rack\": 0,\"category\": 5,\"foo\": 0},\"passage_3\": {\"duid\": \"-9223372006293325951\",\"last_modification_timestamp\": 1508489433517,\"is_deleted\": false,\"route_duid\": {\"structTag\": 50466,\"duid\": \"6350571126703259823\",\"foo\": 0},\"direction\": 2,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488843998469\",\"foo\": 0},\"stop_point_duid\": {\"structTag\": 50465,\"duid\": \"6350786630982826865\",\"foo\": 0},\"vehicle_duid\": {\"structTag\": 50470,\"duid\": \"6356266596935140008\",\"foo\": 0},\"arrival_data\": {\"scheduled_passage_time_utc\": 1508491320,\"scheduled_passage_time\": \"10:22\",\"actual_passage_time_utc\": 1508491321,\"actual_passage_time\": \"10:22\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Merlin Park\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"departure_data\": {\"scheduled_passage_time_utc\": 1508491320,\"scheduled_passage_time\": \"10:22\",\"actual_passage_time_utc\": 1508491321,\"actual_passage_time\": \"10:22\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Seacrest\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"congestion_level\": 1,\"accuracy_level\": 3,\"status\": 1,\"is_accessible\": 0,\"latitude\": 191813517,\"longitude\": -32517458,\"bearing\": 318,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947384349\",\"foo\": 0},\"has_bike_rack\": 0,\"category\": 5,\"foo\": 0},\"passage_4\": {\"duid\": \"-9223372006293325952\",\"last_modification_timestamp\": 1508489433517,\"is_deleted\": false,\"route_duid\": {\"structTag\": 50466,\"duid\": \"6350571126703259823\",\"foo\": 0},\"direction\": 2,\"trip_duid\": {\"structTag\": 50471,\"duid\": \"6351558488843998469\",\"foo\": 0},\"stop_point_duid\": {\"structTag\": 50465,\"duid\": \"6350786630982826855\",\"foo\": 0},\"vehicle_duid\": {\"structTag\": 50470,\"duid\": \"6356266596935140008\",\"foo\": 0},\"arrival_data\": {\"scheduled_passage_time_utc\": 1508491260,\"scheduled_passage_time\": \"10:21\",\"actual_passage_time_utc\": 1508491261,\"actual_passage_time\": \"10:21\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Merlin Park\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"departure_data\": {\"scheduled_passage_time_utc\": 1508491260,\"scheduled_passage_time\": \"10:21\",\"actual_passage_time_utc\": 1508491261,\"actual_passage_time\": \"10:21\",\"service_mode\": 1,\"multilingual_direction_text\": {\"defaultValue\": \"Seacrest\",\"foo\": 0},\"type\": 1,\"foo\": 0},\"congestion_level\": 1,\"accuracy_level\": 3,\"status\": 1,\"is_accessible\": 0,\"latitude\": 191813517,\"longitude\": -32517458,\"bearing\": 318,\"pattern_duid\": {\"structTag\": 50472,\"duid\": \"6349931210947384349\",\"foo\": 0},\"has_bike_rack\": 0,\"category\": 5,\"foo\": 0}";

	void addJsonObjToMap(Map<String, JSONObject> map, String json) throws JSONException {
		JSONObject j = new JSONObject(json);
		map.put(j.getString("duid"), j);
	}

	void addPassagesToMap(Map<String, JSONObject> map, String json) throws JSONException {
		JSONObject j = new JSONObject(json);
		int idx = -1;
		while (true) {
			++idx;
			JSONObject jPassage = j.optJSONObject("passage_" + idx);
			if (jPassage == null) {
				break;
			}
			map.put(jPassage.getString("duid"), jPassage);
		}
	}


	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(dataSavingServiceImpl, "monitoredRoutes",
				"401,402,403,404,405,407,409");
	}

	@Test
	public void downloadAndSaveDataTest() throws JSONException {

		// Map<String, JSONObject> jsonVehicles = new HashMap<>();
		// addJsonObjToMap(jsonVehicles, vehicle1);
		// // addJsonObjToMap(jsonVehicles, vehicle2);
		// Mockito.when(dataDownloaderServiceImpl.downloadVehicles()).thenReturn(jsonVehicles);
		//
		// Map<String, JSONObject> jsonRoutes = new HashMap<>();
		// addJsonObjToMap(jsonRoutes, route1);
		// // addJsonObjToMap(jsonRoutes, route2);
		// Mockito.when(dataDownloaderServiceImpl.downloadRoutes()).thenReturn(jsonRoutes);
		//
		// Map<String, JSONObject> jsonStopPoints = new HashMap<>();
		// Mockito.when(dataDownloaderServiceImpl.downloadStopPoints()).thenReturn(jsonStopPoints);
		//
		// Map<String, JSONObject> jsonStopPassages_trip1 = new HashMap<>();
		// addPassagesToMap(jsonStopPassages_trip1, stopPassages_trip1);
		// Mockito.when(dataDownloaderServiceImpl.downloadStopPassages("6351558488905962536"))
		// .thenReturn(jsonStopPassages_trip1);
		//
		// // Map<String, JSONObject> jsonStopPassages_trip2 = new HashMap<>();
		// //
		// Mockito.when(dataDownloaderServiceImpl.downloadStopPassages("2")).thenReturn(jsonStopPassages_trip2);
		//
		// dataSavingServiceImpl.downloadAndSaveAll();
		//
		// Mockito.when(vehicleRepository.saveAndFlush(org.mockito.Mockito.any()))
		// .thenAnswer(new MockitoIncrIdArgAnswer<Vehicle>(0));
		//
		// Mockito.when(routeRepository.saveAndFlush(org.mockito.Mockito.any()))
		// .thenAnswer(new MockitoIncrIdArgAnswer<Route>(0));
		//
		// Mockito.when(stopPointRepository.saveAndFlush(org.mockito.Mockito.any()))
		// .thenAnswer(new MockitoIncrIdArgAnswer<StopPoint>(0));
		//
		// Mockito.when(stopPassageRepository.saveAndFlush(org.mockito.Mockito.any()))
		// .thenAnswer(new MockitoIncrIdArgAnswer<StopPassage>(0));

	}
}
