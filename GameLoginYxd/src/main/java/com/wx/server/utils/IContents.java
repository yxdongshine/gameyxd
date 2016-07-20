package com.wx.server.utils;

/**
 * ClassName:IConst <br/>
 * Function: TODO (常量类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-6 上午10:50:07 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class IContents {
	/** 编码长度 **/
	public static final int CODE_LENTH = 4;
	/** 会话 **/
	public static final String NET_SESSION = "netsession";
	public static final short SYSTEM_SOCKET_MAX_IDLE_TIMES = Short.parseShort("10");
	
	/*** 服务器列表状态 畅通1 拥挤2 火爆3 维护4 ***/
	
	/** 畅通 **/
	public static final int SERVER_CODE_1 = 1;
	/** 拥挤 **/
	public static final int SERVER_CODE_2 = 2;
	/** 火爆 **/
	public static final int SERVER_CODE_3 = 3;
	/** 维护 **/
	public static final int SERVER_CODE_4 = 4;
	
}
