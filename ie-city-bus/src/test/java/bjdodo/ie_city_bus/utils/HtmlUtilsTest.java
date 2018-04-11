package bjdodo.ie_city_bus.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

public class HtmlUtilsTest {

	@Test
	public void test() {
		
		assertEquals(HtmlUtils.getActiveTripsHtml(null), "No active trips");
	}
}
