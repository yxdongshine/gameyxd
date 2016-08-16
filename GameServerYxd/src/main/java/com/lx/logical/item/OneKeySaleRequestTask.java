package com.lx.logical.item;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.OneKeySaleRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OneKeySaleRequestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-22 下午5:43:46 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_ONE_KEY_SALE_REQUEST_VALUE)
public class OneKeySaleRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			OneKeySaleRequest builder = OneKeySaleRequest.parseFrom(msg.getBody());
			int qualityType = builder.getQualityType();
			ArrayList<Long> itemdataIdAL = itemLogicManage.oneKeySale(serverPlayer, qualityType, dao);
			MessageSend.sendToGate(itemLogicManage.sendOneKeySaleResponse(itemdataIdAL), serverPlayer);
		}
	}
	
}
