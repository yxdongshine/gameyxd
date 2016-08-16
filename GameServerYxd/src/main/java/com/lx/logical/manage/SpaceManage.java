package com.lx.logical.manage;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.map.GateInfo;
import com.engine.config.xml.map.MonsterGroupInfo;
import com.engine.config.xml.map.SpaceInfo;
import com.engine.config.xml.map.SpaceInfoManage;
import com.engine.domain.Role;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.Door;
import com.engine.entityobj.ServerPlayer;
import com.lib.utils.ServerUUID;
import com.loncent.protocol.PublicData.PbAoiEntity;
import com.lx.game.entity.FuBen;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.Monster;
import com.lx.game.monster.action.MonsterAction;

/**
 * ClassName:SpaceManage <br/>
 * Function: (空间管理类). <br/>
 * Date: 2015-7-14 上午11:54:25 <br/>
 * 思路: 1:所有的地图都放在线程池管理
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class SpaceManage {
	
	private static Log log = LogFactory.getLog(SpaceManage.class);
	
	/** 时间间隔 **/
	private final static int INTERAL_TIME = 50;
	
	@Autowired
	private SpaceInfoManage spaceInfoManage;
	@Autowired
	private FuBenManage fuBenManage;
	@Autowired
	private MonsterManage monsterManage;
	
	/** 定时器更新线程 池 **/
	private ScheduledThreadPoolExecutor scheduledThreadPool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);
	
	public SpaceManage() {
	}
	
	/**
	 * 获取传送门数据 getGateType:(). <br/>
	 */
	public GateInfo getGateInfo(long fuBenId, int gateId) {
		FuBen fuBen = fuBenManage.getFuBen(fuBenId);
		if (fuBen == null)
			return null;
		GameSpace space = fuBen.getSpace();
		GateInfo gateInfo = space.getSpaceInfo().gateList.get(gateId);
		return gateInfo;
	}
	
	/**
	 * 创建世界空间 createWorldSpace:(). <br/>
	 */
	public GameSpace createWorldSpace(int mapUid) {
		SpaceInfo spaceInfo = spaceInfoManage.getSpaceInfo(mapUid);
		long sMapUid = mapUid;
		GameSpace space = new GameSpace(spaceInfo, sMapUid);
		// 产生怪物
		createMonsters(spaceInfo.monsterList, space);
		ScheduledFuture<?> future = scheduledThreadPool.scheduleAtFixedRate(space, INTERAL_TIME, INTERAL_TIME, TimeUnit.MILLISECONDS);
		space.setScheduledFuture(future);
		
		return space;
	}
	
	/**
	 * 创建组队副本 createTeamSpace:(). <br/>
	 */
	public GameSpace createTeamSpace(int mapUid) {
		SpaceInfo spaceInfo = spaceInfoManage.getSpaceInfo(mapUid);
		long sMapUid = ServerUUID.getUUID();
		GameSpace space = new GameSpace(spaceInfo, sMapUid);
		// 产生怪物
		createMonsters(spaceInfo.monsterList, space);
		ScheduledFuture<?> future = scheduledThreadPool.scheduleAtFixedRate(space, INTERAL_TIME, INTERAL_TIME, TimeUnit.MILLISECONDS);
		space.setScheduledFuture(future);
		
		return space;
	}
	
	/**
	 * 生成空间ID generateSpaceId:(). <br/>
	 */
	// public int generateSpaceId() {
	// return ++index;
	// }
	
	/**
	 * 创建单人副本 createSingleSpace:(). <br/>
	 */
	public GameSpace createSingleSpace(int mapUid) {
		SpaceInfo spaceInfo = spaceInfoManage.getSpaceInfo(mapUid);
		long sMapUid = ServerUUID.getUUID();
		GameSpace space = new GameSpace(spaceInfo, sMapUid);
		// 产生怪物
		createMonsters(spaceInfo.monsterList, space);
		ScheduledFuture<?> future = scheduledThreadPool.scheduleAtFixedRate(space, INTERAL_TIME, INTERAL_TIME, TimeUnit.MILLISECONDS);
		space.setScheduledFuture(future);
		
		return space;
	}
	
	/**
	 * createMonsters:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param list
	 * @param space
	 */
	public void createMonsters(List<MonsterGroupInfo> list, GameSpace space) {
		int index = 0;
		for (MonsterGroupInfo info : list) {
			for (Integer tid : info.monsterList) {				
				List<AbsMonster> monster = monsterManage.createMonsters(tid, info.x, info.y, info.z, space);
				if(info.isActive == 0) // 未激活 处于睡眠状态
				{
					for(int i=0; i < monster.size(); i++)
					{
						Monster monsterCpy = (Monster)monster.get(i);
						monsterCpy.getAction().setAction(MonsterAction.SUNK_SLEEP);
					}
				}
				space.addMonsterGroup(tid, monster);
			}
		}
	}
	
	/* 客户端移动 clientMove:(). <br/> */
	public void clientMove(ServerPlayer sp, PbAoiEntity pbEntity) {
		long fuBenId = sp.getRole().getFuBenId();
		FuBen fuBen = fuBenManage.getFuBen(fuBenId);
		if (fuBen == null) {
			log.error("clientMove Error. space cant find fuBenId=" + fuBenId);
			return;
		}
		GameSpace space = fuBen.getSpace();
		
		SpaceInfo info = space.getSpaceInfo();
		
		if (!info.isCanWalk(pbEntity.getX(), pbEntity.getY(), pbEntity.getZ())) {
			// log.info("role target pos is cant walk. userName=" + sp.getRole().getUserName() + " targetPos = (" + pbEntity.getX() + "," + pbEntity.getY() + "," + pbEntity.getZ() + ")");
			return;
		}
		
		
		space.move(sp, pbEntity.getX(), pbEntity.getY(), pbEntity.getZ(), pbEntity.getFace(), false);
	}
	
	/**
	 * 获取下一个副本ID <br/>
	 */
	public long getGateNextFuBenId(Role role, int gateId) {
		long fuBenId = role.getFuBenId();
		FuBen fuBen = fuBenManage.getFuBen(fuBenId);
		if (fuBen == null) {
			log.error("getGateNextFuBenId Error. cant find fuBenId=" + fuBenId);
			return 0;
		}
		
		GameSpace space = fuBen.getSpace();
		Door gate = space.getTransGateMaps().get(gateId);
		if (gate == null) {
			return 0;
		}
		
		return gate.getTargetMapId();
	}
	
	/**
	 * 传送角色到默认位置 transRoleToDefault:(). <br/>
	 */
	public void transRoleToDefault(ServerPlayer sp, long sMapUid, float x, float y, float z) {
		Role role = sp.getRole();
		role.setFuBenId(sMapUid);
		role.setX(x);
		role.setY(y);
		role.setZ(z);
		
		// TODO trans
	}
}
