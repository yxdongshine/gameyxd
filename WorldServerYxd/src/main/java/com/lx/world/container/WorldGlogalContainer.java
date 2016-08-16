package com.lx.world.container;

import java.util.concurrent.ConcurrentHashMap;

import com.engine.container.GlogalContainer;
import com.lx.world.data.CheckTokenData;
import com.lx.world.data.CareerTalentData;

/**
 * ClassName:WorldGlogalContainer <br/>
 * Function: TODO (世界服务器的全局容器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-7 下午6:32:34 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class WorldGlogalContainer extends GlogalContainer {
	/** String=token,CheckTokenData **/
	/** 令牌map **/
	private final static ConcurrentHashMap<Long, CheckTokenData> tokenDataMap = new ConcurrentHashMap<Long, CheckTokenData>();
	
	/** long=sessionId,Talent **/
	private final static ConcurrentHashMap<Long, CareerTalentData> careerTalentMap = new ConcurrentHashMap<Long, CareerTalentData>();
	
	public static ConcurrentHashMap<Long, CheckTokenData> getTokendatamap() {
		return tokenDataMap;
	}
	
	public static ConcurrentHashMap<Long, CareerTalentData> getCareertalentmap() {
		return careerTalentMap;
	}
	
}
