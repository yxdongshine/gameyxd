package com.lx.game.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.config.xml.map.BlockData;
import com.engine.config.xml.map.GateInfo;
import com.engine.config.xml.map.NpcInfo;
import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.AbsDropItem;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.Door;
import com.engine.entityobj.IFighter;
import com.engine.entityobj.Npc;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.AbsSpace;
import com.engine.entityobj.space.IArea;
import com.engine.entityobj.space.IMapObject;
import com.lib.utils.IConst;
import com.lib.utils.ServerUUID;
import com.loncent.protocol.game.battle.Battle.BattleHurtRequest;
import com.loncent.protocol.game.battle.Battle.BattleHurtRequest.BattleHurtMsg;
import com.lx.game.entity.listener.IDropItemListener;

/**
 * ClassName:Space <br/>
 * Function: TODO (地图). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-28 下午3:13:32 <br/>
 * 地图的逻辑处理
 * 
 * @author lyh
 * @version
 * @see
 */
public class GameSpace extends AbsSpace {
	private static Log log = LogFactory.getLog(GameSpace.class);
	
	/**
	 * Creates a new instance of Space.
	 * 
	 * @param tpl
	 * @param spaceId
	 * @param callback
	 */
	public GameSpace(SpaceInfo tpl, long sMapUid) {
		super(tpl, sMapUid);
		init();
	}
	
	public void init() {
		SpaceInfo info = getSpaceInfo();
		// 初始化npc
		for (NpcInfo npcInfo : info.npcList) {
			Npc n = createNpc(npcInfo);
			npcMaps.put(n.getId(), n);
			addToMap(n);
		}
		
		// 初始化传送门,传送门只加入地图容器
		if (this.spaceInfo.fubenPojo.getType() == IConst.FUBEN_TYPE_WORLD) {
			for (Entry<Integer, GateInfo> gate : info.gateList.entrySet()) {
				Door door = createDoor(gate.getValue());
				transGateMaps.put(door.getId(), door);
				addToMap(door);
			}
		}
		// 怪物到管理器里面处理
	}
	
	/**
	 * addToMap:(). <br/>
	 * TODO().<br/>
	 * 加入到地图
	 * 
	 * @author lyh
	 * @param obj
	 */
	public void addToMap(IMapObject obj) {
		this.mapObjects.put(obj.getId(), obj);
		// 加入到各自的区域
		IArea area = aoi.getIAreaByXZ(obj.getPosition3D().getX(), obj.getPosition3D().getZ());
		obj.setArea(area);
		area.addMapObject(obj);
		System.out.print("area.getMapObjects().size() = " + area.getMapObjects().size());
	}
	
	/**
	 * 新增传送门 addDoorToMap:(). <br/>
	 */
	public void addDoorToMap(GateInfo gateInfo) {
		Door door = createDoor(gateInfo);
		transGateMaps.put(door.getId(), door);
		addToMap(door);
	}
	
	//
	// /**
	// * doMapObjectAddView:(). <br/>
	// * TODO().<br/>
	// * 把当前区域的数据加入视野
	// *
	// * @author lyh
	// * @param obj
	// */
	// public void doMapObjectAddView(IMapObject obj) {
	// List<IMapObject> broadList = null;
	// for (Map.Entry<Long, IMapObject> viewMap : obj.getArea().getMapObjects().entrySet()) {
	// IMapObject viewObj = viewMap.getValue();
	// if (viewObj == obj) {
	// continue;
	// }
	// // if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_NONE) {
	// // continue;
	// // }
	// // if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_DOOR) {
	// // continue;
	// // }
	// // if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_NPC && this.npcMaps.get(viewObj.getId()) != null) {
	// // continue;
	// // }
	// //
	// // 在视野内
	// if (viewObj.isView(obj)) {
	// continue;
	// }
	// // //对象不可用
	// // if (!viewObj.isEnable()){
	// // continue;
	// // }
	// if (broadList == null) {
	// broadList = new ArrayList<IMapObject>();
	// }
	//
	// // 相互加入视野
	// obj.addView(viewObj);
	// broadList.add(viewObj);
	//
	// viewObj.addView(obj);
	// // 要广播
	// viewObj.getMapObjectMessage().addView(obj);
	// }
	//
	// // 把自己广播给其他玩家
	// if (broadList != null && broadList.size() > 0) {
	// obj.getMapObjectMessage().addView(broadList);
	// }
	// }
	
