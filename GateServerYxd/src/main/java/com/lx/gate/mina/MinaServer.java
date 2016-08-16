package com.lx.gate.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.close.Stopable;
import com.engine.config.xml.ServerInfoManage;
import com.engine.res.XmlPath;
import com.engine.shutdown.ShutDownThread;
import com.lx.gate.mina.listener.MinaServerListener;
import com.lx.server.mina.MinaNetSever;

/**
 * ClassName:GateMinaServer <br/>
 * Function: TODO (网关外头服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:24:41 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaServer extends MinaNetSever implements Stopable {
	
	@Autowired
	private MinaServerListener gateMinaServerListener;
	
	public void startMinaServer() {
		try {
			ShutDownThread.registerCloseableToShutDown(this);
			initialize(ServerInfoManage.curServerInfo.minaPort, XmlPath.COMMOND_XML);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addLisener(gateMinaServerListener);
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
}
