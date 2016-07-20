package com.wx.server.container;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;


/**
 * ClassName:LoginContainer <br/>
 * Function: TODO (登录容器管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:39:33 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class GlobalContainer {
	
	/** 令牌map **/
	public static ConcurrentHashMap<Long, Long> tokenMap = new ConcurrentHashMap<Long, Long>();
	
}