	/**
	 * addView:(). <br/>
	 * TODO().<br/>
	 * 判断相临的8块区域 加入视野 传送门，npc等不能加入视野
	 * 
	 * @author lyh
	 * @param obj
	 * @param nineGridList
	 */
	@Override
	public void doMapObjectAddView(IMapObject obj, List<IArea> nineGridList) {
		List<IMapObject> broadList = null;
		for (IArea area : nineGridList) {
			for (Map.Entry<Long, IMapObject> viewMap : area.getMapObjects().entrySet()) {
				IMapObject viewObj = viewMap.getValue();
				if (viewObj.getId() == obj.getId()) {
					continue;
				}
				if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_NONE) {
					continue;
				}
				/*
				if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_DOOR) {
					continue;
				}
				*/
				
				if (viewObj.getType() == IMapObject.MAP_OBJECT_TYPE_NPC && this.npcMaps.get(viewObj.getId()) != null) {
					continue;
				}
				
				// 在视野内
				if (viewObj.isInView(obj)) {
					continue;
				}
				
				// 没有在视野范围内
				if (!viewObj.isView(obj)) {
					continue;
				}
				
				// //对象不可用
				// if (!viewObj.isEnable()){
				// continue;
				// }
				if (broadList == null) {
					broadList = new ArrayList<IMapObject>();
				}
				
				// 相互加入视野
				obj.addView(viewObj);
				broadList.add(viewObj);
				
				viewObj.addView(obj);
				// 要广播
				viewObj.getMapObjectMessage().addView(obj);
			}
		}
		
