package com.lx.nserver.monster.action;

/**
 * ClassName:WalkAction <br/>
 * Function: TODO (漫游行为,json格式). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-27 下午5:22:31 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class WalkAction {
	
	// {"range":15,"rate":100,"lastTime": 10,"cdTime":10}
	/** 漫游范围（int） **/
	private int range;
	/** 执行的几率（int百分比) **/
	private int rate;
	
	/** 行为持续时间（秒）在此时间内不会触发其他行为，激活除外 **/
	private int lastTime;
	/** 行为冷却时间（秒） **/
	private int cdTime;
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getLastTime() {
		return lastTime;
	}
	
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	
	public int getCdTime() {
		return cdTime;
	}
	
	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}
}
