package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 装备质量表
 **/
public class EquipQualityPojo {
	
	public EquipQualityPojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 品质颜色 **/
	@TxtString
	private String color;
	
	/** 品质名称 **/
	@TxtString
	private String name;
	
	/** 品质等级 **/
	@TxtInt
	private int value;
	
	/** 装备初始分数 **/
	@TxtInt
	private int standard;
	
	/** 装备分数最小值 **/
	@TxtInt
	private int min;
	
	/** 装备分数最大值 **/
	@TxtInt
	private int max;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String _color) {
		color = _color;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int _value) {
		value = _value;
	}
	
	public int getStandard() {
		return standard;
	}
	
	public void setStandard(int _standard) {
		standard = _standard;
	}
	
	public int getMin() {
		return min;
	}
	
	public void setMin(int _min) {
		min = _min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int _max) {
		max = _max;
	}
	
}