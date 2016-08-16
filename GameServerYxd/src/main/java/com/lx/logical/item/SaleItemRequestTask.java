package com.lx.logical.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.SaleItemRequest;
import com.loncent.protocol.game.item.Item.SaleItemResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.Bag;
import com.lx.game.item.PackItem;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.Property;
import com.lx.game.res.item.EquipmentPojoGame;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SaleItemRequest <br/>
 * Function: TODO (卖出物品). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-6 上午11:45:36 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_SALE_ITEM_REQUEST_VALUE)
public class SaleItemRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	private static final Log log = LogFactory.getLog(Bag.class);
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			SaleItemRequest builder = SaleItemRequest.parseFrom(msg.getBody());
			long itemDataId = builder.getItemId();
			int itemNumber = builder.getItemNumber();
			Item item = serverPlayer.getBag().queryItem(itemDataId, serverPlayer.getRole().getId());
			if (item != null) {
				Property property = item.getProperty();
				if (property.getCantSold() > 0) {// 1 表示能买
					List<PackItem> resultLongAL = new ArrayList<PackItem>();
					resultLongAL = serverPlayer.getBag().updateItemInbag(item, -itemNumber, serverPlayer.getRole().getId(), dao);
					if (resultLongAL.size() >= 1) {
						if (resultLongAL.get(0).getAddType() > 0) {// 表示卖出成功
							int price = -1;
							int currency = -1;
							int bagType = -1;
							if (property instanceof PropertyPojoGame) {
								PropertyPojoGame propertyPojoGame = (PropertyPojoGame) property;
								price = propertyPojoGame.getBuyPrice();
								currency = propertyPojoGame.getCurrency();
								bagType = propertyPojoGame.getBagClass();
								
							} else if (property instanceof EquipmentPojoGame) {
								EquipmentPojoGame equipmentPojoGame = (EquipmentPojoGame) property;
								price = equipmentPojoGame.getPrice();
								currency = equipmentPojoGame.getCurrency();
								bagType = equipmentPojoGame.getBagClass();
							}
							for (int i = 1; i < resultLongAL.size(); i++) {
								if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
									// 发送更新道具消息
									MessageSend.sendToGate(itemLogicManage.sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), bagType), serverPlayer);
								} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_DEL) {
									item = resultLongAL.get(i).getItem();
									// 发送放置物品到背包消息
									MessageSend.sendToGate(itemLogicManage.sendDelItemFromBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), bagType), serverPlayer);
								}
							}
							
							serverPlayer.addCurrency(currency, itemNumber, price);
							int result = 1;
							SaleItemResponse.Builder responseBuilder = SaleItemResponse.newBuilder();
							responseBuilder.setResult(result);
							MessageSend.sendToGate(responseBuilder.build(), serverPlayer);
						} else if (resultLongAL.get(0).getAddType() == -2) {
							MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_F), serverPlayer);
						} else if (resultLongAL.get(0).getAddType() == -1) {
							MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_10), serverPlayer);
						}
					}
					
				} else {// 不能买
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_13), serverPlayer);
				}
			} else {
				log.equals("物品不存在");
			}
		}
		
	}
}
