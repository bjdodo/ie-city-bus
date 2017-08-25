package bjdodo.ie_city_bus.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import bjdodo.ie_city_bus.model.customquery.TripDetails;

@Component
public class CustomQueries {

	@Autowired
	private EntityManager entityManager;

	// select arival_time.actual_arrival, stop_passage.id, stop_point.name from
	// (select max(actual_arrival) actual_arrival, trip_id from stop_passage where
	// actual_arrival<CURRENT_TIMESTAMP() group by trip_id) arival_time
	// inner join stop_passage on stop_passage.trip_id=arival_time.trip_id and
	// stop_passage.actual_arrival=arival_time.actual_arrival
	// inner join stop_point on stop_point.id=stop_passage.stop_point_id

	// select trip.id trip_id, vehicle.id vehicle_id, vehicle.lat_long,
	// trip.direction, route.short_name, stop_point.name as final_stop
	// from vehicle
	// inner join trip on vehicle.current_trip_id=trip.id
	// inner join route on trip.route_id=route.id
	// inner join stop_passage on stop_passage.trip_id=trip.id
	// inner join stop_point on stop_passage.stop_point_id=stop_point.id
	// where stop_passage.scheduled_departure is null order by route.short_name

	public List<TripDetails> getActiveTripDetails() {
		// @formatter:off
		Query query = entityManager.createNativeQuery(
				"select vehicle.id vehicle_id, vehicle.lat_long, route.short_name route, current_stop_passage.trip_id trip_id, trip.direction, current_stop_passage.actual_arrival last_arrival, stop_passage.id last_arrival_stop_passage_id, stop_point.name last_arrival_stop_point_name\r\n"
						+ "from\r\n"
						+ "(select max(actual_arrival) actual_arrival, trip_id from stop_passage where  actual_arrival<CURRENT_TIMESTAMP() and trip_id in (select trip_id from vehicle) group by trip_id) current_stop_passage\r\n"
						+ "inner join stop_passage on stop_passage.trip_id=current_stop_passage.trip_id and stop_passage.actual_arrival=current_stop_passage.actual_arrival\r\n"
						+ "inner join stop_point on stop_point.id=stop_passage.stop_point_id\r\n"
						+ "inner join vehicle on vehicle.current_trip_id=current_stop_passage.trip_id\r\n"
						+ "inner join trip on current_stop_passage.trip_id=trip.id\r\n"
						+ "inner join route on trip.route_id=route.id order by route.short_name",
				TripDetails.class);
		// @formatter:on
		// query.setParameter(arg0, arg1);
		
		return query.getResultList();
	}

	public List<TripDetails> getActiveTripDetails(String routeShortName) {
		// @formatter:off
		Query query = entityManager.createNativeQuery(
				"select vehicle.id vehicle_id, vehicle.lat_long, route.short_name route, current_stop_passage.trip_id trip_id, trip.direction, current_stop_passage.actual_arrival last_arrival, stop_passage.id last_arrival_stop_passage_id, stop_point.name last_arrival_stop_point_name\r\n"
						+ "from\r\n"
						+ "(select max(actual_arrival) actual_arrival, trip_id from stop_passage where  actual_arrival<CURRENT_TIMESTAMP() and trip_id in (select trip_id from vehicle) group by trip_id) current_stop_passage\r\n"
						+ "inner join stop_passage on stop_passage.trip_id=current_stop_passage.trip_id and stop_passage.actual_arrival=current_stop_passage.actual_arrival\r\n"
						+ "inner join stop_point on stop_point.id=stop_passage.stop_point_id\r\n"
						+ "inner join vehicle on vehicle.current_trip_id=current_stop_passage.trip_id\r\n"
						+ "inner join trip on current_stop_passage.trip_id=trip.id\r\n"
						+ "inner join route on trip.route_id=route.id where route.short_name=:route",
				TripDetails.class);
		// @formatter:on
		query.setParameter("route", routeShortName);

		return query.getResultList();
	}

	// public List<TripDetails> getTripDetails(long tripId) {
	// // @formatter:off
	// Query query = entityManager.createNativeQuery(
	// " select vehicle.lat_long, trip.direction, route.short_name, stop_point.name
	// as finalstop\r\n"
	// + " from vehicle\r\n" + " inner join trip on vehicle.id=trip.vehicle_id\r\n"
	// + " inner join route on trip.route_id=route.id\r\n"
	// + " inner join stop_passage on stop_passage.trip_id=trip.id\r\n"
	// + " inner join stop_point on stop_passage.stop_point_id=stop_point.id\r\n"
	// + " where stop_passage.scheduled_departure is null and trip.id=:tripid\r\n"
	// + " order by route.short_name",
	// TripDetails.class);
	// // @formatter:on
	// query.setParameter("tripid", tripId);
	//
	// return query.getResultList();
	// }


}
