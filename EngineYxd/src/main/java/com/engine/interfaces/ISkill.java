package com.engine.interfaces;

import com.lx.nserver.txt.SkillTemplatePojo;

/**
 * ClassName:ISkill <br/>
 * Function: TODO (技能接口). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-14 下午2:12:01 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface ISkill {
	
	/*** 1、自己；2、队友（包括自己）；3、敌人 **/
	/** 目标类型=1、自己 **/
	public static final int SKILL_TARGET_OWN = 1;
	/** 目标类型=2、队友（包括自己 **/
	public static final int SKILL_TARGET_TEAM = 2;
	/** 目标类型=3、敌人 **/
	public static final int SKILL_TARGET_ENEMY = 3;
	
	/************** 技能类型 ******************/
	/*** 1、伤害技能；2、状态技能；3、被动技能 **/
	public static final int SKILL_TYPE_DAMAGE = 1;
	public static final int SKILL_TYPE_STATUS = 2;
	public static final int SKILL_TYPE_PASSIVITY = 3;
	
	/*************** 伤害类型 ******************/
	/*** 1、物理；2、法术；3、火；4、冰；5、毒；6、电 ***/
	
	/** 1物理攻击 外功攻击 是 **/
	public static final int DAMAGE_TYPE_EXTERNAL_FORCE_ATTACK = 1;
	
	/*** 2 法术攻击 内功攻击 是 **/
	public static final int DAMAGE_TYPE_INNER_FORCE_ATTACK = 2;
	/** 3 火焰攻击 烈阳攻击 是 **/
	public static final int DAMAGE_TYPE_SCORCHING_SUN_ATTACK = 3;
	/*** 4 雷电攻击 天罡攻击 是 **/
	public static final int DAMAGE_TYPE_PLOUGH_ATTACK = 4;
	
	/** 5 毒素攻击 幽冥攻击 是 **/
	public static final int DAMAGE_TYPE_NETHER_ATTACK = 5;
	
	/** 6 冰冻攻击 太阴攻击 是 **/
	public static final int DAMAGE_TYPE_LUNAR_ATTACK = 6;
	
	/** 7治疗 **/
	public static final int DAMAGE_TYPE_ADD_HP = 7;
	
	/************** 技能消耗类型 *******************/
	/** 0,表示不消耗 **/
	public final static int COST_NONE = 0;
	/** 1、法力值； **/
	public final static int COST_MP = 1;
	/** 2、生命值； **/
	public final static int COST_HP = 2;
	/** 3、怒气值 **/
	public final static int COST_ANGER = 3;
	/**************************************/
	/************ 1单体，2矩形，3扇形，4圆形 **************/
	/** 单体 **/
	public final static int ATT_SINGLE = 1;
	/** 矩形 **/
	public final static int ATT_RECTANG = 2;
	/** 扇形 **/
	public final static int ATT_SECTOR = 3;
	/** 圆形 **/
	public final static int ATT_ROUND = 4;
	
	/**************************/
	
	/**
	 * getId:(). <br/>
	 * TODO().<br/>
	 * 技能 id
	 * 
	 * @author lyh
	 * @return
	 */
	public long getId();
	
	/**
	 * getSkillTemplatePojo:(). <br/>
	 * TODO().<br/>
	 * 获得技能配置文件对象
	 * 
	 * @author lyh
	 * @return
	 */
	public SkillTemplatePojo getSkillTemplatePojo();
	
	/**
	 * setSkillTemplatePojo:(). <br/>
	 * TODO().<br/>
	 * 设置
	 * 
	 * @author lyh
	 * @param pSkillTemplatePojo
	 */
	public void setSkillTemplatePojo(SkillTemplatePojo pSkillTemplatePojo);
	
	
}
