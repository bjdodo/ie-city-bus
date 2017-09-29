package bjdodo.ie_city_bus.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class TripPassage {

	public long getStopPassageId() {
		return stopPassageId;
	}
	public void setStopPassageId(long stopPassageId) {
		this.stopPassageId = stopPassageId;
	}

	public long getTripId() {
		return tripId;
	}

	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
	public String getStopName() {
		return stopName;
	}
	public void setStopName(String stopName) {
		this.stopName = stopName;
	}
	public String getStopLatLong() {
		return stopLatLong;
	}
	public void setStopLatLong(String stopLatLong) {
		this.stopLatLong = stopLatLong;
	}
	public Instant getScheduledDeparture() {
		return scheduledDeparture;
	}
	public void setScheduledDeparture(Instant scheduledDeparture) {
		this.scheduledDeparture = scheduledDeparture;
	}
	public Instant getActualDeparture() {
		return actualDeparture;
	}
	public void setActualDeparture(Instant actualDeparture) {
		this.actualDeparture = actualDeparture;
	}
	public Instant getScheduledArrival() {
		return scheduledArrival;
	}
	public void setScheduledArrival(Instant scheduledArrival) {
		this.scheduledArrival = scheduledArrival;
	}
	public Instant getActualArrival() {
		return actualArrival;
	}
	public void setActualArrival(Instant actualArrival) {
		this.actualArrival = actualArrival;
	}
	public long getStopNumber() {
		return stopNumber;
	}
	public void setStopNumber(long stopNumber) {
		this.stopNumber = stopNumber;
	}


	@Id
	private long stopPassageId;
	private long tripId;
	private String stopName;
	private String stopLatLong;
	private Instant scheduledDeparture;
	private Instant actualDeparture;
	private Instant scheduledArrival;
	private Instant actualArrival;
	private long stopNumber;
}
