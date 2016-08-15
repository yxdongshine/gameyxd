package com.engine.utils.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.engine.config.xml.map.BlockData;
import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.space.Grid;
import com.lib.utils.ServerRandomUtils;

/**
 * ClassName:AStar <br/>
 * Function: TODO (寻路算法 ). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-5 下午5:39:48 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class AStar {
	
	private static Log log = LogFactory.getLog(AStar.class);
	
	/**
	 * 寻路算法
	 * 
	 * @param mapInfos 地图阻挡信息
	 * @param start 开始节点
	 * @param end 结束节点
	 * @param steps 计算步数
	 * @return
	 */
	public static List<Position3D> findRoads(SpaceInfo info, Position3D start, Position3D end, int steps, boolean cal) {
		// 返回的结果路径
		List<Position3D> result = new ArrayList<Position3D>();
		// 开始所在地图格子
		BlockData startGrid = info.getGrid(start);
		// 结束所在地图格子
		BlockData endGrid = info.getGrid(end);
		// 未移动，格子相同s
		if (startGrid.equal(endGrid)) {
			return result;
		}
		
		RoadGrid startRoadGrid = new RoadGrid();
		startRoadGrid.setGrid(startGrid);
		
		RoadGrid endRoadGrid = new RoadGrid();
		endRoadGrid.setGrid(endGrid);
		
		// 待计算格子
		List<RoadGrid> waitting = new ArrayList<RoadGrid>();
		// 已计算格子
		HashMap<Integer, RoadGrid> counted = new HashMap<Integer, RoadGrid>();
		// 遍历过的格子
		HashSet<Integer> passed = new HashSet<Integer>();
		
		waitting.add(startRoadGrid);
		passed.add(startGrid.getKey());
		
		int step = 0;
		
		while (waitting.size() > 0 && (step < steps || steps == -1)) {
			// 取出优先级最高的格子(权值最小)
			RoadGrid grid = waitting.get(0);
			waitting.remove(0);
			step++;
			
			// 到达终点
			if (grid.equal(endRoadGrid)) {
				endRoadGrid = grid;
				break;
			}
			
			// 加入已计算中
			counted.put(grid.getGrid().getKey(), grid);
			
			// 获取周围格子信息
			List<BlockData> rounds = getRoundGrid(grid.getGrid(), info.blocks);
			
			for (int i = 0; i < rounds.size(); i++) {
				BlockData round = rounds.get(i);
				
				// 已经遍历过
				if (round == null || passed.contains(round.getKey())) {
					continue;
				} // 在地图内是不可行走点
				else if (round.getType() == 1) {
					continue;
				} // 斜边判断相邻方向是否都为阻挡
				else if (round.getRow() != grid.getGrid().row && round.getCol() != grid.getGrid().col) {
					if (info.blocks[round.row][grid.getGrid().col] == null || info.blocks[grid.getGrid().row][round.getCol()] == null || (info.blocks[round.row][grid.getGrid().col].getType() == 1 && info.blocks[grid.getGrid().row][round.getCol()].getType() == 1)) {
						continue;
					}
				}
				// // 在地图内是游泳点
				// if (ManagerPool.mapManager.isSwimGrid(round) && !canSwim) {
				// continue;
				// }
				
				RoadGrid roundGrid = new RoadGrid();
				
				roundGrid.setGrid(round);
				
				roundGrid.setFarther(grid.getGrid().getKey());
				
				// 加入遍历过格子
				passed.add(round.getKey());
				
				// 计算权值
				roundGrid.setWeight(countWeight(round, endGrid));
				
				// 插入到待计算列表
				insert(waitting, roundGrid);
				
			}
		}
		
		// 计算路径
		if (endRoadGrid.getFarther() != -1) {
			// 已经找到终点
			RoadGrid _grid = endRoadGrid;
			int r = ServerRandomUtils.nextInt(100);
			int rTmp = (r % 2 == 0) ? info.mapCellWidth : -info.mapCellWidth;
			float tmp = ServerRandomUtils.randomFloat((float)rTmp / 2);
			if (!cal) {
				tmp = 0;
			}
			result.add(0, new Position3D(end.getX() + tmp, end.getY(), end.getZ() + tmp));
			while (_grid.getFarther() != -1) {
				_grid = counted.get(_grid.getFarther());
				if (!cal) {
					r = ServerRandomUtils.nextInt(100);
					rTmp = (r % 2 == 0) ? info.mapCellWidth : -info.mapCellWidth;
					tmp = ServerRandomUtils.randomFloat((float)rTmp / 2);
				}
				result.add(0, new Position3D(info.getGridX(_grid.getGrid().getCol()) + rTmp, start.getY(), info.getGridZ(_grid.getGrid().getRow()) + rTmp));
				// result.add(0, _grid.getGrid().getCenter());
			}
		} else if (step == steps && waitting.size() > 0) {
			// 到达寻路最大步数
			RoadGrid _grid = waitting.get(0);
			// result.add(0, _grid.getGrid().getCenter());
			result.add(0, new Position3D(info.getGridX(_grid.getGrid().getCol()), start.getY(), info.getGridZ(_grid.getGrid().getRow())));
			while (_grid.getFarther() != -1) {
				_grid = counted.get(_grid.getFarther());
				// invert.add(_grid);
				// result.add(0, _grid.getGrid().getCenter());
				result.add(0, new Position3D(info.getGridX(_grid.getGrid().getCol()), start.getY(), info.getGridZ(_grid.getGrid().getRow())));
			}
		}
		
		log.error("怪物寻路:::" + JSON.toJSONString(result));
		return result;
	}
	
	/**
	 * 获得周围格子（包含本格子，共9个）
	 * 
	 * @param grid 格子
	 * @return
	 */
	public static List<BlockData> getRoundGrid(BlockData grid, BlockData[][] mapInfos) {
		List<BlockData> grids = new ArrayList<BlockData>();
		for (int i = 0; i < 9; i++) {
			int x = grid.getCol() + i % 3 - 1;
			int y = grid.getRow() + i / 3 - 1;
			if (x < 0 || x >= mapInfos[0].length || y < 0 || y >= mapInfos.length) {
				continue;
			}
			BlockData _grid = mapInfos[y][x];
			grids.add(_grid);
		}
		return grids;
	}
	
	/**
	 * 计算权值
	 * 
	 * @param start 开始格子
	 * @param end 结束给子
	 * @return
	 */
	private static int countWeight(BlockData start, BlockData end) {
		return Math.abs(end.getCol() - start.getCol()) + Math.abs(end.getRow() - start.getRow());
	}
	
	/**
	 * 按权值插入队列
	 * 
	 * @param waitting 队列
	 * @param grid 格子
	 */
	private static void insert(List<RoadGrid> waitting, RoadGrid grid) {
		
		if (waitting.size() == 0) {
			waitting.add(grid);
			return;
		}
		
		// 头部插入
		for (int i = 0; i < waitting.size(); i++) {
			RoadGrid _grid = waitting.get(i);
			if (_grid.getWeight() < grid.getWeight()) {
				continue;
			}
			waitting.add(i, grid);
			return;
		}
		
		waitting.add(grid);
	}
	
}
