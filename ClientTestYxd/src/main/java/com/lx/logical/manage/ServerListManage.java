package com.lx.logical.manage;

import org.springframework.stereotype.Component;

import com.loncent.protocol.login.LoginServer.RegisterAccountRequest;
import com.loncent.protocol.login.LoginServer.ServerListRequest;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ServerManage <br/>
 * Function: TODO (服务器列表管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午3:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ServerListManage {
	/**
	 * sendServerListRequest:(). <br/>
	 * TODO().<br/>
	 * 请求服务器列表
	 * 
	 * @author lyh
	 */
	public void sendServerListRequest(String playerAccount, IConnect con) {
		ServerListRequest.Builder slr = ServerListRequest.newBuilder();
		slr.setAccountName(playerAccount);
		con.send(slr.build());
		
	}
	
	public void registerUser(IConnect con){
		RegisterAccountRequest.Builder register = RegisterAccountRequest.newBuilder();
		register.setAccountName("yxd");
		register.setAppId("appid");
		register.setChannleId("channle");
		register.setImei("imei");
		register.setLanguage("languge");
		register.setMailAddress("659777399@qq.com");
		register.setPassword("123456");
		register.setPhoneNum("13624874078");
		register.setPhoneType("and");
		register.setVersion("1.0");
		con.send(register.build());
	}
}
