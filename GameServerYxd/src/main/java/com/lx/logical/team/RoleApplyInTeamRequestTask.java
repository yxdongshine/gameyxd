
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleApplyInTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleApplyInTeamRequestTask <br/> 
 * Function: TODO (申请加入某个小队). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-2 上午10:54:12 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_APPLY_IN_TEAM_REQUEST_VALUE)
public class RoleApplyInTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	    
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleApplyInTeamRequest builder=RoleApplyInTeamRequest.parseFrom(msg.getBody());
			long roleInviteId =builder.getRoleInviteId();//申请加入小队的编号
			//判断申请的角色是否已经有小队
			if(!teamGameManager.hasTeam(serverPlayer)){
				ServerPlayer sp =GameGlogalContainer.getRolesMap().get(roleInviteId);
				if(sp!=null){
					if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){//小队存在
						long leaderRoleId=teamGameManager.getLeaderId(sp);
						//取出队长
						ServerPlayer leader =GameGlogalContainer.getRolesMap().get(leaderRoleId);
						//将申请编号加入leader 小队中
						if(teamGameManager.canPassInviteLimit(leader)){
							if(!teamGameManager.joinPassInviteLimit(leader, serverPlayer)){
								//主动推送申请列表给队长
								MessageSend.sendToGate(teamGameManager.buildRoleApplyInTeamListResponse(leader.getTeamModular().getTeam().getRequestList()), leader);
							}else{//提示：不能再次申请
								MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_513), serverPlayer);
							}
							
						}else{//申请超出限制上线人数
							MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_504), serverPlayer);
						}
					}else{//提示：对方不存在小队
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_503), serverPlayer);
					}
				}else{
					log.error("玩家不存在");
				}
			}else{//提示：你已经在小队中
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_506), serverPlayer);
			}
					
		}
    }
	
}
  