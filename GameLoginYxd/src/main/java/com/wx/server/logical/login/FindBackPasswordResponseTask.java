package com.wx.server.logical.login;

import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.FindBackPasswordRequest;
import com.lx.server.mina.session.IConnect;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.msgloader.Head;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理找回密码逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

@Component
@Head(CmdType.C_L_FIND_BACK_PASSWORD_REQUEST_VALUE)
public class FindBackPasswordResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		FindBackPasswordRequest register = FindBackPasswordRequest.parseFrom(msg.getBody());
		
	}
	
}
