package com.lx.nserver.monster.action;

/**
 * ClassName:AtkActions <br/>
 * Function: TODO (攻击行为，json格式). <br/>
 * Trigger 触发条件 当任一行为结束后，检测非CD中并且带触发条件的所有行为，在所有符合触发条件的行为内按执行几率计算出1个行为并执行。 Trigger的选项，暂定1个trigger只能选择1个触发条件 none 不使用触发条件 HPAbove int（百分比） 怪物自身HP多于设定值触发 HPBelow int（百分比） 怪物自身HP少于设定值触发 AddDebuff int
 * 自己和索敌范围内的友军，被添加了debuff的总数量（指自己+友军的数量，不是debuff的数量） AlliesDead int 自己索敌范围内友军死亡数量 AlliesHP int（百分比） 自己索敌范围内任一友军HP百分比低于设定值 EnemyNum int 攻击范围内的敌人数量 Probability 执行几率（int百分比） 行为被执行的几率
 * 
 * KeepDistance 保持距离（int） 怪物与目标的距离过于偏离这个值时，自动移动到这个距离，然后使用技能 Tolerance 距离容差（int） 当实际距离在（距离±距离容差）之内时，视为保持距离成功，开始释放技能 SkillDelay 释放技能延迟（秒） 保持距离成功后，等待多长时间开始释放技能 Skill 技能id
 * 使用该id的技能，可选none和怪物基本属性中skilllist内的技能 SkillInterval 技能使用间隔（秒） 每隔多少秒使用1次技能 SkillAniSpeed 技能动画播放速度（百分比或倍率） SkillTimes 当前行为内使用该技能次数,"达到次数后退出当前行为，默认1" Last "经过此时间后退出当前行为，在此时间内，不执行其他行为，
 * 保持距离作为持续性行为，会一直进行保持距离的动作直到可以释放技能或行为结束" cd Reason: TODO (). <br/>
 * Date: 2015-7-27 下午5:21:42 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class AtkActions {
	// {"triggerType":1,"triggerVal":10,"rate":100,"keepDistance":5,"tolerance":1,"skillDelay":1,"skillId":11001,"skillInterval":2,"skillAniSpeed":10,"skillTimes":1,"lastTime":100,"cdTime:10"
	/** 不使用触发条件 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_NONE = 0;
	/** 怪物自身HP多于设定值触发 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_HPABOVE = 1;
	/** 怪物自身HP少于设定值触发 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_HPBELOW = 2;
	/** 自己和索敌范围内的友军，被添加了debuff的总数量（指自己+友军的数量，不是debuff的数量） **/
	public static final int MONSTER_AI_TRIGGER_TYPE_ADDDEBUFF = 3;
	/** 自己索敌范围内友军死亡数量 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_AlLIESDEAD = 4;
	/** 自己索敌范围内任一友军HP百分比低于设定值 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_AlLIESHP = 5;
	/** 攻击范围内的敌人数量 **/
	public static final int MONSTER_AI_TRIGGER_TYPE_ENEMYNUM = 6;
	
	/**
	 * none 不使用触发条件 HPAbove int（百分比） 怪物自身HP多于设定值触发 HPBelow int（百分比） 怪物自身HP少于设定值触发 AddDebuff int 自己和索敌范围内的友军，被添加了debuff的总数量（指自己+友军的数量，不是debuff的数量） AlliesDead int 自己索敌范围内友军死亡数量 AlliesHP int（百分比）
	 * 自己索敌范围内任一友军HP百分比低于设定值 EnemyNum int 攻击范围内的敌人数量
	 **/
	/** 触发条件 **/
	private int triggerType;
	/** 触发值 **/
	private int triggerVal;
	/** 执行几率（int百分比） 行为被执行的几率 **/
	private int rate;
	/** 保持距离（int） 怪物与目标的距离过于偏离这个值时，自动移动到这个距离，然后使用技能 **/
	private int keepDistance;
	/** 距离容差（int） 当实际距离在（距离±距离容差）之内时，视为保持距离成功，开始释放技能 **/
	private int tolerance;
	/** 释放技能延迟（秒）保持距离成功后，等待多长时间开始释放技能 **/
	private int skillDelay;
	/** 使用该id的技能，可选none和怪物基本属性中skilllist内的技能 **/
	private int skillId;
	/** 技能使用间隔（秒） 每隔多少秒使用1次技能 **/
	private int skillInterval;
	/** 技能动画播放速度（百分比或倍率） **/
	private int skillAniSpeed;
	/** 当前行为内使用该技能次数,"达到次数后退出当前行为，默认1" **/
	private int skillTimes;
	/**
	 * "经过此时间后退出当前行为，在此时间内，不执行其他行为， 保持距离作为持续性行为，会一直进行保持距离的动作直到可以释放技能或行为结束"
	 **/
	private int lastTime;
	/** 行为冷却时间, **/
	private int cdTime;
	
	public int getTriggerType() {
		return triggerType;
	}
	
	public void setTriggerType(int triggerType) {
		this.triggerType = triggerType;
	}
	
	public int getTriggerVal() {
		return triggerVal;
	}
	
	public void setTriggerVal(int triggerVal) {
		this.triggerVal = triggerVal;
	}
	
	public int getRate() {
		return rate;
	}
	
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getKeepDistance() {
		return keepDistance;
	}
	
	public void setKeepDistance(int keepDistance) {
		this.keepDistance = keepDistance;
	}
	
	public int getTolerance() {
		return tolerance;
	}
	
	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}
	
	public int getSkillDelay() {
		return skillDelay;
	}
	
	public void setSkillDelay(int skillDelay) {
		this.skillDelay = skillDelay;
	}
	
	public int getSkillId() {
		return skillId;
	}
	
	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}
	
	public int getSkillInterval() {
		return skillInterval;
	}
	
	public void setSkillInterval(int skillInterval) {
		this.skillInterval = skillInterval;
	}
	
	public int getSkillAniSpeed() {
		return skillAniSpeed;
	}
	
	public void setSkillAniSpeed(int skillAniSpeed) {
		this.skillAniSpeed = skillAniSpeed;
	}
	
	public int getSkillTimes() {
		return skillTimes;
	}
	
	public void setSkillTimes(int skillTimes) {
		this.skillTimes = skillTimes;
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
}
