package com.lx.logical.battle;

import java.util.Random;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.battle.Battle.BattleHurtRequest;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateMsg;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateResponse;
import com.loncent.protocol.game.battle.Battle.BattleHurtRequest.BattleHurtMsg;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.SpaceManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:BattleHurtResponse <br/>
 * Function: (客服端判定伤害). <br/>
 * Date: 2015-8-7 下午5:42:57 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_BATTLE_HURT_REQUEST_VALUE)
public class BattleHurtResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private SpaceManage spaceManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		
		BattleHurtRequest reqMsg = BattleHurtRequest.parseFrom(msg.getBody());
		
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (sp == null) {
			log.error("cant find serverPlayer of ClientId= " + msg.getClientSessionId());
			return;
		}
		sp.getFightAttackQueue().offer(reqMsg);
		
//		Role role = sp.getRole();
//		// TODO技能伤害判定
//		
//		// 组装受伤消息
//		BattleHurtUpdateResponse.Builder updateMsg = BattleHurtUpdateResponse.newBuilder();
//		updateMsg.setAttackerId(role.getId());
//		updateMsg.setAttackerType(PbAoiEntityType.Role);
//		for (BattleHurtMsg hurt : reqMsg.getHurtListList()) {
//			BattleHurtUpdateMsg.Builder hurtUpdate = BattleHurtUpdateMsg.newBuilder();
//			hurtUpdate.setBeHurtId(hurt.getBeHurtId());
//			hurtUpdate.setHurtValue((int) (100 + Math.random() * 100));
//			hurtUpdate.setType(hurt.getType());
//			updateMsg.addHurtList(hurtUpdate);
//		}
//		
//		// 广播
//		for (Entry<Long, IMapObject> obj : sp.getViewMap().entrySet()) {
//			IMapObject mb = obj.getValue();
//			if (mb.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
//				BattleHurtUpdateResponse.Builder cloneMsg = updateMsg.clone();
//				mb.getMapObjectMessage().hurtAction(cloneMsg);
//			}
//		}
//		// 发送给自己
//		MessageSend.sendToGate(updateMsg.build(), sp);
	}
	
}
