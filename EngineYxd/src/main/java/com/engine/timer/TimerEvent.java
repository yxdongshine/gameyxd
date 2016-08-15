
package com.engine.timer;  
/** 
 * ClassName:TimerEvent <br/> 
 * Function: TODO (所有的事件id都在这儿定义). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-2 下午2:05:24 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
public interface TimerEvent {
	//游戏本地通信 0x1000000-0xF000000
	
	/**道具续费**/
	public static final int TIMER_EVENT_ITEM_RENEW = 0x1000000;
	
	/**
	 * 一分钟后投票计算是否驱逐玩家
	 */
	public static final int VOTE_DIRVER_OUT_TEAM = 0x1000001;
	
	
}
  