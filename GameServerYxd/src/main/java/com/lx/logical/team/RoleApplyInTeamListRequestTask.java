
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleApplyInTeamListRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleApplyInTeamListRequestTask <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-2 上午11:28:03 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_APPLY_IN_TEAM_LIST_REQUEST_VALUE)
public class RoleApplyInTeamListRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	    
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleApplyInTeamListRequest builder =RoleApplyInTeamListRequest.parseFrom(msg.getBody());
			if(teamGameManager.getLeaderId(serverPlayer)==serverPlayer.getRole().getId()){//是队长
				MessageSend.sendToGate(teamGameManager.buildRoleApplyInTeamListResponse(serverPlayer.getTeamModular().getTeam().getRequestList()), serverPlayer);
			}else{//提示：不是队长，没有申请人员
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_505), serverPlayer);
			}
		}
    }
	
}
  