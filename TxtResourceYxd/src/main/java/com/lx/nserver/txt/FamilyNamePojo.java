package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 姓氏
 **/
public class FamilyNamePojo {
	
	public FamilyNamePojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 姓氏 **/
	@TxtString
	private String FamilyName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getFamilyName() {
		return FamilyName;
	}
	
	public void setFamilyName(String _FamilyName) {
		FamilyName = _FamilyName;
	}
	
}