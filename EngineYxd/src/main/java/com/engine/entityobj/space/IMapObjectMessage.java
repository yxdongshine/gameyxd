package com.engine.entityobj.space;

import java.util.List;

import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff;
import com.loncent.protocol.game.pbbuff.Buff.BuffActionUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffStatusUpdateResponse;

/**
 * ClassName:IMapObjectListener <br/>
 * Function: TODO (对象监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-28 下午8:12:03 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IMapObjectMessage {
	
	/**
	 * init:(). <br/>
	 * TODO().<br/>
	 * 传一个执行对象
	 * 
	 * @author lyh
	 * @param obj
	 */
	public void init(IMapObject obj);
	
	/** 在视野中增加一个元素 */
	public void addView(IMapObject obj);
	
	/** 在视野中增加一组元素 */
	public void addView(List<IMapObject> objects);
	
	/** 一个元素从视野中离开 */
	public void leftView(IMapObject obj);
	
	/** 一组元素从视野中离开 */
	public void leftView(List<IMapObject> objects);
	
	/**
	 * moveView:(). <br/>
	 * TODO().<br/>
	 * 对象移动
	 * 
	 * @author lyh
	 * @param x
	 * @param y
	 * @param z
	 * @param dir
	 */
	public void moveView(IMapObject moveObject);
	
	/***
	 * 角色释放技能 doAction:(). <br/>
	 */
	void doAction(IMapObject obj, int skillId, int skillUintId, PbPosition3D pos, PbPosition3D rotation,PbPosition3D attackerMove);
	
	void hurtAction(BattleHurtUpdateResponse.Builder hurtUpdateMsg);
	
	void buffAction(BuffActionUpdateResponse.Builder builder);
	
	void buffStatus(BuffStatusUpdateResponse.Builder builder);
}
