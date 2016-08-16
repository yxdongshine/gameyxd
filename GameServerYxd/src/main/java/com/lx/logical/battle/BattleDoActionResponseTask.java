package com.lx.logical.battle;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.battle.Battle.BattleDoActionRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.SpaceManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:BattleDoActionResponseTask <br/>
 * Function: (客户端发出攻击动作). <br/>
 * Date: 2015-8-7 下午4:39:39 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_BATTLE_DO_ACTION_REQUEST_VALUE)
public class BattleDoActionResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private SpaceManage spaceManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		
		BattleDoActionRequest reqMsg = BattleDoActionRequest.parseFrom(msg.getBody());
		int skillId = reqMsg.getSkillId();
		int skillUnitId = reqMsg.getSkillUnitId();
		PbPosition3D pos = reqMsg.getSkillPos();
		PbPosition3D rotation = reqMsg.getSkillRotation();
		PbPosition3D attackerMove = reqMsg.getAttackerMovePos();
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (sp == null) {
			log.error("cant find serverPlayer of ClientId= " + msg.getClientSessionId());
			return;
		}
		
		if (attackerMove != null){//有位移,角色移动
			float oldY = sp.getPosition3D().getY();
			sp.getSpace().move(sp, attackerMove.getX(), attackerMove.getY(), attackerMove.getZ(), attackerMove.getY() - oldY, true);
		}
		
		// 广播
		for (Entry<Long, IMapObject> obj : sp.getPlayerViewMap().entrySet()) {
			IMapObject mb = obj.getValue();
			if (mb.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				mb.getMapObjectMessage().doAction(sp, skillId, skillUnitId, pos, rotation,attackerMove);
			}
		}
	}
	
}
