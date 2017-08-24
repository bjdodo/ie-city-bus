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
	@Id
	private long vehicleId;
	private String latLong;
	private String route;
	private long tripId;
	private int direction;
	private Instant lastArrival;
	private long lastArrivalStopPassageId;
	private String lastArrivalStopPointName;
}