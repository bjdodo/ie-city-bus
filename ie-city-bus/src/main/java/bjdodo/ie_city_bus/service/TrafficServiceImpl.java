package bjdodo.ie_city_bus.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bjdodo.ie_city_bus.controller.data.StopPassageHistoryByStop;
import bjdodo.ie_city_bus.model.TrafficServiceStopPassage;
import bjdodo.ie_city_bus.model.TripDetailStopPassage;
import bjdodo.ie_city_bus.repository.crud.StopPassageRepository;
import bjdodo.ie_city_bus.utils.TripSectionHistoryContainer;

@Service
public class TrafficServiceImpl implements TrafficService {

	private static final Logger log = LoggerFactory.getLogger(TrafficServiceImpl.class);

	@Autowired
	StopPassageRepository stopPassageRepository;

	@Autowired
	private ConfigurationService configurationService;

	@PostConstruct
	public void init() {
		log.info("PredictionServiceImpl init() starting");
		
		recentHistory = new TripSectionHistoryContainer(
				p -> p.getStop2Time().isBefore(
						Instant.now().minusSeconds(60 * configurationService.getPredictionRecentMaxAgeMinutes())));

		List<TrafficServiceStopPassage> passages = stopPassageRepository.findRecentStopPassagesForPredictionService(
				Instant.now().minusSeconds(60 * configurationService.getPredictionRecentMaxAgeMinutes()));

		for (int idx=0; idx<passages.size()-1; idx++) {
			
			TrafficServiceStopPassage passage = passages.get(idx);
			TrafficServiceStopPassage nextPassage = passages.get(idx+1);
			
			if (passage.getTripId() != nextPassage.getTripId()) {
				continue;
			}
			
			recentHistory.recordSectionPassage(passage.getRouteShortName(), passage.getTripId(),
					passage.getTripStartTime(), passage.getStopNumber(), passage.getPassageTime(),
					nextPassage.getStopNumber(), nextPassage.getPassageTime());
		}
		
		log.info(String.format("PredictionServiceImpl init() finished. Loaded %s passages from DB", passages.size()));
		
		// "select t.id, COALESCE(spa.actualDeparture, spa.actualArrival), spo.number, 
		// r.shortName\r\n" +
		// "from StopPassage spa, StopPoint spo, Trip t, Route r\r\n" +
		// "where spa.stopPointId=spo.id and\r\n" +
		// "spa.tripId=t.id and\r\n" +
		// "t.routeId=r.id and\r\n" +
		// "COALESCE(spa.actualDeparture, spa.actualArrival) > :after\r\n" +
		// "order by t.id,  COALESCE(spa.actualDeparture, spa.actualArrival)"

		// select *
		// from stop_passage
		// inner join stop_point on stop_passage.stop_point_id=stop_point.id
		// inner join trip on stop_passage.trip_id=trip.id
		// inner join ROUTE on trip.route_id=route.id
		// where COALESCE(stop_passage.ACTUAL_DEPARTURE, stop_passage.ACTUAL_ARRIVAL) >
		// '2017-12-14T16:11:00'
		// order by trip.id, COALESCE(stop_passage.ACTUAL_DEPARTURE,
		// stop_passage.ACTUAL_ARRIVAL)
	}

	@Override
	public void recordRecentSectionPassage(String routeShortName, long tripId, Instant tripScheduledStartTime,
			int stopPoint1Number, Instant stop1Time, int stopPoint2Number, Instant stop2Time) {
		
		recentHistory.recordSectionPassage(routeShortName, tripId, tripScheduledStartTime,
				stopPoint1Number, stop1Time,
				stopPoint2Number, stop2Time);
	}

	@Override
	public List<StopPassageHistoryByStop> getRecentPassageHistoryForStops(List<TripDetailStopPassage> stopPassages) {

		List<Pair<Long, Long>> sections = new ArrayList<>();

		for (int idx = 0; idx < stopPassages.size() - 1; idx++) {
			sections.add(Pair.of(stopPassages.get(idx).getStopNumber(), stopPassages.get(idx + 1).getStopNumber()));
		}

		List<List<TripSectionPassage>> history = recentHistory.getSectionPassageHistory(sections);

		List<StopPassageHistoryByStop> ret = new ArrayList<>();
		for (int idx = 0; idx < history.size(); idx++) {
			ret.add(new StopPassageHistoryByStop(stopPassages.get(idx), stopPassages.get(idx + 1), history.get(idx)));
		}
		return ret;

	}

	@Override
	public void cleanupOld() {
		recentHistory.cleanup();
	}

	private TripSectionHistoryContainer recentHistory;
	
	

}
