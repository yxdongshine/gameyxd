package com.engine.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ClassName:ItemData <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 下午8:35:00 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Entity
@Table(name = "equipmentitem")
public class ItemData extends DbEntity {
	
	/****/
	private static final long serialVersionUID = -3267935359170487666L;
	
	private int configId;// 配置模板编号
	private int quality;// 品质
	private int itemType;// 在背包中道具类型
	private int indexInBag;// 在背包中物品位置
	private int number;// 数量
	
	private long roleId;// 所属角色编号
	
	private long effectTime;// 物品生效时间
	
	private int renewTimes;// 续费次数
	
	/**
	 * 装备栏中的位置
	 */
	private int pos;
	
	/**
	 * 装备中的分数
	 */
	private int score;
	/**
	 * 装备孔位
	 */
	private int socket;
	
	/**
	 * 物品等级
	 */
	private int level;
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getSocket() {
		return socket;
	}
	
	public void setSocket(int socket) {
		this.socket = socket;
	}
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
	public int getConfigId() {
		return configId;
	}
	
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	
	public int getQuality() {
		return quality;
	}
	
	public void setQuality(int quality) {
		this.quality = quality;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
		
	}
	
	public int getItemType() {
		return itemType;
	}
	
	public void setItemType(int itemType) {
		this.itemType = itemType;
	}
	
	public int getIndexInBag() {
		return indexInBag;
	}
	
	public void setIndexInBag(int indexInBag) {
		this.indexInBag = indexInBag;
	}
	
	public long getRoleId() {
		return roleId;
	}
	
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	public long getEffectTime() {
		return effectTime;
	}
	
	public void setEffectTime(long effectTime) {
		this.effectTime = effectTime;
	}
	
	public int getRenewTimes() {
		return renewTimes;
	}
	
	public void setRenewTimes(int renewTimes) {
		this.renewTimes = renewTimes;
	}
	
}
