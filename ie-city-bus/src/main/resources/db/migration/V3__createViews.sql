
CREATE VIEW ACTIVE_TRIP as
select trip.id trip_id, trip.direction trip_direction, vehicle.id vehicle_id, vehicle.lat_long vehicle_lat_long, route.short_name route_short_name, trip.origin_stop_name, trip.destination_stop_name, trip.scheduled_start, trip.actual_start, trip.scheduled_finish, trip.actual_finish, current_stop_passage.actual_arrival last_arrival, stop_passage.id last_arrival_stop_passage_id, stop_point.name last_arrival_stop_point_name
from
(select max(actual_arrival) actual_arrival, trip_id from stop_passage where  actual_arrival<CURRENT_TIMESTAMP() and trip_id in (select trip_id from vehicle) group by trip_id) current_stop_passage
inner join stop_passage on stop_passage.trip_id=current_stop_passage.trip_id and stop_passage.actual_arrival=current_stop_passage.actual_arrival
			inner join stop_point on stop_point.id=stop_passage.stop_point_id
			inner join trip on current_stop_passage.trip_id=trip.id
			inner join route on trip.route_id=route.id
			inner join vehicle on vehicle.current_trip_id=current_stop_passage.trip_id 
where trip.actual_start<>'1970-01-01 01:00:00.0'