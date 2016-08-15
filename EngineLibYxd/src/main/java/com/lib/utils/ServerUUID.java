/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:ServerUUID.java 
 * Package Name:com.sj.world.utils 
 * Date:2013-12-23下午4:21:37 
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.lib.utils;

import java.util.UUID;

/**
 * ClassName:ServerUUID <br/>
 * Function: TODO (uuid生成类,一般用作数据库主键). <br/>
 * Reason: TODO (). <br/>
 * Date: 2013-12-23 下午4:21:37 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerUUID {
	
	private static long LONG_MAX_VALUE = (long) Math.pow(2, 63);
	
	/**
	 * 取得随机的uuid
	 * 
	 * @return long长码id
	 */
	public static long getUUID() {
		UUID uuid = UUID.randomUUID();
		
		return uuid.getLeastSignificantBits() + LONG_MAX_VALUE;
	}
}
