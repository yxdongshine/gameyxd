package com.lx.world.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.close.Stopable;
import com.engine.config.xml.ServerInfoManage;
import com.engine.res.XmlPath;
import com.engine.service.ConnectorManage;
import com.engine.service.MConnector;
import com.engine.shutdown.ShutDownThread;
import com.lx.server.mina.InnerNetClient;
import com.lx.server.mina.MinaNetClient;
import com.lx.world.mina.listener.MinaClientListener;

/**
 * ClassName:GateInnerClient <br/>
 * Function: TODO (网关客户端). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:42:26 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaClient extends MinaNetClient implements Stopable {
	
	@Autowired
	private MinaClientListener gateInnerClientListener;
	
	public void startInnerClient() {
		// TODO Auto-generated method stub
		try {
			ShutDownThread.registerCloseableToShutDown(this);
			initialize(null, 0, XmlPath.COMMOND_XML);
			MConnector m = new MConnector();
			m.init(ServerInfoManage.curServerInfo, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addLisener(gateInnerClientListener);
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		this.close();
	}
	
}
