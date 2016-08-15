package com.lx.game.item;

import java.util.ArrayList;

/**
 * ClassName:StoreLvConfig <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 下午6:23:24 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class StoreLvConfig {
	private int defaultGrid;// 默认格子数
	private int maxGrid;// 最大格子数
	private ArrayList<BagOpenGridConfig> openGridAL;// 打开格子数配置列表
	
	public int getDefaultGrid() {
		return defaultGrid;
	}
	
	public void setDefaultGrid(int defaultGrid) {
		this.defaultGrid = defaultGrid;
	}
	
	public int getMaxGrid() {
		return maxGrid;
	}
	
	public void setMaxGrid(int maxGrid) {
		this.maxGrid = maxGrid;
	}
	
	public ArrayList<BagOpenGridConfig> getOpenGridAL() {
		return openGridAL;
	}
	
	public void setOpenGridAL(ArrayList<BagOpenGridConfig> openGridAL) {
		this.openGridAL = openGridAL;
	}
	
}
