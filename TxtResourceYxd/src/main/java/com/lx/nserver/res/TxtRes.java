package com.lx.nserver.res;

import com.lib.res.ResPath;

/**
 * ClassName:TxtRes <br/>
 * Function: TODO (txt文件路径类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:39:22 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface TxtRes extends ResPath {
	public static final String TXT = ".txt";
	public static final String TTXT = RES + "txt";
	/** 职业 **/
	public static final String CAREER_TXT = TTXT + SEPARATE + "Career" + TXT;
	/** 职业资质 **/
	public static final String CAREER_TALENT_TXT = TTXT + SEPARATE + "CareerTalent" + TXT;
	/** 属性配置表 **/
	public static final String ATTRIBUTE_TXT = TTXT + SEPARATE + "Attribute" + TXT;
	/** 属性说明配置表 **/
	public static final String ATTRIBUTE_DESC_TXT = TTXT + SEPARATE + "AttributeDesc" + TXT;
	/** 角色初始化配置表 **/
	public static final String ROLE_INIT_TXT = TTXT + SEPARATE + "RoleInit" + TXT;
	/** 角色升级配置表 **/
	public static final String ROLE_LEVEL_UP_TXT = TTXT + SEPARATE + "RoleLevelUp" + TXT;
	
	/** 怪物组配置表 **/
	public static final String MONSTER_GROUP_TXT = TTXT + SEPARATE + "MonsterGroup" + TXT;
	/** 怪物配置表 **/
	public static final String MONSTER_TXT = TTXT + SEPARATE + "Monster" + TXT;
	
	/** 怪物行为配置表 **/
	public static final String MONSTER_ACTION_TXT = TTXT + SEPARATE + "MonsterAction" + TXT;
	
	/** 技能模板配置表 **/
	public static final String SKILL_TEMPLATE_TXT = TTXT + SEPARATE + "SkillTemplate" + TXT;
	
	/** buff类别表 **/  
	public static final String BUFF_TYPE_TXT = TTXT + SEPARATE + "BuffType" + TXT;
	
	/** buff总表 **/  
	public static final String BUFF_TXT = TTXT + SEPARATE + "Buff" + TXT;
	
	/**
	 * 普通道具配置表
	 */
	/** 副本配置表 **/
	public static final String FUBEN_TEMPLATE_TXT = TTXT + SEPARATE + "FuBenTemplate" + TXT;
	
	public static final String PROPERTY_CONFIG = TTXT + SEPARATE + "Property" + TXT;
	/**
	 * 背包设置配置表
	 */
	public static final String BAG_CONFIG = TTXT + SEPARATE + "BagConfig" + TXT;
	/**
	 * 装备配置表
	 */
	public static final String EQUITMENT_CONFIG = TTXT + SEPARATE + "Equipment" + TXT;
	/**
	 * 普通道具使用方式及效果表
	 */
	public static final String FUNCTION_CONFIG = TTXT + SEPARATE + "Function" + TXT;
	/**
	 * 装备孔配置表
	 */
	public static final String SOCKET_CONFIG = TTXT + SEPARATE + "Socket" + TXT;
	/**
	 * 装备质量表
	 */
	public static final String EQUIPMENT_QUALITY_CONFIG = TTXT + SEPARATE + "EquipQuality" + TXT;
	/**
	 * 装备主属性表
	 */
	public static final String MAINATTRIBUTE_CONFIG = TTXT + SEPARATE + "Mainattribute" + TXT;
	/**
	 * 装备主属性成长配置表
	 */
	public static final String EQUIPMENT_LEVEL_GROPU_CONFIG = TTXT + SEPARATE + "EquipmentLevelGrouth" + TXT;
	
	/**
	 * 任务系统设置表
	 */
	public static final String TASK_ALL_SET_CONFIG = TTXT + SEPARATE + "TaskConfig" + TXT;
	
	/**
	 * 任务设置表
	 */
	public static final String TASK_CONFIG = TTXT + SEPARATE + "Task" + TXT;
	
	/** 技能伤害单元配置表 **/
	public static final String SKILL_UNIT_TXT = TTXT + SEPARATE + "SkillUnit" + TXT;
	
	/**
	 * NPC 配置表
	 */
	public static final String NPC_CONFIG = TTXT + SEPARATE + "NPCConfig" + TXT;
	
	/**
	 * 组队 配置表
	 */
	public static final String TEAM_CONFIG =TTXT + SEPARATE + "TeamConfig" + TXT;
	
}
