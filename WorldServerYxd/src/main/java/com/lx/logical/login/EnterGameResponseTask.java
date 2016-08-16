package com.lx.logical.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.EnterGameRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.world.manage.LoginManage;
import com.lx.logical.world.manage.ServerPlayerManage;
import com.lx.server.mina.session.IConnect;
import com.lx.world.send.MessageSend;

/**
 * ClassName:EnterGameResponseTask <br/>
 * Function: TODO (开始进入游戏). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午4:22:49 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_ENTER_GMAE_REQUEST_VALUE)
public class EnterGameResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private LoginManage loginManage;
	
	@Autowired
	private ServerPlayerManage serverPlayerManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		EnterGameRequest request = EnterGameRequest.parseFrom(msg.getBody());
		Role role = dao.findById(Role.class, request.getRoleId());
		if (role != null) {
			ServerPlayer sp = serverPlayerManage.createServerPlayer(msg, role, false);
			MessageSend.sendToGate(loginManage.createRoleResult(1), session, msg.getClientSessionId(), msg.getGateTypeId());
			// 通知道游戏服务器，网关
			MessageSend.sendToGame(serverPlayerManage.createWorldGameEnterGameRequest(role), sp, false);
			
		} else {
			log.error("没有在数据库中找到角色:" + request.getRoleId());
		}
	}
	
}
