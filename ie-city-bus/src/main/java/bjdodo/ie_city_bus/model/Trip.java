package bjdodo.ie_city_bus.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;

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

	public String getOriginStopName() {
		return originStopName;
	}

	public void setOriginStopName(String originStopName) {
		this.originStopName = originStopName;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private long vehicleId;
	private long routeId;
	private int finished;
	private int direction;
	private String destinationStopName;
	private String originStopName;
	private Instant scheduledFinish;
	private Instant scheduledStart;
	private Instant actualFinish;
	private Instant actualStart;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);

	}
}
