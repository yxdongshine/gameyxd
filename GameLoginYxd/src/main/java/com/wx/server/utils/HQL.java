package com.wx.server.utils;

/**
 * ClassName:HQL <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午8:19:05 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface HQL {
	/** 加载大区列表 **/
	public static final String LOAD_AREA_LIST = " from AreaList order by id";
	public static final String LOAD_SERVER_LIST = "from ServerList order by sortIndex";
	
}
