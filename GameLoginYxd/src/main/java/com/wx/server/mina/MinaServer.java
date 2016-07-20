package com.wx.server.mina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.server.mina.MinaNetSever;
import com.lx.server.mina.handler.INetApdapterLisener;
import com.wx.server.config.properties.ServerGameConfig;
import com.wx.server.config.xml.ServerInfoManage;
import com.wx.server.res.XmlPath;
import com.wx.server.thread.Closeable;
import com.wx.server.thread.ShutDownThread;

/**
 * ClassName:Server <br/>
 * Function: TODO (启动mina). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-6 下午3:02:00 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaServer extends MinaNetSever implements Closeable {
	@Autowired
	private ServerListener serverListener;
	
	public void startServer() {
		try {
			this.initialize(ServerInfoManage.curServerInfo.port, XmlPath.COMMOND_XML);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.addLisener(serverListener);
		ShutDownThread.addCloseable(this);
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		stop();
	}
	
}
