package com.engine.timer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:TimerThreadManage <br/>
 * Function: TODO (时间线程管理器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午4:41:33 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

public class TimerThreadManage {
	
	
	/** 定时事件触发容器 **/
	protected static ConcurrentHashMap<Long, AbstractTimerEvent> timerEventLists = new ConcurrentHashMap<Long, AbstractTimerEvent>();
	
	/**
	 * addTimeEvent:(). <br/>
	 * TODO().<br/>
	 * 把定进事件加入到容器
	 * 
	 * @author lyh
	 * @param timerEvent
	 */
	public static void addTimeEvent(AbstractTimerEvent timerEvent) {
		timerEventLists.put(timerEvent.getEventId(), timerEvent);
	}
	
	/** 
	 * findTimerEvent:(). <br/> 
	 * TODO().<br/> 
	 * 查找时间事件
	 * @author lyh 
	 * @param key
	 * @return 
	 */  
	public static AbstractTimerEvent findTimerEventByKey(long key){
		return timerEventLists.get(key);
	}
	
	/** 
	 * remove:(). <br/> 
	 * TODO().<br/> 
	 * 删除时间事件
	 * @author lyh 
	 * @param key
	 * @return 
	 */  
	public static AbstractTimerEvent remove(long key){
		return timerEventLists.remove(key);
	}
	
}
