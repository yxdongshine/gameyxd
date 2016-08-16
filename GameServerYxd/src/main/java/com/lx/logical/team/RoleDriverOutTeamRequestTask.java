
package com.lx.logical.team;  

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleDriverOutTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.game.team.TeamStaticConifg;
import com.lx.game.timer.TaskTimerEvent;
import com.lx.game.timer.VoteDriverOutTeamTimerEvenet;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleDriverOutTeamRequestTask <br/> 
 * Function: TODO (发起驱逐某个玩家). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 上午10:57:53 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_DRIVE_OUT_TEAM_REQUEST_VALUE)
public class RoleDriverOutTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	TeamGameManager teamGameManager;
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleDriverOutTeamRequest builder =RoleDriverOutTeamRequest.parseFrom(msg.getBody());
			long driverOutRoleId =builder.getDriverOutRoleId();
			ServerPlayer beDriversp=GameGlogalContainer.getRolesMap().get(driverOutRoleId);
			if(teamGameManager.hasTeam(serverPlayer)){//
				long leaderId=teamGameManager.getLeaderId(serverPlayer);
				if(TeamGameManager.driverOutMap.get(leaderId)==null){//
					//发起者默认投赞成票数
					HashMap<Long, Integer> voteMap=new HashMap<Long, Integer>();
					voteMap.put(serverPlayer.getRole().getId(), 1);
					TeamGameManager.driverOutMap.put(leaderId, voteMap);
					//设置时间事件 一分钟后统计
					// 注册时间事件
					VoteDriverOutTeamTimerEvenet vdot = new VoteDriverOutTeamTimerEvenet(leaderId,driverOutRoleId,TeamStaticConifg.TEAM_VOTE_TIME*60*1000);
					//下发驱逐的玩家信息
					teamGameManager.sendAllTeamerBuildRoleListTeamResponse(serverPlayer, beDriversp);
				}else{//提示：小队正在投票驱逐，一分钟后再来
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_512), serverPlayer);
				}
			}
			
		}
    }
	
}
  