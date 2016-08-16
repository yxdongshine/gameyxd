package com.lx.game.fight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.space.IArea;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.ISkill;
import com.engine.utils.GameUtils;
import com.lx.game.entity.GameSpace;
import com.lx.nserver.txt.SkillTemplatePojo;
import com.lx.nserver.txt.SkillUnitPojo;

/**
 * ClassName:FightAoe <br/>
 * Function: TODO (AOE选中角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-25 下午3:22:03 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class FightAoe {
	
	/**
	 * 圆攻击技能选择
	 * 
	 * @param attacker 攻击者
	 * @param center 技能中心
	 * @param fighters 战斗者集合
	 * @param radius 半径
	 * @param type 玩家类型选择 0-全部 1-非同队 2-非本帮会
	 * @return
	 */
	public List<IFighter> getFighterInRound(IFighter attacker, Position3D center, List<IFighter> fighters, int radius, GameSpace map) {
		// 目标集合
		List<IFighter> targets = new ArrayList<IFighter>();
		
		// 选择目标
		Iterator<IFighter> iter = fighters.iterator();
		while (iter.hasNext()) {
			IFighter fighter = (IFighter) iter.next();
			if (fighter.isDie()) {
				continue;
			}
			if (fighter.getId() == attacker.getId()) {
				continue;
			}
			
			double distance = center.getAbsDistance(fighter.getPosition3D());
			
			// log.debug("圆形选择" + fighter.getId() + "距离：" + distance);
			// 是否在半径内
			if (distance <= radius) {
				targets.add(fighter);
			}
		}
		
		return targets;
	}
	
	/**
	 * 矩形攻击技能选择
	 * 
	 * @param attacker 攻击者
	 * @param center 技能中心
	 * @param fighters 战斗者集合
	 * @param direction 方向
	 * @param width 宽度
	 * @param radius 半径
	 * @param type 玩家类型选择 0-全部 1-非同队 2-非本帮会
	 * @return
	 */
	public List<IFighter> getFighterInRect(IFighter attacker, Position3D center, List<IFighter> fighters, int width, int height, GameSpace map) {
		
		Position3D start = new Position3D(attacker.getPosition3D().getX(), attacker.getPosition3D().getY(), attacker.getPosition3D().getZ());
		Position3D end = new Position3D(center.getX(), center.getY(), center.getZ());
		Position3D tmpPosition3D = new Position3D(center.getX(), center.getY(), center.getZ());
		// 取经过目标点（point）垂直的斜率
		double k = 0;
		double k1 = 0;
		int vx = 1;
		int vy = 1;
		// 在同一个位置上。
		// if (end.distance(start) == 0) {
		// tmpPosition3D = SearchWayLaw.getNextPosition3D(end.x, end.y, this.fighter.getLogicFacing());
		// }
		// 通过朝向获得斜率
		k = -(start.getZ() - tmpPosition3D.getZ()) / (1.0 * (start.getX() - tmpPosition3D.getX()));
		k1 = (start.getX() - tmpPosition3D.getX()) / (1.0 * (start.getZ() - tmpPosition3D.getZ()));
		vx = (tmpPosition3D.getX() - start.getX()) > 0 ? 1 : -1;
		vy = (tmpPosition3D.getZ() - start.getZ()) > 0 ? 1 : -1;
		
		Result result = frontResult(k, k1, width, height, vx, vy, end);
		
		// 目标集合
		List<IFighter> targets = new ArrayList<IFighter>();
		// 选择目标
		Iterator<IFighter> iter = fighters.iterator();
		while (iter.hasNext()) {
			IFighter attacked = (IFighter) iter.next();
			if (attacked.isDie()) {
				continue;
			}
			if (attacked.getId() == attacker.getId()) {
				continue;
			}
			
			// //这个只是算出攻击都方向
			// double degree = GameUtils.getAngel(attacker.getPosition3D(), fighter.getPosition3D());
			//
			// double dis = attacker.getPosition3D().getXZDistance(fighter.getPosition3D()) - Math.sqrt(height*height + width *width);
			// if (dis <= 0 && degree < 90){
			// targets.add(fighter);
			// }
			int count = 0;
			for (int i = 0, n = 1; i < result.points.length; i++, n++) {
				if (n == result.points.length)
					n = 0;
				if (result.points[n].getXZDistance(attacked.getPosition3D()) + result.points[i].getXZDistance(attacked.getPosition3D()) > result.dis) {
					count++;
				}
			}
			
			if (count > 2) {
				continue;
			}
			targets.add(attacked);
			  
		}
		 
		return targets;
	}
	
	
	/**
	 * selectTarget:(). <br/>
	 * TODO().<br/>
	 * 选中目标
	 * 
	 * @author lyh
	 * @param targetPos
	 * @param stPojo
	 * @param suPojo
	 * @return
	 */
	public List<IFighter> selectTarget(IFighter attacker, IFighter targetObj, SkillUnitPojo suPojo, SkillTemplatePojo stPojo) {
		List<IFighter> targetList = new ArrayList<IFighter>();
		Position3D targetPos = targetObj.getPosition3D();
		// 选择类型
		int type = 0;
		if (stPojo.getFireTargetType() == ISkill.SKILL_TARGET_OWN) {// 自己
			if (suPojo.getAttkAoe() == ISkill.ATT_SINGLE) {
				if (targetObj != null) {
					targetList.add(attacker);
					return targetList;
				}
			}
		} else if (stPojo.getFireTargetType() == ISkill.SKILL_TARGET_TEAM) {// 队友
			if (attacker.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
				type = 2;
			} else {
				type = 1;
			}
		} else if (stPojo.getFireTargetType() == ISkill.SKILL_TARGET_ENEMY) {// 敌人
			if (suPojo.getAttkAoe() == ISkill.ATT_SINGLE) {
				if (targetObj != null) {
					targetList.add(targetObj);
					
				}
				return targetList;
			}
			
			if (targetObj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
				type = 2;
			} else {
				type = 1;
			}
		}
		
		List<IFighter> areaLists = getAreaFighter(attacker, (suPojo.getHurtScope()[0] > suPojo.getHurtScope()[1] ? suPojo.getHurtScope()[0] : suPojo.getHurtScope()[1]), type, (GameSpace) attacker.getSpace());
		
		if (suPojo.getAttkAoe() == ISkill.ATT_RECTANG) {// 矩形
			targetList = getFighterInRect(attacker, targetPos, areaLists, suPojo.getHurtScope()[0], suPojo.getHurtScope()[1], (GameSpace) attacker.getSpace());
		} else if (suPojo.getAttkAoe() == ISkill.ATT_SECTOR) {// 扇形
			// this.getFighterInSector(attacker, targetPos, getAreaFighter(targetPos, radius, 1, attacker.getSpace()), direction, angle, radius, attacker.getSpace());
		} else if (suPojo.getAttkAoe() == ISkill.ATT_ROUND) {// 圆形
			targetList = getFighterInRound(attacker, targetPos, areaLists, suPojo.getHurtScope()[0], (GameSpace) attacker.getSpace());
		}
		return targetList;
	}
	
	/**
	 * getAreaFighter:(). <br/>
	 * TODO().<br/>
	 * 得到以点为中心,radius 为半半径的对象
	 * 
	 * @author lyh
	 * @param pos
	 * @param radius
	 * @param targetType = 0:全部(角色,怪)1:角色 2:怪
	 * @return
	 */
	private List<IFighter> getAreaFighter(IFighter targeter, int radius, int type, GameSpace map) {
		List<IFighter> areaLists = new ArrayList<IFighter>();
		SpaceInfo info = map.getSpaceInfo();
		// int areaId[][] = map.getRoundAreas(pos, info, radius);
		// for (int i = 0; i < areaId.length; i++) {
		// for (int j = 0; j < areaId[i].length; j++) {
		// IArea curArea = map.getAoi().getCellAtRowCol(areaId[i][j] % 1000, areaId[i][j] / 1000);
		for (Map.Entry<Long, IMapObject> entry : targeter.getViewMap().entrySet()) {
			IMapObject obj = entry.getValue();
			if (type == 0) {// 全部
				if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER || obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
					areaLists.add((IFighter) obj);
				}
			} else if (type == 1) {// 角色
				if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
					areaLists.add((IFighter) obj);
				}
			} else if (type == 2) {// 怪
				if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
					areaLists.add((IFighter) obj);
				}
			}
			// }
			// }
		}
		return areaLists;
	}
	
	/**
	 * 根据斜率找目标点前的区域
	 * 
	 * @param k
	 * @param width
	 * @param height
	 * @param vx
	 * @param vy
	 * @return
	 */
	private Result frontResult(double k, double k1, int width, int height, int vx, int vy, Position3D point) {
		Result result = new Result();
		result.dis = 2 * width + 1 + height + 1;
		//result.dis = width + 1 + height/2 + 1;
		if (k1 == Double.NEGATIVE_INFINITY || k1 == Double.POSITIVE_INFINITY) {
			// 与坐标x平行
			result.points[0] = new Position3D(point.getX(), 0, point.getZ() - vy * width);
			result.points[1] = new Position3D(point.getX() + vx * height, 0, point.getZ() - vy * width);
			result.points[2] = new Position3D(point.getX() + vx * height, 0, point.getZ() + vy * width);
			result.points[3] = new Position3D(point.getX(), 0, point.getZ() + vy * width);
		} else {
			// 与坐标y平行
			if (k1 == 0) {
				result.points[0] = new Position3D(point.getX() - vx * width, 0, point.getZ());
				result.points[1] = new Position3D(point.getX() - vx * width, 0, point.getZ() + vy * height);
				result.points[2] = new Position3D(point.getX() + vx * width, 0, point.getZ() + vy * height);
				result.points[3] = new Position3D(point.getX() + vx * width, 0, point.getZ());
			} else {
				float Δx1 = (float)-Math.sqrt(width * width - ((k1 * k1) + 1));
				float Δx2 = (float)Math.sqrt(width * width - ((k1 * k1) + 1));
				float Δy1 = (float)-k1 * Δx1;
				float Δy2 = (float)-k1 * Δx2;
				float Δtx = (float)(vx * Math.sqrt(height * height - ((k1 * k1) + 1)));
				float Δty = (float)-k * Δtx;
				float Δx3 = (float)Math.sqrt(width * width - ((k1 * k1) + 1)) + Δtx;
				float Δx4 = (float)-Math.sqrt(width * width - ((k1 * k1) + 1)) + Δtx;
				float Δy3 =(float) -k1 * (Δx3 - Δtx) + Δty;
				float Δy4 = (float)-k1 * (Δx4 - Δtx) + Δty;
				result.points[0] = new Position3D((float) (point.getX() + Δx1), 0, (float) (point.getZ() + Δy1));
				// result.points[0].setLocation(point.getX() + Δx1,0, point.getZ() + Δy1);
				result.points[1] = new Position3D((float) (point.getX() + Δx2), 0, (float) (point.getZ() + Δy2));
				// result.points[1].setLocation(point.getX() + Δx2, point.getZ() + Δy2);
				result.points[2] = new Position3D((float) (point.getX() + Δx3), 0, (float) (point.getZ() + Δy3));
				// result.points[2].setLocation(point.getX() + Δx3, point.getZ() + Δy3);
				result.points[3] = new Position3D((float) (point.getX() + Δx4), 0, (float) (point.getZ() + Δy4));
				// result.points[3].setLocation(point.getX() + Δx4, point.getZ() + Δy4);
			}
		}
		return result;
	}
	
	/**
	 * 结果对象
	 * 
	 * @author
	 * 
	 */
	class Result {
		/** 矩形确定的四个顶点 */
		protected Position3D[] points = new Position3D[4];
		
		/** 矩形周长的一半 */
		protected double dis = 0;
	}
}
