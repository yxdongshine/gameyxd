
package com.lx.logical.team;  

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleTransferInTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleTransferInTeamRequestTask <br/> 
 * Function: TODO (移交队长). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-2 下午5:34:49 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_TRANSFER_TEAM_HEADER_REQUEST_VALUE)
public class RoleTransferInTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			if(teamGameManager.hasTeam(serverPlayer)){
				if(serverPlayer.getRole().getId()==serverPlayer.getTeamModular().getTeam().getTeamLeader().getRoleId()){//是队长
					RoleTransferInTeamRequest builder=RoleTransferInTeamRequest.parseFrom(msg.getBody());
					long transferRoleId=builder.getTransferRoleId();
					if(serverPlayer.getTeamModular().getTeam().TransferLeader(transferRoleId)){
						List<ServerPlayer> spList= new ArrayList<ServerPlayer>();
						spList.add(serverPlayer);
						spList.add(GameGlogalContainer.getRolesMap().get(transferRoleId));
						teamGameManager.sendAllTeamerTransferLeader(spList);
					}
				}else{//提示：你不是队长，不能移交队长称号
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_509), serverPlayer);
				}
			}
		}
    }
	
}
  