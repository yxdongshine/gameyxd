package com.engine.utils.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClassName:LogUtils <br/>
 * Function: TODO (Log统一工具类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-25 下午5:59:52 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class LogUtils {
	
	/**
	 * getLog:(). <br/>
	 * TODO().<br/>
	 * log 统一调用此方法
	 * 
	 * @author lyh
	 * @param c
	 * @return
	 */
	public static Log getLog(Class<?> c) {
		return LogFactory.getLog(c);
	}
}
