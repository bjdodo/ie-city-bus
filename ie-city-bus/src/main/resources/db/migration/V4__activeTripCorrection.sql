drop view ACTIVE_TRIP;

CREATE VIEW ACTIVE_TRIP as
select trip.id trip_id, trip.direction trip_direction, trip.progress trip_progress,
vehicle.id vehicle_id, vehicle.lat_long vehicle_lat_long, 
route.short_name route_short_name, 
trip.origin_stop_name, trip.destination_stop_name, trip.scheduled_start, trip.actual_start, trip.scheduled_finish, trip.actual_finish,
nearest_stop_point.name nearest_stop_point_name, trip.nearest_stop_point_distance
from trip 
inner join vehicle on vehicle.current_trip_id=trip.id
inner join route on trip.route_id=route.id
inner join stop_point nearest_stop_point on trip.nearest_stop_point_id=nearest_stop_point.id
where 
--trip.actual_start<>'1970-01-01 01:00:00.0' and
trip.finished=0 and vehicle.is_deleted=0
order by route_short_name;

drop view STOP_PASSAGE_OF_TRIP;

CREATE VIEW TRIP_DETAIL_STOP_PASSAGE AS
select stop_passage.id stop_passage_id, stop_passage.trip_id trip_id,
stop_point.name stop_name, stop_point.number stop_number, stop_point.lat_long stop_lat_long,
stop_passage.scheduled_departure, stop_passage.actual_departure,
stop_passage.scheduled_arrival, stop_passage.actual_arrival from stop_passage
inner join stop_point on stop_point.id=stop_passage.stop_point_id
where stop_passage.is_deleted=0;

alter table Vehicle drop column category;
