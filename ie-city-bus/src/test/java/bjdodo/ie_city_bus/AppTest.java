package bjdodo.ie_city_bus;

import java.lang.reflect.Method;
import java.time.Instant;

import org.junit.Test;

//GET http://buseireann.ie/js/jp_promotions.php
//GET http://buseireann.ie/js/jp_routes.php -> prices
//GET http://buseireann.ie/js/jp_destinations.php -> bus stop names, apparently with no links anywhere

//GET http://buseireann.ie/inc/proto/bus_stop_points.php -> all stops in Ireland
//GET http://buseireann.ie/inc/proto/routes.php -> all routes in Ireland
//GET http://buseireann.ie/inc/proto/stopPointTdi.php?latitude_north=191826612&latitude_south=191754612&longitude_east=-32542596&longitude_west=-32614596&_=1502487094574 

// Stop passage is when a bus would pass a stop. this php maps stops to trips. passage and vehicle both have tripid. 
// This php also maps trips to routes.
//GET http://buseireann.ie/inc/proto/stopPassageTdi.php?stop_point=6350786630982826945
//GET http://buseireann.ie/inc/proto/stopPassageTdi.php?trip=6351558488856050205 -> tripid is input arg

//GET http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=191912202&latitude_south=191664018&longitude_east=-32947513&longitude_west=-32052694
//GET http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484 -> this works

// sample https://github.com/sbreatnach/bussedly/blob/master/Models/BusEireannStopRepository.cs

/* SOME QUERIES
 select trip.id tripid, trip.duid tripduid, vehicle.id vehicleid, vehicle.duid vehicleduid, vehicle.lat_long vehiclepos
from route
inner join trip on trip.route_id=route.id
inner join vehicle on trip.vehicle_id=vehicle.id
where route.short_name='X20' and vehicle.trip_duid in (select trip_duid from vehicle)

select * from trip where duid='6351558488832191001'


select max(cnt) from (select count(*) cnt, duid from stop_passage group by duid)

select count(*) from stop_passage

select count(*), vehicle_id, duid from trip group by vehicle_id, duid
select * from vehicle

select * from stop_point
select max(cnt) from (select count(*) cnt, duid from stop_point group by duid) a

select * from vehicle where trip_duid = ''

select vehicle.id vehicleid, trip.duid tripduid, route.short_name, stop_passage.scheduled_arrival, stop_passage.actual_arrival, stop_point.name
from route
inner join trip on trip.route_id=route.id
inner join vehicle on trip.vehicle_id=vehicle.id
inner join stop_passage on stop_passage.trip_id=trip.id
inner join stop_point on stop_passage.stop_point_id=stop_point.id
--where vehicle.id=15
where route.short_name='403'
order by tripduid, scheduled_arrival

select * from stop_passage limit 10;

select count(*) from trip where finished=0
update trip set finished=1 where trip.duid not in (select trip_duid from vehicle)

update trip t set t.finished=1 where t.duid not in (select v.trip_duid from vehicle v)

 */

public class AppTest {
	@Test
	public void testApp() {


	}

	static public String getAssertText(Object o, Class<?> c, String varName) {
		try {
			StringBuffer sb = new StringBuffer();
			for (Method m : c.getMethods()) {
				if (m.getParameterCount() == 0 && !m.getName().equals("getClass") && !m.getName().equals("hashCode")
						&& (m.getName().startsWith("get") || m.getName().startsWith("is")
								|| m.getName().startsWith("has"))) {
					Object result = m.invoke(o);

					sb.append("Assert.assertEquals(");
					sb.append(varName);
					sb.append(".");
					sb.append(m.getName());
					sb.append("(), ");
					if (m.getGenericReturnType() == String.class) {
						sb.append("\"");
						sb.append(result.toString());
						sb.append("\"");
					} else if (m.getGenericReturnType() == double.class || m.getGenericReturnType() == Double.class) {
						sb.append(result.toString());
						sb.append(", 0.001");
					} else if (m.getGenericReturnType() == long.class || m.getGenericReturnType() == Long.class) {
						sb.append(result.toString());
						sb.append("L");
					} else if (m.getGenericReturnType() == Instant.class) {
						sb.append("Instant.ofEpochSecond(");
						sb.append(((Instant) result).getEpochSecond());
						sb.append(")");
					} else {
						sb.append(result.toString());
					}
					sb.append(");\n");
				}
			}

			return sb.toString();
		} catch (Exception ex) {
			return "";
		}
	}
}
