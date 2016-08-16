
package com.lx.game.timer;  

import com.engine.timer.AbstractTimerEvent;
import com.engine.timer.TimerEvent;

/** 
 * ClassName:VoteDriverOutTeamTimerEvenet <br/> 
 * Function: TODO (驱逐队友事件). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 下午3:56:22 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class VoteDriverOutTeamTimerEvenet extends AbstractTimerEvent implements TimerEvent {
	
	public VoteDriverOutTeamTimerEvenet(long eventId,long roleId,long timer) {
		super(eventId,VOTE_DIRVER_OUT_TEAM, timer,roleId);
		// TODO Auto-generated constructor stub
	}

	
	@Override
    public void doNetxTime() {
	    // TODO Auto-generated method stub
	    
    }
	
}
  