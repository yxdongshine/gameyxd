package com.lx.nserver.monster.action;

/**
 * ClassName:IdleAction <br/>
 * Function: TODO (怪物的待机行为,json格式). <br/>
 * Reason: TODO ({"rate":100,"lastTime": 10,"cdTime":10}). <br/>
 * Date: 2015-7-27 下午5:18:12 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class IdleAction {
	/** 执行的几率（int百分比） **/
	private int rate;
	/** 行为持续时间（秒）在此时间内不会触发其他行为，激活除外 **/
	private int lastTime;
	/** 行为冷却时间（秒） 在此时间内不会再次触发该行为 ***/
	private int cdTime;
	
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
