package com.lx.logical.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.CreateRoleRequest;
import com.loncent.protocol.game.login.LoginGameServer.EnterGameRequest;
import com.loncent.protocol.game.login.LoginGameServer.ResetTalentRequest;
import com.loncent.protocol.game.login.LoginGameServer.RoleListResponse;
import com.loncent.protocol.game.login.LoginGameServer.RoleMessageData;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.PlayerManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RoleListResponseTask <br/>
 * Function: TODO (角色列表). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午6:20:27 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_ROLE_LIST_RESPONSE_VALUE)
public class RoleListResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Autowired
	private PlayerManage playerManage;
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		RoleListResponse resp = RoleListResponse.parseFrom(msg.getBody());
		if (resp.getDataList() != null && resp.getDataCount() > 0) {
			for (RoleMessageData data : resp.getDataList()) {
				sendEnterGameRequest(data.getRoleId(), session);
				break;
			}
		} else {
			// 创建角色
			sendResetTalentRequest(1, session);
			sendCreateRoleRequest(session);
		}
	}
	
	/**
	 * sendEnterGameRequest:(). <br/>
	 * TODO().<br/>
	 * 发送进入游戏
	 * 
	 * @author lyh
	 * @param roleId
	 * @param con
	 */
	private void sendEnterGameRequest(long roleId, IConnect con) {
		EnterGameRequest.Builder builder = EnterGameRequest.newBuilder();
		builder.setRoleId(roleId);
		con.send(builder.build());
	}
	
	/**
	 * sendCreateRoleRequest:(). <br/>
	 * TODO().<br/>
	 * 发送创建角色
	 * 
	 * @author lyh
	 * @param con
	 */
	private void sendCreateRoleRequest(IConnect con) {
		CreateRoleRequest.Builder request = CreateRoleRequest.newBuilder();
		request.setChannleId(playerManage.getChannelId());
		request.setRoleName("swedw");
		request.setUserName(playerManage.userName);
		request.setServerId(Integer.parseInt(playerManage.getServerId()));
		con.send(request.build());
	}
	
	private void sendResetTalentRequest(int configId, IConnect con) {
		ResetTalentRequest.Builder build = ResetTalentRequest.newBuilder();
		build.setCareerConfigId(configId);
		con.send(build.build());
		
	}
	
}
