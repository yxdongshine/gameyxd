package com.lx.logical.login;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.ResetTalentResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ResetTalentResponseTask <br/>
 * Function: TODO (接收资质消息). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午6:35:13 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_RESET_TALENT_RESPONSE_VALUE)
public class ResetTalentResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ResetTalentResponse resp = ResetTalentResponse.parseFrom(msg.getBody());
	}
	
}
