package com.engine.service;

import org.apache.mina.core.service.IoConnector;

import com.engine.config.xml.ServerInfo;
import com.lx.server.mina.IClient;
import com.lx.server.mina.InnerNetClient;

/**
 * 连接管理器
 * 
 * @author lyh
 * 
 */
public interface IConnectorProxy {
	public void init(ServerInfo sinfo, IClient connector);
	
	public String getInfo();
	
}
