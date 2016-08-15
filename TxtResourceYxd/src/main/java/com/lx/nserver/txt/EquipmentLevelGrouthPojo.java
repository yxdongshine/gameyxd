package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 装备成长级别配置表
 **/
public class EquipmentLevelGrouthPojo {
	
	public EquipmentLevelGrouthPojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int Id;
	
	/** 级别 **/
	@TxtInt
	private int level;
	
	/** 主属性编号 **/
	@TxtInt
	private int elgId;
	
	/** 值 **/
	@TxtInt
	private int value;
	
	/** vip成长值 **/
	@TxtString
	private String growth1;
	
	/** 普通成长值 **/
	@TxtString
	private String growth0;
	
	public int getId() {
		return Id;
	}
	
	public void setId(int _Id) {
		Id = _Id;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int _level) {
		level = _level;
	}
	
	public int getElgId() {
		return elgId;
	}
	
	public void setElgId(int _elgId) {
		elgId = _elgId;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int _value) {
		value = _value;
	}
	
	public String getGrowth1() {
		return growth1;
	}
	
	public void setGrowth1(String _growth1) {
		growth1 = _growth1;
	}
	
	public String getGrowth0() {
		return growth0;
	}
	
	public void setGrowth0(String _growth0) {
		growth0 = _growth0;
	}
	
}