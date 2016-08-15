package com.lx.nserver.monster.action;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.lib.utils.ToolUtils;

/**
 * ClassName:RageAction <br/>
 * Function: TODO (boss暴怒行为). <br/>
 * Reason: TODO ( 进入暴怒的触发条件 达到条件时，怪物进入暴怒状态 HPBelow int（百分比） 怪物自身HP少于设定值触发 AlliesDead int 自己索敌范围内友军死亡数量 退出暴怒的触发条件 达到条件时，怪物退出暴怒状态 HPAbove int（百分比） 怪物自身HP多于设定值触发 【暴怒】状态持续时间（秒） 【暴怒】状态冷却时间（秒） 暴怒特效 美术特效，怒气等
 * 
 * "游击行为数量，int， 可添加多个游击行为" 可添加多个游击行为 内容和激活状态下的Guerrilla行为一致 内容和激活状态下的HateTrans行为一致 ). <br/>
 * Date: 2015-7-27 下午5:29:16 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class RageAction {
	/** 怪物进入暴怒状态 HPBelow int（百分比） 怪物自身HP少于设定值触发 **/
	public static final int TRIGGER_IN_TYPE_HPBELOW = 1;
	/** AlliesDead int 自己索敌范围内友军死亡数量 退出暴怒的触发条件 达到条件时 **/
	public static final int TRIGGER_IN_TYPE_ALLIESDEAD = 2;
	/** 怪物退出暴怒状态 HPAbove int（百分比） 怪物自身HP多于设定值触发 **/
	public static final int TRIGGER_OUT_TYPE_HPABOVE = 3;
	/**
	 * 进入暴怒的触发条件 达到条件时 ，怪物进入暴怒状态 HPBelow int（百分比） 怪物自身HP少于设定值触发 AlliesDead int 自己索敌范围内友军死亡数量 退出暴怒的触发条件 达到条件时，
	 * 
	 * 怪物退出暴怒状态 HPAbove int（百分比） 怪物自身HP多于设定值触发 【暴怒】状态持续时间（秒） 【暴怒】状态冷却时间（秒） 暴怒特效 美术特效，怒气等
	 * 
	 * "游击行为数量，int， 可添加多个游击行为" 可添加多个游击行为 内容和激活状态下的Guerrilla行为一致 内容和激活状态下的HateTrans行为一致
	 **/
	
	/** 触发进入类型 **/
	private int triggerInType;
	
	/** 解发值 **/
	private int triggerInval;
	/** 触发退出类型 **/
	private int triggerOutType;
	/** 触发退出传下 **/
	private int triggerOutVal;
	/** 行为持续时间 **/
	private int lastTime;
	/** 行为cd时间 **/
	private int cdTime;
	/** 行为光效 **/
	private String effect;
	/** 攻击行为 **/
	private ArrayList<AtkActions> atkActions = new ArrayList<AtkActions>();
	/** 仇恨行为 **/
	private HateAction hateAction;
	
	public int getTriggerInType() {
		return triggerInType;
	}
	
	public void setTriggerInType(int triggerInType) {
		this.triggerInType = triggerInType;
	}
	
	public int getTriggerInval() {
		return triggerInval;
	}
	
	public void setTriggerInval(int triggerInval) {
		this.triggerInval = triggerInval;
	}
	
	public int getTriggerOutType() {
		return triggerOutType;
	}
	
	public void setTriggerOutType(int triggerOutType) {
		this.triggerOutType = triggerOutType;
	}
	
	public int getTriggerOutVal() {
		return triggerOutVal;
	}
	
	public void setTriggerOutVal(int triggerOutVal) {
		this.triggerOutVal = triggerOutVal;
	}
	
	public int getLastTime() {
		return lastTime;
	}
	
	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}
	
	public int getCdTime() {
		return cdTime;
	}
	
	public void setCdTime(int cdTime) {
		this.cdTime = cdTime;
	}
	
	public String getEffect() {
		return effect;
	}
	
	public void setEffect(String effect) {
		this.effect = effect;
	}
	
	public HateAction getHateAction() {
		return hateAction;
	}
	
	public void setHateAction(HateAction hateAction) {
		this.hateAction = hateAction;
	}
	
	public ArrayList<AtkActions> getAtkActions() {
		return atkActions;
	}
	
	public void setAtkActions(ArrayList<AtkActions> atkActions) {
		this.atkActions = atkActions;
	}
}
