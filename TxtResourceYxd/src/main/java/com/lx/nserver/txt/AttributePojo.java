package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 属性85个
 **/
public class AttributePojo {
	
	public AttributePojo() {
	}
	
	/** 编号 **/
	@TxtKey(name = "id")
	@TxtInt(name = "id")
	private int id;
	
	/** 职业id **/
	@TxtInt(name = "careerId")
	private int careerId;
	
	/** 属性类型 **/
	@TxtInt(name = "attributeType")
	private int attributeType;
	
	/** 属性名 **/
	@TxtString(name = "attributeName")
	private String attributeName;
	
	/** 初始值 **/
	@TxtDouble(name = "initVal")
	private double initVal;
	
	/** 参数1 **/
	@TxtDouble(name = "para1")
	private double para1;
	
	/** 参数2 **/
	@TxtDouble(name = "para2")
	private double para2;
	
	/** 参数3 **/
	@TxtDouble(name = "para3")
	private double para3;
	
	/** 最小值 **/
	@TxtInt(name = "minVal")
	private int minVal;
	
	/** 最大值 **/
	@TxtInt(name = "maxVal")
	private int maxVal;
	
	/** 怪物时的最大值 **/
	@TxtInt(name = "monsterMaxVal")
	private int monsterMaxVal;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public int getCareerId() {
		return careerId;
	}
	
	public void setCareerId(int _careerId) {
		careerId = _careerId;
	}
	
	public int getAttributeType() {
		return attributeType;
	}
	
	public void setAttributeType(int _attributeType) {
		attributeType = _attributeType;
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void setAttributeName(String _attributeName) {
		attributeName = _attributeName;
	}
	
	public double getInitVal() {
		return initVal;
	}
	
	public void setInitVal(double _initVal) {
		initVal = _initVal;
	}
	
	public double getPara1() {
		return para1;
	}
	
	public void setPara1(double _para1) {
		para1 = _para1;
	}
	
	public double getPara2() {
		return para2;
	}
	
	public void setPara2(double _para2) {
		para2 = _para2;
	}
	
	public double getPara3() {
		return para3;
	}
	
	public void setPara3(double _para3) {
		para3 = _para3;
	}
	
	public int getMinVal() {
		return minVal;
	}
	
	public void setMinVal(int _minVal) {
		minVal = _minVal;
	}
	
	public int getMaxVal() {
		return maxVal;
	}
	
	public void setMaxVal(int _maxVal) {
		maxVal = _maxVal;
	}
	
	public int getMonsterMaxVal() {
		return monsterMaxVal;
	}
	
	public void setMonsterMaxVal(int _monsterMaxVal) {
		monsterMaxVal = _monsterMaxVal;
	}
	
}