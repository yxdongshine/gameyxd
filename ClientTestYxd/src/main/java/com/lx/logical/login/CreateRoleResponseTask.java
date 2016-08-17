package com.lx.logical.login;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.CreateRoleResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:CreateRoleResponseTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午6:27:35 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_CREATE_ROLE_RESPONSE_VALUE)
public class CreateRoleResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		CreateRoleResponse resp = CreateRoleResponse.parseFrom(msg.getBody());
		log.error("结果:::" + resp.getResult());
	}
	
}
