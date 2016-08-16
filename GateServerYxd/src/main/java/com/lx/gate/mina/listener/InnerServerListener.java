package com.lx.gate.mina.listener;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.gate.control.InnerClientControl;
import com.lx.gate.control.InnerServerControl;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.handler.XMLNetApdapterLisener;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:GateInnerServerListener <br/>
 * Function: TODO (内部服务监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:46:00 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class InnerServerListener extends XMLNetApdapterLisener<InnerMessage> {
	
	@Override
	public void sessionClose(IConnect session) {
		// TODO Auto-generated method stub
		ConnectorManage.removeConnect(session.getId());
		ConnectorManage.removeConnectFromList(session.getRemoteSType(), session.getId());
		ConnectorManage.removeConnectFromListTypeId(session.getRemoteSTypeid(), session.getId());
		session.setConnected(false);
	}
	
	@Override
	public void sessionOpen(IoSession paramNetSession) {
		// TODO Auto-generated method stub
		// 生成服务器用的边接
		IConnect conn = new MinaConnect();
		conn.setAttachment(paramNetSession);
		paramNetSession.setAttribute(NetConst.NET_SESSION, conn);
		conn.setConnected(true);
		ConnectorManage.putConnect(conn);
	}
	
	@Override
	public void sessionTimeOut(IConnect paramNetSession) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected IBeanInvoke<InnerMessage> getInvokeBean(String paramString) {
		// TODO Auto-generated method stub
		return (InnerServerControl) SpringBeanFactory.getSpringBean("innerServerControl");
	}
	
}
