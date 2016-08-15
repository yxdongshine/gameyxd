package com.wx.server.logical.manage;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wx.server.dbdao.EntityDAO;
import com.wx.server.domain.AreaList;
import com.wx.server.domain.ServerList;
import com.wx.server.obj.LoginServerList;
import com.wx.server.utils.HQL;
import com.wx.server.utils.LogUtils;

/**
 * ClassName:LoginManage <br/>
 * Function: TODO (登录逻辑管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:20:57 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class LoginManage implements IManage {
	private static LogUtils log = LogUtils.getLog(LoginManage.class);
	/** 大区容器 LoginServerList=服务器大区和服务器列表对象 **/
	public static CopyOnWriteArraySet<LoginServerList> areaListMap = new CopyOnWriteArraySet<LoginServerList>();
	/** 服务器列表map Integer=服务器id,服务器对象 **/
	public static ConcurrentHashMap<String, ServerList> serverListMap = new ConcurrentHashMap<String, ServerList>();
	
	@Autowired(required=true)
	private com.wx.server.dbdao.EntityDAOInterface entityDAOInterface;
	
	@Override
	public void load() {
		areaListMap.clear();
		serverListMap.clear();
		// 加载大区列表
		// TODO Auto-generated method stub
		List<AreaList> arealist = entityDAOInterface.findByHql(HQL.LOAD_AREA_LIST);
		for (AreaList area : arealist) {
			addArea(area);
		}
		
		// 加载服务器列表
		List<ServerList> serverLists = entityDAOInterface.findByHql(HQL.LOAD_SERVER_LIST);
		for (LoginServerList lsl : areaListMap) {
			for (ServerList server : serverLists) {
				if (server.getAreaId() == lsl.getAreaList().getId()) {
					addServer(lsl, server);
				}
			}
		}
	}
	
	/**
	 * addArea:(). <br/>
	 * TODO().<br/>
	 * 加入大区
	 * 
	 * @author lyh
	 * @param al
	 */
	public static LoginServerList addArea(AreaList al) {
		LoginServerList lsl = contains(al.getId());
		if (lsl == null) {
			lsl = new LoginServerList();
			lsl.setAreaList(al);
			areaListMap.add(lsl);
		}
		
		return lsl;
	}
	
	/**
	 * addServer:(). <br/>
	 * TODO().<br/>
	 * 增加服务器
	 * 
	 * @author lyh
	 * @param pLoginServerList
	 * @param server
	 */
	public static boolean addServer(LoginServerList pLoginServerList, ServerList server) {
		for (ServerList sl : pLoginServerList.getCpServerList()) {
			if (sl.getId() == server.getId()) {
				return false;
			}
		}
		pLoginServerList.getCpServerList().add(server);
		serverListMap.put("" + server.getId(), server);
		log.error("服务器ip:" + server.getIp() + ":port:" + server.getPort() + ":serverId" + server.getId());
		return true;
	}
	
	/**
	 * setServerStatus:(). <br/>
	 * TODO().<br/>
	 * 设置游戏服务器
	 * 
	 * @author lyh
	 * @param sl
	 * @param status
	 */
	public static void setServerStatus(ServerList sl, int status) {
		synchronized (sl) {
			sl.setStatus((byte) status);
		}
	}
	
	/**
	 * contains:(). <br/>
	 * TODO().<br/>
	 * 是否包含
	 * 
	 * @author lyh
	 * @param areaId
	 * @return
	 */
	public static LoginServerList contains(long areaId) {
		for (LoginServerList lsl : areaListMap) {
			if (lsl.getAreaList().getId() == areaId) {
				return lsl;
			}
		}
		return null;
	}
	
}
