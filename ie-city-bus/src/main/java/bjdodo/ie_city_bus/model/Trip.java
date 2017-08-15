package bjdodo.ie_city_bus.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trip {
	
	public Trip(Long id, String duid, long vehicleId) {
		super();
		this.id = id;
		this.duid = duid;
		this.vehicleId = vehicleId;
	}
	
	public Long getId() {
		return id;
	}
	public String getDuid() {
		return duid;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	final private Long id;
	final private String duid;
	final private long vehicleId;
}
