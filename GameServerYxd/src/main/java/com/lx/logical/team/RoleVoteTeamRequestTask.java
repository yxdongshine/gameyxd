
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleVoteTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleVoteTeamRequestTask <br/> 
 * Function: TODO (投票). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 下午4:58:39 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_VOTE_TEAM_REQUEST_VALUE)
public class RoleVoteTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleVoteTeamRequest builder =RoleVoteTeamRequest.parseFrom(msg.getBody());
			int result = builder.getResult();//是否同意
			if(teamGameManager.hasTeam(serverPlayer)){//
				long leaderId=teamGameManager.getLeaderId(serverPlayer);
				if(TeamGameManager.driverOutMap.get(leaderId)!=null){
					TeamGameManager.driverOutMap.get(leaderId).put(serverPlayer.getRole().getId(), result);
				}else{//提示：投票时间已经过了
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_511), serverPlayer);
				}
			}
			
		}
    }
	
}
  