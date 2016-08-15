package com.engine.entityobj;

import com.engine.config.xml.map.NpcInfo;
import com.engine.entityobj.space.AbsMapObject;
import com.engine.entityobj.space.AbsSpace;
import com.engine.entityobj.space.IMapObject;

/**
 * ClassName:NPC <br/>
 * Function: TODO (npc对象). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-23 上午9:14:45 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class Npc extends AbsMapObject {
	
	private NpcInfo npcInfo;
	
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
	
	public NpcInfo getNpcInfo() {
		return npcInfo;
	}
	
	public void setNpcInfo(NpcInfo npcInfo) {
		this.npcInfo = npcInfo;
	}
	
	public int getNpcConfigId() {
		return npcInfo == null ? 0 : npcInfo.tid;
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
