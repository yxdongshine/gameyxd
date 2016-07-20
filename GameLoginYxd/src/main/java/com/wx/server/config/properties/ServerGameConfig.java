/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:ServerGameConfig.java 
 * Package Name:com.sj.engine.config 
 * Date:2014-2-19上午10:56:06 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.wx.server.config.properties;

import java.util.Properties;

/**
 * ClassName:ServerGameConfig <br/>
 * Function: TODO (游戏的配置文件). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-2-19 上午10:56:06 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

public class ServerGameConfig extends ServerProperties {
	// private static final String BUNDLE_NAME = ResPath.userdir + "/game.properties";
	
	/** 登录服务器端口 **/
	public static String lOGIN_SERVER_PORT = "port";
	
	/** http服务器端口 **/
	public static String HTTP_SERVER_PORT = "http_server_port";
	
	/** 逻辑包 **/
	public static String LOGICAL_PACKAGE = "logical_package";
	
	/** #逻辑服与登录服连接端口 **/
	public static String INNER_PORT = "inner_port";
	/** 版本号 **/
	public static String VERSION = "version";
	
	/**
	 * loadGameConfig:(). <br/>
	 * TODO().<br/>
	 * 加载配置
	 * 
	 * @author lyh
	 */
	public static void loadGameConfig() {
		Properties GAME_BUNDLE = loadGameProperties(IPropertiesResPath.GAME_P);
		lOGIN_SERVER_PORT = GAME_BUNDLE.getProperty(lOGIN_SERVER_PORT);
		HTTP_SERVER_PORT = GAME_BUNDLE.getProperty(HTTP_SERVER_PORT);
		LOGICAL_PACKAGE = GAME_BUNDLE.getProperty(LOGICAL_PACKAGE);
		INNER_PORT = GAME_BUNDLE.getProperty(INNER_PORT);
		VERSION = GAME_BUNDLE.getProperty(VERSION);
	}
	
}
