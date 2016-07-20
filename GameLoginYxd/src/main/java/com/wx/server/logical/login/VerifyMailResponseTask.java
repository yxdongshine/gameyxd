package com.wx.server.logical.login;

import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.LoginServerRequest;
import com.loncent.protocol.login.LoginServer.RegisterAccountRequest;
import com.loncent.protocol.login.LoginServer.VerifyMailRequest;
import com.lx.server.mina.session.IConnect;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.msgloader.Head;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理修改密码验证邮箱). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_L_VERIFY_MAIl_REQUEST_VALUE)
public class VerifyMailResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		VerifyMailRequest register = VerifyMailRequest.parseFrom(msg.getBody());
	}
	
}
