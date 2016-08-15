package com.engine.entityobj;

/**
 * ClassName:MonsterStatus <br/>
 * Function: TODO (怪物状态). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-30 下午7:31:26 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MonsterStatus {
	/** 正常 **/
	public static final int STATUS_NORMAL = 0;
	/** 跑回 **/
	public static final int STATUS_RUNBACK = 1;
	/** 死亡中 **/
	public static final int STATUS_DIEING = 2;
	/** 死亡 **/
	public static final int STATUS_DIE = 3;
	/** 死亡等待移除 **/
	public static final int STATUS_DIEWAIT = 4;
	/** 战斗 **/
	public static final int STATUS_FIGHT = 5;
}
