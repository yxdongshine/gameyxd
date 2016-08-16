package com.lx.game.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.engine.config.xml.map.BlockData;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.ServerSkill;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.ISkill;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.PublicData.MonsterMoveData;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.monster.Monster.MonsterMoveResponse;
import com.loncent.protocol.game.monster.Monster.MonsterStopResponse;
import com.lx.game.monster.action.MonsterAction;
import com.lx.game.send.MessageSend;

/**
 * ClassName:Monster <br/>
 * Function: TODO (怪物对象). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-3 上午9:42:57 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class Monster extends AbsMonster {
	
	/** 怪物行为 **/
	private MonsterAction action;
	/** 怪物移动路径列表 **/
	private List<Position3D> roads = new ArrayList<Position3D>();
	

	@Override
	public void removeHatred(IMapObject obj) {
		// TODO Auto-generated method stub
		// 大于追击范围
		if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			double distance = obj.getPosition3D().getAbsDistance(this.getInitPoint3d());
			if (distance >= monsterPojo.getChaseRange()) {
				npcHatred.removeHatred(obj);
			}
		}
	}
	
	@Override
	public boolean isDie() {
		// TODO Auto-generated method stub
		return bDie;
	}
	
	@Override
	public IFighter getTargetFighter() {
		// TODO Auto-generated method stub
		return targetFighter;
	}
	
	@Override
	public void setTargetFighter(IFighter fighter) {
		// TODO Auto-generated method stub
		targetFighter = fighter;
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
	
	public GameSpace getSpace() {
		// TODO Auto-generated method stub
		return (GameSpace) space;
	}
	
	public MonsterAction getAction() {
		return action;
	}
	
	
	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		if (action != null) {
			action.tick(time);
		}
	}
	
	
	/** 
	 * randomSkill:(). <br/> 
	 * TODO().<br/> 
	 * 随机技能
	 * @author lyh 
	 * @return 
	 */  
	public ISkill randomSkill(){
		int skillId = 0;
		if (monsterPojo.getSkills().length == 1){
			skillId = monsterPojo.getSkills()[0];
		}else{
			int i = ServerRandomUtils.nextInt(monsterPojo.getSkills().length);
			skillId = monsterPojo.getSkills()[1];
		}
	
		ISkill ss = getSkillById(skillId);
		return ss;
	}
	
	/**
	 * getTargetPosition:(). <br/>
	 * TODO().<br/>
	 * 得到目标点
	 * 
	 * @author lyh
	 * @param p应该是出生点
	 * @param MaxRange
	 * @param minRange
	 * @return
	 */
	public Position3D getTargetPosition(Position3D p, int maxRange, int minRange, int targetRange) throws Exception {
		
		try {
			float dx, dz;
			for (int i = 0; i < 10; i++) {
				float xRange = ServerRandomUtils.randomFloat(maxRange - minRange);
				float zRange = ServerRandomUtils.randomFloat(maxRange - minRange);
				if ((int) xRange % 2 == 0) {
					xRange = -xRange;
				}
				if ((int) zRange % 2 == 0) {
					zRange = -zRange;
				}
				
				dx = p.getX() + (xRange < 0 ? (xRange - minRange) : (xRange + minRange));
				dz = p.getZ() + (zRange < 0 ? (zRange - minRange) : (zRange + minRange));
				// 判断 是否超过追击距离内
				if (getInitPoint3d().getAbsDistance(new Position3D(dx, p.getY(), dz)) >= targetRange) {
					continue;
				}
				boolean dh = getSpace().getSpaceInfo().isCanWalk(dx, p.getY(), dz);
				if (dh) {
					// 转到chr再转回来,进行规一化,防止卡在障碍
					return new Position3D(dx, p.getY(), dz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Position3D(p);
	}
	
	/**
	 * roundGrid:(). <br/>
	 * TODO().<br/>
	 * 目标周围随机一个点
	 * 
	 * @author lyh
	 * @param target
	 * @param radius
	 * @return
	 */
	public Position3D randomRoundGrid(GameSpace space, Position3D target, int radius) {
		List<BlockData> list = space.getRoundGrid(target, radius, true);
		int index = 0;
		if (list.size() == 1){
			index = 0;
		}else{
			 index = ServerRandomUtils.randomNum(0, list.size() - 1);
		}
		float x = space.getSpaceInfo().getGridX(list.get(index).col);
		float y = list.get(index).getY();
		float z = space.getSpaceInfo().getGridZ(list.get(index).row);
		return new Position3D(x, y, z);
	}
	
	/**
	 * sendMonsterMoveResponse:(). <br/>
	 * TODO().<br/>
	 * 发送怪物移动点
	 * 
	 * @author lyh
	 * @param list
	 * @param monster
	 */
	public void sendMonsterMoveResponse(List<Position3D> list, Position3D targetPos) {
		
		MonsterMoveResponse.Builder builder = MonsterMoveResponse.newBuilder();
		builder.setMonsterId(getId());
		MonsterMoveData.Builder moveBuilder = MonsterMoveData.newBuilder();
		if (targetPos != null) {
			PbPosition3D pb = PbPosition3D.newBuilder().setX(targetPos.getX()).setY(targetPos.getY()).setZ(targetPos.getZ()).build();
			moveBuilder.setTargetPos(pb);
		}
		
		for (Position3D pos : list) {
			PbPosition3D pb = PbPosition3D.newBuilder().setX(pos.getX()).setY(pos.getY()).setZ(pos.getZ()).build();
			moveBuilder.addPosList(pb);
		}
		
		builder.setMoveData(moveBuilder.build());
		MonsterMoveResponse rep = builder.build();
		for (Map.Entry<Long, IMapObject> entry : getPlayerViewMap().entrySet()) {
			IMapObject mo = (IMapObject) entry.getValue();
			if (mo.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				MessageSend.sendToGate(rep, (ServerPlayer) mo);
			}
		}
	}
	
	/**
	 * sendMonsterStopResponse:(). <br/>
	 * TODO().<br/>
	 * 怪物停止消息
	 * 
	 * @author lyh
	 * @param monster
	 */
	public void sendMonsterStopResponse(Position3D target3d,long targetId) {
		MonsterStopResponse.Builder builder = MonsterStopResponse.newBuilder();
		builder.setX(getPosition3D().getX());
		builder.setY(getPosition3D().getY());
		builder.setZ(getPosition3D().getZ());
		builder.setObjId(getId());
		builder.setTargetId(targetId);
		PbPosition3D pbPos = PbPosition3D.newBuilder().setX(target3d.getX()).setY(target3d.getY()).setZ(target3d.getZ()).build();
		builder.setTargetPos(pbPos);
		MonsterStopResponse rep = builder.build();
		for (Map.Entry<Long, IMapObject> entry : getViewMap().entrySet()) {
			IMapObject obj = entry.getValue();
			if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				MessageSend.sendToGate(rep, (ServerPlayer) obj);
			}
		}
	}
	
	public List<Position3D> getRoads() {
		return roads;
	}
	
	public void setRoads(List<Position3D> roads) {
		this.roads = roads;
	}

	public void setAction(MonsterAction action) {
    	this.action = action;
    }
	
}
