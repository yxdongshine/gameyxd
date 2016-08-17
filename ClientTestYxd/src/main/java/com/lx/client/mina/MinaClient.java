package com.lx.client.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sun.net.NetworkClient;

import com.engine.config.xml.ServerInfo;
import com.engine.config.xml.ServerInfoManage;
import com.engine.res.XmlPath;
import com.engine.service.MConnector;
import com.lx.client.mina.listener.MinaClientListener;
import com.lx.logical.manage.PlayerManage;
import com.lx.logical.manage.ServerListManage;
import com.lx.server.mina.MinaNetClient;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

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
public class MinaClient extends MinaNetClient {
	
	@Autowired
	private MinaClientListener gateInnerClientListener;
	
	@Autowired
	private ServerListManage sManage;
	
	public void startInnerClient() {
		// TODO Auto-generated method stub
		try {
			
			initialize(null, 0, XmlPath.COMMOND_XML);
			this.addLisener(gateInnerClientListener);
			ServerInfo info = ServerInfoManage.getSConfig(ServerInfoManage.curServerInfo.connInfo.conns[0].toId);
			InetSocketAddress addr = new InetSocketAddress(info.host, info.port);
			for (int i = 0; i < PlayerManage.MAX_NUM; i++) {
				IoSession ioSession = connect(addr);
				if (ioSession != null) {
					MinaConnect connect = new MinaConnect();
					connect.setId(ioSession.getId());
					
					connect.setConnected(ioSession.isConnected());
					ioSession.setAttribute(NetConst.NET_SESSION, connect);
					connect.setAttachment(ioSession);
					connect.setConnected(true);
					
					sManage.sendServerListRequest("", connect);
					sManage.registerUser(connect);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addLisener(gateInnerClientListener);
	}
	
}
