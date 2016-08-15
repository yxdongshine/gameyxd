package com.engine.entityobj.space;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.engine.config.xml.map.GateInfo;
import com.engine.config.xml.map.NpcInfo;
import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.AbsDropItem;
import com.engine.entityobj.Door;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.Npc;
import com.engine.entityobj.ServerPlayer;
import com.engine.interfaces.TimeTasker;
import com.loncent.protocol.game.monster.Monster;


/**
 * ClassName:Space <br/>
 * Function: 空间(场景,地图). <br/>
 * Date: 2015-7-13 下午4:04:19 <br/>
 * 地图数据层(以后看需求是否要抽成接口)
 * 
 * @author jack
 * @version
 * @see
 */
public abstract class AbsSpace extends TimeTasker {
	/** monster索引起始值 **/
	protected int monsterIdIndex = 30000000;
	/** npc索引起始值 **/
	protected int npcIdIndex = 20000000;
	
	/** 空间AOI **/
	protected Aoi aoi;
	
	/** 空间宽度(x) **/
	protected int width;
	
	/** 空间长度(z) **/
	protected int length;
	
	/** 空间id,这个就是地图信息的id,如果是副本时,请再创建一个变量 **/
	protected int mapUid;
	
	/** space唯一ID，如果是公用副本，则等于spaceId，反之为UUID **/
	protected long serverMapUid;
	
	/** 名称 **/
	protected String name;
	
	/** 等级 **/
	protected int needLevel;
	
	/** 类型 **/
	protected int mapType;
	
	/** 地图空间信息 **/
	protected SpaceInfo spaceInfo;
	
	/** 当前地图上的集合 **/
	protected ConcurrentHashMap<Long, IMapObject> mapObjects = new ConcurrentHashMap<Long, IMapObject>();
	/** 地图上的怪物容器 **/
	protected ConcurrentHashMap<Long, AbsMonster> monsterMaps = new ConcurrentHashMap<Long, AbsMonster>();
	/** 地图上的怪物组 **/  
	protected ConcurrentHashMap<Integer, List<AbsMonster>> monsterGroup = new ConcurrentHashMap<Integer, List<AbsMonster>>();
	
	/** 怪物死亡容器 **/
	protected ConcurrentHashMap<Long, AbsMonster> monsterDeadMaps = new ConcurrentHashMap<Long, AbsMonster>();
	
	/** 地图上的npc容器 **/
	protected ConcurrentHashMap<Long, Npc> npcMaps = new ConcurrentHashMap<Long, Npc>();
	/** 地图上的player容器 **/
	protected ConcurrentHashMap<Long, ServerPlayer> playerMaps = new ConcurrentHashMap<Long, ServerPlayer>();
	
	/** 传送门容器 **/
	protected HashMap<Long, Door> transGateMaps = new HashMap<Long, Door>();
	
	/** 地图上道具掉落**/
	protected ConcurrentHashMap<Long, AbsDropItem> dropItemMaps = new ConcurrentHashMap<Long, AbsDropItem>();
	
	public AbsSpace(SpaceInfo tpl, long sMapUid) {
		this.width = tpl.totalWidth;
		this.length = tpl.totalLength;
		this.mapUid = tpl.mapUid;
		this.serverMapUid = sMapUid;
		spaceInfo = tpl;
		this.aoi = new Aoi(this.width, this.length);		
	}
	
	/**
	 * 清理空间 clearSpace:(). <br/>
	 */
	public void clearSpace() {
		this.mapObjects.clear();
		this.transGateMaps.clear();
		this.dropItemMaps.clear();
		this.playerMaps.clear();
		this.npcMaps.clear();
		this.monsterDeadMaps.clear();
		this.monsterMaps.clear();
	}
	
