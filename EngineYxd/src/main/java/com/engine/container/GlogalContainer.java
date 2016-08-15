package com.engine.container;

import java.util.concurrent.ConcurrentHashMap;

import com.engine.entityobj.ServerPlayer;

/**
 * ClassName:GlogalContainer <br/>
 * Function: TODO (全局容器,全部都是静态的). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-2 下午5:59:25 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class GlogalContainer {
	
	/** 角色id，角色 **/
	private static final ConcurrentHashMap<Long, ServerPlayer> rolesMap = new ConcurrentHashMap<Long, ServerPlayer>();
	
	/** sessionid,角色 **/
	private static final ConcurrentHashMap<Long, ServerPlayer> sessionServerPlayerMap = new ConcurrentHashMap<Long, ServerPlayer>();
	
	public static ConcurrentHashMap<Long, ServerPlayer> getRolesMap() {
		return rolesMap;
	}
	
	public static ConcurrentHashMap<Long, ServerPlayer> getSessionServerPlayerMap() {
		return sessionServerPlayerMap;
	}
	
}
