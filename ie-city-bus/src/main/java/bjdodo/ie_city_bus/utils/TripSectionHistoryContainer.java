package bjdodo.ie_city_bus.utils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bjdodo.ie_city_bus.service.TrafficService;
import bjdodo.ie_city_bus.service.TrafficService.TripSectionPassage;

public class TripSectionHistoryContainer {

	private static final Logger log = LoggerFactory.getLogger(TripSectionHistoryContainer.class);

	public TripSectionHistoryContainer(Predicate<TrafficService.TripSectionPassage> cleanupPredicate) {
		this.cleanupPredicate = cleanupPredicate;
	}

	public synchronized void recordSectionPassage(String routeShortName, long tripId, Instant tripScheduledStartTime,
			long stopPoint1Number, Instant stop1Time, long stopPoint2Number, Instant stop2Time) {

		String fragmentsKey = stopPoint1Number + "_" + stopPoint2Number;

//		if (stop1Time.isAfter(Instant.now()) || stop2Time.isAfter(Instant.now())) {
//
//			log.debug("TripSectionHistoryContainer (after now so) NOT adding " + tripId + " to " + fragmentsKey
//					+ " " + stopPoint1Number + "@" + stop1Time.toString()
//					+ " " + stopPoint2Number + "@" + stop2Time.toString());
//
//			return;
//		}

		Map<Long, TrafficService.TripSectionPassage> stopPointsSections = sections.get(fragmentsKey);
		if (stopPointsSections == null) {
			stopPointsSections = new HashMap<>();
			sections.put(fragmentsKey, stopPointsSections);
		}

		TrafficService.TripSectionPassage current = stopPointsSections.get(tripId);
		if (current == null ||
				(current.getStop1Time().isBefore(stop1Time) && current.getStop2Time().isBefore(stop2Time))) {

			log.debug("TripSectionHistoryContainer adding " + tripId + " to " + fragmentsKey
					+ " " + stopPoint1Number + "@" + stop1Time.toString()
					+ " " + stopPoint2Number + "@" + stop2Time.toString());

			stopPointsSections.put(tripId,
					new TrafficService.TripSectionPassage(routeShortName, tripId, tripScheduledStartTime,
							stopPoint1Number, stop1Time, stopPoint2Number, stop2Time));
		}
		else {
			log.debug("TripSectionHistoryContainer (container had a later on so) NOT adding " + tripId + " to " + fragmentsKey
					+ " " + stopPoint1Number + "@" + stop1Time.toString()
					+ " " + stopPoint2Number + "@" + stop2Time.toString());
		}
	}

	public synchronized void dumpContents() {
		log.debug("TripSectionHistoryContainer dumpContents start");
		for (Map.Entry<String, Map<Long, TrafficService.TripSectionPassage>> section : sections.entrySet()) {
			for (Map.Entry<Long, TrafficService.TripSectionPassage> trip : section.getValue().entrySet()) {
				log.debug(trip.getValue().toString());
			}
		}
		log.debug("TripSectionHistoryContainer dumpContents finish");
	}
	public synchronized void cleanup() {

		for (Map.Entry<String, Map<Long, TrafficService.TripSectionPassage>> section : sections.entrySet()) {
			List<Long> tripToRemoveFromSection = new ArrayList<>();
			for (Map.Entry<Long, TrafficService.TripSectionPassage> trip : section.getValue().entrySet()) {
				if (cleanupPredicate.test(trip.getValue())) {
					tripToRemoveFromSection.add(trip.getKey());
				}
			}

			for (Long tripId : tripToRemoveFromSection) {

				log.debug("TripSectionHistoryContainer removing " + tripId + " from " + section.getKey());
				section.getValue().remove(tripId);
			}
		}
	}

	public synchronized List<TripSectionPassage> getSectionPassageHistory(Pair<Long, Long> stopPoints) {

		if (stopPoints.getLeft() == null || stopPoints.getRight() == null) {
			throw new IllegalArgumentException(
					"TripSectionHistoryContainer.getSectionPassageHistory - null passed in for a stoppoint");
		}
		String fragmentsKey = stopPoints.getLeft() + "_" + stopPoints.getRight();
		Map<Long, TrafficService.TripSectionPassage> stopPointsSections = sections.get(fragmentsKey);

		if (stopPointsSections == null || stopPointsSections.isEmpty()) {
			return new ArrayList<>();
		} else {
			ArrayList<TripSectionPassage> ret = new ArrayList<>();
			for (TripSectionPassage section : stopPointsSections.values()) {
				ret.add(section.cloneMe());
			}
			ret.sort((tsp1, tsp2) -> tsp1.getStop1Time().compareTo(tsp2.getStop1Time()));
			return ret;
		}
	}

	public synchronized List<List<TripSectionPassage>> getSectionPassageHistory(
			List<Pair<Long, Long>> stopPoints) {
		List<List<TripSectionPassage>> ret = new ArrayList<>();
		for (Pair<Long, Long> stops : stopPoints) {
			ret.add(this.getSectionPassageHistory(stops));
		}
		return ret;
	}

	protected Predicate<TrafficService.TripSectionPassage> cleanupPredicate;
	protected Map<String, Map<Long, TrafficService.TripSectionPassage>> sections = new HashMap<>();
}
