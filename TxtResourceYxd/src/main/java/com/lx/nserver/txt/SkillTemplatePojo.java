package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 技能总表
 **/
public class SkillTemplatePojo {
	
	public SkillTemplatePojo() {
	}
	
	/** 技能ID **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 技能名称 **/
	@TxtString
	private String name;
	
	/** 类别 1、伤害技能；2、状态技能；3、被动技能 **/
	@TxtInt
	private int type;
	
	/** 学习等级 **/
	@TxtInt
	private int learnLevel;
	
	/** 学习前置技能及等级 **/
	@TxtString
	private String preLearnSkill;
	
	/** 技能最大等级 **/
	@TxtInt
	private int maxLevel;
	
	/** 学习消耗技能点 **/
	@TxtInt
	private int learnCost;
	
	
	/** 职业 **/
	@TxtInt
	private int careerId;
	
	/** 技能目标类型 1、自己；2、队友（包括自己）；3、敌人 **/
	@TxtInt
	private int fireTargetType;
	
	/** 技能cd时间(毫秒) **/
	@TxtInt
	private int cdTime;
	
	/** 施放技能消耗 **/
	@TxtInt
	private int cost;
	
	/** 限制场景ID **/
	@TxtString
	private String limitSceneId;
	
	/** 被动作用属性 **/
	@TxtString
	private String passiveAttr;
	
	/** 单元技能id **/
	@TxtString
	private String unitId;
	private SkillUnitPojo skillUnitPojo[] = new SkillUnitPojo[0];
	/**技能动画播放时间(毫秒)**/
	@TxtInt
	private int animationTime;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int _type) {
		type = _type;
	}
	
	public int getLearnLevel() {
		return learnLevel;
	}
	
	public void setLearnLevel(int _learnLevel) {
		learnLevel = _learnLevel;
	}
	
	public String getPreLearnSkill() {
		return preLearnSkill;
	}
	
	public void setPreLearnSkill(String _preLearnSkill) {
		preLearnSkill = _preLearnSkill;
	}
	
	public int getMaxLevel() {
		return maxLevel;
	}
	
	public void setMaxLevel(int _maxLevel) {
		maxLevel = _maxLevel;
	}
	
	public int getLearnCost() {
		return learnCost;
	}
	
	public void setLearnCost(int _learnCost) {
		learnCost = _learnCost;
	}
	
	
	public int getCareerId() {
		return careerId;
	}
	
	public void setCareerId(int _careerId) {
		careerId = _careerId;
	}
	
	public int getFireTargetType() {
		return fireTargetType;
	}
	
	public void setFireTargetType(int _fireTargetType) {
		fireTargetType = _fireTargetType;
	}
	
	public int getCdTime() {
		return cdTime;
	}
	
	public void setCdTime(int _cdTime) {
		cdTime = _cdTime;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int _cost) {
		cost = _cost;
	}
	
	public String getLimitSceneId() {
		return limitSceneId;
	}
	
	public void setLimitSceneId(String _limitSceneId) {
		limitSceneId = _limitSceneId;
	}
	
	public String getPassiveAttr() {
		return passiveAttr;
	}
	
	public void setPassiveAttr(String _passiveAttr) {
		passiveAttr = _passiveAttr;
	}
	
	public String getUnitId() {
		return unitId;
	}
	
	public void setUnitId(String _unitId) {
		unitId = _unitId;
	}
	public int getAnimationTime(){
		return animationTime;
	}

	public void setAnimationTime(int _animationTime){
		animationTime= _animationTime;
	}
	
	public SkillUnitPojo[] getSkillUnitPojo() {
		return skillUnitPojo;
	}
	
	public void setSkillUnitPojo(SkillUnitPojo[] skillUnitPojo) {
		this.skillUnitPojo = skillUnitPojo;
	}
	
	/**
	 * getSkillUnitPojoById:(). <br/>
	 * TODO().<br/>
	 * 获得技能伤害单元
	 * 
	 * @author lyh
	 * @param id
	 * @return
	 */
	public SkillUnitPojo getSkillUnitPojoById(int id) {
		for (SkillUnitPojo sup : skillUnitPojo) {
			if (sup.getId() == id) {
				return sup;
			}
		}
		return null;
	}
	
}