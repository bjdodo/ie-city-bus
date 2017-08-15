package bjdodo.ie_city_bus;

import java.lang.reflect.Method;
import java.util.Date;

import org.junit.Test;



/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
    public void testApp()
    {
//		HttpService svc = new HttpServiceImpl();
//		String resp = svc.get("http://www.google.com");
//		Assert.assertTrue(resp.length() > 0);

    }
	
	static public String getAssertText(Object o, Class<?> c, String varName)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			for (Method m : c.getMethods())
			{
				if (m.getParameterCount() == 0 && !m.getName().equals("getClass") && !m.getName().equals("hashCode") &&
						(m.getName().startsWith("get") || m.getName().startsWith("is") || m.getName().startsWith("has")))
				{
					Object result = m.invoke(o);
					
					sb.append("Assert.assertEquals(");
					sb.append(varName);
					sb.append(".");
					sb.append(m.getName());
					sb.append("(), ");
					if (m.getGenericReturnType() == String.class)
					{
						sb.append("\"");
						sb.append(result.toString());
						sb.append("\"");
					}
					else if (m.getGenericReturnType() == double.class || m.getGenericReturnType() == Double.class)
					{
						sb.append(result.toString());
						sb.append(", 0.001");
					}
					else if (m.getGenericReturnType() == long.class || m.getGenericReturnType() == Long.class)
					{
						sb.append(result.toString());
						sb.append("L");
					}
					else if (m.getGenericReturnType() == Date.class)
					{
						sb.append("new Date(");
						sb.append(((Date)result).getTime());
						sb.append(")");
					}
					else
					{
						sb.append(result.toString());
					}
					sb.append(");\n");
				}
			}
			
			return sb.toString();
		}
		catch(Exception ex)
		{
			return "";
		}
	}
}
