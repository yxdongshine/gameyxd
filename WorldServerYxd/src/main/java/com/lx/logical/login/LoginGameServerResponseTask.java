package com.lx.logical.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.engine.service.ConnectorManage;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.LoginGameServerRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.world.manage.LoginManage;
import com.lx.server.mina.session.IConnect;
import com.lx.world.container.WorldGlogalContainer;
import com.lx.world.data.CheckTokenData;

/**
 * ClassName:LoginGameServerResponseTask <br/>
 * Function: TODO (处理角色登录游戏服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午6:25:31 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_LOGIN_GAME_SERVER_REQUEST_VALUE)
public class LoginGameServerResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private LoginManage loginManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		LoginGameServerRequest request = LoginGameServerRequest.parseFrom(msg.getBody());
		IConnect con = ConnectorManage.getConnectFromList(ServerType.LOGIN_SERVER);
		if (con != null) {
			loginManage.sendCheckTokenRequest(request.getAccountName(), request.getToken(), con);
			CheckTokenData data = new CheckTokenData();
			data.setCon(session);
			data.setReq(request);
			data.setInnerMessage(msg);
			
			WorldGlogalContainer.getTokendatamap().put(request.getToken(), data);
		} else {
			log.error("没有连接上登录服务器");
		}
	}
	
}
