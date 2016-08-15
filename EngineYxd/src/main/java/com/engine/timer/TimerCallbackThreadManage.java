
package com.engine.timer;  

import java.util.concurrent.ConcurrentHashMap;

/** 
 * ClassName:TimerCallbackThreadManage <br/> 
 * Function: (定时器回调线程管理器). <br/> 
 * Date:     2015-9-6 下午5:03:29 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
public class TimerCallbackThreadManage {
	
	/** 定时器回调容器 **/  
	protected static ConcurrentHashMap<Long, TimerCallback> timerCallbackMap = new ConcurrentHashMap<Long, TimerCallback>();

	/** 当前定时器ID **/  
	private static long curTimerId = 0;
	
	/** 
	 * 生成定时器ID
	 * generateTimerId:(). <br/> 
	 */  
	private static long generateTimerId()
	{
		return ++curTimerId;
	}
	
	/** 
	 * 添加回调
	 * addTimerCallback:(). <br/> 
	 */  
	public static long addTimerCallback(TimerCallback callback)
	{
		long timerId = generateTimerId();
		timerCallbackMap.put(timerId, callback);
		return timerId;
	}
	
	/** 
	 * 移除回调
	 * removeTimerCallback:(). <br/> 
	 */  
	public static void removeTimerCallback(long timerId)
	{
		if(!timerCallbackMap.containsKey(timerId))
			return;
		
		timerCallbackMap.remove(timerId);
	}
	
	/** 
	 * 查找定时器回调
	 * findTimerCallback:(). <br/> 
	 */  
	public static TimerCallback findTimerCallback(long timerId)
	{
		if(!timerCallbackMap.containsKey(timerId))
			return null;
		return timerCallbackMap.get(timerId);
	}
}
  