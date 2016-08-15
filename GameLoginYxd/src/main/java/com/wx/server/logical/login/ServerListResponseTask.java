package com.wx.server.logical.login;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.ServerListData;
import com.loncent.protocol.login.LoginServer.ServerListRequest;
import com.loncent.protocol.login.LoginServer.ServerListResponse;
import com.loncent.protocol.login.LoginServer.ServerListResponse.AreaListData;
import com.lx.server.mina.session.IConnect;
import com.wx.server.domain.AreaList;
import com.wx.server.domain.Player;
import com.wx.server.domain.ServerList;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.logical.manage.LoginManage;
import com.wx.server.msgloader.Head;
import com.wx.server.obj.LoginServerList;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理服务器列表逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_L_SERVER_LIST_REQUEST_VALUE)
public class ServerListResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerListRequest pServerListRequest = ServerListRequest.parseFrom(msg.getBody());
		ServerListData sld = null;// 最近登录或者最新的服务器
		// 判断是否已经存在帐号
		if (pServerListRequest.getAccountName() != null && !pServerListRequest.getAccountName().equals("")) {
			List<Player> playerList = entityDAOInterface.findByProperty(Player.class, "accountName", pServerListRequest.getAccountName());
			if (playerList.size() > 0) {
				Player p = playerList.get(0);
				ServerList slServer = LoginManage.serverListMap.get("" + p.getLastServerId());
				if (slServer != null) {
					sld = createServerListData(slServer);
				}
			}
		}
		sendServerListResponse(session, pServerListRequest, sld);
		// //查找到推荐服
		// if (sld == null){
		//
		// }
	}
	
	/**
	 * sendServerListResponse:(). <br/>
	 * TODO().<br/>
	 * 发送服务器列表
	 * 
	 * @author lyh
	 * @param session
	 * @param pServerListRequest
	 */
	private void sendServerListResponse(IConnect session, ServerListRequest pServerListRequest, ServerListData sld) {
		ServerListResponse.Builder builder = ServerListResponse.newBuilder();
		if (sld != null) {
			builder.setLastLoginServer(sld);
		}
		
		for (LoginServerList lsl : LoginManage.areaListMap) {
			AreaList pAreaList = lsl.getAreaList();
			AreaListData.Builder data = AreaListData.newBuilder();
			data.setAreaId((int) pAreaList.getId());
			data.setAreaName(pAreaList.getAreaName());
			
			for (ServerList sl : lsl.getCpServerList()) {
				data.addList(createServerListData(sl));
			}
			
			builder.addAreaList(data.build());
		}
		// log.error("yxd******builder.getAreaListCount()长度**"+builder.getAreaListList().size());
		
		session.send(builder.build());
	}
	
	/**
	 * createServerListData:(). <br/>
	 * TODO().<br/>
	 * 创建服务器列表项协议数据包
	 * 
	 * @author lyh
	 * @param sl
	 * @return
	 */
	private ServerListData createServerListData(ServerList sl) {
		ServerListData.Builder slData = ServerListData.newBuilder();
		slData.setServerId((int) sl.getId());
		slData.setIsNewServer(sl.getIsNewServer() == 1 ? true : false);
		slData.setServerName(sl.getServerName());
		slData.setServerStatus(sl.getStatus());
		return slData.build();
	}
}
