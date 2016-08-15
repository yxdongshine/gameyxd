package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 女性名
 **/
public class FemaleNamePojo {
	
	public FemaleNamePojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 女性名 **/
	@TxtString
	private String FemaleName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getFemaleName() {
		return FemaleName;
	}
	
	public void setFemaleName(String _FemaleName) {
		FemaleName = _FemaleName;
	}
	
}