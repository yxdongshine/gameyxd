package com.wx.server.utils;

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
	
	private Log log = null;
	
	public LogUtils( Log log){
		this.log = log;
	}
	
	/**
	 * getLog:(). <br/>
	 * TODO().<br/>
	 * log 统一调用此方法
	 * 
	 * @author lyh
	 * @param c
	 * @return
	 */
	public static LogUtils getLog(Class<?> c) {
		return   new LogUtils(LogFactory.getLog(c));
	}
	
	public void debug(Object obj){
		log.debug(obj);
	}
	
	public void info(Object obj){
		log.info(obj);
	}
	
	public void error(Object obj){
		log.error(obj);
	}
	
	
	public void debug(Object obj,Throwable t){
		log.debug(obj,t);
	}
	
	public void info(Object obj,Throwable t){
		log.info(obj,t);
	}
	
	public void error(Object obj,Throwable t){
		log.error(obj,t);
	}
}
