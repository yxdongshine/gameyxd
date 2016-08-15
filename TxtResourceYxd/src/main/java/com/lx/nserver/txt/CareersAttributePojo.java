package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 职业属性配置
 **/
public class CareersAttributePojo {
	
	public CareersAttributePojo() {
	}
	
	/** 职业索引 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 属性编号 **/
	@TxtInt
	private int index;
	
	/** name **/
	@TxtString
	private String name;
	
	/** 初始化值 **/
	@TxtInt
	private int initial;
	
	/** 参数1 **/
	@TxtInt
	private int para1;
	
	/** 参数2 **/
	@TxtInt
	private int para2;
	
	/** 参数3 **/
	@TxtInt
	private int para3;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int _index) {
		index = _index;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public int getInitial() {
		return initial;
	}
	
	public void setInitial(int _initial) {
		initial = _initial;
	}
	
	public int getPara1() {
		return para1;
	}
	
	public void setPara1(int _para1) {
		para1 = _para1;
	}
	
	public int getPara2() {
		return para2;
	}
	
	public void setPara2(int _para2) {
		para2 = _para2;
	}
	
	public int getPara3() {
		return para3;
	}
	
	public void setPara3(int _para3) {
		para3 = _para3;
	}
	
}