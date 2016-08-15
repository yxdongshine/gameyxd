package com.engine.entityobj.space;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.entityobj.ServerPlayer;
import com.engine.interfaces.ITickable;
import com.engine.signallight.SingnalLightManager;

/**
 * ClassName:AoiCell <br/>
 * Function: (AOI单元格,(一块区域)). <br/>
 * Date: 2015-7-13 下午2:28:38 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class AoiCell implements IArea {
	
	// /** 日志 **/
	// private Log log = LogFactory.getLog(AoiCell.class);
	
	/** 行坐标 **/
	private int startRow;
	
	/** 列坐标 **/
	private int startCol;
	
	/** 当前区域内实体map **/
	private Map<Long, IMapObject> areaMapObjectMap = new ConcurrentHashMap<Long, IMapObject>();
	
	/** 此区域的角色 **/
	private Map<Long, IMapObject> playersMap = new ConcurrentHashMap<Long, IMapObject>();
	
	/** 九宫格周边的8个邻居 **/
	private List<IArea> neighbors = new ArrayList<IArea>();
	
	public AoiCell(int row, int col) {
		this.startRow = row;
		this.startCol = col;
	}
	
	// /**
	// * 获取cell中实体数量 getEntityNum:(). <br/>
	// */
	// public int getAllEntityNum() {
	// return this.aoiEntityMap.size();
	// }
	
	// /**
	// * 获取cell内指定类型实体数量 getRoleNum:(). <br/>
	// */
	// public int getEntityNum(PbAoiEntityType entityType) {
	// int number = 0;
	// for (Entry<Long, AoiEntity> entry : this.aoiEntityMap.entrySet()) {
	// AoiEntity entity = entry.getValue();
	// if (entity.type == entityType)
	// number++;
	// }
	// return number;
	// }
	
	/**
	 * 添加邻居 addNeighbor:(). <br/>
	 */
	public void addNeighbor(AoiCell aoiCell) {
		if (neighbors.contains(aoiCell))
			return;
		this.neighbors.add(aoiCell);
	}
	
	// /**
	// * 添加AOI实体 addAoiEntity:(). <br/>
	// */
	// public void addAoiEntity(AoiEntity entity, boolean isUpdateList) {
	// if (aoiEntityMap.containsKey(entity.id))
	// return;
	// if (isUpdateList) {
	// // 观察者订阅者更新
	// List<AoiEntity> careList = getCareEntity(entity);
	// for (Iterator<AoiEntity> it = careList.iterator(); it.hasNext();) {
	// AoiEntity otherEntity = it.next();
	// entity.addNewWatcher(otherEntity);
	// otherEntity.addNewSubjecter(entity);
	// }
	// }
	// this.aoiEntityMap.put(entity.id, entity);
	// }
	
	// /**
	// * 移除AOI实体 removeAoiEntity:(). <br/>
	// */
	// public void removeAoiEntity(AoiEntity entity, boolean isUpdateList) {
	// if (!aoiEntityMap.containsKey(entity.id)) {
	// log.error("aoiEntityMap dont have the entity. row=" + this.row + " col=" + this.col + " remove entityId=" + entity.id + " name=" + entity.name);
	// return;
	// }
	// if (isUpdateList) {
	// // 订阅者观察者更新
	// List<AoiEntity> list = entity.getSubjectList();
	// for (Iterator<AoiEntity> it = list.iterator(); it.hasNext();) {
	// AoiEntity otherEntity = it.next();
	// otherEntity.addOutWatcher(entity);
	// otherEntity.addOutSubjecter(entity);
	// }
	// }
	// this.aoiEntityMap.remove(entity.id);
	// }
	
	// /**
	// * 获取关心的实体(九宫格内,视线范围内的Entity) getCareEntity:(). <br/>
	// */
	// public ArrayList<AoiEntity> getCareEntity(AoiEntity entity) {
	// ArrayList<AoiEntity> careList = new ArrayList<AoiEntity>();
	//
	// for (Entry<Long, AoiEntity> entry : this.aoiEntityMap.entrySet()) {
	// AoiEntity otherEntity = entry.getValue();
	// // 排除自己
	// if (otherEntity.id == entity.id)
	// continue;
	// /* log.info("isCanSeeInfo. curId=" + entity.id + " otherId=" + otherEntity.id +" isCanSee=" + entity.isCanSee(otherEntity) + " distance=" +
	// * entity.pos3d.getAbsDistance(otherEntity.pos3d)); */
	// if (entity.isCanSee(otherEntity)) {
	// careList.add(otherEntity);
	// }
	// }
	//
	// for (Iterator<AoiCell> it = this.neighbors.iterator(); it.hasNext();) {
	// for (Entry<Long, AoiEntity> entry : it.next().aoiEntityMap.entrySet()) {
	// AoiEntity otherEntity = entry.getValue();
	// // 排除自己
	// if (otherEntity.id == entity.id)
	// continue;
	//
	// if (entity.isCanSee(otherEntity)) {
	// careList.add(otherEntity);
	// }
	// }
	// }
	// return careList;
	// }
	
	// /**
	// * 更新实体 此函数比较费时,如果在一个区域出现聚集，会导致耗时增加 updateEntity:(). <br/>
	// */
	// public void updateEntity(AoiEntity entity) {
	// // 退出视野
	// for (Iterator<AoiEntity> it = entity.getWatcherList().iterator(); it.hasNext();) {
	// AoiEntity otherEntity = it.next();
	// if (!entity.isCanSee(otherEntity)) {
	// entity.addOutWatcher(otherEntity);
	// otherEntity.addOutSubjecter(entity);
	// }
	// }
	//
	// // 进入视野
	// ArrayList<AoiEntity> careList = this.getCareEntity(entity);
	// careList.removeAll(entity.getWatcherList()); // 计算差集
	// for (Iterator<AoiEntity> it = careList.iterator(); it.hasNext();) {
	// AoiEntity otherEntity = it.next();
	// entity.addNewWatcher(otherEntity);
	// otherEntity.addNewSubjecter(entity);
	// }
	// }
	
	// /**
	// * 实体行走 entityWalk:(). <br/>
	// */
	// public void entityWalk(AoiEntity entity, long deltaTime) {
	// if (entity == null)
	// return;
	// entity.walk(deltaTime);
	// }
	//
	// @Override
	// public void tick(long time) {
	// for (Entry<Long, AoiEntity> entry : this.aoiEntityMap.entrySet()) {
	// AoiEntity entity = entry.getValue();
	// // npc不进行tick
	// if (entity.type == PbAoiEntityType.Npc)
	// continue;
	//
	// SingnalLightManager.setOn(entity.id + "");
	// entity.TickTheList();
	// // 行走（怪物可行走）
	// if ((entity.action == 2) && (entity.type == PbAoiEntityType.Monster)) {
	// entityWalk(entity, time);
	// }
	// // 更新视野范围内,耗时(可以加入慢速tick)
	// updateEntity(entity);
	// SingnalLightManager.setOff(entity.id + "");
	// }
	// }
	
	@Override
	public int getStartCol() {
		// TODO Auto-generated method stub
		return this.startCol;
	}
	
	@Override
	public int getStartRow() {
		// TODO Auto-generated method stub
		return this.startRow;
	}
	
	@Override
	public void addMapObject(IMapObject obj) {
		// TODO Auto-generated method stub
		this.areaMapObjectMap.put(obj.getId(), obj);
		if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			playersMap.put(obj.getId(), obj);
		}
	}
	
	@Override
	public void removeMapObject(IMapObject obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return;
		}
		areaMapObjectMap.remove(obj.getId());
		if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			playersMap.remove(obj.getId());
		}
	}
	
	@Override
	public Map<Long, IMapObject> getMapObjects() {
		// TODO Auto-generated method stub
		return areaMapObjectMap;
	}
	
	@Override
	public List<IArea> getNineArea() {
		// TODO Auto-generated method stub
		return neighbors;
	}
	
	@Override
	public Map<Long, IMapObject> getPlayers() {
		// TODO Auto-generated method stub
		return playersMap;
	}
	
}
