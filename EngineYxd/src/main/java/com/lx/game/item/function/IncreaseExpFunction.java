package com.lx.game.item.function;

import com.engine.entityobj.ServerPlayer;
import com.lx.nserver.txt.FunctionPojo;

/**
 * 
 * @author ming
 * 
 */
public class IncreaseExpFunction extends Function {
	
	/**
	 * 加经验值
	 */
	int value;
	
	public IncreaseExpFunction() {
		// TODO Auto-generated constructor stub
	}
	
	/**
     *
     */
	@Override
	public void parse(FunctionPojo function) {
		value = function.getCount();
	}
	
	/**
	 * 使用
	 */
	@Override
	protected boolean onActive(ServerPlayer me, ServerPlayer targets) {
		// player.increaseExp(value, false, true);
		// 增加用户的经验
		me.changceExp(value);
		return true;
	}
	
	/**
     *
     */
	@Override
	public boolean canActive(ServerPlayer target, ServerPlayer targets) {
		return true;
	}
	
}
