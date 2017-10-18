package bjdodo.ie_city_bus.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DataDownloaderServiceImpl implements DataDownloaderService {

	private static final Logger log = LoggerFactory.getLogger(DataDownloaderServiceImpl.class);

	@Autowired
	private HttpService httpService;

	// This is injected from the config file
	@Value("${ie_city_bus.latLongRectangle}")
	String latLongRectangle;

	@Value("${ie_city_bus.downloadedDataSaveDir}")
	String downloadedDataSaveDir;

	private String logTimeStamp;

	// This is only useful for managing the logfiles if we save the downloaded data
	// for diagnostics
	public void startDownloadBatch() {

		logTimeStamp = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Z"))
				.format(Instant.now(Clock.systemUTC())).replaceAll("-", "").replaceAll(":", "");

		if (downloadedDataSaveDir == null || downloadedDataSaveDir.isEmpty()) {
			return;
		}

		File logDir = new File(downloadedDataSaveDir);
		if (!logDir.exists() || !logDir.isDirectory()) {
			log.debug(
					"saveDataToLog() cannot do its job because downloadedDataSaveDir points to a non-existent directory");
			return;
		}

		File dir = new File(downloadedDataSaveDir);
		File[] files = dir.listFiles();
		Arrays.sort(files, Collections.reverseOrder());
		for (int idx = 2000; idx < files.length; ++idx) {
			files[idx].delete();
		}
	}

	private void saveDataToLog(String logName, String data) {

		if (downloadedDataSaveDir == null || downloadedDataSaveDir.isEmpty()) {
			return;
		}

		File logDir = new File(downloadedDataSaveDir);
		if (!logDir.exists() || !logDir.isDirectory()) {
			log.debug(
					"saveDataToLog() cannot do its job because downloadedDataSaveDir points to a non-existent directory");
			return;
		}

		if (!downloadedDataSaveDir.endsWith(File.separator)) {
			downloadedDataSaveDir += File.separator;
		}

		File logFile = new File(downloadedDataSaveDir + logTimeStamp + "_" + logName + ".log");

		// log.info("Saving downloaded data into " + logFile.getPath());

		try (FileWriter fw = new FileWriter(logFile)) {
			try (PrintWriter pw = new PrintWriter(fw)) {
				pw.append(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("saveDataToLog() caught an exception", e);
		}

	}

	@Override
	public Map<String, JSONObject> downloadVehicles() throws JSONException {

		String resp = httpService.get(
				"http://buseireann.ie/inc/proto/vehicleTdi.php?" + latLongRectangle);
		saveDataToLog("downloadVehicles", resp);
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero vehicles");
			return new HashMap<String, JSONObject>();
		}


		log.trace("downloadVehicles " + resp);
		JSONObject obj = new JSONObject(resp);

		JSONObject vehicleTdi = obj.getJSONObject("vehicleTdi");

		Map<String, JSONObject> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject bus = vehicleTdi.optJSONObject("bus_" + cnt++);
			if (bus == null) {
				break;
			} else {
				ret.put(bus.getString("duid"), bus);
			}
		}

		return ret;
	}

	@Override
	public Map<String, JSONObject> downloadRoutes() throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/routes.php");
		// saveDataToLog("downloadRoutes", resp);
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, JSONObject>();
		}

		// log.trace("downloadRoutes " + resp);

		// This text starts with a javascript variable declaration, we trim that off and
		// the ; from the end
		if (!resp.startsWith("var obj_routes = ") || !resp.endsWith(";")) {
			log.error(String.format("Unexpected string returned for routes '%s...'", resp.substring(0, 100)));
			return new HashMap<String, JSONObject>();
		}
		resp = resp.substring("var obj_routes = ".length());
		resp = resp.substring(0, resp.length() - 1);
		
		// The field direction_extensions appears twice in every entry which is invalid.
		// I need to get rid of it.
		int cnt=0;
		resp = resp.replace("\"direction_extensions\": {\"direction\": 1",
				"\"direction_extensions_1\": {\"direction\": 1");
		resp = resp.replace("\"direction_extensions\": {\"direction\": 2",
				"\"direction_extensions_2\": {\"direction\": 2");

		JSONObject obj = new JSONObject(resp);

		JSONObject routeTdi = obj.getJSONObject("routeTdi");

		Map<String, JSONObject> ret = new HashMap<>();

		while (true) {
			JSONObject route = routeTdi.optJSONObject("routes_" + cnt++);
			if (route == null) {
				break;
			} else {
				ret.put(route.getString("duid"), route);
			}
		}
		return ret;
	}

	public Map<String, JSONObject> downloadStopPoints() throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/bus_stop_points.php");
		// saveDataToLog("downloadStopPoints", resp);
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, JSONObject>();
		}

		// log.trace("downloadStopPoints " + resp);

		// This text starts with a javascript variable declaration, we trim that off and
		// the ; from the end
		if (!resp.startsWith("var obj_bus_stop_points = ") || !resp.endsWith(";")) {
			log.error(String.format("Unexpected string returned for bus stops '{0}...'", resp.substring(0, 100)));
			return new HashMap<String, JSONObject>();
		}
		resp = resp.substring("var obj_bus_stop_points = ".length());
		resp = resp.substring(0, resp.length() - 1);

		JSONObject obj = new JSONObject(resp);

		JSONObject bus_stops = obj.getJSONObject("bus_stops");

		Map<String, JSONObject> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject stopPoint = bus_stops.optJSONObject("bus_stop_" + cnt++);
			if (stopPoint == null) {
				break;
			} else {
				ret.put(stopPoint.getString("duid"), stopPoint);
			}
		}
		return ret;
	}

	public Map<String, JSONObject> downloadStopPassages(String tripDuid) throws JSONException {

		String resp = httpService.get("http://buseireann.ie/inc/proto/stopPassageTdi.php?trip=" + tripDuid);
		saveDataToLog("downloadStopPassages+" + tripDuid, resp);
		if (resp == null || resp.isEmpty()) {
			log.info("http get request returned zero routes");
			return new HashMap<String, JSONObject>();
		}

		// log.trace("downloadStopPassages [tripduid:" + tripDuid + "] " + resp);

		JSONObject obj = new JSONObject(resp);

		JSONObject stopPassageTdi = obj.getJSONObject("stopPassageTdi");

		Map<String, JSONObject> ret = new HashMap<>();

		int cnt = 0;
		while (true) {
			JSONObject stopPassage = stopPassageTdi.optJSONObject("passage_" + cnt++);
			if (stopPassage == null) {
				break;
			} else {

				ret.put(stopPassage.getString("duid"), stopPassage);
			}
		}
		return ret;
	}
}
