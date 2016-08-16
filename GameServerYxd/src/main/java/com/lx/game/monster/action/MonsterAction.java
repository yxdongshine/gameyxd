package com.lx.game.monster.action;

import java.util.Map;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.ITickable;
import com.loncent.protocol.game.monster.Monster.MonsterCreateResonse;
import com.loncent.protocol.game.monster.Monster.MonsterData;
import com.lx.game.entity.Monster;
import com.lx.game.send.MessageSend;

/**
 * ClassName:MonsterAction <br/>
 * Function: TODO (怪物行为). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-31 下午5:50:47 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MonsterAction implements ITickable {
	
	/** 存活 */
	public final static int LIVE = 1;
	
	/** 等待 */
	public final static int WAIT = 2;
	
	/** 死亡 */
	public final static int DEATH = 3;
	
	/** 修正战斗时间 */
	public final static int CORRECTION = 4;
	
	/** 苏醒 */
	public final static int REVIVE = 5;
	
	/** 沉睡 */
	public final static int SUNK_SLEEP = 6;
	
	// /** 休闲状态(默认状态) */
	// public final static int IDLE_ACTION = 0;
	//
	// /** 激活状态 */
	// public final static int ACTIVED_ACTION = 1;
	
	/** 行为 **/
	private int action = 0;
	
	/** 怪物移动行为 **/
	private MonterMoveAction moveAction;
	
	/** 怪物攻击行为 **/
	private MonsterAttackAction atkAction;
	
	private Monster monster;
	/** 间隔时间 **/
	private long waitActionTime;
	
	public MonsterAction(Monster mon) {
		monster = mon;
		moveAction = new MonterMoveAction(monster);
		atkAction = new MonsterAttackAction(monster, moveAction);
		moveAction.setMonsterAttackAction(atkAction);
		action = LIVE;
	}
	
	public int getAction() {
    	return action;
    }



	public void setAction(int action) {
    	this.action = action;
    }



	/**
	 * refreshDefualtStatus:(). <br/>
	 * TODO().<br/>
	 * 刷新状态
	 * 
	 * @author lyh
	 * @param time
	 */
	public void refreshDefualtStatus(long time) {
		// this.npc.npcMovAct.initMoveState();
		// if (this.npc.npcMovAct.moveToRush != null) {
		// this.npc.npcMovAct.moveToRush();
		// return;
		// }
		// if (this.npc.npcStatus.isPatrol()) {
		// this.npc.npcMovAct.patrol();
		// } else {
		// this.npc.npcMovAct.walk();
		// }
		// this.npc.npcAtkAct.returned();
		// this.isCorrection = true;
		// this.npc.setWaitMoveTime(time + 2000);
	}
	
	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		long beginTime = System.currentTimeMillis();
		if (action == LIVE) {// 存活
			doLive(beginTime);
		} else if (action == WAIT) {// 等待状态,复活
			doWait();
		} else if (action == DEATH) {// 死亡,没有移除
			doDead();
		}
	}
	
	/** 
	 * setDeathAction:(). <br/> 
	 * TODO().<br/> 
	 * 死亡状态
	 * @author lyh  
	 */  
	public void setDeathAction(){
		action = DEATH;
	}
	
	/**
	 * doWait:(). <br/>
	 * TODO().<br/>
	 * 死亡复活等待
	 * 
	 * @author lyh
	 */
	public void doWait() {
		long time = System.currentTimeMillis();
		if (waitActionTime >= time) {// 可以让他复活了
			// 血和重标重置
			monster.setHp(monster.getMonsterPojo().getHp());
			monster.setX(monster.getInitPoint3d().getX());
			monster.setY(monster.getInitPoint3d().getY());
			monster.setZ(monster.getInitPoint3d().getZ());
			monster.getSpace().getMonsterMaps().put(monster.getId(), monster);
		
			monster.getSpace().getMonsterDeadMaps().remove(monster.getId());
			action = LIVE;
			monster.getSpace().addToMap(monster);
			monster.getSpace().doMapObjectAddView(monster, monster.getArea().getNineArea());
			sendMonsterCreateResonse();
		}
	}
	
	/** 
	 * sendMonsterCreateResonse:(). <br/> 
	 * TODO().<br/> 
	 * 发送重生的怪物
	 * @author lyh  
	 */  
	public void sendMonsterCreateResonse(){
		MonsterCreateResonse resp = MonsterCreateResonse.newBuilder().setMonsterData(createMonsterData(monster)).build();
		for (Map.Entry<Long, IMapObject> entry : monster.getPlayerViewMap().entrySet()){
			ServerPlayer sp = (ServerPlayer)entry.getValue();
			MessageSend.sendToGate(resp, sp);
		}
	}
	
	/**
	 * createMonsterData:(). <br/>
	 * TODO().<br/>
	 * 创建怪物数据
	 * 
	 * @author lyh
	 * @param monster
	 * @return
	 */
	public MonsterData createMonsterData(AbsMonster monster) {
		MonsterData.Builder data = MonsterData.newBuilder();
		data.setHp(monster.getHp());
		data.setMaxHp(monster.getMaxHp());
		data.setMonsterId(monster.getId());
		data.setSpeed(monster.getAttribute().getAttribute(Attribute.SPEED));
		data.setX(monster.getPosition3D().getX());
		data.setY(monster.getPosition3D().getY());
		data.setZ(monster.getPosition3D().getZ());
		data.setFace(monster.getDir());
		data.setMonsterConfigId(monster.getMonsterPojo().getId());
		return data.build();
	} 
	
	
	/**
	 * doDead:(). <br/>
	 * TODO().<br/>
	 * 死亡,尸体存放时间
	 * 
	 * @author lyh
	 */
	public void doDead() {
		long time = System.currentTimeMillis();
		if (waitActionTime >= time) {
			// 设置移除
			monster.getSpace().getMonsterDeadMaps().put(monster.getId(), monster);
//			monster.getSpace().getMonsterMaps().remove(monster.getId());
			monster.getSpace().exitMap(monster);
			waitActionTime = System.currentTimeMillis() + monster.getMonsterPojo().getRebornTime();
			action = WAIT;
		}
	}
	
	
	/**
	 * selectDeadAction:(). <br/>
	 * TODO().<br/>
	 * 死亡状态
	 * 
	 * @author lyh
	 */
	public void selectDeadAction() {
		if (action == DEATH) {
			return;
		}
		action = DEATH;
		waitActionTime = System.currentTimeMillis() + monster.getMonsterPojo().getCorpseTime();
	}
	
	/**
	 * doLive:(). <br/>
	 * TODO().<br/>
	 * 处理存活时的状态
	 * 
	 * @author lyh
	 */
	public void doLive(long time) {
		moveAction.tick(time);
		atkAction.tick(time);
	}

	public MonterMoveAction getMoveAction() {
    	return moveAction;
    }

	public void setMoveAction(MonterMoveAction moveAction) {
    	this.moveAction = moveAction;
    }

	public MonsterAttackAction getAtkAction() {
    	return atkAction;
    }

	public void setAtkAction(MonsterAttackAction atkAction) {
    	this.atkAction = atkAction;
    }
	
}
