package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 人物属性表
 **/
public class AttributesPojo {
	
	public AttributesPojo() {
	}
	
	/** 编号 **/
	@TxtKey(name = "index")
	@TxtInt(name = "index")
	private int index;
	
	/** 编号 **/
	@TxtString(name = "id")
	private String id;
	
	/** 前缀 **/
	@TxtString(name = "prefix")
	private String prefix;
	
	/** 后缀 **/
	@TxtString(name = "suffix")
	private String suffix;
	
	/** 最小值 **/
	@TxtInt(name = "min")
	private int min;
	
	/** 最大值 **/
	@TxtInt(name = "max")
	private int max;
	
	/** 未知 **/
	@TxtInt(name = "monstermax")
	private int monstermax;
	
	/** 名称 **/
	@TxtString(name = "name")
	private String name;
	
	/** 特殊 **/
	@TxtString(name = "special")
	private String special;
	
	/** 未知 **/
	@TxtInt(name = "death")
	private int death;
	
	/** 未知 **/
	@TxtString(name = "weaponscale")
	private String weaponscale;
	
	/** 使用 影响 **/
	@TxtString(name = "effect")
	private String effect;
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int _index) {
		index = _index;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String _id) {
		id = _id;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String _prefix) {
		prefix = _prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public void setSuffix(String _suffix) {
		suffix = _suffix;
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
	
	public int getMonstermax() {
		return monstermax;
	}
	
	public void setMonstermax(int _monstermax) {
		monstermax = _monstermax;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public String getSpecial() {
		return special;
	}
	
	public void setSpecial(String _special) {
		special = _special;
	}
	
	public int getDeath() {
		return death;
	}
	
	public void setDeath(int _death) {
		death = _death;
	}
	
	public String getWeaponscale() {
		return weaponscale;
	}
	
	public void setWeaponscale(String _weaponscale) {
		weaponscale = _weaponscale;
	}
	
	public String getEffect() {
		return effect;
	}
	
	public void setEffect(String _effect) {
		effect = _effect;
	}
	
}