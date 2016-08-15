package com.engine.entityobj;

/**
 * ClassName:PlayerStatus <br/>
 * Function: TODO (角色状态). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-23 上午9:12:24 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class PlayerStatus {
	/** 正常 **/
	public static final int STATUS_NORMAL = 1 << 0;
	/** 进入图 **/
	public static final int STATUS_ENTER_MAP = 1 << 1;
	/** 登陆中 **/
	public static final int STATUS_LOGINING = 1 << 2;
	/** 退出中 **/
	public static final int STATUS_EXITING = 1 << 3;
	/** 退出 **/
	public static final int STATUS_EXIT = 1 << 4;
	
	/** 战斗状态 **/
	public static final int STATUS_FIGHTING = 1 << 5;
	/** 死亡 **/
	public static final int STATUS_DIE = 1 << 6;
	/*** 角色站立状态 */
	public static final int STATUS_STAND = 1 << 7;
	/*** 角色走动状态 */
	public static final int STATUS_RUN = 1 << 8;
	/*** 角色切地图状态 */
	public static final int STATUS_CHANGE_MAP = 1 << 9;
	/*** 角色传送地图状态 */
	public static final int STATUS_TRANS_MAP = 1 << 10;
	
	//
	// /**
	// * 二次跳跃
	// */
	// DOUBLEJUMP(0x00000400, 0xFFFF00FF),
	// /**
	// * 格挡准备
	// */
	// BLOCKPREPARE(0x00000800, 0xFFFF00FF),
	// /**
	// * 格挡
	// */
	// BLOCK(0x00001000, 0xFFFF00FF),
	// /**
	// * 打坐
	// */
	// SIT(0x00002000, 0xFFFF00FF),
	// /**
	// * 游泳
	// */
	// SWIM(0x00004000, 0xFFFF00FF),
	// /**
	// * 无复活
	// */
	// NOREVIVE(0x00000000, 0xFFFEFFFF),
	// /**
	// * 自动复活
	// */
	// AUTOREVIVE(0x00010000, 0xFFFEFFFF),
	// /**
	// * 非交易状态
	// */
	// NOTRANSACTION(0x00000000, 0xFFFDFFFF),
	// /**
	// * 交易
	// */
	// TRANSACTION(0x00020000, 0xFFFDFFFF),
	// /**
	// * 非挂机状态
	// */
	// NOAUTOFIGHT(0x00000000, 0xFFFBFFFF),
	// /**
	// * 挂机
	// */
	// AUTOFIGHT(0x00040000, 0xFFFBFFFF),
	// /**
	// * 龙元心法计时结束
	// */
	// LONGYUANEND(0x00000000, 0xFFF7FFFF),
	// /**
	// * 龙元心法开始计时状态（用来防止计时器重复调用）
	// */
	// LONGYUANSTART(0x00080000, 0xFFF7FFFF),
	//
	// /**
	// * 原地复活计时结束
	// */
	// REVIVEEND(0x00000000, 0xFFEFFFFF),
	// /**
	// * 原地复活开始计时状态（用来防止计时器重复调用）
	// */
	// REVIVESTART(0x00100000, 0xFFEFFFFF),
	// /**
	// * 地图中
	// */
	// INMAP(0x00000000, 0xFFDFFFFF),
	// /**
	// * 换地图中
	// */
	// CHANGEMAP(0x00200000, 0xFFDFFFFF),
	// /**
	// * 非采集
	// */
	// NOGATHER(0x00000000, 0xFFBFFFFF),
	// /**
	// * 采集
	// */
	// GATHER(0x00400000, 0xFFBFFFFF), ;
}
