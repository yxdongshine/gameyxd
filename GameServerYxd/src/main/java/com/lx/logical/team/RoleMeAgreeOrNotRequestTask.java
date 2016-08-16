
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleMeAgreeOrNotRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.game.team.TeamStaticConifg;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleMeAgreeOrNotRequestTask <br/> 
 * Function: TODO (受邀请的玩家是否同意). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-1 下午3:11:31 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_ME_AGREE_OR_NOT_REQUEST_VALUE)
public class RoleMeAgreeOrNotRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage>{
    @Autowired
    TeamGameManager teamGameManager;
    
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleMeAgreeOrNotRequest builder =RoleMeAgreeOrNotRequest.parseFrom(msg.getBody());
			int result=builder.getResult();//结果
			long roleInviteId=builder.getRoleInviteId();
			ServerPlayer sp = GameGlogalContainer.getRolesMap().get(roleInviteId);
			if(result==TeamStaticConifg.TEAM_INVITE_AGREE){//同意 
				if(!teamGameManager.hasTeam(sp)){//构建团队
					if(!teamGameManager.hasTeamer(sp, serverPlayer.getRole().getId())){
						//构建团队
						teamGameManager.buildTeam(sp,serverPlayer);
					}else{//提示:已经存在小队中
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_510), serverPlayer);
					}
					
				}else{//将邀请者加入团队中
					if(!teamGameManager.hasTeamer(sp, serverPlayer.getRole().getId())){
						if(!sp.getTeamModular().isFullStarffed()){
							teamGameManager.addTeamerInTeam(sp, serverPlayer);
						}else{//提示：小队中满员，不能再添加
							MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_508), serverPlayer);
						}
					}else{//提示:已经存在小队中
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_510), serverPlayer);
					}
					
				}
				//群发一个提示添加成功的消息
				teamGameManager.sendAllTeamerBulidTeam(serverPlayer, TeamStaticConifg.TEAM_INVITE_AGREE);
				//发送给所有小队成员小队的全部信息
				teamGameManager.sendAllTeamerAllInfo(serverPlayer);
			}else{//不同意
				MessageSend.sendToGate(teamGameManager.buildRoleInviteOtherResponse(TeamStaticConifg.TEAM_INVITE_AGREE_NOT,serverPlayer), sp);
			}
		}
    }
	
}
  