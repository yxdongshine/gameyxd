package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.BuyItemRequest;
import com.loncent.protocol.game.item.Item.BuyItemResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:BuyItemRequestTest <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-14 上午10:00:08 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_BUY_ITEM_RESPONSE_VALUE)
public class BuyItemResponseTest extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		
		BuyItemResponse builder = BuyItemResponse.parseFrom(msg.getBody());
		log.error("购买结果：" + builder.getResult());
	}
	
}
