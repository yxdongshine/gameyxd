package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.PutOnEquipToBodyResquest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.logical.manage.PlayerManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:PutOnEquipToBodyResquest <br/>
 * Function: TODO (穿上装备). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-6 下午3:15:19 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_PUTON_EQUIP_TO_BODY_REQUEST_VALUE)
public class PutOnEquipToBodyResquestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			PutOnEquipToBodyResquest builder = PutOnEquipToBodyResquest.parseFrom(msg.getBody());
			long equipmentDataId = builder.getItemId();
			int solt = -1;
			int result = itemLogicManage.putEquipment(serverPlayer, equipmentDataId, dao);
			if (result < 0) {// 使用失败
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_11), serverPlayer);
			} else {
				solt = result;
				itemLogicManage.sendPutOnEquipToBodyResponse(equipmentDataId, solt, serverPlayer);
			}
		}
	}
	
}
