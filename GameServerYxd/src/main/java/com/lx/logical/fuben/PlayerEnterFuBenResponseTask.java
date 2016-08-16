package com.lx.logical.fuben;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.PlayerStatus;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.lib.utils.IConst;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.entity.FuBen;
import com.loncent.protocol.game.fuben.FuBen.PlayerEnterFuBenRequest;
import com.loncent.protocol.game.fuben.FuBen.PlayerEnterFuBenResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.FuBenManage;
import com.lx.nserver.txt.FuBenTemplatePojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:PlayerEnterFuBenResponseTask <br/>
 * Function: (玩家进入副本). <br/>
 * Date: 2015-8-22 下午4:34:24 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_PLAYER_ENTER_FUBEN_REQUEST_VALUE)
public class PlayerEnterFuBenResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private FuBenManage fuBenManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		PlayerEnterFuBenResponse.Builder repMsg = PlayerEnterFuBenResponse.newBuilder();
		PlayerEnterFuBenRequest reqMsg = PlayerEnterFuBenRequest.parseFrom(msg.getBody());
		int mapUid = reqMsg.getMapUid();
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		
		FuBenTemplatePojo pojo = fuBenManage.getFuBenPojo(mapUid);
		if (pojo == null) {
			// TODO mapUID错误
			return;
		}
		
		if (pojo.getType() != IConst.FUBEN_TYPE_SINGLE) {
			// TODO 该mapUid不是单人副本
			return;
		}
		/* // 等级 if(role.getRoleLevel() < pojo.getEnterLevel()) { MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_21), sp); return; } // 体力 if(role.getVits() < pojo.getCostManual()) {
		 * MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_22), sp); return; } */
		// 创建副本
		FuBen fuBen = fuBenManage.createSingleFuBen(mapUid);
		fuBen.startFuBen();
		
		fuBenManage.willEnterFuBenMaps.put(role.getId(), fuBen.getFubenId());
		sp.setStatus(PlayerStatus.STATUS_TRANS_MAP);
		
		repMsg.setMapUid(mapUid);
		MessageSend.sendToGate(repMsg.build(), sp);
	}
	
}
