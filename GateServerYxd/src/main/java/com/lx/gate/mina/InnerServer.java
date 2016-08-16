package com.lx.gate.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.close.Stopable;
import com.engine.config.xml.ServerInfoManage;
import com.engine.res.XmlPath;
import com.engine.shutdown.ShutDownThread;
import com.lx.gate.mina.listener.InnerServerListener;
import com.lx.server.mina.InnerNetSever;

/**
 * ClassName:GateInnerServer <br/>
 * Function: TODO (网关内部服务器)). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:44:53 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class InnerServer extends InnerNetSever implements Stopable {
	
	@Autowired
	private InnerServerListener gateInnerServerListener;
	
	public void startInnerServer() {
		// TODO Auto-generated method stub
		try {
			ShutDownThread.registerCloseableToShutDown(this);
			initialize(ServerInfoManage.curServerInfo.port, XmlPath.COMMOND_XML);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addLisener(gateInnerServerListener);
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
	}
	
}
