package bjdodo.ie_city_bus.model.customquery;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomQueries {

	@Autowired
	private EntityManager entityManager;

	public List<Object> runRandomQuery(String query) {
		Query q = entityManager.createQuery(query);
		return q.getResultList();
	}

	// findtrips
	// select trip.id trip_id,
	// stop_point.id start_stop_point_id, stop_point.name start_stop_point_name,
	// stop_point.lat_long start_lat_long,
	// stop_passage.scheduled_departure start_scheduled_departure,
	// stop_passage.actual_departure start_expected_departure,
	// destination_stops.stop_point_id finish_stop_id,
	// destination_stops.stop_point_name finish_stop_name
	// from vehicle
	// inner join trip on vehicle.current_trip_id=trip.id
	// inner join stop_passage on stop_passage.trip_id=trip.id
	// inner join stop_point on stop_point.id=stop_passage.stop_point_id
	// inner join
	// (select stop_passage.trip_id, stop_passage.scheduled_arrival,
	// stop_passage.actual_arrival expected_arrival, stop_passage.stop_point_id
	// stop_point_id, stop_point.name stop_point_name
	// from stop_passage
	// inner join stop_point on stop_point.id=stop_passage.stop_point_id
	// where stop_point.name in ('Eyre Square East (Stop No 6)', 'Eyre Square North
	// (Stop No 2)', 'Eyre Square North (Stop No 1)', 'Eyre Square East (Stop No
	// 5)', 'Eyre Square East (Stop No 8)', 'Eyre Square East (Stop No 7)', 'Eyre
	// Square South (Stop No 9)'))
	// destination_stops on destination_stops.trip_id=trip.id
	// where stop_point.name='Dublin Rd (GMIT)' and
	// stop_passage.actual_departure>=now()
	// order by stop_passage.actual_departure

	// select t, spg from ActiveTrip t, Vehicle v, StopPassage spg, StopPoint spt
	// where v.currentTripId=t.id and spg.tripId=t.id and spg.stopPointId=spt.id
	// and spt.name='Dublin Rd (Opp GMIT)' and spg.actualDeparture>=now()
	// and t.id IN (
	// select spg.tripId from StopPassage spg, StopPoint spt, Vehicle v where
	// spg.stopPointId=spt.id and v.currentTripId=spg.tripId and spt.name in (
	// 'Eyre Square East (Stop No 6)', 'Eyre Square North (Stop No 2)', 'Eyre Square
	// North (Stop No 1)', 'Eyre Square East (Stop No 5)',
	// 'Eyre Square East (Stop No 8)', 'Eyre Square East (Stop No 7)', 'Eyre Square
	// South (Stop No 9)')
	// )
	// order by spg.actualDeparture

	public List<Object> findTrips(String originStop, String[] destinationStops) {
		String query = "select t, spg from ActiveTrip t, Vehicle v, StopPassage spg, StopPoint spt"
				+ " where v.currentTripId=t.id and spg.tripId=t.id and spg.stopPointId=spt.id"
				+ " and spt.name=:originStop and spg.actualDeparture>=now() and t.id IN ("
				+ " select spg.tripId from StopPassage spg, StopPoint spt, Vehicle v where"
				+ " spg.stopPointId=spt.id and v.currentTripId=spg.tripId and spg.actualArrival>now() and spt.name in (:destinationStops) )"
				+ " order by spg.actualDeparture";
		Query q = entityManager.createQuery(query);
		q.setParameter("originStop", originStop);
		q.setParameter("destinationStops", Arrays.asList(destinationStops));
		return q.getResultList();
	}
}
