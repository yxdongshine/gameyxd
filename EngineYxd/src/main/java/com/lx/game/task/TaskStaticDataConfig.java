package com.lx.game.task;

/**
 * ClassName:TaskStaticDataConfig <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午2:44:10 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class TaskStaticDataConfig {
	// 暴露但不能接收
	public static final byte TASK_STATE_DIAPALY_NO_ACCEPT = 1;
	// 可以接受
	public static final byte TASK_STATE_CAN_ACCEPT = 2;
	// 已经接受没有完成
	public static final byte TASK_STATE_ACCEPTED_NO_FINISH = 3;
	// 已经完成没有提交
	public static final byte TASK_STATE_FINISHED_NO_HAND = 4;
	// 提交并且完成
	public static final byte TASK_STATE_FINSHED_HANDED = 5;
	
	// 对话
	public static final byte TASK_TYPE_CONVERSATION = 3;
	// 杀怪
	public static final byte TASK_TYPE_KILL_MOSTER = 1;
	// 收集
	public static final byte TASK_TYPE_COLLECT = 2;
	
	// 完成任务道具结算前
	public static final byte FINISH_TASK_BALANCE_BEFROCE = 0;
	// //完成任务道具结算后
	public static final byte FINISH_TASK_BALANCE_AFTER = 1;
}
