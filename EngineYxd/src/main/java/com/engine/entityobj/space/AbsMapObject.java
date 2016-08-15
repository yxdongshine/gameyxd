package com.engine.entityobj.space;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.engine.domain.Role;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.interfaces.IFightListener;
import com.engine.interfaces.IListener;

/**
 * ClassName:AbsMapObject <br/>
 * Function: TODO (地图对象的数据层). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-28 下午4:15:36 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class AbsMapObject implements IMapObject {
	/** 视野距离10米 **/
	public static final int VIEW_DISTANCE = 50;
	/** 怪物惟一id **/
	protected long id;
	/** 怪物属性 **/
	protected Attribute attribute;
	/** 3d坐标 **/
	protected Position3D point3d;
	
	/** 对象类型 **/
	protected int type;
	
	/** 角色名 **/
	protected String name;
	
	/** 方向 **/
	protected float dir;
	
	/** 区域 **/
	protected IArea area;
	
	/** 地图 **/
	protected AbsSpace space;
	
	/** 视野容器 **/
	protected Map<Long, IMapObject> viewMap = new ConcurrentHashMap<Long, IMapObject>();
	
	/** 视野容器(角色) **/
	protected Map<Long, IMapObject> playerViewMap = new ConcurrentHashMap<Long, IMapObject>();
	
	/** 视野容器(怪物) **/
	protected Map<Long, IMapObject> monsterViewMap = new ConcurrentHashMap<Long, IMapObject>();
	
	/** 广播消息监听器 **/
	protected IMapObjectMessage messageSend;
	

	
	@Override
	public void removeHatred(IMapObject obj) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		if (this.getPosition3D() != null) {
			getPosition3D().setX(x);
		}
	}
	
	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		if (this.getPosition3D() != null) {
			getPosition3D().setY(y);
		}
	}
	
	@Override
	public void setZ(float z) {
		// TODO Auto-generated method stub
		if (this.getPosition3D() != null) {
			getPosition3D().setZ(z);
		}
	}
	
	@Override
	public void leftView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		// Role role = ((ServerPlayer) this).getRole();
		// Role role2 = ((ServerPlayer) mapObj).getRole();
		// System.err.println("left notify to UserName=  " + role.getUserName() + " leftUserName=" + role2.getUserName());
		viewMap.remove(mapObj.getId());
		if (mapObj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER){
			playerViewMap.remove(mapObj.getId());
		}else if (mapObj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER){
			monsterViewMap.remove(mapObj.getId());
		}
	}
	
	@Override
	public boolean isInView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		return viewMap.get(mapObj.getId()) != null;
	}
	
	@Override
	public void addView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		// Role role = ((ServerPlayer) this).getRole();
		// Role role2 = ((ServerPlayer) mapObj).getRole();
		// System.err.println("add notify to UserName=  " + role.getUserName() + " enterUserName=" + role2.getUserName());
		viewMap.put(mapObj.getId(), mapObj);
		if (mapObj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER){
			playerViewMap.put(mapObj.getId(),mapObj);
		}else if (mapObj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER){
			monsterViewMap.put(mapObj.getId(),mapObj);
		}
	}
	
	@Override
	public void clearView() {
		// TODO Auto-generated method stub
		viewMap.clear();
		playerViewMap.clear();
		monsterViewMap.clear();
	}
	
	@Override
	public boolean isView(IMapObject mapObj) {
		// TODO Auto-generated method stub
		return isInViewDistance(mapObj.getPosition3D());
	}
	
	@Override
	public AbsSpace getSpace() {
		// TODO Auto-generated method stub
		return space;
	}
	
	@Override
	public void setSpace(AbsSpace space) {
		// TODO Auto-generated method stub
		this.space = space;
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	@Override
	public Position3D getPosition3D() {
		// TODO Auto-generated method stub
		return point3d;
	}
	
	@Override
	public void setPosition3D(float x, float y, float z) {
		// TODO Auto-generated method stub
		point3d = new Position3D(x, y, z);
	}
	
	@Override
	public Attribute getAttribute() {
		// TODO Auto-generated method stub
		return attribute;
	}
	
	@Override
	public void setAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		this.attribute = attr;
	}
	
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return type;
	}
	
	@Override
	public void setType(int type) {
		// TODO Auto-generated method stub
		this.type = type;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	@Override
	public void setName(String str) {
		// TODO Auto-generated method stub
		name = str;
	}
	
	@Override
	public float getDir() {
		// TODO Auto-generated method stub
		return dir;
	}
	
	@Override
	public void setDir(float dir) {
		// TODO Auto-generated method stub
		this.dir = dir;
	}
	
	@Override
	public void setArea(IArea area) {
		// TODO Auto-generated method stub
		this.area = area;
	}
	
	@Override
	public IArea getArea() {
		// TODO Auto-generated method stub
		return area;
	}
	
	public Map<Long, IMapObject> getViewMap() {
		return viewMap;
	}
	
	public void setViewMap(Map<Long, IMapObject> viewMap) {
		this.viewMap = viewMap;
	}
	
	@Override
	public IMapObjectMessage getMapObjectMessage() {
		// TODO Auto-generated method stub
		return messageSend;
	}
	
	@Override
	public void setMapObjectMessage(IMapObjectMessage listener) {
		// TODO Auto-generated method stub
		messageSend = listener;
	}
	
	/**
	 * 判断otherPos是否在视野范围内 isInViewDistance:(). <br/>
	 * ().<br/>
	 */
	public boolean isInViewDistance(Position3D otherPos) {
		// return this.pos3d.isInRange(otherPos, this.viewDistance);
		double distance = this.point3d.getAbsDistance(otherPos);
		return distance >= VIEW_DISTANCE ? false : true;
	}
	
	@Override
	public void setIFightListener(IFightListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public IFightListener getIFightListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void setListener(IListener listener) {
	    // TODO Auto-generated method stub
	
    }

	
	public Map<Long, IMapObject> getPlayerViewMap() {
    	return playerViewMap;
    }

	public Map<Long, IMapObject> getMonsterViewMap() {
    	return monsterViewMap;
    }


	
}
