package com.lx.game.item;

/**
 * ClassName:BagOpenGridConfig <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 下午6:20:04 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class BagOpenGridConfig {
	
	private int count;// 一次性开格子数
	private int currency;// 货币类型
	private int fee;// 价格数量
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCurrency() {
		return currency;
	}
	
	public void setCurrency(int currency) {
		this.currency = currency;
	}
	
	public int getFee() {
		return fee;
	}
	
	public void setFee(int fee) {
		this.fee = fee;
	}
	
}
