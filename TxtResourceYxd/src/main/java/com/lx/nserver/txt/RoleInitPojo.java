package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 角色初始设置
 **/
public class RoleInitPojo {
	
	public RoleInitPojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 初始游戏币 **/
	@TxtInt
	private int initMoney;
	
	/** 道具栏初始格子数 **/
	@TxtInt
	private int initItemGrids;
	
	/** 初始背包装备格子数 **/
	@TxtInt
	private int initEquipGrids;
	
	/** 特殊背包格子数 **/
	@TxtInt
	private int initSpecialGrids;
	
	/** 地图Id **/
	@TxtInt
	private int mapId;
	
	/** X坐标 **/
	@TxtFloat
	private float x;
	
	/** Y坐标 **/
	@TxtFloat
	private float y;
	
	/** Z坐标 **/
	@TxtFloat
	private float z;
	
	/** 角以方向 **/
	@TxtFloat
	private float dir;
	
	/** 任务id **/
	@TxtInt
	private int initTaskId;
	
	/** 属性点系数 **/
	@TxtInt
	private int upPoint;
	
	/** 加点基数 **/
	@TxtInt
	private int upAddPoint;
	
	/** 技能基数 **/
	@TxtInt
	private int upSkill;
	
	/** 最大体力 **/
	@TxtInt
	private int maxVits;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public int getInitMoney() {
		return initMoney;
	}
	
	public void setInitMoney(int _initMoney) {
		initMoney = _initMoney;
	}
	
	public int getInitItemGrids() {
		return initItemGrids;
	}
	
	public void setInitItemGrids(int _initItemGrids) {
		initItemGrids = _initItemGrids;
	}
	
	public int getInitEquipGrids() {
		return initEquipGrids;
	}
	
	public void setInitEquipGrids(int _initEquipGrids) {
		initEquipGrids = _initEquipGrids;
	}
	
	public int getInitSpecialGrids() {
		return initSpecialGrids;
	}
	
	public void setInitSpecialGrids(int _initSpecialGrids) {
		initSpecialGrids = _initSpecialGrids;
	}
	
	public int getMapId() {
		return mapId;
	}
	
	public void setMapId(int _mapId) {
		mapId = _mapId;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float _x) {
		x = _x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float _y) {
		y = _y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float _z) {
		z = _z;
	}
	
	public float getDir() {
		return dir;
	}
	
	public void setDir(float _dir) {
		dir = _dir;
	}
	
	public int getInitTaskId() {
		return initTaskId;
	}
	
	public void setInitTaskId(int _initTaskId) {
		initTaskId = _initTaskId;
	}
	
	public int getUpPoint() {
		return upPoint;
	}
	
	public void setUpPoint(int _upPoint) {
		upPoint = _upPoint;
	}
	
	public int getUpAddPoint() {
		return upAddPoint;
	}
	
	public void setUpAddPoint(int _upAddPoint) {
		upAddPoint = _upAddPoint;
	}
	
	public int getUpSkill() {
		return upSkill;
	}
	
	public void setUpSkill(int _upSkill) {
		upSkill = _upSkill;
	}
	
	public int getMaxVits() {
		return maxVits;
	}
	
	public void setMaxVits(int _maxVits) {
		maxVits = _maxVits;
	}
	
}