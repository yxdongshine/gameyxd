package com.lx.logical.space;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.PublicData.PbAoiEntity;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.game.entity.GameSpace;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.BuffManage;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.MonsterManage;
import com.lx.logical.manage.SpaceManage;
import com.lx.server.mina.session.IConnect;
import com.loncent.protocol.game.space.Space.SpaceClientSceneDataRequest;
import com.loncent.protocol.game.space.Space.SpaceClientSceneDataResponse;

/**
 * ClassName:SpaceClientSceneDataResponseTask <br/>
 * Function: (请求场景数据). <br/>
 * Date: 2015-7-29 下午3:26:42 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_SCENE_DATA_REQUEST_VALUE)
public class SpaceClientSceneDataResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private MonsterManage monsterManage;
	@Autowired
	private FuBenManage fuBenManage;
	@Autowired
	private BuffManage buffManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		SpaceClientSceneDataResponse.Builder repMsg = SpaceClientSceneDataResponse.newBuilder();
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		
		long fuBenId = fuBenManage.willEnterFuBenMaps.get(role.getId());
		FuBen fuBen = fuBenManage.getFuBen(fuBenId);
		if (fuBen == null) {
			log.error("cant find fubenId = " + fuBenId);
			return;
		}
		
		GameSpace space = fuBen.getSpace();
		for (Map.Entry<Long, IMapObject> entry : space.getMapObjects().entrySet()) {
			IMapObject mObj = entry.getValue();
			PbAoiEntityType type = PbAoiEntityType.valueOf(mObj.getType());
			// 过滤掉自己的数据
			if (mObj.getId() == role.getId() || mObj.getType() == PbAoiEntityType.Monster_VALUE)
				continue;
			log.info(" scene data id = " + mObj.getId());
			PbAoiEntity.Builder pbEntity = PbAoiEntity.newBuilder();
			pbEntity.setId(mObj.getId());
			pbEntity.setFace(mObj.getDir());
			pbEntity.setX(mObj.getPosition3D().getX());
			pbEntity.setY(mObj.getPosition3D().getY());
			pbEntity.setZ(mObj.getPosition3D().getZ());
			if (mObj.getType() == PbAoiEntityType.Role_VALUE) {
				// 查找角色的职业id
				ServerPlayer oSp = (ServerPlayer) mObj;
				pbEntity.setTid(oSp.getRole().getCareerConfigId() + 20000);
				pbEntity.setCareerId(oSp.getRole().getCareerConfigId());
				pbEntity.setLevel(oSp.getLevel());
				pbEntity.setName(oSp.getRole().getRoleName());
			} else if (mObj.getType() == PbAoiEntityType.Monster_VALUE) {
				// int tid =monsterManager.getTid(long roleId);
				int tid = 24000;// 山贼
				pbEntity.setTid(tid);
			} else if (mObj.getType() == PbAoiEntityType.Npc_VALUE) {
				int tid = 21000;
				pbEntity.setTid(tid);
			}
			
			pbEntity.setType(type);
			repMsg.addAllEntity(pbEntity);
		}
				
		// 发送怪物
		monsterManage.sendInitMonstersResponse(space.getMonsterMaps(), sp);
		// TODO 发送NPC
		monsterManage.sendInitNpcsResponse(space.getNpcMaps(), sp);
		// TODO 发送其他玩家
		
		// TODO 发送传送阵
		// 测试添加buff
		//buffManage.addBuff(sp, PbAoiEntityType.Role_VALUE, 1);
		
		MessageSend.sendToGate(repMsg.build(), sp);
	}
	
}
