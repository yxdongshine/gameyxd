package com.wx.server.obj;

import java.util.concurrent.CopyOnWriteArraySet;

import com.wx.server.domain.AreaList;
import com.wx.server.domain.ServerList;

/**
 * ClassName:LoginServerList <br/>
 * Function: TODO (登录服务器列表类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午7:56:58 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class LoginServerList {
	private AreaList areaList = null;
	private CopyOnWriteArraySet<ServerList> cpServerList = new CopyOnWriteArraySet<ServerList>();
	
	public AreaList getAreaList() {
		return areaList;
	}
	
	public void setAreaList(AreaList areaList) {
		this.areaList = areaList;
	}
	
	public CopyOnWriteArraySet<ServerList> getCpServerList() {
		return cpServerList;
	}
	
	public void setCpServerList(CopyOnWriteArraySet<ServerList> cpServerList) {
		this.cpServerList = cpServerList;
	}
	
}
