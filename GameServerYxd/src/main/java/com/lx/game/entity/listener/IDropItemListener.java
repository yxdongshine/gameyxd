package com.lx.game.entity.listener;

import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.IListener;
import com.lx.game.entity.DropItem;

/**
 * IDropItemListener <br/>
 * Function: TODO (道具监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-29 下午5:37:35 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IDropItemListener extends IListener {

	/** 
	 * addItemToBag:(). <br/> 
	 * TODO().<br/> 
	 * 把道具加入到背包
	 * @author lyh  
	 */  
	public void addItemToBag(DropItem dItem);
	
	 
}
