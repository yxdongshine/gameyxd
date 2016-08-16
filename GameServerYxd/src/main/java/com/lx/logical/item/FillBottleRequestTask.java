package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.FillBottleRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:FillBottleRequestTask <br/>
 * Function: TODO (补满). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-10 下午3:14:20 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_FILL_BOTTLE_REQUEST_VALUE)
public class FillBottleRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			FillBottleRequest builder = FillBottleRequest.parseFrom(msg.getBody());
			int configId = builder.getItemConfigId();
			if (serverPlayer.getBag().getBottleAndRevive()[0].getItemData().getConfigId() == configId) {
				itemLogicManage.FillBottle(serverPlayer);
			}
		}
	}
	
}
