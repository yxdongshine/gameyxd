package com.engine.entityobj.space;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.engine.interfaces.ITickable;
import com.lib.utils.IConst;

/**
 * ClassName:Aoi <br/>
 * Function: (AOI). <br/>
 * Date: 2015-7-13 下午3:00:35 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class Aoi implements ITickable {
	
	private Log log = LogFactory.getLog(Aoi.class);
	
	/** 总行数 **/
	private int maxRow;
	/** 总列数 **/
	private int maxCol;
	/** cell数组 **/
	private AoiCell[][] aoiCells;
	
	public Aoi(int width, int length) {
		maxCol = (width % IConst.AOI_GRID_SIZE != 0) ? (width / IConst.AOI_GRID_SIZE + 1) : (width / IConst.AOI_GRID_SIZE);
		maxRow = (length % IConst.AOI_GRID_SIZE != 0) ? (length / IConst.AOI_GRID_SIZE + 1) : (length / IConst.AOI_GRID_SIZE);
		this.maxCol += 1;
		this.maxRow += 1;
		initCells(this.maxRow, this.maxCol);
	}
	
	/**
	 * 初始化AOICells initCells:(). <br/>
	 */
	public void initCells(int maxRow, int maxCol) {
		aoiCells = new AoiCell[maxRow][maxCol];
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
				aoiCells[i][j] = new AoiCell(i, j);
				aoiCells[i][j].addNeighbor(aoiCells[i][j]);
			}
		}
		
		for (int i = 0; i < maxRow; i++) {
			for (int j = 0; j < maxCol; j++) {
				AoiCell topLeftCell = getCellAtRowCol(i - 1, j - 1);// 左上
				if (topLeftCell != null)
					aoiCells[i][j].addNeighbor(topLeftCell);
				
				AoiCell topCell = getCellAtRowCol(i - 1, j);// 上
				if (topCell != null)
					aoiCells[i][j].addNeighbor(topCell);
				
				AoiCell topRightCell = getCellAtRowCol(i - 1, j + 1);// 右上
				if (topRightCell != null)
					aoiCells[i][j].addNeighbor(topRightCell);
				
				AoiCell leftCell = getCellAtRowCol(i, j - 1);// 左
				if (leftCell != null)
					aoiCells[i][j].addNeighbor(leftCell);
				
				AoiCell rightCell = getCellAtRowCol(i, j + 1);// 右
				if (rightCell != null)
					aoiCells[i][j].addNeighbor(rightCell);
				
				AoiCell leftDownCell = getCellAtRowCol(i + 1, j - 1);// 左下
				if (leftDownCell != null)
					aoiCells[i][j].addNeighbor(leftDownCell);
				
				AoiCell downCell = getCellAtRowCol(i + 1, j);// 下
				if (downCell != null)
					aoiCells[i][j].addNeighbor(downCell);
				
				AoiCell rightDownCell = getCellAtRowCol(i + 1, j + 1);// 右下
				if (rightDownCell != null)
					aoiCells[i][j].addNeighbor(rightDownCell);
			}
		}
	}
	
	/**
	 * 获取指定行列的AOICell getCellAtRowCol:(). <br/>
	 */
	public AoiCell getCellAtRowCol(int row, int col) {
		if (!isValidRowCol(row, col))
			return null;
		return aoiCells[row][col];
	}
	
	/**
	 * 指定行列是否合法 isValidRowCol:(). <br/>
	 */
	public boolean isValidRowCol(int row, int col) {
		if (row < 0 || row >= this.maxRow || col < 0 || col >= this.maxCol) {
			return false;
		}
		return true;
	}
	
	/** 获取地图区域 **/
	public IArea getIAreaByXZ(float x, float z) {
		int ax = (int) (x % IConst.AOI_GRID_SIZE != 0 ? x / IConst.AOI_GRID_SIZE + 1 : x / IConst.AOI_GRID_SIZE);
		int ay = (int) (z % IConst.AOI_GRID_SIZE != 0 ? z / IConst.AOI_GRID_SIZE + 1 : z / IConst.AOI_GRID_SIZE);
		return aoiCells[ay][ax];
	}
	
	/** 获取行列坐标 **/
	public int[] getRowColByXZ(float x, float z) {
		int rowCol[] = new int[2];
		rowCol[0] = (int) (x % IConst.AOI_GRID_SIZE != 0 ? x / IConst.AOI_GRID_SIZE + 1 : x / IConst.AOI_GRID_SIZE);
		rowCol[1] = (int) (z % IConst.AOI_GRID_SIZE != 0 ? z / IConst.AOI_GRID_SIZE + 1 : z / IConst.AOI_GRID_SIZE);
		return rowCol;
	}
	
	@Override
	public void tick(long time) {
		
	}
}
