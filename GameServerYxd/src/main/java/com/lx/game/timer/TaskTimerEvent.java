package com.lx.game.timer;

import com.engine.timer.AbstractTimerEvent;
import com.engine.timer.TimerEvent;

/**
 * ClassName:TaskTimerEvent <br/>
 * Function: TODO (定时续费数据结构). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-10 下午4:41:16 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class TaskTimerEvent extends AbstractTimerEvent implements TimerEvent {

	
	public TaskTimerEvent(long eventId,long roleId,long timer) {
		super(eventId,TIMER_EVENT_ITEM_RENEW, timer,roleId);
		// TODO Auto-generated constructor stub
	}


	@Override
    public void doNetxTime() {
	    // TODO Auto-generated method stub
	  //  this.timer = System.currentTimeMillis() + 30000;
    }


	
}
