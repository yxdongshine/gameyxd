package com.engine.entityobj.space;

import java.util.List;
import java.util.Map;

import com.engine.entityobj.ServerPlayer;

/**
 * ClassName:IArea <br/>
 * Function: TODO (地图的一块区域接口). <br/>
 * 一个场景有多块区域，减少搜索次数 Reason: TODO (). <br/>
 * Date: 2015-7-28 上午10:13:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IArea {
	
	/**
	 * getStartCol:(). <br/>
	 * TODO().<br/>
	 * 本区域块的启始列
	 * 
	 * @author lyh
	 * @return
	 */
	public int getStartCol();
	
	/**
	 * getStartRow:(). <br/>
	 * TODO().<br/>
	 * 本区域的启始行
	 * 
	 * @author lyh
	 * @return
	 */
	public int getStartRow();
	
	/**
	 * addMapObject:(). <br/>
	 * TODO().<br/>
	 * 把地图对象加入区域
	 * 
	 * @author lyh
	 * @param obj
	 */
	public void addMapObject(IMapObject obj);
	
	/**
	 * removeMapObject:(). <br/>
	 * TODO().<br/>
	 * 从地图区域删除地图对象
	 * 
	 * @author lyh
	 * @param obj
	 */
	public void removeMapObject(IMapObject obj);
	
	/**
	 * getMapObjects:(). <br/>
	 * TODO().<br/>
	 * 得到区域内的所有元素
	 * 
	 * @author lyh
	 * @return
	 */
	public Map<Long, IMapObject> getMapObjects();
	
	/**
	 * getNineArea:(). <br/>
	 * TODO().<br/>
	 * 获得九宫格
	 * 
	 * @author lyh
	 * @return
	 */
	public List<IArea> getNineArea();
	
	/** 获得此区域中的所有玩家 */
	public Map<Long, IMapObject> getPlayers();
	
}
