package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.DelItemFromBagRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.res.item.Item;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:DelItemFromBagRequestTask <br/>
 * Function: TODO (删除物品). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午6:28:16 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_DEL_ITEM_FROM_BAG_REQUEST_VALUE)
public class DelItemFromBagRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		DelItemFromBagRequest builder = DelItemFromBagRequest.parseFrom(msg.getBody());
		long ItemDataId = builder.getItemId();
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			Item item = serverPlayer.getBag().delItemInBag(ItemDataId, serverPlayer.getRole().getId(), dao, -1);
			if (item != null) {
				MessageSend.sendToGate(itemLogicManage.sendDelItemFromBagResponse(ItemDataId, item.getItemData().getItemType()), serverPlayer);
			}
		}
		
	}
	
}
