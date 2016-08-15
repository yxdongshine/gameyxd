package com.lx.game.item;

import com.engine.entityobj.ServerPlayer;
import com.lx.game.item.function.Function;
import com.lx.game.item.function.FunctionAnalyze;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.nserver.txt.FunctionPojo;

/**
 * ClassName:CommonToolItem <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-3 上午11:56:16 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class CommonToolItem extends Item {
	
	private PropertyPojoGame commonProperty;
	
	private long id;
	
	/**
	 * 物品持有的使用配置
	 */
	private Function[] funtions;
	
	/**
	 * Creates a new instance of CommonToolItem.
	 * 
	 */
	public CommonToolItem(PropertyPojoGame commonProperty, long uuid) {
		// TODO Auto-generated constructor stub
		this.commonProperty = commonProperty;
		this.id = uuid;
		this.initFunction();
		
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	@Override
	public PropertyPojoGame getProperty() {
		return commonProperty;
	}
	
	public void setCommonProperty(PropertyPojoGame commonProperty) {
		this.commonProperty = commonProperty;
	}
	
	public Function[] getFuntions() {
		return funtions;
	}
	
	public void setFuntions(Function[] funtions) {
		this.funtions = funtions;
	}
	
	@Override
	public byte canUse(ServerPlayer me, ServerPlayer target) {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public byte use(ServerPlayer me, ServerPlayer target) {
		boolean success = false;
		if (this.getFuntions() == null) {
			this.initFunction();
		}
		// 对自己使
		if (me != null) {
			for (Function fun : this.getFuntions()) {
				if (fun != null && fun.canActive(me, target)) {
					if (fun.active(me, target)) {
						success |= true;
					}
				}
			}
		}
		// 对别人使
		if (target != null) {
			
		}
		if (success) {
			if (this.commonProperty.getUseWithRemove() == 0 ? true : false) {
				return USE_ITEM_SUCESS_WITH_REMOVE;
			} else {
				return USE_ITEM_SUCESS_WITH_OUT_REMOVE;
			}
			
		}
		return -1;
	}
	
	/**
	 * 初始化Function initFunction:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void initFunction() {
		if (getFuntions() == null) {// 开始初始化
			String functionIds = this.commonProperty.getFunction();
			if (functionIds != null && functionIds.trim().length() > 0) {
				String[] subFunctionIds = functionIds.split("\\*");
				if (subFunctionIds != null && subFunctionIds.length > 0) {
					// 初始化function数组
					funtions = new Function[subFunctionIds.length];
					for (int i = 0; i < subFunctionIds.length; i++) {
						FunctionPojo functionPojo = ItemConfigLoad.getFunctionHashMap().get(Integer.parseInt(subFunctionIds[i]));
						if (functionPojo != null) {// 初始化成function
							Function function = FunctionAnalyze.analytical(functionPojo);
							this.funtions[i] = function;
						}
					}
				}
			}
		}
	}
}
