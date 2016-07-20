package com.wx.server.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ClassName:AreaList <br/>
 * Function: TODO (大区列表). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午6:48:07 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Entity
@Table(name = "area_list")
public class AreaList {
	
	private long id;
	/** 大区名称 **/
	private String areaName;
	/** 大区状态 **/
	private byte status;
	
	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getAreaName() {
		return areaName;
	}
	
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	
	public byte getStatus() {
		return status;
	}
	
	public void setStatus(byte status) {
		this.status = status;
	}
	
}
