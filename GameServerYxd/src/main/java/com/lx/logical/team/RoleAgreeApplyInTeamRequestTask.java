
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleAgreeApplyInTeamRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.game.team.TeamModular;
import com.lx.game.team.Teamer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleAgreeApplyInTeamRequestTask <br/> 
 * Function: TODO (同意某角色入队). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-2 下午2:40:44 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_AGREE_APPLY_IN_TEAM_REQUEST_VALUE)
public class RoleAgreeApplyInTeamRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
	@Autowired
	TeamGameManager teamGameManager;
	
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleAgreeApplyInTeamRequest builder =RoleAgreeApplyInTeamRequest.parseFrom(msg.getBody());
			long agreeRoleInviteId =builder.getAgreeRoleInviteId();
			ServerPlayer sp =GameGlogalContainer.getRolesMap().get(agreeRoleInviteId);
			if(!teamGameManager.hasTeam(sp)){
				//将申请者加入自己的小队中
				if(teamGameManager.hasTeam(serverPlayer)){
					if(!serverPlayer.getTeamModular().isFullStarffed()){
						Teamer teamer=new Teamer(sp.getRole().getId(),(byte)serverPlayer.getTeamModular().nextTeamerOrder());
						//设置sp的team编号
						TeamModular agreeRoleInvitetm=new TeamModular();
						agreeRoleInvitetm.setTeamId(serverPlayer.getTeamModular().getTeamId());
						sp.setTeamModular(agreeRoleInvitetm);
						//添加成员
						serverPlayer.getTeamModular().getTeam().getObjects().add(teamer);
						//移除申请列表
						serverPlayer.getTeamModular().removeRequestRoleId(sp.getRole().getId());
						//发送申请列表
						MessageSend.sendToGate(teamGameManager.buildRoleApplyInTeamListResponse(serverPlayer.getTeamModular().getTeam().getRequestList()), serverPlayer);
						//群发整个小队列表给每个人
						teamGameManager.sendAllTeamerAllInfo(serverPlayer);
					}else{//提示：小队中满员，不能再添加
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_508), serverPlayer);
					}
				}
			}else{//提示：对方已经存在小队，不能加入自己的小队中
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_507), serverPlayer);
			}
		}
    }
	
}
  