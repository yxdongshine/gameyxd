package com.lx.game.monster.action;

import com.engine.entityobj.IFighter;
import com.engine.interfaces.ITickable;
import com.lx.game.entity.Monster;
import com.lx.nserver.txt.SkillTemplatePojo;

/**
 * ClassName:MonsterAttackAction <br/>
 * Function: TODO (怪物行为). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-3 下午2:17:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MonsterAttackAction implements ITickable {
	
	/** 被动 */
	public final static int PASSIVE = 1;
	
	/** 准备战斗 */
	public final static int READY_ATTACK = 2;
	
	/** 攻击 */
	public final static int ATTACK = 3;
	
	/** 逃跑 */
	public final static int ESCAPE = 4;
	
	/** 呼救 */
	public final static int SOS = 5;
	
	/** 攻击行为 **/
	private int attkAction;
	
	private Monster monster;
	private MonterMoveAction monterMoveAction;
	
	/** 等待时间 **/
	private long waitTime = 0;//
	/** 默认时间 **/
	private int defaultTime = 150;
	/** 使用技能次数 **/
	private int useSkillCount = 0;
	
	public MonsterAttackAction(Monster mon, MonterMoveAction move) {
		monster = mon;
		monterMoveAction = move;
		selectReadyAttack();
	}
	
	/**
	 * selectReadyAttack:(). <br/>
	 * TODO().<br/>
	 * 时刻准备战斗
	 * 
	 * @author lyh
	 */
	public void selectReadyAttack() {
		if (!monster.isAtkType()) {
			return;
		}
		if (attkAction == READY_ATTACK) {
			return;
		} else {
			attkAction = READY_ATTACK;
		}
		waitTime = System.currentTimeMillis() + defaultTime;
		
	}
	
	/**
	 * doReadyAttack:(). <br/>
	 * TODO().<br/>
	 * 准备攻击
	 * 
	 * @author lyh
	 */
	public void doReadyAttack() {
		// 取得最大仇恨值的目标
		IFighter target = (IFighter) monster.getNpcHatred().getMaxHatred(monster.isAtkType());
		if (target == null) {
			return;
		}
		monster.setTargetFighter(target);
		// 移动行为变成追击
		monterMoveAction.selectChase(target);
		attkAction = ATTACK;
		// 这儿应该是技能的攻击时间间隔
		waitTime = System.currentTimeMillis() + defaultTime;
	}
	
	long lessTime = 0;
	
	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		long beginTime = System.currentTimeMillis();
		if (waitTime - lessTime >= beginTime) {
			return;
		}
		lessTime = waitTime - lessTime - beginTime;
		if (lessTime < 0) {
			lessTime = 0;
		}
		if (waitTime == 0) {
			waitTime = beginTime + defaultTime;
		}
		
		if (attkAction == READY_ATTACK) {
			this.doReadyAttack();
		} else if (attkAction == ATTACK) {
			doAttack();
		}
		
	}
	
	public void doAttack() {
		waitTime = System.currentTimeMillis() + defaultTime;
		IFighter target = monster.getTargetFighter();
		if (target == null) {// 再锁定一下怪
			target = (IFighter) monster.getNpcHatred().getMaxHatred(monster.isAtkType());
			if (target == null) {// 没有找到最大的仇恨怪 ,返回怪物出生点
				monterMoveAction.selectChaseBack();
				selectReadyAttack();
				// this.npc.npcAtkAct.returned() ;
				// this.chaseBack();
				return;
			}
			monster.setTargetFighter(target);
		}
		
		// 目标已死
		if (target.isDie()) {
			monterMoveAction.selectChaseBack();
			monster.getNpcHatred().removeHatred(target);
			return;
		}
		
		// 定身或者睡眠中
		
		// 选 中一个技能
		SkillTemplatePojo skillPojo = monster.getUseSkill().getSkillTemplatePojo();
		if (skillPojo.getSkillUnitPojo().length <= 0) {// 技能不存在
			monterMoveAction.selectChaseBack();
			monster.getNpcHatred().removeHatred(target);
			return;
		}
		
		
		// 不在动画冷却时间内
		Long endTime = monster.getColdTimeMap().get(IFighter.SKILL_ANI_TYPE);
		if (endTime != null &&  endTime > System.currentTimeMillis()) {
			return;
		}
		
//		endTime = monster.getColdTimeMap().get(IFighter.SKILL_COLD_TYPE);
//		if (endTime != null &&  endTime > System.currentTimeMillis()) {
//			return;
//		}
		
		// 判断技能攻击范围(如果当前距离与目标距离不在攻击范围,就执行追击)
		if (target.getPosition3D().getXZDistance(monster.getPosition3D()) >= skillPojo.getSkillUnitPojo()[0].getAttackDistance()) {
			// 要在攻击距离内
			monterMoveAction.selectChase(target);
			return;
		}
		
		// 发送停止移动消息
		monster.sendMonsterStopResponse(target.getPosition3D(), target.getId());
		monster.getRoads().clear();
		// // 打的时候是战斗状态
		// monster.setStatus(MonsterStatus.STATUS_FIGHT);
		// 战斗
		monster.getIFightListener().monsterFight(monster, target, monster.getUseSkill());
		System.err.println(monster.getPosition3D().getX() +"::"+monster.getPosition3D().getZ() +"::怪物:::"+monster.getName() + "::打角色::"+target.getName());
		
		monster.getColdTimeMap().put(IFighter.SKILL_COLD_TYPE, System.currentTimeMillis() + skillPojo.getCdTime());
		monster.getColdTimeMap().put(IFighter.SKILL_ANI_TYPE, System.currentTimeMillis() + skillPojo.getAnimationTime());
		// // 正常状态
		// monster.setStatus(MonsterStatus.STATUS_NORMAL);
		if (useSkillCount >= 3) {
			useSkillCount = 0;
			monster.setUseSkill(monster.randomSkill());
		} else {
			useSkillCount++;
			int skillId = monster.getMonsterPojo().getSkills()[0];
			monster.setUseSkill(monster.getSkillById(skillId));
		}
		monster.setTargetFighter(null);
		// 目标已死
		if (target.isDie()) {
			monster.getNpcHatred().clearZeroHatred(target, false);
			// 重新锁定目标
			target = (IFighter) monster.getNpcHatred().getMaxHatred(monster.isAtkType());
			if (target == null) {
				monterMoveAction.selectChaseBack();
				selectReadyAttack();
			} else {
				monterMoveAction.doCharse();
			}
		}
		
	}
	
}
