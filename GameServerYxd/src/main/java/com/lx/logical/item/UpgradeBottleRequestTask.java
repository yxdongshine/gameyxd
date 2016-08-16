package com.lx.logical.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.timer.TimerThreadManage;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.UpgradeBottleRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;
import com.lx.game.timer.TaskTimerEvent;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.nserver.txt.FunctionPojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:UpgradeBottleRequestTask <br/>
 * Function: TODO (升级药瓶). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-10 下午3:09:46 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_UPGRADE_BOTTLE_REQUEST_VALUE)
public class UpgradeBottleRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			UpgradeBottleRequest builder = UpgradeBottleRequest.parseFrom(msg.getBody());
			int configId = builder.getItemConfigId();
			PropertyPojoGame ppg = (PropertyPojoGame) ItemConfigLoad.getPropertyHashMap().get(configId);
			if (ppg != null) {
				if (ppg.getLevel() < ItemStaticConfig.BOTTLE_TYPE_FOREVER) {// 升级
					// 看是否能够满足扣掉货币
					if (serverPlayer.isDebitSuccess(ppg.getCurrency(), 1, ppg.getBuyPrice())) {// 如果扣款成功
						Item item = serverPlayer.getBag().getBottleAndRevive()[0];
						// 设置升级前的药瓶编号
						serverPlayer.setLastBattleId(item.getProperty().getId());
						item.setProperty(ppg);
						item.getItemData().setLevel(ppg.getLevel());
						item.getItemData().setNumber(ppg.getFolderableNum());
						item.getItemData().setConfigId(ppg.getId());
						item.getItemData().setEffectTime(System.currentTimeMillis());
						serverPlayer.getBag().getBottleAndRevive()[0] = item;
						FunctionPojo functionPojo = null;
						if (ppg.getLevel() < ItemStaticConfig.BOTTLE_TYPE_SEVEN_DAY) {
							String functionId = ppg.getFunction();
							functionPojo = ItemConfigLoad.getFunctionHashMap().get(Integer.parseInt(functionId));
							// 注册时间事件
							TaskTimerEvent ttv = new TaskTimerEvent(item.getId(),serverPlayer.getId(),functionPojo.getEffectiveTime() * 60 + System.currentTimeMillis());
							
						}
						
						// 更新数据库
						itemLogicManage.updateItem(item);
						// 发送更新药瓶消息
						MessageSend.sendToGate(itemLogicManage.buildUpgradeBottleResponse(1, item.getProperty().getId(), ItemStaticConfig.BAG_TYPE_BOTTLE, item.getItemData().getNumber()), serverPlayer);
						// 发送剩余时间
						if (functionPojo != null) {
							MessageSend.sendToGate(itemLogicManage.buildBottleRestResponse(functionPojo.getEffectiveTime() * 60), serverPlayer);
						}
					} else {// 提示余额不足
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_12), serverPlayer);
					}
				} else {// 提示不能再升级
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_14), serverPlayer);
				}
				
			}
		}
	}
	
}
