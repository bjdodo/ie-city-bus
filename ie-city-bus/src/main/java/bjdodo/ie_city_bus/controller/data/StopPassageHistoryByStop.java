package bjdodo.ie_city_bus.controller.data;

import java.util.List;

import bjdodo.ie_city_bus.model.TripDetailStopPassage;
import bjdodo.ie_city_bus.service.TrafficService.TripSectionPassage;

public class StopPassageHistoryByStop {

	public StopPassageHistoryByStop(TripDetailStopPassage stopPointFrom, TripDetailStopPassage stopPointTo,
			List<TripSectionPassage> tripSectionPassages) {
		super();
		this.fromStopName = stopPointFrom.getStopName();
		this.fromStopLatLong = stopPointFrom.getStopLatLong();
		this.fromStopNumber = stopPointFrom.getStopNumber();
		
		this.toStopName = stopPointTo.getStopName();
		this.toStopLatLong = stopPointTo.getStopLatLong();
		this.toStopNumber = stopPointTo.getStopNumber();
		
		this.tripSectionPassages = tripSectionPassages;
	}


	public String getFromStopName() {
		return fromStopName;
	}
	public String getFromStopLatLong() {
		return fromStopLatLong;
	}
	public long getFromStopNumber() {
		return fromStopNumber;
	}
	public String getToStopName() {
		return toStopName;
	}
	public String getToStopLatLong() {
		return toStopLatLong;
	}
	public long getToStopNumber() {
		return toStopNumber;
	}

	public List<TripSectionPassage> getTripSectionPassages() {
		return tripSectionPassages;
	}


	private String fromStopName;
	private String fromStopLatLong;
	private long fromStopNumber;
	
	private String toStopName;
	private String toStopLatLong;
	private long toStopNumber;
	
	List<TripSectionPassage> tripSectionPassages;
}