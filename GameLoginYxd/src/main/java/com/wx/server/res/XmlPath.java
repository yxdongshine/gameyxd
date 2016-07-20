package com.wx.server.res;

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
	public static final String SERVERS_CONFIG_XML = RES_DIR + "servers_config" + XML;
	
	/** 通信协议命令 **/
	public static final String COMMOND_XML = RES_DIR + "proxy" + XML;
}
