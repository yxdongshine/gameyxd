package com.lx.game.entity;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.domain.Role;
import com.engine.entityobj.PlayerStatus;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.entityobj.space.IMapObjectMessage;
import com.loncent.protocol.PublicData.MonsterMoveData;
import com.loncent.protocol.PublicData.PbAoiEntity;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.battle.Battle.BattleDoActionUpdateResponse;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffActionUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffStatusUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffActionUpdateResponse.Builder;
import com.loncent.protocol.game.space.Space.SpaceAoiMoveResponse;
import com.loncent.protocol.game.space.Space.SpaceAoiUpdateResponse;
import com.lx.game.send.MessageSend;

/**
 * ClassName:MapObjectListener <br/>
 * Function: TODO (地图对象广播监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-28 下午8:15:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MapObjectMessage implements IMapObjectMessage {
	private IMapObject mapObject;
	protected static Log log = LogFactory.getLog(MapObjectMessage.class);
	
	@Override
	public void init(IMapObject obj) {
		// TODO Auto-generated method stub
		mapObject = obj;
	}
	
	private PbAoiEntity createPbAoiEntity(IMapObject obj, PbAoiEntityType type) {
		PbAoiEntity.Builder builder = PbAoiEntity.newBuilder();
		if (type == PbAoiEntityType.DropItem){
			DropItem dropItem = (DropItem)obj;
			if (dropItem.getProtectTime() > 0 ){//道具剩余保护时间
				builder.setFace((int)(dropItem.getProtectTime() - System.currentTimeMillis()));
				builder.setViewDistance((int)(dropItem.getDisappearTime()- System.currentTimeMillis()));//道具剩余消失时间
				builder.setOwnerId(dropItem.getOwnerId());
				builder.setDropItemType(dropItem.getDropType());
				builder.setTid(dropItem.getItemConfigId());
				builder.setAction(dropItem.getNum());
			}else{
				
			}
		}else{
			builder.setFace(obj.getDir());
		}
		builder.setId(obj.getId());
		if (type == PbAoiEntityType.Role) {
			ServerPlayer sp = (ServerPlayer) obj;
			builder.setTid(sp.getRole().getCareerConfigId() + 20000);
			builder.setCareerId(sp.getRole().getCareerConfigId());
			builder.setLevel(sp.getLevel());
			builder.setName(sp.getRole().getRoleName());
		}
		if (obj.getName() != null){
			builder.setName(obj.getName());
		}else{
			//log.error(obj.getId()+"obj:::"+obj.getType());
		}
		builder.setType(type);
		builder.setX(obj.getPosition3D().getX());
		builder.setY(obj.getPosition3D().getY());
		builder.setZ(obj.getPosition3D().getZ());
		if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {// 怪物特殊处理
			Monster monster = (Monster) obj;
			MonsterMoveData.Builder moveBuilder = MonsterMoveData.newBuilder();
			if (monster.getTargetFighter() != null) {
				Position3D targetPos = monster.getTargetFighter().getPosition3D();
				PbPosition3D pb = PbPosition3D.newBuilder().setX(targetPos.getX()).setY(targetPos.getY()).setZ(targetPos.getZ()).build();
				moveBuilder.setTargetPos(pb);
			}
			
			List<Position3D> roads = monster.getRoads();
			for (Position3D p : roads) {
				PbPosition3D pos = PbPosition3D.newBuilder().setX(p.getX()).setY(p.getY()).setZ(p.getZ()).build();
				moveBuilder.addPosList(pos);
			}
			builder.setMonsterMove(moveBuilder.build());
		}
		return builder.build();
		
	}
	
	@Override
	public void addView(IMapObject obj) {
		if (mapObject.getType() != IMapObject.MAP_OBJECT_TYPE_PLAYER || mapObject.getId() == obj.getId()) {
			return;
		}
		SpaceAoiUpdateResponse.Builder msg = SpaceAoiUpdateResponse.newBuilder();
		msg.addNewList(createPbAoiEntity(obj, PbAoiEntityType.valueOf(obj.getType())));
		// System.err.println(mapObject.getId() + "add 1::" + mapObject.getSpace().getSpaceId() + "::mapObjectLeft::" + obj.getId() + ":" + obj.getSpace().getSpaceId());
		
		// TODO Auto-generated method stub
		MessageSend.sendToGate(msg.build(), (ServerPlayer) mapObject);
	}
	
	@Override
	public void addView(List<IMapObject> objects) {
		// TODO Auto-generated method stub
		if (mapObject.getType() != IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			return;
		}
		SpaceAoiUpdateResponse.Builder msg = SpaceAoiUpdateResponse.newBuilder();
		for (IMapObject obj : objects) {
			if (obj.getId() == mapObject.getId())
				continue;
			msg.addNewList(createPbAoiEntity(obj, PbAoiEntityType.valueOf(obj.getType())));
			// System.err.println(mapObject.getId() + "add2::" + mapObject.getSpace().getSpaceId() + "::mapObjectLeft::" + obj.getId() + ":" + obj.getSpace().getSpaceId());
		}
		MessageSend.sendToGate(msg.build(), (ServerPlayer) mapObject);
	}
	
	@Override
	public void leftView(IMapObject obj) {
		// TODO Auto-generated method stub
		if (mapObject.getType() != IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			return;
		}
		SpaceAoiUpdateResponse.Builder msg = SpaceAoiUpdateResponse.newBuilder();
		msg.addOutList(createPbAoiEntity(obj, PbAoiEntityType.valueOf(obj.getType())));
		MessageSend.sendToGate(msg.build(), (ServerPlayer) mapObject);
		// System.err.println(mapObject.getId() + "::" + mapObject.getSpace().getSpaceId() + "::mapObjectLeft::" + obj.getId() + ":" + obj.getSpace().getSpaceId());
	}
	
	@Override
	public void leftView(List<IMapObject> objects) {
		// TODO Auto-generated method stub
		if (mapObject.getType() != IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			return;
		}
		SpaceAoiUpdateResponse.Builder msg = SpaceAoiUpdateResponse.newBuilder();
		for (IMapObject obj : objects) {
			msg.addOutList(createPbAoiEntity(obj, PbAoiEntityType.valueOf(obj.getType())));
			// System.err.println(mapObject.getId() + ":111:" + mapObject.getSpace().getSpaceId() + "::mapObjectLeft::" + obj.getId() + ":" + obj.getSpace().getSpaceId());
		}
		MessageSend.sendToGate(msg.build(), (ServerPlayer) mapObject);
		
	}
	
	@Override
	public void moveView(IMapObject moveObj) {
		// TODO Auto-generated method stub
		if (mapObject.getType() != IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			return;
		}
		ServerPlayer sp = ((ServerPlayer) mapObject);
		if (sp.getStatus() != PlayerStatus.STATUS_ENTER_MAP) {
			return;
		}
		SpaceAoiMoveResponse.Builder msg = SpaceAoiMoveResponse.newBuilder();
		msg.setToX(moveObj.getPosition3D().getX());
		msg.setToY(moveObj.getPosition3D().getY());
		msg.setToZ(moveObj.getPosition3D().getZ());
		msg.setFace(moveObj.getDir());
		msg.setId(moveObj.getId());
		msg.setType(PbAoiEntityType.valueOf(moveObj.getType()));
		// Role role = ((ServerPlayer) mapObject).getRole();
		// Role role2 = ((ServerPlayer) moveObj).getRole();
		// System.err.println("move notify to id=" + mapObject.getId()
		// + " getUserName=  " + role.getUserName() + " moveId=" + moveObj.getId()
		// + " UserName="+ role2.getUserName());
		MessageSend.sendToGate(msg.build(), (ServerPlayer) mapObject);
	}
	
	@Override
	public void doAction(IMapObject obj, int skillId, int skillUintId, PbPosition3D pos, PbPosition3D rotation,PbPosition3D attackerMove) {
		
		BattleDoActionUpdateResponse.Builder updateMsg = BattleDoActionUpdateResponse.newBuilder();
		updateMsg.setId(obj.getId());
		updateMsg.setSkillId(skillId);
		updateMsg.setSkillUnitId(skillUintId);
		updateMsg.setSkillPos(pos);
		updateMsg.setSkillRotation(rotation);
		if (attackerMove != null){
			updateMsg.setAttackerMovePos(attackerMove);
		}
		updateMsg.setType(PbAoiEntityType.valueOf(obj.getType()));
		MessageSend.sendToGate(updateMsg.build(), (ServerPlayer) mapObject);
	}
	
	@Override
	public void hurtAction(BattleHurtUpdateResponse.Builder hurtUpdateMsg) {
		MessageSend.sendToGate(hurtUpdateMsg.build(), (ServerPlayer) mapObject);
	}

	@Override
    public void buffAction(BuffActionUpdateResponse.Builder builder) {
	   MessageSend.sendToGate(builder.build(), (ServerPlayer) mapObject);	    
    }

	@Override
    public void buffStatus(BuffStatusUpdateResponse.Builder builder) 
	{
		MessageSend.sendToGate(builder.build(), (ServerPlayer) mapObject);	 
    }
}
