package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 角色升级的下一级经验
 **/
public class RoleLevelUpPojo {
	
	public RoleLevelUpPojo() {
	}
	
	/** 等级 **/
	@TxtKey
	@TxtInt
	private int level;
	
	/** 升到下一级的经验值 **/
	@TxtInt
	private int levelUpExp;
	
	/** 是否可以升到下一级 **/
	@TxtByte
	private byte isLeveUp;
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int _level) {
		level = _level;
	}
	
	public int getLevelUpExp() {
		return levelUpExp;
	}
	
	public void setLevelUpExp(int _levelUpExp) {
		levelUpExp = _levelUpExp;
	}
	
	public byte getIsLeveUp() {
		return isLeveUp;
	}
	
	public void setIsLeveUp(byte _isLeveUp) {
		isLeveUp = _isLeveUp;
	}
	
}