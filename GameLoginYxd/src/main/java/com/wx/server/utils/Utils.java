package com.wx.server.utils;

import java.lang.reflect.Method;

/**
 * ClassName:Utils <br/>
 * Function: TODO (工具类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-6 下午12:55:14 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class Utils {
	/**
	 * 反射相应的方法
	 * 
	 * @param method
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Object reflectTaskMethod(Class<?> pClass, String method, Object[] obj) throws Exception {
		Class c = pClass;
		Method md[] = c.getMethods();
		for (Method m : md) {
			if (m.getName().equalsIgnoreCase(method)) {
				Object o = m.invoke(c.newInstance(), obj);
				return o;
				
			}
		}
		return null;
	}
}