	@Override
	public void onTime(long time) {
		// TODO Auto-generated method stub
		// System.err.println(this);
		try {
			// 活着的更新怪物
			for (Map.Entry<Long, AbsMonster> mm : monsterMaps.entrySet()) {
				AbsMonster m = mm.getValue();
				m.tick(time);
			}
			
			for (Map.Entry<Long, AbsMonster> mm : monsterDeadMaps.entrySet()) {
				AbsMonster m = mm.getValue();
				m.tick(time);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public Aoi getAoi() {
		return aoi;
	}
	
	public void setAoi(Aoi aoi) {
		this.aoi = aoi;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getMapUid() {
		return mapUid;
	}
	
	public void setMapUid(int mapUid) {
		this.mapUid = mapUid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getNeedLevel() {
		return needLevel;
	}
	
	public void setNeedLevel(int needLevel) {
		this.needLevel = needLevel;
	}
	
	public int getMapType() {
		return mapType;
	}
	
	public void setMapType(int mapType) {
		this.mapType = mapType;
	}
	
	public SpaceInfo getSpaceInfo() {
		return spaceInfo;
	}
	
	public void setSpaceInfo(SpaceInfo spaceInfo) {
		this.spaceInfo = spaceInfo;
	}
	
	public ConcurrentHashMap<Long, IMapObject> getMapObjects() {
		return mapObjects;
	}
	
	// public void setMapObjects(ConcurrentHashMap<Long, IMapObject> mapObjects)
	// {
	// this.mapObjects = mapObjects;
	// }
	
	public ConcurrentHashMap<Long, AbsMonster> getMonsterMaps() {
		return monsterMaps;
	}
	
	// public void setMonsterMaps(ConcurrentHashMap<Long, AbsMonster>
	// monsterMaps) {
	// this.monsterMaps = monsterMaps;
	// }
	
	public ConcurrentHashMap<Long, Npc> getNpcMaps() {
		return npcMaps;
	}
	
	// public void setNpcMaps(ConcurrentHashMap<Long, Npc> npcMaps) {
	// this.npcMaps = npcMaps;
	// }
	
	public ConcurrentHashMap<Long, ServerPlayer> getPlayerMaps() {
		return playerMaps;
	}
	
	// public void setPlayerMaps(ConcurrentHashMap<Long, ServerPlayer>
	// playerMaps) {
	// this.playerMaps = playerMaps;
	// }
	
	public HashMap<Long, Door> getTransGateMaps() {
		return transGateMaps;
	}
	
	// public void setTransGateMaps(HashMap<Long, Door> transGateMaps) {
	// this.transGateMaps = transGateMaps;
	// }
	
	/**
	 * createNpc:(). <br/>
	 * TODO().<br/>
	 * 创建 npc
	 * 
	 * @author lyh
	 * @param npcInfo
	 */
	protected Npc createNpc(NpcInfo npcInfo) {
		Npc npc = new Npc();
		npc.setId(generateNpcId());
		npc.setNpcInfo(npcInfo);
		npc.setName(npcInfo.name);
		npc.setDir(npcInfo.face);
		npc.setType(IMapObject.MAP_OBJECT_TYPE_NPC);
		npc.setSpace(this);
		npc.setPosition3D(npcInfo.x, npcInfo.y, npcInfo.z);
		
		return npc;
	}
	
	/**
	 * createDoor:(). <br/>
	 * TODO().<br/>
	 * 创建传送门
	 * 
	 * @author lyh
	 * @param gateInfo
	 * @return
	 */
	protected Door createDoor(GateInfo gateInfo) {
		Door door = new Door();
		door.setId(gateInfo.tid);
		door.setTargetMapId(gateInfo.toTargetId);
		door.setType(IMapObject.MAP_OBJECT_TYPE_DOOR);
		door.setSpace(this);
		door.setPosition3D(gateInfo.x, gateInfo.y, gateInfo.z);
		return door;
	}
	
	/**
	 * 生成怪物ID generateMonsterId:(). <br/>
	 */
	public int generateMonsterId() {
		return ++monsterIdIndex;
	}
	
	/**
	 * 生成npcId generateNpcId:(). <br/>
	 */
	public int generateNpcId() {
		return ++npcIdIndex;
	}
	
	
	public ConcurrentHashMap<Long, AbsMonster> getMonsterDeadMaps() {
		return monsterDeadMaps;
	}
	
	public long getServerMapUid() {
		return serverMapUid;
	}
	
	public void setServerMapUid(long serverMapUid) {
		this.serverMapUid = serverMapUid;
	}

	public ConcurrentHashMap<Long, AbsDropItem> getDropItemMaps() {
    	return dropItemMaps;
    }
	
	public void addMonsterGroup(int monsterGroupId, List<AbsMonster> monsterList)
	{
		this.monsterGroup.put(monsterGroupId, monsterList);
	}
	
	public void removeMonsterGronp(int monsterGroupId)
	{
		this.monsterGroup.remove(monsterGroupId);
	}
	
	public abstract void doMapObjectAddView(IMapObject obj, List<IArea> nineGridList);
	
	public abstract void doMapObjectLeftView(IMapObject obj);
	
	public abstract void move(IMapObject sp, float newX, float newY, float newZ, float dir, boolean monster);
}
