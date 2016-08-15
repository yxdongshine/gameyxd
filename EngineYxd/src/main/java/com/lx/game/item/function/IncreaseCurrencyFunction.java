package com.lx.game.item.function;

import com.engine.entityobj.ServerPlayer;
import com.lx.nserver.txt.FunctionPojo;

/**
 * ClassName:IncreaseCurrencyFunction <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午2:47:04 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class IncreaseCurrencyFunction extends Function {
	
	/**
	 * 货币值
	 */
	int value;
	
	/**
	 * 货币类型
	 */
	int type;
	
	/**
	 * Creates a new instance of IncreaseCurrencyFunction.
	 * 
	 */
	public IncreaseCurrencyFunction() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void parse(FunctionPojo function) {
		// TODO Auto-generated method stub
		
		this.type = function.getCurrency();
		this.value = function.getCount();
	}
	
	@Override
	public boolean canActive(ServerPlayer me, ServerPlayer targets) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean active(ServerPlayer me, ServerPlayer targets) {
		// TODO Auto-generated method stub
		me.addCurrency(type, 1, value);
		return true;
	}
	
}
