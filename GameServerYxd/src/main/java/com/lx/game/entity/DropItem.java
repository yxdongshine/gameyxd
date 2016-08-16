package com.lx.game.entity;

import com.engine.entityobj.AbsDropItem;
import com.engine.entityobj.space.AbsMapObject;
import com.engine.interfaces.IListener;
import com.lx.game.entity.listener.IDropItemListener;

/**
 * ClassName:DropItem <br/>
 * Function: TODO (地图上掉落的道具). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-27 上午9:53:31 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class DropItem extends AbsDropItem{
	
//	/**1分钟**/  
//	private static final int DEFAULT_DISAPPEAR_TIME = 60000;
//	/**保护时间30秒**/  
//	private static final int DEFAULT_PROTECT_TIME = 30000;
//	
//	///////////////////////掉落道具类型////////////////////////
//	public static final int DROP_TYPE_MONEY = 1;
//	public static final int DROP_TYPE_ITME = 2;
//	
//	/**道具所有者**/  
//	private long ownerId;
//	
//	/**道具配置文件id**/  
//	private int itemConfigId;
//	
//	/****/  
//	private int num;
//	/****/  
//	private int dropType;
//	
//	/**保护到期时间**/  
//	private long protectTime;
//	
//	/**消失到期时间**/  
//	private long disappearTime;
//	
//	/**拾取此道具的角色id**/  
//	private long roleId;
//	
//	/**监听器**/  
//	protected IDropItemListener listener;
//	
//	@Override
//    public int getLevel() {
//	    // TODO Auto-generated method stub
//	    return 0;
//    }
//
//	@Override
//    public boolean isEnable() {
//	    // TODO Auto-generated method stub
//	    return false;
//    }
//
//	@Override
//    public void setEnable(boolean bUse) {
//	    // TODO Auto-generated method stub
//	    
//    }
//
//	public int getItemConfigId() {
//    	return itemConfigId;
//    }
//
//	public void setItemConfigId(int itemConfigId) {
//    	this.itemConfigId = itemConfigId;
//    }
//
//	public int getNum() {
//    	return num;
//    }
//
//	public void setNum(int num) {
//    	this.num = num;
//    }
//
//	public int getDropType() {
//    	return dropType;
//    }
//
//	public void setDropType(int dropType) {
//    	this.dropType = dropType;
//    }
//
//	public long getOwnerId() {
//    	return ownerId;
//    }
//
//	public void setOwnerId(long ownerId) {
//    	this.ownerId = ownerId;
//    }
//
//	public long getProtectTime() {
//    	return protectTime;
//    }
//
//	public void setProtectTime() {
//    	this.protectTime =  System.currentTimeMillis() + DEFAULT_PROTECT_TIME;
//    }
//
//	public long getDisappearTime() {
//    	return disappearTime;
//    }
//
//	public void setDisappearTime() {
//    	this.disappearTime = DEFAULT_DISAPPEAR_TIME+ System.currentTimeMillis();
//    }
//
//	public long getRoleId() {
//    	return roleId;
//    }
//
//	public void setRoleId(long roleId) {
//    	this.roleId = roleId;
//    }

	/**监听器**/  
	protected IDropItemListener listener;
	
    public void setListener(IListener listener) {
	    // TODO Auto-generated method stub
    	this.listener = (IDropItemListener)listener;
    }
    
	public IListener getListener() {
    	return listener;
    }
	
	
}