		// 把自己广播给其他玩家
		if (broadList != null && broadList.size() > 0) {
			obj.getMapObjectMessage().addView(broadList);
		}
	}
	
	/**
	 * leftView:(). <br/>
	 * TODO().<br/>
	 * 清除 不在自己视野的对象
	 * 
	 * @author lyh
	 * @param obj
	 * @param nineGridList
	 */
	@Override
	public void doMapObjectLeftView(IMapObject obj) {
		List<IMapObject> broadList = null;
		
		for (Map.Entry<Long, IMapObject> viewMap : obj.getViewMap().entrySet()) {
			IMapObject viewObj = viewMap.getValue();
			// 删除仇恨值
			obj.removeHatred(viewObj);
			viewObj.removeHatred(obj);
			// 在视野内
			if (obj.isView(viewObj)) {
				continue;
			}
			// //对象不可用
			// if (!viewObj.isEnable()){
			// continue;
			// }
			if (broadList == null) {
				broadList = new ArrayList<IMapObject>();
			}
			
			// 相互加入视野
			obj.leftView(viewObj);
			broadList.add(viewObj);
			
			viewObj.leftView(obj);
			// 要广播
			viewObj.getMapObjectMessage().leftView(obj);
		}
		
		// 把自己广播给其他玩家
		if (broadList != null && broadList.size() > 0) {
			obj.getMapObjectMessage().leftView(broadList);
		}
	}
	
	/**
	 * playerIntoMap:(). <br/>
	 * TODO().<br/>
	 * 角色进入地图
	 * 
	 * @author lyh
	 * @param sp
	 */
	public void playerIntoMap(ServerPlayer sp) {
		addToMap(sp);
		this.playerMaps.put(sp.getId(), sp);
		// 加放视野
		doMapObjectAddView(sp, sp.getArea().getNineArea());
	}
	
	/**
	 * exitMap:(). <br/>
	 * TODO().<br/>
	 * 退出地图
	 * 
	 * @author lyh
	 * @param sp
	 */
	public void exitMap(IMapObject sp) {
		if (sp.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			playerMaps.remove(sp.getId());
		} else if (sp.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
			monsterMaps.remove(sp.getId());
		}
		
		mapObjects.remove(sp.getId());
		// 告诉别人,自己离开他们的视野
		for (Map.Entry<Long, IMapObject> obj : sp.getViewMap().entrySet()) {
			IMapObject mObj = obj.getValue();
			mObj.leftView(sp);
			mObj.getMapObjectMessage().leftView(sp);
			System.err.println(mObj.getViewMap().size() + "::mobj::" +mObj.getName() + "::"+mObj.getId() );
		}
		sp.clearView();
		// 退出区域块
		if (sp.getArea() != null) {
			sp.getArea().removeMapObject(sp);
		}
		sp.setArea(null);
	}
	
	@Override
	protected Npc createNpc(NpcInfo npcInfo) {
		// TODO Auto-generated method stub
		Npc npc = super.createNpc(npcInfo);
		MapObjectMessage mom = new MapObjectMessage();
		mom.init(npc);
		npc.setMapObjectMessage(mom);
		return npc;
	}
	
	@Override
	protected Door createDoor(GateInfo gateInfo) {
		// TODO Auto-generated method stub
		Door door = super.createDoor(gateInfo);
		MapObjectMessage mom = new MapObjectMessage();
		mom.init(door);
		door.setMapObjectMessage(mom);
		return door;
	}
	
	/**
	 * playerMove:(). <br/>
	 * TODO().<br/>
	 * 角色移动
	 * 
	 * @author lyh
	 * @param sp
	 * @param newX
	 * @param newY
	 * @param newZ
	 * @param dir
	 */
	public void move(IMapObject sp, float newX, float newY, float newZ, float dir, boolean monster) {
		// 还没有判断 能不能移动
		float oldX = sp.getPosition3D().getX();
		float oldY = sp.getPosition3D().getY();
		float oldZ = sp.getPosition3D().getZ();
		// 判断是否在同一区域
		IArea oldArea = getAoi().getIAreaByXZ(oldX, oldZ);
		IArea newArea = getAoi().getIAreaByXZ(newX, newZ);
		if (oldArea == null || newArea == null) {
			return;
		}
		sp.setX(newX);
		sp.setY(newY);
		sp.setZ(newZ);
		sp.setDir(dir);
		if (oldArea != newArea) {// 不在同一区域
			oldArea.removeMapObject(sp);
			newArea.addMapObject(sp);
			sp.setArea(newArea);
		}
		doMapObjectLeftView(sp);
		doMapObjectAddView(sp, newArea.getNineArea());
		// 发送移动消息
		if (monster) {
			return;
		}
		
		// 广播
		for (Entry<Long, IMapObject> obj : sp.getViewMap().entrySet()) {
			IMapObject mb = obj.getValue();
			if (mb.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				mb.getMapObjectMessage().moveView(sp);
			}
		}
	}
	
	/**
	 * 获得半径内的区域Id列表
	 * 
	 * @param position 中心点
	 * @param radius 半径
	 * @return
	 */
	public int[][] getRoundAreas(Position3D position, SpaceInfo sInfo, int radius) {
		// 上边界
		float topPosition = position.getZ() - radius;
		// 下边界
		float buttomPisition = position.getZ() + radius;
		// 左边界
		float leftPosition = position.getX() - radius;
		// 右边界
		float rightPosition = position.getX() + radius;
		
		// Q_mapBean mapInfo = ManagerPool.dataManager.q_mapContainer.getMap().get(map.getMapModelid());
		
		if (topPosition < 0) {
			topPosition = 0;
		}
		
		if (buttomPisition >= sInfo.totalLength) {
			buttomPisition = sInfo.totalLength - 1;
		}
		
		if (leftPosition < 0) {
			leftPosition = 0;
		}
		
		if (rightPosition >= sInfo.totalWidth) {
			rightPosition = sInfo.totalWidth - 1;
		}
		int clen = (int) (rightPosition - leftPosition);
		int rlen = (int) (buttomPisition - topPosition);
		int maxCol = (clen % IConst.AOI_GRID_SIZE != 0) ? (clen / IConst.AOI_GRID_SIZE + 1) : (clen / IConst.AOI_GRID_SIZE);
		int maxRow = (rlen % IConst.AOI_GRID_SIZE != 0) ? (rlen / IConst.AOI_GRID_SIZE + 1) : (rlen / IConst.AOI_GRID_SIZE);
		
		// int topArea = getAreaId(new Position(position.getX(), topPosition));
		// int buttomArea = getAreaId(new Position(position.getX(), buttomPisition));
		// int leftArea = getAreaId(new Position(leftPosition, position.getY()));
		// int rightArea = getAreaId(new Position(rightPosition, position.getY()));
		
		int width = maxCol;
		int height = maxRow;
		
		int left = (int) ((leftPosition % IConst.AOI_GRID_SIZE != 0) ? (leftPosition / IConst.AOI_GRID_SIZE + 1) : (leftPosition / IConst.AOI_GRID_SIZE));
		int top = (int) ((topPosition % IConst.AOI_GRID_SIZE != 0) ? (topPosition / IConst.AOI_GRID_SIZE + 1) : (topPosition / IConst.AOI_GRID_SIZE));
		
		int[][] areas = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				areas[i][j] = (left + j) * 1000 + top + i;
			}
		}
		return areas;
	}
	
	/**
	 * getRoundGrid:(). <br/>
	 * TODO().<br/>
	 * 得到目标点周边能走的格式
	 * 
	 * @author lyh
	 * @param targetPos
	 * @return
	 */
	public List<BlockData> getRoundGrid(Position3D targetPos, int range, boolean hasTarget) {
		List<BlockData> list = new ArrayList<BlockData>();
		float x = targetPos.getX() - range;
		int left = getSpaceInfo().getCol(x);
		
		x = targetPos.getX() + range;
		int right = getSpaceInfo().getCol(x);
		float z = targetPos.getZ() - range;
		if (z < 0) {
			z = 0;
		}
		int top = getSpaceInfo().getRow(z);
		
		z = targetPos.getZ() + range;
		int buttom = getSpaceInfo().getRow(z);
		
		for (int i = top; i <= buttom; i++) {
			for (int j = left; j <= right; j++) {
				BlockData _grid = getSpaceInfo().blocks[i][j];
				if (_grid == null) {
					continue;
				}
				// 不包含就踢除掉
				if (!hasTarget && _grid.getCol() == getSpaceInfo().getCol(targetPos.getX()) && _grid.getRow() == getSpaceInfo().getRow(targetPos.getZ())) {
					continue;
				}
				
				// 判断是否能走
				if (_grid.getType() == 1) {
					continue;
				}
				list.add(_grid);
			}
		}
		return list;
		
	}
	
	List<IFighter> list = new ArrayList<IFighter>();
	
	@Override
	public void onTime(long time) {
		// TODO Auto-generated method stub
		// 玩家更新
		for (Map.Entry<Long, ServerPlayer> entry : playerMaps.entrySet()) {
			ServerPlayer sp = entry.getValue();
			if (!sp.getFightAttackQueue().isEmpty()) {
				BattleHurtRequest startFight = sp.getFightAttackQueue().poll();
				// Position3D targetPos = new Position3D(startFight.getSkillRotation().getX(), startFight.getSkillRotation().getY(), startFight.getSkillRotation().getZ());
				// 得到技能 id,
				if (startFight != null && startFight.getHurtListList() != null && startFight.getHurtListCount() > 0) {
					try {
						
						list.clear();
						for (BattleHurtMsg msg : startFight.getHurtListList()) {
							IMapObject defencer = getMapObjects().get(msg.getBeHurtId());
							if (defencer != null) {
								// 只有怪和角色才能被打
								if (defencer.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER || defencer.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
									IFighter attacted = (IFighter) defencer;
									if (!attacted.isDie()) {// 没有死
										list.add(attacted);
									}
								}
							} else {
								log.error("角色不存在::" + msg.getBeHurtId());
							}
						}
						if (list.size() == 0) {
							break;
						}
						log.error("战斗::"+sp.getName());
						sp.getIFightListener().playerFight(sp, startFight.getSkillId(), startFight.getSkillUnitId(), list);
					} catch (Exception e) {
						e.printStackTrace();
						log.error("战斗有问题::");
					}
				}
			}
		}
		super.onTime(time);
		processDropItem();
	}
	
	private List<DropItem> removeDropItem = new ArrayList<DropItem>();
	
	/**
	 * processDropItem:(). <br/>
	 * TODO().<br/>
	 * 处理掉落的道具(每次tick检查一次)
	 * 
	 * @author lyh
	 */
	public void processDropItem() {
		for (Map.Entry<Long, AbsDropItem> entry : getDropItemMaps().entrySet()) {
			try {
				DropItem dItem = (DropItem) entry.getValue();
				// 有人发了拾取动作
				boolean pickUp = false;
				if (dItem.getRoleId() > 0) {
					removeDropItem.add(dItem);
					pickUp = true;
					//
					((IDropItemListener) dItem.getListener()).addItemToBag(dItem);
				}
				
				long curTime = System.currentTimeMillis();
				// 时间已到
				if (dItem.getDisappearTime() >= curTime && !pickUp) {
					removeDropItem.add(dItem);
				}
			} catch (Exception e) {
				log.error("掉落道具有问题!!");
			}
		}
		
		for (DropItem item : removeDropItem) {
			getDropItemMaps().remove(item.getId());
		}
		removeDropItem.clear();
	}
	
	/**
	 * 怪物死亡 OnMonsterDead:(). <br/>
	 */
	public boolean OnMonsterDead(Monster monster) {
		// 世界副本不动态生成传送门
		List<AbsMonster> monsterList = this.monsterGroup.get(monster.getGroupId());
		if (this.spaceInfo.fubenPojo.getType() != IConst.FUBEN_TYPE_WORLD) {
			if (monster.getMonsterType() != 1 || monsterList.size() <= 1) // 非普通怪死亡或者怪物组死亡完
			{
				// 判断是否触发传送门
				for (Entry<Integer, GateInfo> gate : this.spaceInfo.gateList.entrySet()) {
					GateInfo gateInfo = gate.getValue();
					if (gateInfo.openMonsterGroupId == monster.getGroupId()) {
						Door door = createDoor(gateInfo);
						transGateMaps.put(door.getId(), door);
						addToMap(door);
						// 加放视野
						doMapObjectAddView(door, door.getArea().getNineArea());
						return true;
					}
				}
			}
		}
		monsterList.remove(monster);
		return false;
	}
}
