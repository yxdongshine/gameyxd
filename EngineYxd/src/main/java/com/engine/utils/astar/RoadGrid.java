package com.engine.utils.astar;

import com.engine.config.xml.map.BlockData;
import com.engine.entityobj.space.Grid;

/**
 * ClassName:RoadGrid <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-5 下午5:44:40 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class RoadGrid {
	// 权值
	private int weight;
	// 父格子
	private int farther = -1;
	// 格子
	private BlockData grid;
	
	public RoadGrid() {
	}
	
	public int getWeight() {
		return weight;
	}
	
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	public int getFarther() {
		return farther;
	}
	
	public void setFarther(int farther) {
		this.farther = farther;
	}
	
	public BlockData getGrid() {
		return grid;
	}
	
	public void setGrid(BlockData grid) {
		this.grid = grid;
	}
	
	public boolean equal(RoadGrid grid) {
		return this.grid.equal(grid.getGrid());
	}
}
