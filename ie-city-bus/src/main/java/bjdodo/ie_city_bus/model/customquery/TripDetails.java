package bjdodo.ie_city_bus.model.customquery;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TripDetails {

	public long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getLatLong() {
		return latLong;
	}
	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	public long getTripId() {
		return tripId;
	}
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public Instant getLastArrival() {
		return lastArrival;
	}
	public void setLastArrival(Instant lastArrival) {
		this.lastArrival = lastArrival;
	}
	public long getLastArrivalStopPassageId() {
		return lastArrivalStopPassageId;
	}
	public void setLastArrivalStopPassageId(long lastArrivalStopPassageId) {
		this.lastArrivalStopPassageId = lastArrivalStopPassageId;
	}
	public String getLastArrivalStopPointName() {
		return lastArrivalStopPointName;
	}
	public void setLastArrivalStopPointName(String lastArrivalStopPointName) {
		this.lastArrivalStopPointName = lastArrivalStopPointName;
	}

	public String getDestinationStopName() {
		return destinationStopName;
	}

	public void setDestinationStopName(String destinationStopName) {
		this.destinationStopName = destinationStopName;
	}

	public Instant getScheduledFinish() {
		return scheduledFinish;
	}

	public void setScheduledFinish(Instant scheduledFinish) {
		this.scheduledFinish = scheduledFinish;
	}

	public Instant getScheduledStart() {
		return scheduledStart;
	}

	public void setScheduledStart(Instant scheduledStart) {
		this.scheduledStart = scheduledStart;
	}

	public Instant getActualFinish() {
		return actualFinish;
	}

	public void setActualFinish(Instant actualFinish) {
		this.actualFinish = actualFinish;
	}

	public Instant getActualStart() {
		return actualStart;
	}

	public void setActualStart(Instant actualStart) {
		this.actualStart = actualStart;
	}

	@Id
	private long vehicleId;
	private String latLong;
	private String route;
	private long tripId;
	private int direction;
	private Instant lastArrival;
	private long lastArrivalStopPassageId;
	private String lastArrivalStopPointName;
	private String destinationStopName;
	private Instant scheduledFinish;
	private Instant scheduledStart;
	private Instant actualFinish;
	private Instant actualStart;
}