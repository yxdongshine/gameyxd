
package com.lx.logical.timer;  

import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.timer.TimerEvent;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.timer.TaskTimerEvent;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:VoteDriverOutTeamTimerTask <br/> 
 * Function: TODO (投票驱逐玩家). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 下午3:47:17 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(TimerEvent.VOTE_DIRVER_OUT_TEAM)
public class VoteDriverOutTeamTimerTask extends RequestTaskAdapter implements GameMessage<TaskTimerEvent>{
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(TaskTimerEvent msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer sp = GameGlogalContainer.getRolesMap().get(msg.getRoleId());
		if (sp == null){//被投票驱逐的玩家
			log.error("没有角色直接去拿 数据库的数据");
			//暂时返回
			return;
		}
		int agree=0;
		int teamSize=0;
		if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){
			teamSize = sp.getTeamModular().getTeam().getObjects().size();
		}
		for (Iterator iterator = TeamGameManager.driverOutMap.get(msg.getRoleId()).values().iterator(); iterator.hasNext();) {
			Integer  reslutValue = (Integer ) iterator.next();
	        if(reslutValue==1){//表示同意
	        	agree++;
	        }
        }
		if(agree>(teamSize-1)/2){//驱逐这个玩家
			teamGameManager.diverOutTeam(sp, msg.getRoleId());
		}
		
		//移除投票数据结构
		TeamGameManager.driverOutMap.remove(msg.getRoleId());
    } 
	
}
  