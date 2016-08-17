package com.lx.logical.serverlist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.ServerListData;
import com.loncent.protocol.login.LoginServer.ServerListResponse;
import com.loncent.protocol.login.LoginServer.ServerListResponse.AreaListData;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.PlayerManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ServerListResponseTask <br/>
 * Function: TODO (接收服务器列表). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午3:39:01 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.L_C_SERVER_LIST_RESPONSE_VALUE)
public class ServerListResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Autowired
	private PlayerManage playerManage;
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerListResponse slrb = ServerListResponse.parseFrom(msg.getBody());
		AreaListData data = slrb.getAreaListList().get(0);
		ServerListData sData = data.getList(0);
		playerManage.setServerId("" + sData.getServerId());
		for (int i = 0; i < PlayerManage.MAX_NUM; i++) {
			playerManage.sendLoginServerRequest(session, playerManage.userName + (i + 2), "" + 1, "" + data.getAreaId(), "1", playerManage.password, sData.getServerId(), playerManage.getVersion());
		}
	}
	
}
