package com.engine.config.xml.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.engine.entityobj.Position3D;
import com.lx.nserver.model.FuBenTemplateModel;
import com.lx.nserver.model.SkillTemplateModel;
import com.lx.nserver.txt.FuBenTemplatePojo;

/**
 * ClassName:SpaceInfo <br/>
 * Function: (空间配置信息). <br/>
 * Date: 2015-7-14 下午3:46:57 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class SpaceInfo {
	/** 空间模板ID **/
	public int mapUid;
	
	/** 空间总宽度 **/
	public int totalWidth;
	
	/** 空间总长度 **/
	public int totalLength;
	
	/** 地图单元格宽度 **/
	public int mapCellWidth;
	
	/** 地图单元格长度 **/
	public int mapCellLength;
	
	/** 地图最大行 **/
	public int maxRow;
	
	/** 地图最大列 **/
	public int maxCol;
	
	/** 出生点 **/
	public Position3D birthPlace;
	
	/** npc列表 **/
	public ArrayList<NpcInfo> npcList;
	
	/** 怪物列表 **/
	public ArrayList<MonsterGroupInfo> monsterList;
	
	/** 传送门列表 **/
	public Map<Integer, GateInfo> gateList;
	
	/** 地图格子数据 **/
	public BlockData[][] blocks;
	
	/** 副本配置 **/
	public FuBenTemplatePojo fubenPojo;
	
	public SpaceInfo() {
		npcList = new ArrayList<NpcInfo>();
		monsterList = new ArrayList<MonsterGroupInfo>();
		gateList = new HashMap<Integer, GateInfo>();
		birthPlace = new Position3D();
		fubenPojo = new FuBenTemplatePojo();
	}
	
	public void initBlock(int totalWidth, int totalLength, int mapCellWidth, int mapCellLength) {
		this.mapCellLength = mapCellLength;
		this.mapCellWidth = mapCellWidth;
		maxCol = (int) (totalWidth % mapCellWidth != 0 ? totalWidth / mapCellWidth + 1 : totalWidth / mapCellWidth);
		maxRow = (int) (totalLength % mapCellLength != 0 ? totalLength / mapCellLength + 1 : totalLength / mapCellLength);
		// maxCol += 1;
		// maxRow += 1;
		blocks = new BlockData[maxRow][maxCol];
	}
	
	/**
	 * 
	 * 是否可行走 isCanWalk:(). <br/>
	 * 检查位置是否合法 checkPosition:(). <br/>
	 */
	private boolean checkPosition(Position3D position) {
		int col = this.getCol(position.getX());
		int row = this.getRow(position.getZ());
		if (row < 0 || row >= this.maxRow || col < 0 || col >= this.maxCol) {
			return false;
		}
		return true;
	}
	
	// /**
	// * 获取指定位置的地图网格坐标 getMapRowColByPos:(). <br/>
	// */
	// public int[] getMapRowColByPos(Position3D position) {
	// int rowCol[] = new int[2];
	// rowCol[0] = (int) (position.getX() % mapCellWidth != 0 ? position.getX()/ mapCellWidth + 1 : position.getX() / mapCellWidth);
	// rowCol[1] = (int) (position.getZ() % mapCellLength != 0 ? position.getZ() / mapCellLength + 1 : position.getZ() / mapCellLength);
	// return rowCol;
	// }
	
	/**
	 * 是否可行走 isCanWalk:(). <br/>
	 */
	public boolean isCanWalk(Position3D position) {
		if (!checkPosition(position))
			return false;
		// int[] rowCol = this.getMapRowColByPos(position);
		int col = this.getCol(position.getX());
		int row = this.getRow(position.getZ());
		BlockData data = this.blocks[row][col];
		if (data == null)
			return false;
		
		if (data.type == 1)
			return false;
		return true;
	}
	
	/**
	 * getRow:(). <br/>
	 * TODO().<br/>
	 * 用坐标获得行
	 * 
	 * @author lyh
	 * @param z
	 * @return
	 */
	public int getRow(float z) {
		float gridZ = (z);
		int row = (int) (gridZ / mapCellLength);
		if (row < 0) {
			row = 0;
		} else if (row >= maxRow) {
			row = maxRow - 1;
		}
		return row;
	}
	
	/**
	 * getCol:(). <br/>
	 * TODO().<br/>
	 * 用坐标获得列
	 * 
	 * @author lyh
	 * @param x
	 * @return
	 */
	public int getCol(float x) {
		float gridX = (x);
		int col = (int) (gridX / mapCellWidth);
		if (col < 0) {
			col = 0;
		} else if (col >= maxCol) {
			col = maxCol - 1;
		}
		return col;
	}
	
	public boolean isCanWalk(float x, float y, float z) {
		int col = this.getCol(x);
		int row = this.getRow(z);
		if (row < 0 || row >= this.maxRow || col < 0 || col >= this.maxCol) {
			return false;
		}
		if (blocks[row][col] == null)
			return false;
		int type = this.blocks[row][col].type;
		if (type == 1)
			return false;
		return true;
	}
	
	/**
	 * 根据坐标获得所在地图格子
	 * 
	 * @param pos 坐标
	 * @return
	 */
	public BlockData getGrid(Position3D pos) {
		int col = this.getCol(pos.getX());
		int row = this.getRow(pos.getZ());
		if (row < 0 || row >= this.maxRow || col < 0 || col >= this.maxCol) {
			return null;
		}
		if (blocks[row][col] == null)
			return null;
		return blocks[row][col];
	}
	
	public boolean isCanWalk(int col, int height, int row) {
		if (row < 0 || row >= this.maxRow || col < 0 || col >= this.maxCol) {
			return false;
		}
		if (blocks[row][col] == null)
			return false;
		int type = this.blocks[row][col].type;
		if (type == 1)
			return false;
		return true;
	}
	
	/**
	 * getGridX:(). <br/>
	 * TODO().<br/>
	 * 获得格子X轴的中心点坐标
	 * 
	 * @author lyh
	 * @param col
	 * @return
	 */
	public float getGridX(int col) {
		return 1.0f * (mapCellWidth * col) + (1.0f)*mapCellWidth / 2;
	}
	
	/**
	 * getGridZ:(). <br/>
	 * TODO().<br/>
	 * 获得格子Z轴的中心点坐标
	 * 
	 * @author lyh
	 * @param row
	 * @return
	 */
	public float getGridZ(int row) {
		return 1.0f * (mapCellLength * row) + (1.0f)*mapCellLength / 2;
	}
	
	/**
	 * 获取block数据 getBlockData:(). <br/>
	 */
	public BlockData getBlockData(Position3D position) {
		if (!checkPosition(position))
			return null;
		int col = this.getCol(position.getX());
		int row = this.getRow(position.getZ());
		BlockData data = this.blocks[row][col];
		if (data == null)
			return null;
		
		return data;
	}
}
