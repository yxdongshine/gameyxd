package com.lx.game.item;

import com.lx.game.res.item.Item;

/**
 * ClassName:PackItem <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-31 上午8:55:33 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class PackItem {
	private int addType;// 添加或者更新类型
	private int updateNumber;// 更新数
	private Item item;// 被包装的物品
	
	public int getAddType() {
		return addType;
	}
	
	public void setAddType(int addType) {
		this.addType = addType;
	}
	
	public Item getItem() {
		return item;
	}
	
	public void setItem(Item item) {
		this.item = item;
	}
	
	public int getUpdateNumber() {
		return updateNumber;
	}
	
	public void setUpdateNumber(int updateNumber) {
		this.updateNumber = updateNumber;
	}
	
	public PackItem(int addType, int updateNumber, Item item) {
		// TODO Auto-generated constructor stub
		this.addType = addType;
		this.updateNumber = updateNumber;
		this.item = item;
	}
}
