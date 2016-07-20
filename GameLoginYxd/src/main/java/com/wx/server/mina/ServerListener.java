package com.wx.server.mina;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.handler.XMLNetApdapterLisener;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.session.NetSession;
import com.lx.server.mina.utils.NetConst;
import com.wx.server.beanfactory.SpringBeanFactory;

/**
 * ClassName:ServerListener <br/>
 * Function: TODO (监听服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-6 下午3:18:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ServerListener extends XMLNetApdapterLisener<MinaMessage> {
	
	@SuppressWarnings("unchecked")
	@Override
	protected IBeanInvoke<MinaMessage> getInvokeBean(String paramString) {
		// TODO Auto-generated method stub
		IBeanInvoke<MinaMessage> bean = (IBeanInvoke<MinaMessage>) SpringBeanFactory.getSpringBean(paramString);
		return bean;
	}
	
	@Override
	public void sessionClose(IConnect session) {
		// TODO Auto-generated method stub
		System.err.println("关闭了session");
		if (session != null) {
			session.setConnected(false);
		}
	}
	
	@Override
	public void sessionOpen(IoSession paramNetSession) {
		// TODO Auto-generated method stub
		System.err.println("打开了session");
		IConnect conn = new MinaConnect();
		conn.setAttachment(paramNetSession);
		paramNetSession.setAttribute(NetConst.NET_SESSION, conn);
		conn.setConnected(true);
	}
	
	@Override
	public void sessionTimeOut(IConnect paramNetSession) {
		// TODO Auto-generated method stub
		
	}
	
}
