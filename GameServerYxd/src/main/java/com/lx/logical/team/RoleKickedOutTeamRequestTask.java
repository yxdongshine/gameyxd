
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleKickedOutTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleKickedOutTeamRequestTask <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-6 下午5:15:20 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_KICKED_OUT_TEAM_REQUEST_VALUE)
public class RoleKickedOutTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	TeamGameManager teamGameManager;
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleKickedOutTeamRequest builder =RoleKickedOutTeamRequest.parseFrom(msg.getBody());
			long kickedOutRoleId =builder.getKickedOutRoleId();
			teamGameManager.diverOutTeam(serverPlayer, kickedOutRoleId);
		}
    }
	
}
  