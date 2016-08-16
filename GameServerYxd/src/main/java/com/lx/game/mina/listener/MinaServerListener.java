package com.lx.game.mina.listener;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.game.control.MinaServerControl;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.handler.XMLNetApdapterLisener;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:GateMinaServerListener <br/>
 * Function: TODO (mina监听类). <br/>
 * Reason: TODO (暂时不用). <br/>
 * Date: 2015-7-1 下午2:40:27 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Deprecated
public class MinaServerListener extends XMLNetApdapterLisener<MinaMessage> {
	
	@Override
	public void sessionClose(IConnect session) {
		// TODO Auto-generated method stub
		// 通知各个服务器
		ConnectorManage.removeMinaConnect(session.getId());
		session.setConnected(false);
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
