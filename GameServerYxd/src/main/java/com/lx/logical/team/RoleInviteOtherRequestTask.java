
package com.lx.logical.team;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.team.Team.RoleInviteOtherRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.util.MathUtil;
import com.lx.game.send.MessageSend;
import com.lx.game.team.TeamModular;
import com.lx.game.team.TeamStaticConifg;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:RoleInviteOtherRequestTask <br/> 
 * Function: TODO (邀请组队). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-9-1 上午10:50:24 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_ROLE_INVITE_OTHER_REQUEST_VALUE)
public class RoleInviteOtherRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
    @Autowired
    TeamGameManager teamGameManager;
    
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			if(serverPlayer.getTeamModular()==null){
				TeamModular teamModular=new TeamModular();
				serverPlayer.setTeamModular(teamModular);
			}
			//
			RoleInviteOtherRequest builder =RoleInviteOtherRequest.parseFrom(msg.getBody());
			long inviteRoleId=builder.getRoleOtherId();
			//long launchInviteRoleId=teamGameManager.getLeaderId(serverPlayer);
			if(!teamGameManager.isFull(serverPlayer)){
				if(MathUtil.isPassCDTime(TeamStaticConifg.TEAM_INVITE_TIME, serverPlayer.getTeamModular().getInviteTime())){
					ServerPlayer sp = GameGlogalContainer.getRolesMap().get(inviteRoleId);
					if(sp!=null){
						if(sp.getTeamModular()!=null&&sp.getTeamModular().getTeam()!=null){//表示对方已经存在队伍
							//聊天频道显示：对方已有队伍
						}else{//发送邀请
							MessageSend.sendToGate(teamGameManager.BuildRoleOtherInviteMeResponse(serverPlayer), sp);
						}
					
					}else{//玩家没有上线
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_502), serverPlayer);
					}
				}else{//超过间隔时间
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_501), serverPlayer);
				}
			}else{//提示满员不能再要求
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_500), serverPlayer);
			}
		}
    }
	
}
  