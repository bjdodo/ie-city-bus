package bjdodo.ie_city_bus.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

	@Test
	public void test() {
		double result = Utils.distFrom(1.0, 1.1, 1.2, 1.3);
		Assert.assertEquals(31447.768816785356, result, 0.0001);

	}

}
