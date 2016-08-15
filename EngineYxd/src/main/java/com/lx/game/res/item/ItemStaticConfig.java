package com.lx.game.res.item;

/**
 * ClassName:ItemStaticConfig <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 下午2:03:38 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class ItemStaticConfig {
	/**
	 * 绑定类型
	 */
	public static final int BIND_TYPE_GET = 0;// 获取时绑定
	public static final int BIND_TYPE_EQUIPPED = 1;// 装备后绑定
	public static final int BIND_TYPE_NOTBIND = 2;// 不绑定
	
	/**
	 * 货币类型
	 */
	public static final int CURRENCY_TYPE_DOLD = 0;// 金币
	public static final int CURRENCY_TYPE_DIAMONDS = 1;// 钻石
	
	/**
	 * 品质
	 */
	public static final int QUALITY_COLOR_WHITE = 0;// 白色
	public static final int QUALITY_COLOR_GREEN = 1;// 绿色
	public static final int QUALITY_COLOR_BULE = 2;// 蓝色
	public static final int QUALITY_COLOR_GOLDEN = 3;// 金色
	public static final int QUALITY_COLOR_PURPLE = 4;// 紫色
	public static final int QUALITY_COLOR_ORANGE = 5;// 橙色
	public static final int QUALITY_COLOR_RED = 6;// 红色
	
	/**
	 * 道具分类id
	 */
	public static final int TOOL_TYPE_TASK = 0;// 任务道具
	public static final int TOOL_TYPE_TASK_NOT = 1;// 非任务道具
	
	/**
	 * 道具类型
	 */
	public static final int TOOL_TYPE_EQUIPMENT = 0;// 装备道具
	public static final int TOOL_TYPE_COMMON = 1;// 普通道具
	public static final int TOOL_TYPE_SPECIAL = 2;// 特殊道具
	
	/**
	 * 左侧从上至下依次为：武器，戒指，项链，时装。 右侧从上至下依次为：头，肩，身，披风（圣门为“鞋子”）。
	 * 
	 */
	public static final int EQUIP_BAR_WEAPON = 1;
	public static final int EQUIP_BAR_RING = 2;
	public static final int EQUIP_BAR_NECKLACE = 3;
	public static final int EQUIP_BAR_FASHTION_DRESS = 4;
	public static final int EQUIP_BAR_HEAD = 5;
	public static final int EQUIP_BAR_SHOULDER = 6;
	public static final int EQUIP_BAR_BODY = 7;
	public static final int EQUIP_BAR_CLOCK = 8;
	
	/**
	 * 背包类型
	 */
	public static final int BAG_TYPE_EQUIPMENT = 0;// 装备道具
	public static final int BAG_TYPE_COMMON = 1;// 普通道具
	public static final int BAG_TYPE_SPECIAL = 2;// 特殊道具
	public static final int BAG_TYPE_ON_BODY = 3;// 已经穿在身上的道具
	public static final int BAG_TYPE_BOTTLE = 4;// 药瓶
	
	/**
	 * 添加类型
	 * 
	 */
	public static final int BAG_ADD_TYPE_UPDATE = 0;
	public static final int BAG_ADD_TYPE_ADD = 1;
	public static final int BAG_ADD_TYPE_DEL = 2;
	
	/**
	 * 道具材料类型
	 */
	public static final int PROP_TYPE_BOTTLE = 4;// 药瓶
	
	public static final int PROP_TYPE_REVIVE = 5;// 复活币
	
	/**
	 * 
	 * 药瓶等级
	 */
	public static final int BOTTLE_TYPE_INTI = 1;// 初始等级
	
	public static final int BOTTLE_TYPE_ONE_DAY = 2;// 一天装
	
	public static final int BOTTLE_TYPE_SEVEN_DAY = 3;// 七天 装
	
	public static final int BOTTLE_TYPE_FOREVER = 4;// 永久装
	
}
