package com.lx.gate.mina.listener;

import org.apache.commons.logging.Log;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.service.ConnectorManage;
import com.engine.utils.log.LogUtils;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.inner.InnerToMessage.GateGamePlayerExitRequest;
import com.loncent.protocol.inner.InnerToMessage.GateWorldPlayerExitRequest;
import com.lx.gate.control.MinaServerControl;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.handler.XMLNetApdapterLisener;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:GateMinaServerListener <br/>
 * Function: TODO (mina监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:40:27 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaServerListener extends XMLNetApdapterLisener<MinaMessage> {
	
	Log log = LogUtils.getLog(MinaServerListener.class);
	
	@Override
	public void sessionClose(IConnect session) {
		// TODO Auto-generated method stub
		// 通知各个服务器
		ConnectorManage.removeMinaConnect(session.getId());
		session.setConnected(false);
		
		// 通知游戏服,通知世界服
		IConnect con = ConnectorManage.getConnectFromList(ServerType.WORLD_SERVER);
		if (con != null) {
			sendGateWorldPlayerExitRequest(con, session.getId());
		} else {
			log.error("没有连接到世界服务器");
		}
		
		con = ConnectorManage.getConnectFromList(ServerType.GAME_SERVER);
		if (con != null) {
			sendGateGamePlayerExitRequest(con, session.getId());
		} else {
			log.error("没有连接到游戏服务器");
		}
		
	}
	
	/**
	 * sendGateWorldPlayerExitRequest:(). <br/>
	 * TODO().<br/>
	 * 通知世界服务器角色退出
	 * 
	 * @author lyh
	 * @param con
	 */
	private void sendGateWorldPlayerExitRequest(IConnect con, long sessionId) {
		GateWorldPlayerExitRequest request = GateWorldPlayerExitRequest.newBuilder().setSessionId(sessionId).build();
		con.send(request);
	}
	
	private void sendGateGamePlayerExitRequest(IConnect con, long sessionId) {
		GateGamePlayerExitRequest request = GateGamePlayerExitRequest.newBuilder().setSessionId(sessionId).build();
		con.send(request);
	}
	
	@Override
	public void sessionOpen(IoSession paramNetSession) {
		// TODO Auto-generated method stub
		IConnect conn = new MinaConnect();
		conn.setAttachment(paramNetSession);
		paramNetSession.setAttribute(NetConst.NET_SESSION, conn);
		conn.setConnected(true);
		ConnectorManage.putMinaConnect(conn);
	}
	
	@Override
	public void sessionTimeOut(IConnect paramNetSession) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected IBeanInvoke<MinaMessage> getInvokeBean(String paramString) {
		// TODO Auto-generated method stub
		return (MinaServerControl) SpringBeanFactory.getSpringBean("minaServerControl");
	}
	
}
