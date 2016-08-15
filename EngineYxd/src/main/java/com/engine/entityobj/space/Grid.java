package com.engine.entityobj.space;

import com.engine.entityobj.Position3D;

/**
 * ClassName:Grid <br/>
 * Function: TODO (格子). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-5 下午6:11:34 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Deprecated
public class Grid {
	// x坐标
	private int col;
	// Z坐标
	private int row;
	/* Y轴** */
	private int height;
	// 是否阻挡
	private int block;
	
	// // 中心点坐标
	// private Position3D center;
	
	public int getCol() {
		return col;
	}
	
	public int getRow() {
		return row;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
	
	public int getBlock() {
		return block;
	}
	
	public void setBlock(int block) {
		this.block = block;
	}
	
	/**
	 * equal:(). <br/>
	 * TODO().<br/>
	 * 所在格子是否是同一个格子
	 * 
	 * @author lyh
	 * @param grid
	 * @return
	 */
	public boolean equal(Grid grid) {
		if (this.col == grid.getCol() && this.row == grid.getRow())
			return true;
		else
			return false;
	}
	
	public int getKey() {
		return this.col * 10000 + this.row;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	// public Position3D getCenter() {
	// return center;
	// }
	//
	// public void setCenter(Position3D center) {
	// this.center = center;
	// }
}
