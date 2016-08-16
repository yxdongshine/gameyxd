package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.DelItemFromBagResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:DelItemFromBagResponse <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午6:28:16 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_DEL_ITEM_FROM_BAG_RESPONSE_VALUE)
public class DelItemFromBagResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		DelItemFromBagResponse builder = DelItemFromBagResponse.parseFrom(msg.getBody());
		long ItemDataId = builder.getItemId();
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			serverPlayer.getBag().delItemInBag(ItemDataId, serverPlayer.getRole().getId(), dao, -1);
		}
		
	}
	
}
