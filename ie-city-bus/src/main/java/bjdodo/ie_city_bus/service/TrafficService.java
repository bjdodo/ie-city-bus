package bjdodo.ie_city_bus.service;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import bjdodo.ie_city_bus.controller.data.StopPassageHistoryByStop;
import bjdodo.ie_city_bus.model.TripDetailStopPassage;

public interface TrafficService {

	void recordRecentSectionPassage(String shortName, long id, Instant scheduledStart, int number, Instant time1,
			int number2, Instant time2);

	List<StopPassageHistoryByStop> getRecentPassageHistoryForStops(List<TripDetailStopPassage> stopPassages);

	// Map<Pair<Integer, Integer>, List<TripSectionPassage>>
	// getPassageHistory(List<Pair<Integer, Integer>> stopPoints);

	void cleanupOld();

	
	
	// This represents the distance between two stops
	public static class TripSectionPassage  {

		public TripSectionPassage(String routeShortName, long tripId, Instant tripScheduledStartTime, long stop1Number,
				Instant stop1Time, long stop2Number, Instant stop2Time) {
			super();
			this.routeShortName = routeShortName;
			this.tripId = tripId;
			this.tripScheduledStartTime = tripScheduledStartTime;
			this.stop1Number = stop1Number;
			this.stop1Time = stop1Time;
			this.stop2Number = stop2Number;
			this.stop2Time = stop2Time;

		}

		public TripSectionPassage cloneMe() {
			return new TripSectionPassage(this.routeShortName, this.tripId, this.tripScheduledStartTime,
					this.getStop1Number(), this.getStop1Time(), this.getStop2Number(), this.getStop2Time());
		}

		public String getRouteShortName() {
			return routeShortName;
		}

		public long getTripId() {
			return tripId;
		}

		public Instant getTripScheduledStartTime() {
			return tripScheduledStartTime;
		}
		
		public long getStop1Number() {
			return stop1Number;
		}

		public Instant getStop1Time() {
			return stop1Time;
		}

		public long getStop2Number() {
			return stop2Number;
		}

		public Instant getStop2Time() {
			return stop2Time;
		}

		private String routeShortName;
		private long tripId;
		private Instant tripScheduledStartTime;
		private long stop1Number;
		private Instant stop1Time;
		private long stop2Number;
		private Instant stop2Time;

		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);

		}
	}

}
