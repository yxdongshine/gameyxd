package com.engine.entityobj.space;

import java.util.Map;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.Position3D;
import com.engine.interfaces.IFightListener;
import com.engine.interfaces.IListener;

/**
 * ClassName:IMapObject <br/>
 * Function: TODO (地图上所有的对象接口). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-24 下午2:29:27 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IMapObject {
	public static final int MAP_OBJECT_TYPE_NONE = -1;
	/** 怪物 **/
	public static final int MAP_OBJECT_TYPE_MONSTER = 0;
	/** 角色 **/
	public static final int MAP_OBJECT_TYPE_PLAYER = 1;
	/** NPC **/
	public static final int MAP_OBJECT_TYPE_NPC = 2;
	/** 宝箱 **/
	public static final int MAP_OBJECT_TYPE_TREASUREBOX = 3;
	/** 宠物 **/
	public static final int MAP_OBJECT_TYPE_PET = 4;
	/** 坐骑 **/
	public static final int MAP_OBJECT_TYPE_HOURSE = 5;
	/** 传送门 **/
	public static final int MAP_OBJECT_TYPE_DOOR = 6;
	/** 掉落物 **/
	public static final int MAP_OBJECT_TYPE_DROP_ITEM = 7;
	/** 机关 **/
	public static final int MAP_OBJECT_TYPE_GEAR = 8;
	
	/**
	 * getId:(). <br/>
	 * TODO().<br/>
	 * 性一id
	 * 
	 * @author lyh
	 * @return
	 */
	public long getId();
	
	/**
	 * setId:(). <br/>
	 * TODO().<br/>
	 * 设置id
	 * 
	 * @author lyh
	 * @param id
	 */
	public void setId(long id);
	
	/**
	 * getName:(). <br/>
	 * TODO().<br/>
	 * 得到对象名
	 * 
	 * @author lyh
	 * @return
	 */
	public String getName();
	
	/**
	 * setName:(). <br/>
	 * TODO().<br/>
	 * 设置对象名
	 * 
	 * @author lyh
	 */
	public void setName(String name);
	
	/**
	 * getPosition3D:(). <br/>
	 * TODO().<br/>
	 * 获得3d坐标对象
	 * 
	 * @author lyh
	 * @return
	 */
	public Position3D getPosition3D();
	
	/**
	 * setPosition3D:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition3D(float x, float y, float z);
	
	// 设置坐标轴
	public void setX(float x);
	
	public void setY(float y);
	
	public void setZ(float z);
	
	/**
	 * getAttribute:(). <br/>
	 * TODO().<br/>
	 * 获得对象属性
	 * 
	 * @author lyh
	 * @return
	 */
	public Attribute getAttribute();
	
	/**
	 * setAttribute:(). <br/>
	 * TODO().<br/>
	 * 设置属性
	 * 
	 * @author lyh
	 * @param attr
	 */
	public void setAttribute(Attribute attr);
	
	/**
	 * getLevel:(). <br/>
	 * TODO().<br/>
	 * 获得等级
	 * 
	 * @author lyh
	 * @return
	 */
	public int getLevel();
	
	/**
	 * getType:(). <br/>
	 * TODO().<br/>
	 * 地图对象类型
	 * 
	 * @author lyh
	 * @return
	 */
	public int getType();
	
	/**
	 * dir:(). <br/>
	 * TODO().<br/>
	 * 对象方向
	 * 
	 * @author lyh
	 * @return
	 */
	public float getDir();
	
	/**
	 * setDir:(). <br/>
	 * TODO().<br/>
	 * 设置方向
	 * 
	 * @author lyh
	 * @param dir
	 * @return
	 */
	public void setDir(float dir);
	
	/**
	 * setType:(). <br/>
	 * TODO().<br/>
	 * 设置地图上对象类型
	 * 
	 * @author lyh
	 * @param type
	 */
	public void setType(int type);
	
	/**
	 * lefeView:(). <br/>
	 * TODO().<br/>
	 * 离开视野
	 * 
	 * @author lyh
	 * @param mapObj
	 */
	public void leftView(IMapObject mapObj);
	
	/**
	 * getViewMap:(). <br/>
	 * TODO().<br/>
	 * 获得视野容器
	 * 
	 * @author lyh
	 * @return
	 */
	public Map<Long, IMapObject> getViewMap();
	
	/**
	 * addView:(). <br/>
	 * TODO().<br/>
	 * 离开视野
	 * 
	 * @author lyh
	 * @param mapObj
	 */
	public void addView(IMapObject mapObj);
	
	/**
	 * clearView:(). <br/>
	 * TODO().<br/>
	 * 清空视野
	 * 
	 * @author lyh
	 */
	public void clearView();
	
	/**
	 * isView:(). <br/>
	 * TODO().<br/>
	 * 是否在视野里
	 * 
	 * @author lyh
	 * @param mapObj
	 * @return
	 */
	public boolean isView(IMapObject mapObj);
	
	/**
	 * 是否在. <br/>
	 */
	public boolean isInView(IMapObject mapObj);
	
	/**
	 * getSpace:(). <br/>
	 * TODO().<br/>
	 * 得到地图对象所在的空间
	 * 
	 * @author lyh
	 * @return
	 */
	public AbsSpace getSpace();
	
	/**
	 * setSpace:(). <br/>
	 * TODO().<br/>
	 * 设置地图对象所在的空间
	 * 
	 * @author lyh
	 * @param space
	 */
	public void setSpace(AbsSpace space);
	
	/**
	 * setArea:(). <br/>
	 * TODO().<br/>
	 * 设置对象所在的区域
	 * 
	 * @author lyh
	 * @param area
	 */
	public void setArea(IArea area);
	
	/**
	 * getArea:(). <br/>
	 * TODO().<br/>
	 * 得到对象所在的那一块区域
	 * 
	 * @author lyh
	 */
	public IArea getArea();
	
	/**
	 * getMapObjectMessage:(). <br/>
	 * TODO().<br/>
	 * 获得广播消息监听队象
	 * 
	 * @author lyh
	 * @return
	 */
	public IMapObjectMessage getMapObjectMessage();
	
	/**
	 * setMapObjectMessage:(). <br/>
	 * TODO().<br/>
	 * 设置广播消息监听对象
	 * 
	 * @author lyh
	 * @param listener
	 */
	public void setMapObjectMessage(IMapObjectMessage listener);
	
	/**
	 * isEnable:(). <br/>
	 * TODO().<br/>
	 * 判断是否可通知
	 * 
	 * @author lyh
	 * @return
	 */
	public boolean isEnable();
	
	/**
	 * setEnable:(). <br/>
	 * TODO().<br/>
	 * 设置是否可用
	 * 
	 * @author lyh
	 * @param bUse
	 */
	public void setEnable(boolean bUse);
	
	/**
	 * setIFightListener:(). <br/>
	 * TODO().<br/>
	 * 设置监听器
	 * 
	 * @author lyh
	 * @param listener
	 */
	public void setIFightListener(IFightListener listener);
	
	/**
	 * removeHatred:(). <br/>
	 * TODO().<br/>
	 * 删除
	 * 
	 * @author lyh
	 * @param obj
	 */
	public void removeHatred(IMapObject obj);
	
	/**
	 * getListener:(). <br/>
	 * TODO().<br/>
	 * 获得监听事件
	 * 
	 * @author lyh
	 * @return
	 */
	public IFightListener getIFightListener();
	
	public void setListener(IListener listener);
	
	
	/** 
	 * getPlayerViewMap:(). <br/> 
	 * TODO().<br/> 
	 * 视野里的角色容器
	 * @author lyh 
	 * @return 
	 */  
	public Map<Long, IMapObject> getPlayerViewMap();

	/** 
	 * getMonsterViewMap:(). <br/> 
	 * TODO().<br/> 
	 * 视野里的怪物容器
	 * @author lyh 
	 * @return 
	 */  
	public Map<Long, IMapObject> getMonsterViewMap();
}
