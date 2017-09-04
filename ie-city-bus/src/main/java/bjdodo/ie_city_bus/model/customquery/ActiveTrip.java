package bjdodo.ie_city_bus.model.customquery;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class ActiveTrip {

	public long getTripId() {
		return tripId;
	}
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}

	public int getTripDirection() {
		return tripDirection;
	}

	public void setTripDirection(int tripDirection) {
		this.tripDirection = tripDirection;
	}

	public long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleLatLong() {
		return vehicleLatLong;
	}

	public void setVehicleLatLong(String vehicleLatLong) {
		this.vehicleLatLong = vehicleLatLong;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}
	public String getOriginStopName() {
		return originStopName;
	}
	public void setOriginStopName(String originStopName) {
		this.originStopName = originStopName;
	}
	public String getDestinationStopName() {
		return destinationStopName;
	}
	public void setDestinationStopName(String destinationStopName) {
		this.destinationStopName = destinationStopName;
	}

	public Instant getScheduledStart() {
		return scheduledStart;
	}

	public void setScheduledStart(Instant scheduledStart) {
		this.scheduledStart = scheduledStart;
	}

	public Instant getActualStart() {
		return actualStart;
	}

	public void setActualStart(Instant actualStart) {
		this.actualStart = actualStart;
	}
	public Instant getScheduledFinish() {
		return scheduledFinish;
	}
	public void setScheduledFinish(Instant scheduledFinish) {
		this.scheduledFinish = scheduledFinish;
	}
	public Instant getActualFinish() {
		return actualFinish;
	}
	public void setActualFinish(Instant actualFinish) {
		this.actualFinish = actualFinish;
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
	private long tripId;
	private int tripDirection;
	private long vehicleId;
	private String vehicleLatLong;
	private String routeShortName;
	private String originStopName;
	private String destinationStopName;
	private Instant scheduledStart;
	private Instant actualStart;
	private Instant scheduledFinish;
	private Instant actualFinish;
	private Instant lastArrival;
	private long lastArrivalStopPassageId;
	private String lastArrivalStopPointName;
}