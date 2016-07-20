package com.wx.server.config.xml;

import org.dom4j.Element;

import com.wx.server.utils.ServerType;
import com.wx.server.utils.XmlUtils;

/**
 * 服务基本配置信息
 * 
 * @author lyh
 * 
 */
public class ServerInfo {
	/** 服务类型 (如网关,世界服务器等) */
	public int type;
	
	/** 服务名字 */
	public String name;
	
	/** 主机地址 */
	public String host;
	
	/** 服务器类型id(每个游戏服务器性一) */
	public int serverTypeId;
	
	/** 本服务器属于哪个服务器组 */
	public String group;
	
	/** 内部端口 **/
	public int port;
	
	/** mina端口,外端端口 **/
	public int minaPort;
	
	/** 连接器信息 */
	public ConnctorInfo connInfo;
	
	/** 线程处理方式 */
	public String threadMode;
	
	/** http端口号 */
	public int httpPort;
	
	/** * 依据xml初始化 */
	@SuppressWarnings("unchecked")
	public void init(Element sElem) {
		// 服务类型
		String tempStr = sElem.getName();
		this.type = ServerType.getServerType(tempStr);
		// 服务名
		this.name = XmlUtils.getElementAsString(sElem, "name");
		// 服务id
		this.serverTypeId = XmlUtils.getElementAsInt(sElem, "id");
		// 服务组
		this.group = XmlUtils.getElementAsString(sElem, "group");
		if (group == null) {
			group = "";
		}
		// 主机地址
		this.host = XmlUtils.getElementAsString(sElem, "host");
		
		// 线程模型
		this.threadMode = XmlUtils.getElementAsString(sElem, "threadMode");
		
		// 监听器信息
		Element accElemt = sElem.element("acceptor");
		port = XmlUtils.getAttributeAsInt(accElemt, "port");
		
		Element minaElemt = sElem.element("minaAcceptor");
		minaPort = XmlUtils.getAttributeAsInt(minaElemt, "port");
		
		// 连接器信息
		Element connELem = sElem.element("connector");
		if (connELem != null) {
			connInfo = new ConnctorInfo();
			connInfo.init(connELem);
		}
		
		// http服务器
		Element https = sElem.element("http");
		httpPort = XmlUtils.getAttributeAsInt(https, "httpPort");
		
	}
}
