package com.engine.res;

import com.lib.res.ResPath;

/**
 * ClassName:XmlPath <br/>
 * Function: TODO (xml格式的配置文件路径). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-25 下午4:55:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface XmlPath extends ResPath {
	public static final String XML = ".xml";
	/** 服务器配置文件路径 **/
	public static final String SERVERS_CONFIG_XML = RES + "servers_config" + XML;
	
	/** 通信协议命令 **/
	public static final String COMMOND_XML = RES + "command" + XML;
	
	/** 地图数据文件夹 **/
	public static final String MAP_PATH = RES + "map/";
}
