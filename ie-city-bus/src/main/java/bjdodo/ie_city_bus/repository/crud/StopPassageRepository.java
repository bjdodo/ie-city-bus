package bjdodo.ie_city_bus.repository.crud;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import bjdodo.ie_city_bus.model.crud.StopPassage;
import bjdodo.ie_city_bus.service.TrafficServiceImpl;

public interface StopPassageRepository extends JpaRepository<StopPassage, Long> {

	// @Query("select count(bsp) FROM StopPassage bsp WHERE bsp.duid = ?1")
	// long countByDuid(String duid);

	public StopPassage findByDuid(String duid);

	@Query("select sp FROM StopPassage sp WHERE sp.tripId=?1")
	public List<StopPassage> findByTripId(long tripId);

	// select *
	// from stop_passage
	// inner join stop_point on stop_passage.stop_point_id=stop_point.id
	// inner join trip on stop_passage.trip_id=trip.id
	// inner join ROUTE on trip.route_id=route.id
	// where COALESCE(stop_passage.ACTUAL_DEPARTURE, stop_passage.ACTUAL_ARRIVAL) >
	// '2017-12-14T16:11:00'
	// order by trip.id, COALESCE(stop_passage.ACTUAL_DEPARTURE,
	// stop_passage.ACTUAL_ARRIVAL)

	@Query("select NEW bjdodo.ie_city_bus.model.TrafficServiceStopPassage(spa.id, t.id, t.scheduledStart, COALESCE(spa.actualDeparture, spa.actualArrival), spo.number, r.shortName) "
			+
			"from StopPassage spa, StopPoint spo, Trip t, Route r " +
			"where spa.isDeleted=false and spa.actualEstimated=0 and "
			+ "spa.stopPointId=spo.id and " +
			"spa.tripId=t.id and " +
			"t.routeId=r.id and " +
			"COALESCE(spa.actualDeparture, spa.actualArrival) > ?1 " +
			"order by t.id, COALESCE(spa.actualDeparture, spa.actualArrival)")
	public <T> List<T> findRecentStopPassagesForPredictionService(Instant minTime);
	
	@Transactional
	@Modifying
	@Query("update StopPassage sp set sp.actualEstimated=0 where sp.tripId not in (select id from Trip t where t.finished=0)")
	// @Query("update Trip t set t.finished=1 where t.finished=0 and ((t.id not in
	// (select v.currentTripId from Vehicle v)) or (t.actualFinish<now()))")
	int closeStopPassagesOfFinishedTrips();
}
