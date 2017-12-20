package bjdodo.ie_city_bus.model;

import java.time.Instant;

public class TrafficServiceStopPassage {

	public TrafficServiceStopPassage(long id, long tripId, Instant tripStartTime, Instant passageTime,
			int stopNumber, String routeShortName) {
		super();
		this.id = id;
		this.tripId = tripId;
		this.tripStartTime = tripStartTime;
		this.passageTime = passageTime;
		this.stopNumber = stopNumber;
		this.routeShortName = routeShortName;
	}

	public long getId() {
		return id;
	}

	public long getTripId() {
		return tripId;
	}

	public Instant getTripStartTime() {
		return tripStartTime;
	}

	public Instant getPassageTime() {
		return passageTime;
	}

	public long getStopNumber() {
		return stopNumber;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	private long id;
	private long tripId;
	private Instant tripStartTime;
	private Instant passageTime;
	private long stopNumber;
	private String routeShortName;
}