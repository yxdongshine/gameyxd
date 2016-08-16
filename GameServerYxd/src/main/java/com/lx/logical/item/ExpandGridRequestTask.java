package com.lx.logical.item;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.ExpandGridRequest;
import com.loncent.protocol.game.item.Item.ExpandGridResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.BagConfigLoad;
import com.lx.game.item.BagOpenGridConfig;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ExpandGridRequest <br/>
 * Function: TODO (扩充格子数). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-9 下午4:57:21 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_EXPAND_GRID_REQUEST_VALUE)
public class ExpandGridRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			ExpandGridRequest builder = ExpandGridRequest.parseFrom(msg.getBody());
			int bagType = builder.getBagType();
			int index = (serverPlayer.getBag().getSubBags()[bagType].capacity - BagConfigLoad.bagCfg[bagType].getDefaultGrid()) / 4;
			BagOpenGridConfig bagOpenGridConfig = BagConfigLoad.bagCfg[bagType].getOpenGridAL().get(index);
			if (serverPlayer.isDebitSuccess(bagOpenGridConfig.getCurrency(), 1, bagOpenGridConfig.getFee())) {
				// 扣款成功 增加格子
				serverPlayer.getBag().getSubBags()[bagType].capacity += bagOpenGridConfig.getCount();
				serverPlayer.getBag().getSubBags()[bagType].AddCells(bagOpenGridConfig.getCount());
				if (bagType == 0) {// 装备
					serverPlayer.getRole().setEquitGridNumber(serverPlayer.getRole().getEquitGridNumber() + bagOpenGridConfig.getCount());
				} else if (bagType == 1) {// 普通道具
					serverPlayer.getRole().setCommonGridNumber(serverPlayer.getRole().getCommonGridNumber() + bagOpenGridConfig.getCount());
				}
				// 发送增加的格子数
				ExpandGridResponse.Builder responseBuilder = ExpandGridResponse.newBuilder();
				responseBuilder.setBagType(bagType);
				responseBuilder.setGridNums(bagOpenGridConfig.getCount());
				MessageSend.sendToGate(responseBuilder.build(), serverPlayer);
			} else {
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_12), serverPlayer);
			}
		}
	}
	
}
