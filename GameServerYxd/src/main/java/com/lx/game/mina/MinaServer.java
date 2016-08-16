package com.lx.game.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.ServerInfoManage;
import com.engine.res.XmlPath;
import com.lx.game.mina.listener.MinaServerListener;
import com.lx.server.mina.MinaNetSever;

/**
 * ClassName:GateMinaServer <br/>
 * Function: TODO (网关外头服务器). <br/>
 * Reason: TODO (暂时不用). <br/>
 * Date: 2015-7-1 下午2:24:41 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Deprecated
public class MinaServer extends MinaNetSever {
	
	@Autowired
	private MinaServerListener gateMinaServerListener;
	
	public void startMinaServer() {
		try {
			initialize(ServerInfoManage.curServerInfo.minaPort, XmlPath.COMMOND_XML);
		} catch (Exception e) {
			e.printStackTrace();
		}
		addLisener(gateMinaServerListener);
	}
	
}
