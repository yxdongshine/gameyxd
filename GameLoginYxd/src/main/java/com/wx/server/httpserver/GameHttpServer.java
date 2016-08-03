/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:GameHttpServer.java 
 * Package Name:com.sj.world.httpserver 
 * Date:2014-2-18下午5:34:29 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.wx.server.httpserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpServer;
import com.wx.server.config.properties.ServerGameConfig;
import com.wx.server.httphandler.UpdateServerListHandler;
import com.wx.server.thread.Closeable;
import com.wx.server.thread.ShutDownThread;
import com.wx.server.utils.LogUtils;

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
public class GameHttpServer implements Closeable {
	private LogUtils log = LogUtils.getLog(GameHttpServer.class);
	
	@SuppressWarnings("restriction")
	HttpServer hs = null;
	
	@SuppressWarnings("restriction")
	public void start() {
		String port = ServerGameConfig.HTTP_SERVER_PORT;
		try {
			log.error("端口::" + port);
			hs = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), 0);// 设置HttpServer的端口为8888
			hs.createContext("/update_server", new UpdateServerListHandler());
			log.error("结束::" + port);
			hs.setExecutor(null); // creates a default executor
			hs.start();
			ShutDownThread.addCloseable(this);
		} catch (IOException e) {
			
			log.error("-----", e);
		}
		
	}
	
	public void stop() {
		if (hs != null) {
			hs.stop(1);
		}
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		stop();
	}
}
