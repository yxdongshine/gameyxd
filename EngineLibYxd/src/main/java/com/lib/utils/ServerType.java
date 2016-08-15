package com.lib.utils;

/**
 * ClassName:ServerType <br/>
 * Function: TODO (服务器类型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-25 下午5:17:42 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerType {
	/**
	 * 网关服务器
	 */
	public static int GATE_SERVER = 1;
	/**
	 * 游戏服务器
	 */
	public static int GAME_SERVER = 2;
	/**
	 * 世界服务器
	 */
	public static int WORLD_SERVER = 3;
	/**
	 * 登录服务器
	 */
	public static int LOGIN_SERVER = 4;
	/**
	 * 公共服务器
	 */
	public static int PUBLIC_SERVER = 5;
	
	/**
	 * log服务器
	 */
	public static int LOG_SERVER = 6;
	
	/**
	 * 客户端
	 */
	public static int CLIENT = 7;
	
	private static final String game = "game";
	private static final String world = "world";
	private static final String gate = "gate";
	private static final String login = "login";
	private static final String publicGame = "public";
	private static final String log = "log";
	private static final String client = "client";
	
	/**
	 * getServerType:(). <br/>
	 * TODO().<br/>
	 * 得到服务器类型
	 * 
	 * @author lyh
	 * @param str
	 * @return
	 */
	public static int getServerType(String str) {
		if (str.equals(game)) {
			return GAME_SERVER;
		} else if (str.equals(world)) {
			return WORLD_SERVER;
		} else if (str.equals(login)) {
			return LOGIN_SERVER;
		} else if (str.equals(publicGame)) {
			return PUBLIC_SERVER;
		} else if (str.equals(log)) {
			return LOG_SERVER;
		} else if (str.equals(client)) {
			return CLIENT;
		}
		return GATE_SERVER;
		
	}
}
