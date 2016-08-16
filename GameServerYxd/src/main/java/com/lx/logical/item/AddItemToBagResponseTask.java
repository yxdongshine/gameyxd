package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;
import com.lx.game.send.*;

/**
 * 添加道具到背包 ClassName:AddItemToBagResponse <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-2 下午2:07:10 <br/>
 * 
 * @author yxd
 * 
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_ADD_ITEM_TO_BAG_RESPONSE_VALUE)
public class AddItemToBagResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		/* com.loncent.protocol.game.item.Item.AddItemToBagResponse builder= com.loncent.protocol.game.item.Item.AddItemToBagResponse.parseFrom(msg.getBody()); //根据回话取出游戏玩家对象 ServerPlayer
		 * serverPlayer= GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId()); if(serverPlayer!=null){ int itemDataId= (int) builder.getItemData().getItemId(); int
		 * itemConfigId=builder.getItemData().getItemTypeId(); int count=builder.getItemData().getItemNum(); int result=serverPlayer.getBag().putItemInBag(itemDataId, count,
		 * serverPlayer.getRole().getId(),dao); if(result==-1){ MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_14), serverPlayer); } } */
	}
	
}
