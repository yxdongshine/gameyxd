package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 男性名
 **/
public class MaleNamePojo {
	
	public MaleNamePojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 男性名 **/
	@TxtString
	private String MaleName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getMaleName() {
		return MaleName;
	}
	
	public void setMaleName(String _MaleName) {
		MaleName = _MaleName;
	}
	
}