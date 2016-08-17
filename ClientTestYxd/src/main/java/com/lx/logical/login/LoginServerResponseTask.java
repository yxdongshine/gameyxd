package com.lx.logical.login;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.LoginGameServerRequest;
import com.loncent.protocol.login.LoginServer.LoginServerResponse;
import com.lx.client.mina.MinaClient;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.PlayerManage;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:LoginServerResponseTask <br/>
 * Function: TODO (处理登录游戏服). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午5:07:59 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.L_C_LOGIN_SERVER_RESPONSE_VALUE)
public class LoginServerResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Autowired
	private MinaClient minaClient;
	@Autowired
	private PlayerManage playerManage;
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		LoginServerResponse resp = LoginServerResponse.parseFrom(msg.getBody());
		session.close();
		
		if (resp.getStatus() <= 0) {
			return;
		}
		// 连接游戏服务器
		log.error("ip::" + resp.getIp() + "port::" + resp.getPort());
		InetSocketAddress addr = new InetSocketAddress(resp.getIp(), Integer.parseInt(resp.getPort()));
		IoSession ioSession = minaClient.connect(addr);
		if (ioSession != null) {
			MinaConnect connect = new MinaConnect();
			connect.setId(ioSession.getId());
			
			connect.setConnected(ioSession.isConnected());
			ioSession.setAttribute(NetConst.NET_SESSION, connect);
			connect.setAttachment(ioSession);
			connect.setConnected(true);
			playerManage.setConnect(connect);
			sendLoginGameServerRequest(resp.getToken());
			log.error("连接游戏服务器成功:" + resp.getIp() + ":" + resp.getPort());
		} else {
			log.error("ioSession为空:" + resp.getIp() + ":" + resp.getPort());
		}
		
	}
	
	/**
	 * sendLoginGameServerRequest:(). <br/>
	 * TODO().<br/>
	 * 进入游戏服务器
	 * 
	 * @author lyh
	 * @param token
	 */
	private void sendLoginGameServerRequest(long token) {
		LoginGameServerRequest.Builder request = LoginGameServerRequest.newBuilder();
		request.setAccountName(playerManage.userName);
		request.setServerId(Integer.parseInt(playerManage.getServerId()));
		request.setToken(token);
		request.setVersion(playerManage.getVersion());
		playerManage.getConnect().send(request.build());
	}
	
}
