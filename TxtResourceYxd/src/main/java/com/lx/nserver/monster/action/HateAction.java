package com.lx.nserver.monster.action;

/**
 * ClassName:HateAction <br/>
 * Function: TODO (仇恨行为，json). <br/>
 * Reason: TODO (Parallel 是否并行【勾选框】 "勾选时，AI必定执行【传播仇恨】，并且不参与行为执行几率的计算，AI同时正常计算和执行其他行为； 未勾选时，根据执行几率和其他行为统一计算是否执行" Probability 执行的几率（int百分比） HateTransRange 仇恨传播半径 HateTransNum 最多传送给几个怪（int） HateTrans
 * 每次传送多少仇恨（int） Last 行为持续时间（秒） CD 行为冷却时间（秒） ). <br/>
 * Date: 2015-7-27 下午5:28:53 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class HateAction {
	// {"sureDo":1,"rate":100,"range":12,"broadcastNum":1,"broadcastPerVal":1,"lastTime":10,"cdTime":2}
	/**
	 * 是否并行【勾选框】 "勾选时，AI必定执行【传播仇恨】，并且不参与行为执行几率的计算，AI同时正常计算和执行其他行为； 未勾选时，根据执行几率和其他行为统一计算是否执行"
	 **/
	private int sureDo;
	/** 执行的几率（int百分比） **/
	private int rate;
	/** 仇恨传播半径 **/
	private int range;
	/** 最多传送给几个怪（int） **/
	private int broadcastNum;
	/** 每次传送多少仇恨（int） **/
	private int broadcastPerVal;
	/** 行为持续时间（秒） **/
	private int lastTime;
	/** 行为冷却时间（秒） **/
	private int cdTimes;
	
	public int getSureDo() {
		return sureDo;
	}
	
	public void setSureDo(int sureDo) {
		this.sureDo = sureDo;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getBroadcastNum() {
		return broadcastNum;
	}
	
	public void setBroadcastNum(int broadcastNum) {
		this.broadcastNum = broadcastNum;
	}
	
	public int getBroadcastPerVal() {
		return broadcastPerVal;
	}
	
	public void setBroadcastPerVal(int broadcastPerVal) {
		this.broadcastPerVal = broadcastPerVal;
	}
	
	public int getLastTime() {
		return lastTime;
	}
	
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	
	public int getCdTimes() {
		return cdTimes;
	}
	
	public void setCdTimes(int cdTimes) {
		this.cdTimes = cdTimes;
	}
	
}
