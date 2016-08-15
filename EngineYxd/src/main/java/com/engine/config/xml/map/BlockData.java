package com.engine.config.xml.map;

import com.engine.entityobj.space.Grid;

/**
 * ClassName:BlockData <br/>
 * Function: (阻挡数据). <br/>
 * Date: 2015-8-6 上午11:08:31 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class BlockData {
	
	public BlockData() {
		row = 0;
		col = 0;
		type = 1;
		y = 0;
	}
	
	public BlockData(int row, int col, int type, float y) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.y = y;
	}
	
	// 所在行
	public int row;
	// 所在列
	public int col;
	// 类型(0-可行走，1-静态阻挡)
	public int type;
	// 高度坐标
	public float y;
	
	/**
	 * equal:(). <br/>
	 * TODO().<br/>
	 * 所在格子是否是同一个格子
	 * 
	 * @author lyh
	 * @param grid
	 * @return
	 */
	public boolean equal(BlockData grid) {
		if (this.col == grid.col && this.row == grid.row)
			return true;
		else
			return false;
	}
	
	public int getKey() {
		return this.col * 10000 + this.row;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
}
