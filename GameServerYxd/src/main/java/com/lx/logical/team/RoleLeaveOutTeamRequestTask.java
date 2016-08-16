
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleLeaveOutTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleLeaveOutTeamRequestTask <br/> 
 * Function: TODO (离开团队). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 下午6:22:21 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_LEAVE_OUT_TEAM_REQUEST_VALUE)
public class RoleLeaveOutTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleLeaveOutTeamRequest builder =RoleLeaveOutTeamRequest.parseFrom(msg.getBody());
			long leaveOutRoleId =builder.getLeaveOutRoleId();
			teamGameManager.diverOutTeam(serverPlayer, leaveOutRoleId);
		}
    }
	
}
  