
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleIdDismissTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleIdDismissTeamRequestTask <br/> 
 * Function: TODO (解散小队). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 上午10:14:00 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_DISMISS_TEAM_REQUEST_VALUE)
public class RoleIdDismissTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			//RoleIdDismissTeamRequest builder =RoleIdDismissTeamRequest.parseFrom(msg.getBody());
			//long leaderRoleId=builder.getLeaderRoleId();
			if(serverPlayer.getRole().getId()==serverPlayer.getTeamModular().getTeam().getTeamLeader().getRoleId()){//本人必须是队长
				teamGameManager.dismissTeam(serverPlayer);
			}else{//提示：你不是队长，不能解散队伍
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_510), serverPlayer);
			}
		}
    }
	
}
  