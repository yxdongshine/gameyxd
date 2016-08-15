package com.engine.entityobj;

import com.engine.entityobj.space.AbsMapObject;
import com.engine.entityobj.space.AbsSpace;
import com.engine.entityobj.space.IMapObject;

/**
 * ClassName:Door <br/>
 * Function: TODO (传送门). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-28 下午5:50:52 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class Door extends AbsMapObject {
	
	/** 传送到地图的id **/
	private int targetMapId;
	
	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void leftView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public AbsSpace getSpace() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setSpace(AbsSpace space) {
		// TODO Auto-generated method stub
		
	}
	
	public int getTargetMapId() {
		return targetMapId;
	}
	
	public void setTargetMapId(int targetMapId) {
		this.targetMapId = targetMapId;
	}
	
	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setEnable(boolean bUse) {
		// TODO Auto-generated method stub
		
	}
	
}
