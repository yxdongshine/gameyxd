package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.ArrangeTypeBagRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.ItemManage;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ArrangeTypeBagResponseTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-23 上午10:17:06 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_TYPE_BAG_ARRANGE_REQUEST_VALUE)
public class ArrangeTypeBagRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			ArrangeTypeBagRequest builder = ArrangeTypeBagRequest.parseFrom(msg.getBody());
			int bagType = builder.getBagType();
			if (bagType >= 0) {
				serverPlayer.getBag().getSubBags()[bagType].arrange();
			}
			// 发送排序后的背包数据
			MessageSend.sendToGate(itemLogicManage.sendArrangeTypeBagResponse(serverPlayer, bagType), serverPlayer);
		}
		
	}
	
}
