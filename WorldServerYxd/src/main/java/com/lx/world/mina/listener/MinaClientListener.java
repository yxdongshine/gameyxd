package com.lx.world.mina.listener;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.handler.XMLNetApdapterLisener;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;
import com.lx.world.control.MinaClientControl;

/**
 * ClassName:GateInnerClientListener <br/>
 * Function: TODO (网关客户端连接监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:43:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaClientListener extends XMLNetApdapterLisener<MinaMessage> {
	
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
		
	}
	
	@Override
	public void sessionTimeOut(IConnect paramNetSession) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected IBeanInvoke<MinaMessage> getInvokeBean(String paramString) {
		// TODO Auto-generated method stub
		return (MinaClientControl) SpringBeanFactory.getSpringBean("minaClientControl");
	}
	
}
