package com.lx.logical.gate;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.game.login.LoginGameServer.EnterGameResponse;
import com.loncent.protocol.inner.InnerToMessage.GameGateEnterGameSuccessResponse;
import com.lx.gate.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:GameGateEnterGameSuccessResponseTask <br/>
 * Function: TODO (世界服通知网关服务器进入游戏成功). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午10:23:50 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.GAME_GATE_ENTER_GAME_SUCCESS_RESPONSE_VALUE)
public class GameGateEnterGameSuccessResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		GameGateEnterGameSuccessResponse resq = GameGateEnterGameSuccessResponse.parseFrom(msg.getBody());
		IConnect con = ConnectorManage.getMinaConnect(msg.getClientSessionId());
		if (con != null) {
			con.setRoleId(resq.getRoleId());
			MessageSend.sendGateToClient(createEnterGameResponse(resq.getRoleId()), msg.getClientSessionId());
			log.error("send client ok" + msg.getClientSessionId());
		} else {
			log.error("不存在id" + msg.getClientSessionId());
		}
	}
	
	private EnterGameResponse createEnterGameResponse(long roleId) {
		EnterGameResponse resp = EnterGameResponse.newBuilder().setResult(1).setRoleId(roleId).build();
		return resp;
	}
}
