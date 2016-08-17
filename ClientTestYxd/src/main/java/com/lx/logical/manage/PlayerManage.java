package com.lx.logical.manage;

import org.springframework.stereotype.Component;

import com.loncent.protocol.login.LoginServer.LoginServerRequest;
import com.loncent.protocol.login.LoginServer.ServerListRequest;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:PlayerManage <br/>
 * Function: TODO (角色管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午3:09:07 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class PlayerManage {
	public String userName = "jack@test.com";
	public String password = "jack123";
	public static int MAX_NUM = 1;
	private IConnect connect;
	
	/** 服务器id **/
	private String serverId;
	
	private final String version = "1.00";
	
	private final String channelId = "1";
	
	/**
	 * sendLoginServerRequest:(). <br/>
	 * TODO().<br/>
	 * 发送登录信息
	 * 
	 * @author lyh
	 * @param con
	 * @param userName
	 * @param appId
	 * @param areaId
	 * @param channelId
	 * @param password
	 * @param serverId
	 * @param vsersion
	 */
	public void sendLoginServerRequest(IConnect con, String userName, String appId, String areaId, String channelId, String password, int serverId, String vsersion) {
		LoginServerRequest.Builder request = LoginServerRequest.newBuilder();
		request.setAccountName(userName);
		request.setAppId(appId);
		request.setAreaId(areaId);
		request.setChannleId(channelId);
		request.setPassword(password);
		request.setServerId(serverId);
		request.setVersion(vsersion);
		con.send(request.build());
	}
	
	public IConnect getConnect() {
		return connect;
	}
	
	public void setConnect(IConnect connect) {
		this.connect = connect;
	}
	
	public String getServerId() {
		return serverId;
	}
	
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public String getChannelId() {
		return channelId;
	}
	
}
