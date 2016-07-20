package com.wx.server.config.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.wx.server.res.XmlPath;
import com.wx.server.utils.XmlUtils;

/**
 * 
 * ClassName: ServerInfoManage <br/>
 * Function: TODO (服务器配置读取类). <br/>
 * Reason: TODO (serverInfo容器管理类). <br/>
 * date: 2015-6-25 下午4:50:11 <br/>
 * 
 * @author lyh
 * @version
 */
public class ServerInfoManage {
	
	/** 默认服务配置加载地址 */
	public static String DEFAULT_PATH = XmlPath.SERVERS_CONFIG_XML;
	
	/** 服务配置表 */
	private static Map<Integer, ServerInfo> sConfigs = null;
	
	/** 当前的服务器配置信息 **/
	public static ServerInfo curServerInfo = null;
	
	/** 默认路径加载服务配置 */
	public static void loadContext() {
		loadContext(null);
	}
	
	/** 指定路径加载服务配置 */
	@SuppressWarnings("unchecked")
	public static void loadContext(String path) {
		if (path == null) {
			path = DEFAULT_PATH;
		}
		
		Document droot = null;
		try {
			droot = XmlUtils.readAsDocument(path);
			Element root = droot.getRootElement();
			List<Element> elms = root.elements();
			sConfigs = new HashMap<Integer, ServerInfo>();
			for (Element sElem : elms) {
				ServerInfo server = new ServerInfo();
				server.init(sElem);
				sConfigs.put(server.serverTypeId, server);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/** 依据id，获得服务配置 */
	public static ServerInfo getSConfig(int sid) {
		if (sConfigs == null) {
			loadContext();
		}
		return sConfigs.get(sid);
	}
	
	/** 获得服务配置列表 */
	public static ServerInfo[] getSConfigs() {
		return sConfigs.values().toArray(new ServerInfo[sConfigs.size()]);
	}
	
	/** 根据服务组名,获得服务配 获得服务配置列表 */
	public static ServerInfo[] getSConfigByGroup(String group) {
		List<ServerInfo> list = new ArrayList<ServerInfo>();
		ServerInfo[] sInfos = getSConfigs();
		for (ServerInfo sInfo : sInfos) {
			if (sInfo.group == null) {
				continue;
			}
			if (sInfo.group.equals(group)) {
				list.add(sInfo);
			}
		}
		
		ServerInfo[] resArray = new ServerInfo[list.size()];
		for (int i = 0; i < resArray.length; i++) {
			resArray[i] = list.get(i);
		}
		return resArray;
	}
	
}
