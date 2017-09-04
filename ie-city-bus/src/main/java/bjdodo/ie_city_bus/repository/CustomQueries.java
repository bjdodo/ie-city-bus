package bjdodo.ie_city_bus.repository;

// import java.util.List;
//
// import javax.persistence.EntityManager;
// import javax.persistence.Query;
//
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// import bjdodo.ie_city_bus.model.customquery.ActiveTrip;
//
// @Component
// public class CustomQueries {
//
// @Autowired
// private EntityManager entityManager;
//
// // select arival_time.actual_arrival, stop_passage.id, stop_point.name from
// // (select max(actual_arrival) actual_arrival, trip_id from stop_passage
// where
// // actual_arrival<CURRENT_TIMESTAMP() group by trip_id) arival_time
// // inner join stop_passage on stop_passage.trip_id=arival_time.trip_id and
// // stop_passage.actual_arrival=arival_time.actual_arrival
// // inner join stop_point on stop_point.id=stop_passage.stop_point_id
//
// // select trip.id trip_id, vehicle.id vehicle_id, vehicle.lat_long,
// // trip.direction, route.short_name, stop_point.name as final_stop
// // from vehicle
// // inner join trip on vehicle.current_trip_id=trip.id
// // inner join route on trip.route_id=route.id
// // inner join stop_passage on stop_passage.trip_id=trip.id
// // inner join stop_point on stop_passage.stop_point_id=stop_point.id
// // where stop_passage.scheduled_departure is null order by route.short_name
//
// // @formatter:off
// private String activeTripDetailsSelectAndJoin = "select vehicle.id
// vehicle_id, vehicle.lat_long, route.short_name route,
// current_stop_passage.trip_id trip_id, trip.direction, trip.origin_stop_name,
// trip.destination_stop_name, trip.scheduled_start, trip.actual_start,
// trip.scheduled_finish, trip.actual_finish,
// current_stop_passage.actual_arrival last_arrival, stop_passage.id
// last_arrival_stop_passage_id, stop_point.name
// last_arrival_stop_point_name\r\n"
// + "from\r\n"
// + "(select max(actual_arrival) actual_arrival, trip_id from stop_passage
// where actual_arrival<CURRENT_TIMESTAMP() and trip_id in (select trip_id from
// vehicle) group by trip_id) current_stop_passage\r\n"
// + "inner join stop_passage on
// stop_passage.trip_id=current_stop_passage.trip_id and
// stop_passage.actual_arrival=current_stop_passage.actual_arrival\r\n"
// + "inner join stop_point on stop_point.id=stop_passage.stop_point_id "
// + "inner join trip on current_stop_passage.trip_id=trip.id "
// + "inner join route on trip.route_id=route.id "
// + "inner join vehicle on vehicle.current_trip_id=current_stop_passage.trip_id
// ";
//
// // private String anyTripDetailsSelectAndJoin = "select vehicle.id
// vehicle_id,
// // route.short_name route, current_stop_passage.trip_id trip_id,
// trip.direction,
// // trip.origin_stop_name, trip.destination_stop_name, trip.scheduled_start,
// // trip.actual_start, trip.scheduled_finish, trip.actual_finish,
// // current_stop_passage.actual_arrival last_arrival, stop_passage.id
// // last_arrival_stop_passage_id, stop_point.name
// // last_arrival_stop_point_name\r\n"
// // + "from\r\n"
// // + "(select max(actual_arrival) actual_arrival, trip_id from stop_passage
// // where actual_arrival<CURRENT_TIMESTAMP() and trip_id in (select trip_id
// from
// // vehicle) group by trip_id) current_stop_passage\r\n"
// // + "inner join stop_passage on
// // stop_passage.trip_id=current_stop_passage.trip_id and
// // stop_passage.actual_arrival=current_stop_passage.actual_arrival\r\n"
// // + "inner join stop_point on stop_point.id=stop_passage.stop_point_id "
// // + "inner join trip on current_stop_passage.trip_id=trip.id "
// // + "inner join route on trip.route_id=route.id "
// // + "inner join vehicle on vehicle.id=trip.vehicle_id ";
//
// // @formatter:on
//
// @SuppressWarnings("unchecked")
// public List<ActiveTrip> getActiveTripDetails() {
//
// Query query = entityManager.createNativeQuery(
// activeTripDetailsSelectAndJoin + "order by route.short_name",
// ActiveTrip.class);
// return query.getResultList();
// }
//
// @SuppressWarnings("unchecked")
// public List<ActiveTrip> getRouteActiveTripDetails(String routeShortName) {
//
// Query query = entityManager.createNativeQuery(
// activeTripDetailsSelectAndJoin + "where route.short_name=:route",
// ActiveTrip.class);
// query.setParameter("route", routeShortName);
// return query.getResultList();
// }
//
// @SuppressWarnings("unchecked")
// public List<ActiveTrip> getActiveTripDetailsById(long[] tripId) {
//
// Query query = entityManager.createNativeQuery(activeTripDetailsSelectAndJoin
// + "where trip.id in (:tripId)",
// ActiveTrip.class);
// query.setParameter("tripId", StringUtils.join(tripId, ','));
// return query.getResultList();
// }
//
// // @SuppressWarnings("unchecked")
// // public List<TripDetails> getAnyTripDetails(long tripId) {
// //
// // Query query = entityManager.createNativeQuery(anyTripDetailsSelectAndJoin
// +
// // "where trip.id=:tripId",
// // TripDetails.class);
// // query.setParameter("tripId", tripId);
// // return query.getResultList();
// // }
//
//
// // findtrips
// // select trip.id trip_id,
// // stop_point.id start_stop_point_id, stop_point.lat_long start_lat_long,
// // stop_passage.scheduled_departure start_scheduled_departure,
// // stop_passage.actual_departure start_expected_departure,
// // destination_stops.stop_point_id finish_stop_id,
// // destination_stops.stop_point_name finish_stop_name
// // from vehicle
// // inner join trip on vehicle.current_trip_id=trip.id
// // inner join stop_passage on stop_passage.trip_id=trip.id
// // inner join stop_point on stop_point.id=stop_passage.stop_point_id
// // inner join
// // (select stop_passage.trip_id, stop_passage.scheduled_arrival,
// // stop_passage.actual_arrival expected_arrival, stop_passage.stop_point_id
// // stop_point_id, stop_point.name stop_point_name
// // from stop_passage
// // inner join stop_point on stop_point.id=stop_passage.stop_point_id
// // where stop_passage.stop_point_id in (11,32,85,124,191,211,247))
// // destination_stops on destination_stops.trip_id=trip.id
// // where stop_point.id in (97) and stop_passage.actual_departure>=now()
// // order by stop_passage.actual_departure
//
//
// }
