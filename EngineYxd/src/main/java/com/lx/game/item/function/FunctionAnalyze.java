package com.lx.game.item.function;

import com.lx.nserver.txt.FunctionPojo;
import com.sun.org.apache.xpath.internal.functions.FuncId;

/**
 * ClassName:FunctionAnalyze <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午11:30:00 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class FunctionAnalyze {
	
	/**
	 * 根据类型编号转换相应的Function analyticalType:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param typeId
	 * @return
	 */
	static Function analyticalType(long typeId) {
		Function function = null;
		switch ((int) typeId) {
			case Function.FUNCTION_ITEM_REWARDEXP: // //奖励经验
				function = new IncreaseExpFunction();
				break;
			case Function.FUNCTION_ITEM_CURRENCY: // 增加货币
				function = new IncreaseCurrencyFunction();
				break;
			case Function.FUNCTION_ITEM_RECOVER: // 恢复血
				function = new RecoverFunction();
				break;
			default:
				break;
		}
		function.setType(typeId);
		return function;
	}
	
	/**
	 * 构建一个具体的function analytical:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param functionPojo
	 * @return
	 */
	public static Function analytical(FunctionPojo functionPojo) {
		Function function = analyticalType(functionPojo.getType());
		function.parse(functionPojo);
		return function;
	}
}
