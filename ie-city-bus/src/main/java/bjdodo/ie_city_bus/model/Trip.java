package bjdodo.ie_city_bus.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trip {


	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDuid() {
		return duid;
	}
	public void setDuid(String duid) {
		this.duid = duid;
	}
	public long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public long getRouteId() {
		return routeId;
	}
	public void setRouteId(long routeId) {
		this.routeId = routeId;
	}
	public int getFinished() {
		return finished;
	}
	public void setFinished(int finished) {
		this.finished = finished;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public String getDestinationStopName() {
		return destinationStopName;
	}
	public void setDestinationStopName(String destinationStopName) {
		this.destinationStopName = destinationStopName;
	}

	public Instant getScheduledArrival() {
		return scheduledArrival;
	}

	public void setScheduledArrival(Instant scheduledArrival) {
		this.scheduledArrival = scheduledArrival;
	}

	public Instant getScheduledDeparture() {
		return scheduledDeparture;
	}

	public void setScheduledDeparture(Instant scheduledDeparture) {
		this.scheduledDeparture = scheduledDeparture;
	}

	public Instant getActualArrival() {
		return actualArrival;
	}

	public void setActualArrival(Instant actualArrival) {
		this.actualArrival = actualArrival;
	}

	public Instant getActualDeparture() {
		return actualDeparture;
	}

	public void setActualDeparture(Instant actualDeparture) {
		this.actualDeparture = actualDeparture;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private long vehicleId;
	private long routeId;
	private int finished;
	private int direction;
	private String destinationStopName;
	private Instant scheduledArrival;
	private Instant scheduledDeparture;
	private Instant actualArrival;
	private Instant actualDeparture;

}
