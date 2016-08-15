/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:ServerGameConfig.java 
 * Package Name:com.sj.engine.config 
 * Date:2014-2-19上午10:56:06 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.engine.properties;

import java.util.Properties;

import com.lib.res.UserDir;
import com.lib.utils.ToolUtils;

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
	private static final String BUNDLE_NAME = UserDir.USER_DIR + "/game.properties";
	
	public static String SERVER_ID = "serverid";
	
	public static long APP_ID = 1001;
	
	public static int GM_CMD = 0;
	
	/** 防沉迷 */
	public static int INDUL = 1;
	
	/** 关闭屏蔽字1 **/
	public static int SHIELD = 0;
	// 语言包路径
	public static String LANGUAGE_PACK_PATH = "language_pack_path";
	
	/** 包路径 **/
	public static String PACKAGE = "Package";
	
	/** 版本号 **/
	public static String VERSION = "version";
	
	/** txt包路径 **/
	public static String TXT_PACKAGE = "txt_package";
	
	/**
	 * loadGameConfig:(). <br/>
	 * TODO().<br/>
	 * 加载配置
	 * 
	 * @author lyh
	 */
	public static void loadGameConfig() {
		Properties GAME_BUNDLE = loadGameProperties(BUNDLE_NAME);
		
		SERVER_ID = GAME_BUNDLE.getProperty("serverid");
		
		INDUL = Integer.parseInt(GAME_BUNDLE.getProperty("indul"));
		
		GM_CMD = Integer.parseInt(GAME_BUNDLE.getProperty("GmCmd"));
		SHIELD = Integer.parseInt(GAME_BUNDLE.getProperty("shield"));
		LANGUAGE_PACK_PATH = GAME_BUNDLE.getProperty("language_pack_path");
		
		PACKAGE = GAME_BUNDLE.getProperty("Package");
		VERSION = GAME_BUNDLE.getProperty("version");
		TXT_PACKAGE = GAME_BUNDLE.getProperty("txt_package");
	}
	
}
