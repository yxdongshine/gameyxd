/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:DecoderRequest.java 
 * Package Name:com.sj.world.utils 
 * Date:2014-2-19上午9:50:51 
 * Copyright (c) 2014, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.lib.utils;

import java.util.HashMap;

/**
 * ClassName:DecoderRequest <br/>
 * Function: TODO (解析HTTP收到的数据格式). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-2-19 上午9:50:51 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class DecoderRequest {
	// 存储内容
	HashMap<String, String> attributes = new HashMap<String, String>();
	
	/**
	 * 构造函数
	 * 
	 * @param body
	 */
	public DecoderRequest(String body) {
		if (body == null) {
			return;
		}
		String[] strs = body.split("&");
		for (String keyValues : strs) {
			String[] attribute = keyValues.split("=");
			if (attribute.length > 2) {
				continue;
			}
			if (attribute.length == 1) {
				String key = attribute[0];
				String value = "";
				attributes.put(key, value);
			}
			if (attribute.length == 2) {
				String key = attribute[0];
				String value = attribute[1];
				attributes.put(key, value);
			}
		}
	}
	
	/**
	 * Function name:getAttribute Description: 通过key值取得value
	 * 
	 * @param key key值
	 * @return String
	 */
	public String getAttribute(String key) {
		return this.attributes.get(key);
	}
	
	/**
	 * Function name:getSize Description: 得到数据的数量
	 * 
	 * @return 数量
	 */
	public int getSize() {
		return this.attributes.size();
	}
	
}
