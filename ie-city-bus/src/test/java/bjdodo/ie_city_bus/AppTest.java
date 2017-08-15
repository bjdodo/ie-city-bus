package bjdodo.ie_city_bus;

import java.lang.reflect.Method;
import java.util.Date;

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
//GET http://buseireann.ie/inc/proto/stopPassageTdi.php?trip=6351558488856035339 -> tripid is input arg

//GET http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=191912202&latitude_south=191664018&longitude_east=-32947513&longitude_west=-32052694
//GET http://buseireann.ie/inc/proto/vehicleTdi.php?latitude_north=192043441&latitude_south=191572963&longitude_east=-32237122&longitude_west=-32939484 -> this works

// sample https://github.com/sbreatnach/bussedly/blob/master/Models/BusEireannStopRepository.cs

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
					} else if (m.getGenericReturnType() == Date.class) {
						sb.append("new Date(");
						sb.append(((Date) result).getTime());
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
