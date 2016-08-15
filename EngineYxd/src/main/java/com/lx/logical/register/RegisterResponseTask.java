package com.lx.logical.register;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.InnerToMessage.RegisterRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RegisterResponseTask <br/>
 * Function: TODO (处理连接注册). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午6:12:51 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.SC_SS_REGISTER_REQUEST_VALUE)
public class RegisterResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		RegisterRequest rr = RegisterRequest.parseFrom(msg.getBody());
		session.setRemoteSTypeid(rr.getServerTypeId());
		session.setRemoteSType(rr.getServerType());
		session.setRemoteName(rr.getServerName());
		session.setRemoteGroup(rr.getServerGroup());
		session.setRemouteAdress(new InetSocketAddress(rr.getServerHost(), rr.getServerPort()));
		ConnectorManage.putConnectToList(session);
	}
	
}
