package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 剧情
 **/
public class EpitasisPojo {
	
	public EpitasisPojo() {
	}
	
	/** 剧情编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 剧本对话编号序列(编号顺序对话顺序，使用;作为编号间的分隔) **/
	@TxtString
	private String ScenarioNumberList;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getScenarioNumberList() {
		return ScenarioNumberList;
	}
	
	public void setScenarioNumberList(String _ScenarioNumberList) {
		ScenarioNumberList = _ScenarioNumberList;
	}
	
}