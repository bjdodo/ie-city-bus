package bjdodo.ie_city_bus.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomDBStatementCalls {

	@Autowired
	private EntityManager entityManager;

	public List<Object> runRandomQuery(String query) {
		Query q = entityManager.createQuery(query);
		return q.getResultList();
	}

	// select *
	// from stop_passage
	// inner join stop_point on stop_passage.stop_point_id=stop_point.id
	// inner join trip on stop_passage.trip_id=trip.id
	// inner join ROUTE on trip.route_id=route.id
	// where COALESCE(stop_passage.ACTUAL_DEPARTURE, stop_passage.ACTUAL_ARRIVAL) >
	// '2017-12-14T16:11:00'
	// order by trip.id, COALESCE(stop_passage.ACTUAL_DEPARTURE,
	// stop_passage.ACTUAL_ARRIVAL)

	public <T> List<T> runQuery(String query, Map<String, Object> arguments) {
		Query q = entityManager.createQuery(query);
		for (Map.Entry<String, Object> arg : arguments.entrySet()) {
			q.setParameter(arg.getKey(), arg.getValue());
		}
		return q.getResultList();
	}

//	public List<RecentStopPassage> getRecentStopPassages(Instant after) {
//		Query q = entityManager.createQuery(
//				"select t.id, COALESCE(spa.actualDeparture, spa.actualArrival), spo.number,  r.shortName\r\n" +
//						"from StopPassage spa, StopPoint spo, Trip t, Route r\r\n" +
//						"where spa.stopPointId=spo.id and\r\n" +
//						"spa.tripId=t.id and\r\n" +
//						"t.routeId=r.id and\r\n" +
//						"COALESCE(spa.actualDeparture, spa.actualArrival) > :after\r\n" +
//						"order by t.id,  COALESCE(spa.actualDeparture, spa.actualArrival)");
//		q.setParameter("after", after);
//		List<Object[]> result = q.getResultList();
//		
//		
//	}

	@Transactional
	public int setStaleVehiclesDeleted() {

		Query q = entityManager
				.createQuery("UPDATE Vehicle  v set v.isDeleted=true where v.referenceTime<:referenceTime");
		// If a vehicle hasn't communicated in for 30 minutes we'll say it is deleted.
		// If it calls in again the flag will be unset anyway.
		q.setParameter("referenceTime", Instant.now().minusSeconds(1800));
		return q.executeUpdate();
	}

	@Transactional
	public int deleteOldTrips(long maxTripAgeSec) {

		Query q = entityManager
				.createQuery("delete from Trip t where t.finished=1 and t.scheduledStart<:referenceTime");
		// Delete trips that are considered too old by configuration
		q.setParameter("referenceTime", Instant.now().minusSeconds(maxTripAgeSec));
		return q.executeUpdate();
	}
}
