package com.wx.server.utils;

import java.util.UUID;

/**
 * ClassName:ServerUUID <br/>
 * Function: TODO (uuid生成类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-8 下午2:58:24 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerUUID {
	private static long LONG_MAX_VALUE = (long) Math.pow(2, 63);
	
	/**
	 * createVerifyCode:(). <br/>
	 * TODO().<br/>
	 * 用uuid生成不重复的编码
	 * 
	 * @author lyh
	 * @return
	 */
	public static long createVerifyCode() {
		UUID uid = UUID.randomUUID();
		return uid.getLeastSignificantBits() + LONG_MAX_VALUE;
	}
}
