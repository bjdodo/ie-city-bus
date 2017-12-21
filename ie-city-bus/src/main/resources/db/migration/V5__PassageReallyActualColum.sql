alter table STOP_PASSAGE add ACTUAL_ESTIMATED bit not null default(0);

drop view TRIP_DETAIL_STOP_PASSAGE;

CREATE VIEW TRIP_DETAIL_STOP_PASSAGE AS
select stop_passage.id stop_passage_id, stop_passage.trip_id trip_id,
stop_point.name stop_name, stop_point.number stop_number, stop_point.lat_long stop_lat_long,
stop_passage.scheduled_departure, stop_passage.actual_departure,
stop_passage.scheduled_arrival, stop_passage.actual_arrival,
stop_passage.ACTUAL_ESTIMATED from stop_passage
inner join stop_point on stop_point.id=stop_passage.stop_point_id
where stop_passage.is_deleted=0;