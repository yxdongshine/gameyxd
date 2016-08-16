package com.lx.game.monster.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.entityobj.IFighter;
import com.engine.entityobj.MonsterStatus;
import com.engine.entityobj.NpcHatred;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IArea;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.ITickable;
import com.engine.utils.astar.AStar;
import com.engine.utils.astar.AStar2;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.monster.Monster.MonsterMoveResponse;
import com.lx.game.entity.Monster;
import com.lx.game.send.MessageSend;
import com.lx.nserver.txt.MonsterPojo;
import com.lx.nserver.txt.SkillTemplatePojo;

/**
 * ClassName:MonterMoveAction <br/>
 * Function: TODO (怪物移动行为). <br/>
 * Reason: TODO (称动行为分为 待机,巡逻,追击,追击返回). <br/>
 * 
 * Date: 2015-8-3 下午2:16:50 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MonterMoveAction implements ITickable {
	private static Log log = LogFactory.getLog(MonterMoveAction.class);
	/** 待机 */
	public final static int IDLE = 0;
	/** 追击 */
	public final static int CHASE = 2;
	
	/** 巡逻 */
	public final static int PATROL = 4;
	
	/** 逃跑 */
	public final static int ESCAPE = 5;
	
	/** 追击返回 */
	public final static int CHASE_BACK = 6;
	
	/** 移动行为 **/
	private int moveAction = IDLE;
	
	private Monster monster;
	
	/** 目标点坐标 **/
	private Position3D targetPosition3d;
	/** 下一次时间暗间隔 **/
	private long waitMoveTime;
	private MonsterPojo pojo;
	private MonsterAttackAction monsterAttackAction;
	
	public MonterMoveAction(Monster mon) {
		monster = mon;
		pojo = monster.getMonsterPojo();
		randomAction();
	}
	
	/**
	 * canChase:(). <br/>
	 * TODO().<br/>
	 * 追击初始化
	 * 
	 * @author lyh
	 */
	public void selectChase(IFighter fighter) {
		// 找到目标
		if (moveAction == CHASE) {
			return;
		}
		
		moveAction = CHASE;
		this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
		// 计算追击目标距离
		// 得到路径点
		monster.getRoads().clear();
		AStar2 star = new AStar2(monster.getSpace().getSpaceInfo(), 100);
		monster.setRoads(star.find(monster.getPosition3D(), monster.randomRoundGrid(monster.getSpace(), fighter.getPosition3D(), 1), true));
		// monster.setRoads(AStar.findRoads(monster.getSpace().getSpaceInfo(), monster.getPosition3D(), monster.randomRoundGrid(monster.getSpace(), fighter.getPosition3D(), 2), -1, true));
		targetPosition3d = new Position3D(fighter.getPosition3D());
		monster.sendMonsterMoveResponse(monster.getRoads(), targetPosition3d);
		
	}
	
	/**
	 * selectChaseBack:(). <br/>
	 * TODO().<br/>
	 * 追击返回状态
	 * 
	 * @author lyh
	 */
	public void selectChaseBack() {
		// 找到目标
		if (moveAction == CHASE_BACK) {
			return;
		}
		monster.setTargetFighter(null);
		moveAction = CHASE_BACK;
		this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
		// 计算追击目标距离
		// 得到路径点
		monster.getRoads().clear();
		// monster.getSpace().moveTo(monster.getPosition3D(), monster.getInitPoint3d(), charseBackList);
		AStar2 star = new AStar2(monster.getSpace().getSpaceInfo(), 100);
		monster.setRoads(star.find(monster.getPosition3D(), monster.getInitPoint3d(), false));
		// monster.setRoads(AStar.findRoads(monster.getSpace().getSpaceInfo(), monster.getPosition3D(), monster.randomRoundGrid(monster.getSpace(), monster.getInitPoint3d(), 2), -1, false));
		if (!monster.getInitPoint3d().equal(monster.getRoads().get(monster.getRoads().size() - 1))) {
			monster.getRoads().add(monster.getInitPoint3d());
		}
		monster.sendMonsterMoveResponse(monster.getRoads(), null);
		targetPosition3d = null;
	}
	
	/**
	 * initIdle:(). <br/>
	 * TODO().<br/>
	 * 选中待机状态
	 * 
	 * @author lyh
	 */
	public void selectIdle() {
		if (moveAction == IDLE) {
			return;
		}
		moveAction = IDLE;
		waitMoveTime = System.currentTimeMillis() + pojo.getIdleLastTime();
	}
	
	/**
	 * selectPatrol:(). <br/>
	 * TODO().<br/>
	 * 选 中待机行为
	 * 
	 * @author lyh
	 */
	public void selectPatrol() {
		// if (moveAction == PATROL) {
		// return;
		// }
		moveAction = PATROL;
		this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
		// 确定巡逻点
		// 在巡逻范围内随机10个点
		calculatePatrol(monster.getPosition3D());
		// 设置目标
		IFighter fighter = (IFighter) monster.getNpcHatred().getMaxHatred(false);
		if (fighter != null && monster.isAtkType()) {
			monster.setTargetFighter(fighter);
			// 设置为追击状态
			selectChase(fighter);
		} else {
			monster.sendMonsterMoveResponse(monster.getRoads(), null);
		}
		
	}
	
	/**
	 * initPatrol:(). <br/>
	 * TODO().<br/>
	 * 计算巡逻路径列表
	 * 
	 * @author lyh
	 */
	public void calculatePatrol(Position3D curPos) {
		monster.getRoads().clear();// 表空以前的
		int range = this.pojo.getPatrolRange();
		// 计算目标点
		Position3D target = null;
		try {
			target = monster.getTargetPosition(monster.getInitPoint3d(), range, 0, pojo.getChaseRange());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 得到路径点
		// monster.setRoads(AStar.findRoads(monster.getSpace().getSpaceInfo(), curPos, target, -1, false));
		AStar2 star = new AStar2(monster.getSpace().getSpaceInfo(), 100);
		monster.setRoads(star.find(curPos, target, false));
	}
	
	/**
	 * randomAction:(). <br/>
	 * TODO().<br/>
	 * 根据机率巡逻和待机选一
	 * 
	 * @author lyh
	 */
	public void randomAction() {
		monster.getRoads().clear();
		targetPosition3d = null;
		int ar[] = { pojo.getIdleRate(), pojo.getPatrolRate() };
		int act[] = { IDLE, PATROL };
		int rate = pojo.getIdleRate() + pojo.getPatrolRate();
		int r = ServerRandomUtils.randomNum(0, rate);
		int tmp = 0;
		int i = 0;
		for (i = 0; i < ar.length; i++) {
			if (r > tmp && r < tmp + ar[i]) {
				break;
			} else {
				tmp += ar[i];
			}
		}
		
		if (i == 0) {
			selectIdle();
		} else {
			// 初始化巡逻
			selectPatrol();
		}
		
	}
	
	long lessTime = 0;
	
	@Override
	public void tick(long time) {
		try {
			// TODO Auto-generated method stub
			long curTime = System.currentTimeMillis();
			
			Long endTime = monster.getColdTimeMap().get(IFighter.SKILL_ANI_TYPE);
			if (endTime != null && endTime > curTime) {
				return;
			}
			
			if (waitMoveTime == 0) {
				waitMoveTime = curTime + pojo.getIdleLastTime();
			}
			
			// 在等待时间内返回
			if (waitMoveTime - lessTime >= curTime) {
				return;
			}
			
			lessTime = waitMoveTime - lessTime - curTime;
			if (lessTime < 0) {
				lessTime = 0;
			}
			if (moveAction == IDLE) {// 待机状态
				this.randomAction();// 转入其他状态
			} else if (moveAction == PATROL) {// 巡逻
				doPatrol();
			} else if (moveAction == CHASE) {// 追击
				doCharse();
			} else if (moveAction == ESCAPE) {// 逃跑
			
			} else if (moveAction == CHASE_BACK) {// 追击返回
				doCharseBack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("怪物行为有问题::", e);
		}
	}
	
	/**
	 * doPatrol:(). <br/>
	 * TODO().<br/>
	 * 处理巡逻，逻辑 1:每次只拿出第一个点
	 * 
	 * @author lyh
	 */
	/**
	 * doPatrol:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 */
	public void doPatrol() {
		if (monster.getRoads().size() > 0) {
			// 判断 是不是在警戒范围,
			int area[][] = monster.getSpace().getRoundAreas(monster.getPosition3D(), monster.getSpace().getSpaceInfo(), pojo.getSearchRange());
			for (int i = 0; i < area.length; i++) {
				for (int j = 0; j < area[i].length; j++) {
					IArea searchIArea = monster.getSpace().getAoi().getCellAtRowCol(area[i][j] % 1000, area[i][j] / 1000);
					boolean isFirst = true;
					for (Map.Entry<Long, IMapObject> map : searchIArea.getPlayers().entrySet()) {
						IMapObject obj = map.getValue();
						// 计算距离
						int distance = (int) obj.getPosition3D().getXZDistance(monster.getPosition3D());
						if (distance <= pojo.getSearchRange() && monster.isAtkType()) {
							monster.getNpcHatred().addHatred(obj, NpcHatred.addVal(isFirst), monster.isAtkType());
							isFirst = false;
						} else {
							
						}
					}
				}
			}
			
			// 设置目标
			IFighter fighter = (IFighter) monster.getNpcHatred().getMaxHatred(false);
			if (fighter != null && monster.isAtkType()) {
				monster.setTargetFighter(fighter);
				// 设置为追击状态
				selectChase(fighter);
			} else {
				// 没目标的情况下才巡逻
				this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
				Position3D tartgetPos = monster.getRoads().remove(0);
				if (Math.abs(tartgetPos.getX() - monster.getPosition3D().getX()) > 2f || Math.abs(tartgetPos.getZ() - monster.getPosition3D().getZ()) > 2f) {
					log.error(tartgetPos.getX() + ":" + tartgetPos.getY() + ":" + tartgetPos.getZ() + "::巡逻 坐标有问题::" + monster.getPosition3D().getX() + ":" + monster.getPosition3D().getY() + ":" + monster.getPosition3D().getZ());
				}
				monster.getSpace().move(monster, tartgetPos.getX(), tartgetPos.getY(), tartgetPos.getZ(), tartgetPos.getY() - monster.getPosition3D().getY(), true);
			}
		} else {
			this.randomAction();// 转入其他状态
		}
	}
	
	/**
	 * doCharse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 */
	public void doCharse() {
		// 在战斗中返回(做战斗时判断 )
		if (monster.getStatus() == MonsterStatus.STATUS_FIGHT) {// 正在战斗
			return;
		}
		
		IFighter target = monster.getTargetFighter();
		if (target == null) {// 再锁定一下怪
			target = (IFighter) monster.getNpcHatred().getMaxHatred(monster.isAtkType());
			if (target == null) {// 没有找到最大的仇恨怪 ,返回怪物出生点
				selectChaseBack();
				monsterAttackAction.selectReadyAttack();
				return;
			}
			monster.setTargetFighter(target);
		}
		
		double distance = target.getPosition3D().getXZDistance(monster.getInitPoint3d());
		if (distance >= pojo.getChaseRange()) {// 超出追击范围
			if (target != null) {
				monster.getNpcHatred().removeHatred(target);
			}
			monster.setTargetFighter(null);
			// 返回追击状态
			selectChaseBack();
			monsterAttackAction.selectReadyAttack();
			return;
		}
		
		// 选 中一个技能
		SkillTemplatePojo skillPojo = monster.getUseSkill().getSkillTemplatePojo();
		if (skillPojo.getSkillUnitPojo().length <= 0) {
			// 追击返回状态
			selectChaseBack();
			monsterAttackAction.selectReadyAttack();
			return;
		}
		
		// 判断技能攻击范围(如果当前距离与目标距离不在攻击范围,就执行追击)
		if (target.getPosition3D().getXZDistance(monster.getPosition3D()) < skillPojo.getSkillUnitPojo()[0].getAttackDistance()) {
			return;
		}
		
		if (monster.getRoads().size() == 0) {// 最后一个
			// 追击返回状态
			this.selectPatrol();
			monsterAttackAction.selectReadyAttack();
			return;
		}
		// 如果目标坐标已走动,重新算路径
		if (!targetPosition3d.equal(target.getPosition3D())) {
			monster.getRoads().clear();
			targetPosition3d = new Position3D(target.getPosition3D());
			
			AStar2 star = new AStar2(monster.getSpace().getSpaceInfo(), 100);
			monster.setRoads(star.find(monster.getPosition3D(), monster.randomRoundGrid(monster.getSpace(), targetPosition3d, 1), true));
			// monster.setRoads(AStar.findRoads(monster.getSpace().getSpaceInfo(), monster.getPosition3D(), monster.randomRoundGrid(monster.getSpace(), targetPosition3d, 2), -1, true));
			monster.sendMonsterMoveResponse(monster.getRoads(), targetPosition3d);
			return;
		}
		// 开始追击
		Position3D tartgetPos = monster.getRoads().remove(0);
		if (Math.abs(tartgetPos.getX() - monster.getPosition3D().getX()) > 2f || Math.abs(tartgetPos.getZ() - monster.getPosition3D().getZ()) > 2f) {
			log.error(tartgetPos.getX() + ":" + tartgetPos.getY() + ":" + tartgetPos.getZ() + "::追击 坐标有问题::" + monster.getPosition3D().getX() + ":" + monster.getPosition3D().getY() + ":" + monster.getPosition3D().getZ());
		}
		monster.getSpace().move(monster, tartgetPos.getX(), tartgetPos.getY(), tartgetPos.getZ(), tartgetPos.getY() - monster.getPosition3D().getY(), true);
		this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
		
		// if (monster.getStatus() == MonsterStatus.STATUS_DIE || monster.getStatus() == MonsterStatus.STATUS_DIEWAIT || monster.getStatus() == MonsterStatus.STATUS_RUNBACK || monster.getStatus() ==
		// MonsterStatus.STATUS_DIEING) {
		// return;
		// }
	}
	
	/**
	 * doCharseBack:(). <br/>
	 * TODO().<br/>
	 * 处理追击返回
	 * 
	 * @author lyh
	 */
	public void doCharseBack() {
		if (monster.getRoads().size() == 0) {// 最后一个
			// 追击返回状态
			this.randomAction();
			return;
		}
		
		Position3D tartgetPos = monster.getRoads().remove(0);
		if (Math.abs(tartgetPos.getX() - monster.getPosition3D().getX()) > 2f || Math.abs(tartgetPos.getZ() - monster.getPosition3D().getZ()) > 2f) {
			log.error(tartgetPos.getX() + ":" + tartgetPos.getY() + ":" + tartgetPos.getZ() + "::追击 返回坐标有问题::" + monster.getPosition3D().getX() + ":" + monster.getPosition3D().getY() + ":" + monster.getPosition3D().getZ());
		}
		monster.getSpace().move(monster, tartgetPos.getX(), tartgetPos.getY(), tartgetPos.getZ(), tartgetPos.getY() - monster.getPosition3D().getY(), true);
		this.waitMoveTime = System.currentTimeMillis() + pojo.getPatrolInterval();
	}
	
	public MonsterAttackAction getMonsterAttackAction() {
		return monsterAttackAction;
	}
	
	public void setMonsterAttackAction(MonsterAttackAction monsterAttackAction) {
		this.monsterAttackAction = monsterAttackAction;
	}
	
}
