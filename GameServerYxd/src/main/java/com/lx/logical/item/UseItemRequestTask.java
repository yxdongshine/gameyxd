package com.lx.logical.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.UseItemRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.CommonToolItem;
import com.lx.game.item.EquitItem;
import com.lx.game.item.PackItem;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:UseItemRequest <br/>
 * Function: TODO (使用道具). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-9 下午3:24:33 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_USE_ITEM_RESQUEST_VALUE)
public class UseItemRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			UseItemRequest builder = UseItemRequest.parseFrom(msg.getBody());
			long itemDataId = builder.getItemId();
			int itemNumber = builder.getItemNum();
			Item item = serverPlayer.getBag().queryItem(itemDataId, serverPlayer.getRole().getId());
			if (item instanceof EquitItem) {// 如果是装备
				// 更新背包数据
				// byte result=serverPlayer.getBag().updateItemInbag(itemDataId, -1, serverPlayer.getRole().getId());
				// if(result==111){//减少数量成功
				int result = itemLogicManage.putEquipment(serverPlayer, itemDataId, dao);
				if (result < 0) {// 使用失败
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_11), serverPlayer);
				} else {
					itemLogicManage.sendPutOnEquipToBodyResponse(itemDataId, result, serverPlayer);
				}
			} else if (item instanceof CommonToolItem) {
				CommonToolItem commonItem = (CommonToolItem) item;
				
				if (commonItem.canUse(serverPlayer, null) == 1) {
					List<PackItem> resultLongAL = new ArrayList<PackItem>();
					// 更新背包数据
					resultLongAL = serverPlayer.getBag().updateItemInbag(item, -itemNumber, serverPlayer.getRole().getId(), dao);
					if (resultLongAL.get(0).getAddType() > 0) {// 减少数量成功
						for (int i = 0; i < itemNumber; i++) {
							commonItem.use(serverPlayer, null);
						}
						
						for (int i = 1; i < resultLongAL.size(); i++) {
							if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
								// 发送更新道具消息
								MessageSend.sendToGate(itemLogicManage.sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), resultLongAL.get(i).getItem().getItemData().getItemType()), serverPlayer);
							} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_DEL) {
								item = resultLongAL.get(i).getItem();
								// 发送放置物品到背包消息
								MessageSend.sendToGate(itemLogicManage.sendDelItemFromBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getItem().getItemData().getItemType()), serverPlayer);
							}
						}
					} else if (resultLongAL.get(0).getAddType() == -2) {
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_F), serverPlayer);
					} else if (resultLongAL.get(0).getAddType() == -1) {
						MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_10), serverPlayer);
					}
				} else {
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_11), serverPlayer);
				}
				
			}
			
		}
	}
	
}
