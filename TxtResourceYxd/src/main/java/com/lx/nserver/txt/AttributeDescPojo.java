package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 属性说明
 **/
public class AttributeDescPojo {
	
	public AttributeDescPojo() {
	}
	
	/** 属性类型 **/
	@TxtKey(name = "attrType")
	@TxtInt(name = "attrType")
	private int attrType;
	
	/** 属性说明 **/
	@TxtString(name = "desc")
	private String desc;
	
	/** 是否显示 **/
	@TxtByte(name = "bDisplay")
	private byte bDisplay;
	
	public int getAttrType() {
		return attrType;
	}
	
	public void setAttrType(int _attrType) {
		attrType = _attrType;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String _desc) {
		desc = _desc;
	}
	
	public byte getBDisplay() {
		return bDisplay;
	}
	
	public void setBDisplay(byte _bDisplay) {
		bDisplay = _bDisplay;
	}
	
}