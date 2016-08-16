package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:UpdateItemToBagResponse <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-2 下午3:15:38 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_UPDATE_ITEM_TO_BAG_RESPONSE_VALUE)
public class UpdateItemToBagResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		long roleId = 0;
		if (serverPlayer != null) {
			roleId = serverPlayer.getRole().getId();
			com.loncent.protocol.game.item.Item.UpdateItemToBagResponse builder = com.loncent.protocol.game.item.Item.UpdateItemToBagResponse.parseFrom(msg.getBody());
			long itemDataId = builder.getItemId();
			int number = builder.getItemNum();
			// byte result=serverPlayer.getBag().updateItemInbag(itemDataId, number, roleId,dao);
			// if(result==-1){
			// MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_14), serverPlayer);
			// }else if(result==0){
			// MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_15), serverPlayer);
			// }else if(result==1){
			// MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_16), serverPlayer);
			// }
		}
		
	}
	
}
