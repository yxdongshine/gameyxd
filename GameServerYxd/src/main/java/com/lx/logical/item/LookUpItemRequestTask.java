package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.domain.ItemData;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.LookUpItemRequest;
import com.loncent.protocol.game.item.Item.LookUpItemResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.EquitItem;
import com.lx.game.res.item.Item;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:LookUpItemRequest <br/>
 * Function: TODO (查看具体id的道具). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-6 下午3:00:14 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_LOOK_UP_ITEM_REQUEST_VALUE)
public class LookUpItemRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			LookUpItemRequest builder = LookUpItemRequest.parseFrom(msg.getBody());
			long ItemDataId = builder.getItemId();
			Item item = serverPlayer.getBag().queryItem(ItemDataId, serverPlayer.getRole().getId());
			LookUpItemResponse.Builder responseBulider = LookUpItemResponse.newBuilder();
			if (item != null && item.getItemData() != null) {
				ItemData itemData = item.getItemData();
				responseBulider.setItemId(itemData.getId());
				responseBulider.setItemNum(itemData.getNumber());
				responseBulider.setItemType(itemData.getItemType());
				responseBulider.setItemTypeId(itemData.getConfigId());
				responseBulider.setQuality(itemData.getQuality());
				responseBulider.setIndexInBag(itemData.getIndexInBag());
				if (item instanceof EquitItem) {
					responseBulider.setPos(itemData.getPos());
					responseBulider.setScore(itemData.getScore());
					responseBulider.setSocket(itemData.getSocket());
				}
				
				MessageSend.sendToGate(responseBulider.build(), serverPlayer);
			}
		}
	}
	
}
