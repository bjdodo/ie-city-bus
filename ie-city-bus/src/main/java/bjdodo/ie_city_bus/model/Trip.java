package bjdodo.ie_city_bus.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trip {

	public Trip() {
	}

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String duid;
	private long vehicleId;
	private long routeId;
	private int finished;

}
