package com.wx.server.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * ClassName:AreaList <br/>
 * Function: TODO (服务器列表). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午6:48:07 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@SuppressWarnings("deprecation")
@Entity
@Table(name = "server_list")
public class ServerList {
	
	private long id;
	/** 服务名称 **/
	private String serverName;
	/** 最大上限人数 **/
	private int maxPlayerLimit;
	/** 服务器状态 **/
	private byte status;
	/** 大区id **/
	private long areaId;
	/** 服务器ip **/
	private String ip;
	/** 服务器端口 **/
	private String port;
	/** 服务器索引 **/
	private int sortIndex;
	
	/** 是否是新的服务器 **/
	private byte isNewServer;
	
	/** 当前appId,在逻辑服也是配置 **/
	private int currentAppId;
	
	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public int getMaxPlayerLimit() {
		return maxPlayerLimit;
	}
	
	public void setMaxPlayerLimit(int maxPlayerLimit) {
		this.maxPlayerLimit = maxPlayerLimit;
	}
	
	public byte getStatus() {
		return status;
	}
	
	public void setStatus(byte status) {
		this.status = status;
	}
	
	@Index(name = "area_id")
	public long getAreaId() {
		return areaId;
	}
	
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public int getSortIndex() {
		return sortIndex;
	}
	
	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}
	
	public byte getIsNewServer() {
		return isNewServer;
	}
	
	public void setIsNewServer(byte isNewServer) {
		this.isNewServer = isNewServer;
	}
	
	public int getCurrentAppId() {
		return currentAppId;
	}
	
	public void setCurrentAppId(int currentAppId) {
		this.currentAppId = currentAppId;
	}
	
}
