package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.timer.AbstractTimerEvent;
import com.engine.timer.TimerThreadManage;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.RenewBottleRequest;
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
 * ClassName:RenewBottleRequestTask <br/>
 * Function: TODO (续费). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-10 下午3:13:09 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_RENEW_BOTTLE_REQUEST_VALUE)
public class RenewBottleRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RenewBottleRequest builder = RenewBottleRequest.parseFrom(msg.getBody());
			int configId = builder.getItemConfigId();
			if (serverPlayer.getBag().getBottleAndRevive()[0].getItemData().getConfigId() == configId) {
				PropertyPojoGame ppg = (PropertyPojoGame) serverPlayer.getBag().getBottleAndRevive()[0].getProperty();
				if (ppg != null) {
					// 看是否能够满足扣掉货币
					if (serverPlayer.isDebitSuccess(ppg.getCurrency(), 1, ppg.getBuyPrice())) {// 如果扣款成功
						// 设置物品续费次数
						Item item = serverPlayer.getBag().getBottleAndRevive()[0];
						item.getItemData().setNumber(ppg.getFolderableNum());
						item.getItemData().setRenewTimes(item.getItemData().getRenewTimes() + 1);
						serverPlayer.getBag().getBottleAndRevive()[0] = item;
						// 修改事件的时间
						String functionId = ppg.getFunction();
						FunctionPojo functionPojo = null;
						functionPojo = ItemConfigLoad.getFunctionHashMap().get(Integer.parseInt(functionId));
						AbstractTimerEvent ate = TimerThreadManage.findTimerEventByKey(item.getItemData().getId());
						if (ate != null) {
							long currTime = ate.addTime(functionPojo.getEffectiveTime() * 60 * 1000);
							// 更新数据库
							itemLogicManage.updateItem(item);
							// 发送更新药瓶消息
							MessageSend.sendToGate(itemLogicManage.buildRenewBottleResponse(1, ItemStaticConfig.BAG_TYPE_BOTTLE, item.getItemData().getNumber()), serverPlayer);
							// 发送剩余时间
							if (functionPojo != null) {
								MessageSend.sendToGate(itemLogicManage.buildBottleRestResponse(currTime - System.currentTimeMillis()), serverPlayer);
							}
						}
					} else {// 提示余额不足
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_12), serverPlayer);
					}
				}
			}
		}
	}
	
}
