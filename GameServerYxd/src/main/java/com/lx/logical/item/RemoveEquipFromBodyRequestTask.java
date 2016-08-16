package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.RemoveEquipFromBodyRequest;
import com.loncent.protocol.game.item.Item.RemoveEquipFromBodyResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RemoveEquipFromBodyRequest <br/>
 * Function: TODO (脱掉装备). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-6 下午4:55:02 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_REMOVE_EQUIP_FROM_BODY_REQUEST_VALUE)
public class RemoveEquipFromBodyRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RemoveEquipFromBodyRequest builder = RemoveEquipFromBodyRequest.parseFrom(msg.getBody());
			long ItemDataId = builder.getItemId();
			
			int result = itemLogicManage.takeoff(serverPlayer, ItemDataId, dao);
			if (result < 0) {// 使用失败
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_11), serverPlayer);
			} else {
				/* RemoveEquipFromBodyResponse.Builder responseBuilder = RemoveEquipFromBodyResponse.newBuilder(); responseBuilder.setItemId(equipDataId); responseBuilder.setPos(result);
				 * MessageSend.sendToGate(responseBuilder.build(), serverPlayer); */
				Item item = serverPlayer.getBag().queryItem(ItemDataId, serverPlayer.getRole().getId());
				if (item != null) {
					MessageSend.sendToGate(itemLogicManage.sendDelItemFromBagResponse(ItemDataId, ItemStaticConfig.BAG_TYPE_ON_BODY), serverPlayer);
				}
			}
			
		}
		
	}
	
}
