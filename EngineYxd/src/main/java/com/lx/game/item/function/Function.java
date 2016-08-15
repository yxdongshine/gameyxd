package com.lx.game.item.function;

import com.engine.entityobj.ServerPlayer;
import com.lx.nserver.txt.FunctionPojo;

/**
 * ClassName:Function <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午11:23:30 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class Function {
	
	/**
	 * 类型
	 */
	private long type;
	
	/**
	 * 功能配置编号
	 */
	public static final int FUNCTION_ITEM_REWARDEXP = 0;// 奖励经验
	public static final int FUNCTION_ITEM_CURRENCY = 1;// 奖励货币
	public static final int FUNCTION_ITEM_RECOVER = 2;// 使用药瓶
	
	/**
	 * 是否可以用 canActive:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param me
	 * @param targets
	 * @return
	 */
	public boolean canActive(ServerPlayer me, ServerPlayer targets) {
		
		return true;
	}
	
	/**
	 * 使用结果成功与否 active:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param me
	 * @param targets
	 * @return
	 */
	public boolean active(ServerPlayer me, ServerPlayer targets) {
		
		return true;
	}
	
	/**
	 * 
	 * parse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param function
	 */
	protected void parse(FunctionPojo function) {
		
	}
	
	/**
	 * 获取Trigger 标识ID
	 * 
	 * @return
	 */
	public final long getType() {
		return type;
	}
	
	public final void setType(long t) {
		type = t;
	}
	
	/**
	 * onActive:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param target
	 * @param targets
	 * @return
	 */
	protected boolean onActive(ServerPlayer target, ServerPlayer targets) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
