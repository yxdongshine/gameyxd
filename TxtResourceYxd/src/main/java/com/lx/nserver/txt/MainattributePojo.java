package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 装备主要属性表
 **/
public class MainattributePojo {
	
	public MainattributePojo() {
	}
	
	/** 编号 **/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int id;
	
	/** 名称 **/
	@TxtString(name = "")
	private String name;
	
	/** 装备级别等表编号 **/
	@TxtString(name = "")
	private String equipmentId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public String getEquipmentId() {
		return equipmentId;
	}
	
	public void setEquipmentId(String _equipmentId) {
		equipmentId = _equipmentId;
	}
	
}