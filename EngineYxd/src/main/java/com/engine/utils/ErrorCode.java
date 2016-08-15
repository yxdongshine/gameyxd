package com.engine.utils;

/**
 * ClassName:StatusCode <br/>
 * Function: TODO (状态码). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:06:40 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ErrorCode {
	
	
	
	/****
	 * 
	 * 
	 * 
	 * yxd 状态码 500 -1000
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	
	
	
	
	
	
	
	/** 用户名密码不能为空 **/
	public static final int STATUS_1 = 0x0001;
	/** 你输入的账号或密码不合法 **/
	public static final int STATUS_2 = 0x0002;
	/** 该用户名已存在 **/
	public static final int STATUS_3 = 0x0003;
	/** 用户名或者密码错误 **/
	public static final int STATUS_4 = 0x0004;
	/** 密码修改成功 **/
	public static final int STATUS_5 = 0x0005;
	/** 用户不存在 **/
	public static final int STATUS_6 = 0x0006;
	
	/** 服务器正在维护 **/
	public static final int STATUS_7 = 0x0007;
	
	/** 该名称已存在 **/
	public static final int STATUS_8 = 0x0008;
	/** 角色名称不能为空 **/
	public static final int STATUS_9 = 0x0009;
	/** 角色名称长度不符 **/
	public static final int STATUS_A = 0x000A;
	/** 角色名称不能包含非法字符 **/
	public static final int STATUS_B = 0x000B;
	/** 已超出申请的最大人数 **/
	public static final int STATUS_C = 0x000C;
	
	/** 版本号有错误 **/
	public static final int STATUS_D = 0x000D;
	
	/**
	 * 背包已满
	 */
	public static final int STATUS_E = 0x000E;
	
	/**
	 * 状态异常
	 */
	public static final int STATUS_F = 0x000F;
	/**
	 * 物品数量不够
	 */
	public static final int STATUS_10 = 0x0010;
	
	/**
	 * 使用物品失败
	 */
	public static final int STATUS_11 = 0x0011;
	
	/**
	 * 余额不足，请充值
	 */
	public static final int STATUS_12 = 0x0012;
	
	/**
	 * 不能卖
	 */
	public static final int STATUS_13 = 0x0013;
	
	/**
	 * 药瓶不能再升级
	 */
	public static final int STATUS_14 = 0x0014;
	
	/** 攻击冷却中 **/
	public static final int STATUS_15 = 0x0015;
	
	/** 没有此技能,不能攻击 **/
	public static final int STATUS_16 = 0x0016;
	/** MP不足 **/
	public static final int STATUS_17 = 0x0017;
	/** HP不足 **/
	public static final int STATUS_18 = 0x0018;
	/** 怒气值不足 **/
	public static final int STATUS_19 = 0x0019;
	/**没有大还丹,不能原地复活 **/
	public static final int STATUS_1A = 0x001A;

	/**元宝不足,不能复活 **/
	public static final int STATUS_1B = 0x001B;
	/**正播放战斗动画 **/
	public static final int STATUS_1C = 0x001C;
	
	/** 等级不足 **/
	public static final int STATUS_2000 = 0x2000;
	
	/** 体力不足 **/
	public static final int STATUS_2001 = 0x2001;
	
	/** 今日次数已使用完 **/
	public static final int STATUS_2002 = 0x2002;
	
	
	/**
	 * 当前队伍已满，不能邀请
	 */
	public static final int STATUS_500 = 0x0500;
	
	/**
	 * 对同一人发送组队请求的时间间隔为5秒钟。
	 */
	public static final int STATUS_501 = 0x0501;
	
	/**
	 * 玩家没有上线
	 */
	public static final int STATUS_502 = 0x0502;
	
	/**
	 * 对方不存在小队
	 */
	public static final int STATUS_503 = 0x503;
	
	/**
	 * 申请超出限制上线人数
	 */
	public static final int STATUS_504 = 0x0504;
	
	/**
	 * 不是队长，没有申请人员
	 */
	public static final int STATUS_505 = 0x0505;
	
	/**
	 * 你已经在小队中
	 */
	public static final int STATUS_506 = 0x0506;
	
	/**
	 * 对方已经存在小队，不能加入自己的小队中
	 */
	public static final int STATUS_507= 0x0507;
	
	/**
	 * 小队中满员，不能再添加
	 */
	public static final int STATUS_508= 0x0508;
	
	/**
	 * 你不是队长，不能移交队长称号
	 */
	public static final int STATUS_509= 0x0509;
	
	/**
	 * 你不是队长，不能解散队伍
	 */
	public static final int STATUS_510= 0x0510;
	
	/**
	 * 投票时间已经过了
	 */
	public static final int STATUS_511= 0x511;
	
	/**
	 * 小队正在投票驱逐，一分钟后再来
	 */
	public static final int STATUS_512= 0x512;
	
	/**
	 * 不能再次申请
	 */
	public static final int STATUS_513= 0x0513;
	
	/**
	 * 已经存在小队中
	 */
	public static final int STATUS_514= 0x0514;
}
