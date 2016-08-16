package com.lx.world.data;

import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.game.login.LoginGameServer.LoginGameServerRequest;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:CheckTokenData <br/>
 * Function: TODO (登录令牌验证数据). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午6:34:00 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class CheckTokenData {
	
	private LoginGameServerRequest req;
	private IConnect con;
	
	private InnerMessage innerMessage;
	
	public LoginGameServerRequest getReq() {
		return req;
	}
	
	public void setReq(LoginGameServerRequest req) {
		this.req = req;
	}
	
	public IConnect getCon() {
		return con;
	}
	
	public void setCon(IConnect con) {
		this.con = con;
	}
	
	public InnerMessage getInnerMessage() {
		return innerMessage;
	}
	
	public void setInnerMessage(InnerMessage innerMessage) {
		this.innerMessage = innerMessage;
	}
}
