package bjdodo.ie_city_bus.controller.data;

import org.springframework.beans.BeanUtils;

import bjdodo.ie_city_bus.model.TripDetailStopPassage;

public class StopPassageDetail extends TripDetailStopPassage {

	public StopPassageDetail() {
	}

	public StopPassageDetail(TripDetailStopPassage tripPassage) {
		BeanUtils.copyProperties(tripPassage, this);
	}

	public long getMetersFromVehicle() {
		return metersFromVehicle;
	}

	public void setMetersFromVehicle(long metersFromVehicle) {
		this.metersFromVehicle = metersFromVehicle;
	}

	private long metersFromVehicle;
}
