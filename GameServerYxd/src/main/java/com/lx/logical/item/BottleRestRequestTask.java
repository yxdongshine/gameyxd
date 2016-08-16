package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.ItemData;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.BottleRestRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.nserver.txt.FunctionPojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:BottleRestRequestTask <br/>
 * Function: TODO (请求剩余时间,目前只针对药瓶). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-11 下午3:16:34 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_BOTTLE_REST_REQUEST_VALUE)
public class BottleRestRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			BottleRestRequest builder = BottleRestRequest.parseFrom(msg.getBody());
			long itemDataId = builder.getItemId();
			Item item = serverPlayer.getBag().getBottleAndRevive()[0];// 药瓶
			if (item != null && item.getItemData() != null && item.getProperty() != null && item.getItemData().getId() == itemDataId) {
				ItemData itemdata = item.getItemData();
				PropertyPojoGame ppg = (PropertyPojoGame) item.getProperty();
				String functionId = ppg.getFunction();
				FunctionPojo functionPojo = ItemConfigLoad.getFunctionHashMap().get(Integer.parseInt(functionId));
				if (ppg.getLevel() >= ItemStaticConfig.BOTTLE_TYPE_ONE_DAY || ppg.getLevel() <= ItemStaticConfig.BOTTLE_TYPE_SEVEN_DAY) {
					long restTime = itemdata.getEffectTime() + functionPojo.getEffectiveTime() * itemdata.getRenewTimes() * 60 * 1000 - System.currentTimeMillis();
					MessageSend.sendToGate(itemLogicManage.buildBottleRestResponse(restTime), serverPlayer);
				} else {
					log.error("药瓶没得有效时间");
				}
				
			}
		}
	}
	
}
