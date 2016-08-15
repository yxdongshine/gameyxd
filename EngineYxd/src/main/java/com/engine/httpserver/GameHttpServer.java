/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:GameHttpServer.java 
 * Package Name:com.sj.world.httpserver 
 * Date:2014-2-18下午5:34:29 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.engine.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpServer;

/**
 * ClassName:GameHttpServer <br/>
 * Function: TODO (HTTP服务器管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-2-18 下午5:34:29 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class GameHttpServer {
	private Log log = LogFactory.getLog(GameHttpServer.class);
	
	HttpServer hs = null;
	
	public void start() {
		// String port = ServerGameConfig.RECHARGE_GM_PORT;
		try {
			// log.error("平台服务器端口::" + port);
			// hs = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);// 设置HttpServer的端口为8888
			// hs.createContext("/platform", new PlatformToServerHandler());
			// log.error("平台服务器已开启::" + port);
			hs.setExecutor(null); // creates a default executor
			hs.start();
			
		} catch (Exception e) {
			
			log.error("平台服务器有错误-----", e);
		}
		
	}
	
	public void stop() {
		if (hs != null) {
			hs.stop(1);
		}
	}
}
