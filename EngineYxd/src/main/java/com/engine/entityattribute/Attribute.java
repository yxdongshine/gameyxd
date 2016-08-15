package com.engine.entityattribute;

/**
 * ClassName:RoleAttribute <br/>
 * Function: TODO (属性). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-13 下午5:32:49 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class Attribute {
	
	/** 0 力量 气力 是 **/
	public static final int AIR = 1;
	
	/** 1 敏捷 灵敏 是 **/
	public static final int AGILITY = 2;
	
	/** 2 智力 内力 是 **/
	public static final int INNER_FORCE = 3;
	
	/** 3 控制 掌控 是 **/
	public static final int CONTROL = 4;
	
	/** 4 防御 坚韧 是 **/
	public static final int TENACITY = 5;
	/** 5 物理攻击 外功攻击 是 **/
	public static final int EXTERNAL_FORCE_ATTACK = 6;
	
	/*** 6 法术攻击 内功攻击 是 **/
	public static final int INNER_FORCE_ATTACK = 7;
	/** 7 火焰攻击 烈阳攻击 是 **/
	public static final int SCORCHING_SUN_ATTACK = 8;
	/*** 8 雷电攻击 天罡攻击 是 **/
	public static final int PLOUGH_ATTACK = 9;
	
	/** 9 毒素攻击 幽冥攻击 是 **/
	public static final int NETHER_ATTACK = 10;
	/** 10 冰冻攻击 太阴攻击 是 **/
	public static final int LUNAR_ATTACK = 11;
	/** 11 近程命中 近战命中 是 **/
	public static final int MELEE_HIT_RATE = 12;
	
	/** 12远程命中 是 未改名的沿用原名即可 **/
	public static final int REMOTE_HIT_RATE = 13;
	/** 13 近程回避 近战闪避 是 **/
	public static final int MELEE_DODGE_RATE = 14;
	/** 14 远程回避 远程闪避 是 **/
	public static final int REMOTE_DODGE_RATE = 15;
	
	/** 15 最大生命 是 **/
	public static final int MAX_HP = 16;
	/** 16 当前生命 **/
	public static final int CUR_HP = 17;
	/** 17 最大法力 最大真气 是 **/
	public static final int MAX_MP = 18;
	/** 18 当前法力 当前真气 **/
	public static final int CUR_MP = 19;
	/** 19 护甲 防御 是 **/
	public static final int DEFENCE = 20;
	
	/** 20 物理伤害减免 外功伤害减免 是 **/
	public static final int EXTERNAL_FORCE_DAMAGE_REDUCE = 21;
	/** 21 法术伤害减免 内功伤害减免 是 **/
	public static final int INNER_FORCE_DAMAGE_REDUCE = 22;
	
	/** 22火焰伤害减免 烈阳伤害减免 是 */
	public static final int SCORCHING_SUN_DAMAGE_REDUCE = 23;
	
	/** 23 雷电伤害减免 天罡伤害减免 是 **/
	public static final int PLOUGH_DAMAGE_REDUCE = 24;
	
	/** 24毒素伤害减免 幽冥伤害减免 是 */
	public static final int NETHER_DAMAGE_REDUCE = 25;
	
	/** 25 冰冻伤害减免 太阴伤害减免 是 **/
	public static final int LUNAR_DAMAGE_REDUCE = 26;
	
	/** 26 暴击率 会心率 是 **/
	public static final int KNOWING_RATE = 27;
	
	/** 27 格挡率 抵挡率 是 **/
	public static final int PARRY_RATE = 28;
	/** 28 攻击频率 **/
	public static final int ATTACK_SPEED = 29;
	/** 29 攻击范围 **/
	public static final int ATTACK_SCOPE = 30;
	
	/** 30 移动速度 **/
	public static final int SPEED = 31;
	/** 31 所有伤害降低 是 **/
	public static final int ALL_HURT_LOWER = 32;
	
	/** 32经验获得基数 **/
	public static final int EXP_BASE = 33;
	
	/** 33 对人伤害系数 是 **/
	public static final int TARGET_ROLE_COEFFICIENT = 34;
	/** 34 对怪伤害系数 **/
	public static final int TARGET_MONSTER_COEFFICIENT = 35;
	/** 35 对宠伤害系数 **/
	public static final int TARGET_PET_COEFFICIENT = 36;
	/** 36 对召唤物伤害系数 **/
	public static final int TARGET_CALL_MONSTER_COEFFICIENT = 37;
	/*** 37 被人伤害系数 是 **/
	public static final int COEFFICIENT_FROM_ROLE = 38;
	
	/** 38 被怪伤害系数 **/
	public static final int COEFFICIENT_FROM_MONSTER = 39;
	
	/** 39 被宠伤害系数 **/
	public static final int COEFFICIENT_FROM_PET = 40;
	/** 40 被召唤物伤害系数 **/
	public static final int COEFFICIENT_FROM_CALL_MONSTER = 41;
	/** 41 回血速度 生命回复速度 是 **/
	public static final int HP_RECOVER_SPEED = 42;
	/** 42 回魔速度 真气回复速度 是 */
	public static final int MP_RECOVER_SPEED = 43;
	/** 43 投射物移动速度 */
	public static final int SPRIT_SPEED = 44;
	
	/** 44投射穿透率 **/
	public static final int SPRIT_PIERCE_RATE = 45;
	/** 45 致晕 眩晕概率 是 **/
	public static final int DIZZINESS_RATE = 46;
	/** 46 眩晕抵抗 眩晕抵抗 是 **/
	public static final int DIZZINESS_RESISTANCE = 47;
	/** 47 吸血比例 生命偷取比例 是 **/
	public static final int HP_STEAL = 48;
	/** 48 吸魔比例 真气偷取比例 是 **/
	public static final int MP_STEAL = 49;
	/** 49 冰冻概率 是 **/
	public static final int FROZEN_RATE = 50;
	/** 50冰冻抗性 是 */
	public static final int FROZEN_RESISTANCE = 51;
	
	/** 51生命吸取抗性 生命偷取抗性 **/
	public static final int HP_STEAL_RESISTANCE = 52;
	/** 52 法力吸取抗性 真气偷取抗性 */
	public static final int MP_STEAL_RESISTANCE = 53;
	/** 53 燃血抗性 生命流失抗性 **/
	public static final int HP_BURN_RESISTANCE = 54;
	/** 54 燃魔抗性 真气流失抗性 **/
	public static final int MP_BURN_RESISTANCE = 55;
	/** 55 物理伤害吸收 外功伤害吸收 是 **/
	public static final int EXTERNAL_FORCE_DAMAGE_OBSORB = 56;
	/** 56 法力伤害吸收 内功伤害吸收 是 **/
	public static final int INNER_FORCE_DAMAGE_OBSORB = 57;
	
	/** 57 火焰伤害吸收 烈阳伤害吸收 是 **/
	public static final int SCORCHING_SUN_DAMAGE_OBSORB = 58;
	
	/** 58 雷电伤害吸收 天罡伤害吸收 是 **/
	public static final int PLOUGH_DAMAGE_OBSORB = 59;
	
	/** 59 毒素伤害吸收 幽冥伤害吸收 是 **/
	public static final int NETHER_DAMAGE_OBSORB = 60;
	
	/** 60 冰冻伤害吸收 太阴伤害吸收 是 **/
	public static final int LUNAR_DAMAGE_OBSORB = 61;
	
	/** 61 物理反击 外功反弹 **/
	public static final int EXTERNAL_FORCE_REBIND = 62;
	/** 62 法术反击 内功反弹 **/
	public static final int INNER_FORCE_REBIND = 63;
	
	/** 63 火焰反击 烈阳反弹 **/
	public static final int SCORCHING_SUN_REBIND = 64;
	
	/** 64 雷电反击 天罡反弹 **/
	public static final int PLOUGH_REBIND = 65;
	
	/** 65 毒素反击 幽冥反弹 **/
	public static final int NETHER_REBIND = 66;
	
	/** 66 冰冻反击 太阴反弹 **/
	public static final int LUNAR_REBIND = 67;
	
	/** 67 物理伤害系数 外功伤害系数 是 **/
	public static final int EXTERNAL_FORCE_DAMAGE_COEFFICIENT = 68;
	/** 68 法力伤害系数 内功伤害系数 是 **/
	public static final int INNER_FORCE_DAMAGE_COEFFICIENT = 69;
	
	/** 69 火焰伤害系数 烈阳伤害系数 是 */
	public static final int SCORCHING_SUN_DAMAGE_COEFFICIENT = 70;
	
	/** 70 雷电伤害系数 天罡伤害系数 是 **/
	public static final int PLOUGH_DAMAGE_COEFFICIENT = 71;
	
	/** 71 毒素伤害系数 幽冥伤害系数 是 */
	public static final int NETHER_DAMAGE_COEFFICIENT = 72;
	
	/** 72 冰冻伤害系数 太阴伤害系数 是 **/
	public static final int LUNAR_DAMAGE_COEFFICIENT = 73;
	/*** 73 暴击伤害 会心伤害 是 **/
	public static final int KNOWING_HURT_RATE = 74;
	
	/** 74 格挡伤害 抵挡能力 是 **/
	public static final int PARRY_HURT_RATE = 75;
	
	/** 75所有伤害提升是 **/
	public static final int ALL_HURT_ADD = 76;
	
	/** 76 撕裂伤口 重创攻击 是 **/
	public static final int HEAVY_ATTACK = 77;
	/** 77 壁垒防御 护体防御 是 **/
	public static final int WATCH_BOX_DEF = 78;
	
	/** 78 麻痹概率 定身概率 是 **/
	public static final int NO_MOVE_RATE = 79;
	
	/** 79 麻痹抗性 定身抗性 是 **/
	public static final int NO_MOVE_RESISTANCE = 80;
	/** 80 韧性 是 **/
	public static final int TOUGHNESS_RATE = 81;
	/** 82 韧性效果 韧性能力 是 * */
	public static final int TOUGHNESS_EFFECT = 82;
	
	/** 81 破甲 破防 是 **/
	public static final int DISRUPTING = 83;
	/** 83 破甲能力 破防能力 是 **/
	public static final int DISRUPTING_EFFECT = 84;
	
	/** 84 额外技能点 */
	public static final int EXTRA_SKILL_POINT = 85;
	public static final int ATTRIBUTE_LEN = 85;
	// DPH 普攻伤害 是 XML中无该属性，但游戏属性面板中有。
	
	/** 基础属性数组 **/
	protected double baseAttribute[] = new double[ATTRIBUTE_LEN];
	
	/** buff属性数组 **/  
	protected double buffAttribute[] = new double[ATTRIBUTE_LEN];
	
	/** 讲算之后属性 **/
	protected int attribute[] = new int[ATTRIBUTE_LEN];
	
	/**
	 * updateAttribute:(). <br/>
	 * TODO().<br/>
	 * 更新属性
	 * 
	 * @author lyh
	 * @param attrType
	 */
	public void updateAttribute(int attrType) {
		if (attrType == CUR_HP || attrType == CUR_MP) {
			return;
		}
		// attribute[attrType - 1]
		double attVal = baseAttribute[attrType - 1];
		// 追加buff属性
		attVal += buffAttribute[attrType - 1];
		
		attribute[attrType - 1] = (int) Math.ceil(attVal);// 不小于整数
	}
	
	/**
	 * addBaseAttribute:(). <br/>
	 * TODO().<br/>
	 * 加入基础属性
	 * 
	 * @author lyh
	 * @param attrType
	 * @param val
	 */
	public void addBaseAttribute(int attrType, double val) {
		baseAttribute[attrType - 1] = val;
	}
	
	/** 
	 * 加入buff属性
	 * addBuffAttribute:(). <br/> 
	 */  
	public void addBuffAttribute(int attrType, double val)
	{
		buffAttribute[attrType - 1] = val;
	}
	
	public void clear() {
		attribute = null;
		attribute = new int[ATTRIBUTE_LEN];
	}
	
	/**
	 * dph:(). <br/>
	 * TODO().<br/>
	 * 计算damage
	 * 
	 * @author lyh
	 * @return
	 */
	public int damage() {
		// // dph = 物理攻击力+法术攻击力+火焰攻击力+雷电攻击力+光明攻击力+黑暗攻击力+无视防御伤害
		int damage = getAttribute(Attribute.EXTERNAL_FORCE_ATTACK);
		damage += getAttribute(Attribute.INNER_FORCE_ATTACK);
		damage += getAttribute(Attribute.SCORCHING_SUN_ATTACK);
		damage += getAttribute(Attribute.PLOUGH_ATTACK);
		damage += getAttribute(Attribute.NETHER_ATTACK);
		damage += getAttribute(Attribute.LUNAR_ATTACK);
		return damage;
	}
	
	public double[] getBaseAttribute() {
		return baseAttribute;
	}
	
	public void setBaseAttribute(double[] baseAttribute) {
		this.baseAttribute = baseAttribute;
	}
	
	/**
	 * getAttribute:(). <br/>
	 * TODO().<br/>
	 * 属性值
	 * 
	 * @author lyh
	 * @param attrType
	 * @return
	 */
	public int getAttribute(int attrType) {
		return attribute[attrType - 1];
	}
	
	/**
	 * 刷新相关属性
	 * 
	 * @param index
	 */
	public void refreshRelatedAttribute(int attrType) {
		switch (attrType) {
			case Attribute.AIR:// 气力
				// 暴击 力量 等级
				// 档格 力量 等级
				updateAttribute(Attribute.EXTERNAL_FORCE_ATTACK);
				break;
			case Attribute.INNER_FORCE:// 内力
				// 最大法力 智力 等级
				updateAttribute(Attribute.MAX_MP);
				updateAttribute(Attribute.INNER_FORCE_ATTACK);
				break;
			case Attribute.AGILITY:// 敏捷
				// 近程回避 敏捷 等级
				// 近程命中 敏捷 等级
				// 远程回避 敏捷 等级
				// 远程命中 敏捷 等级
				updateAttribute(Attribute.EXTERNAL_FORCE_ATTACK);
				break;
			case Attribute.CONTROL:// 控制
				updateAttribute(Attribute.INNER_FORCE_ATTACK);
				break;
			case Attribute.TENACITY:// 坚韧
				// 护甲 防御 等级
				// 最大生命 防御 等级
				updateAttribute(Attribute.DEFENCE);
				refreshRelatedAttribute(Attribute.DEFENCE);// 2013-8-27 ming 历史问题 刷新相关属性
				updateAttribute(Attribute.MAX_MP);
				break;
			case Attribute.DEFENCE:// 护甲
				updateAttribute(Attribute.ALL_HURT_LOWER);
				break;
		}
	}
}
