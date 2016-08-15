package com.lx.nserver.txt;

import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.read.tool.txt.*;

/**
 * 怪物组
 **/
public class MonsterGroupPojo {
	
	public MonsterGroupPojo() {
	}
	
	/** 怪物组id **/
	@TxtKey(name = "id")
	@TxtInt(name = "id")
	private int id;
	
	/** 怪物组名称 **/
	@TxtString
	private String groupName;
	
	/** 包含的怪物 **/
	@TxtString
	private String monsters;
	
	/** 怪物组 **/
	private int monsterId[][] = new int[0][];
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String _groupName) {
		groupName = _groupName;
	}
	
	public String getMonsters() {
		return monsters;
	}
	
	public void setMonsters(String _monsters) {
		monsters = _monsters;
		if (!ToolUtils.isStringNull(_monsters)) {
			monsterId = ToolUtils.splitStr(monsters, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getMonsterId() {
		return monsterId;
	}
	
}