package com.engine.service;

import com.engine.config.xml.ServerInfo;

/**
 * 服务管理器
 * 
 * @author lyh
 * 
 */
public interface IAccptorProxy {
	public void init(ServerInfo sinfo);
	
	public String getInfo();
}
