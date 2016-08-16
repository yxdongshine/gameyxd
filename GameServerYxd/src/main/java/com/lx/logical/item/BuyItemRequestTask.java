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
import com.loncent.protocol.game.item.Item.BuyItemRequest;
import com.loncent.protocol.game.item.Item.BuyItemResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.ItemManage;
import com.lx.game.item.PackItem;
import com.lx.game.item.util.MathUtil;
import com.lx.game.res.item.EquipmentPojoGame;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.Property;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * 
 * ClassName: BuyItemRequestTask <br/>
 * Function: TODO (购买物品). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-7-21 下午5:12:45 <br/>
 * 
 * @author yxd
 * @version
 */
@Component
@Head(CmdType.C_S_BUY_ITEM_REQUEST_VALUE)
public class BuyItemRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		long roleId = 0;
		if (serverPlayer != null) {
			boolean isEquip = false;
			roleId = serverPlayer.getRole().getId();
			BuyItemRequest builder = BuyItemRequest.parseFrom(msg.getBody());
			int itemTypeId = builder.getItemTypeId();
			int number = builder.getItemNum();
			byte result = -1;
			Property property = ItemConfigLoad.getPrototype(itemTypeId);
			int bagType = -1;
			int price = -1;
			int currency = -1;
			if (property instanceof PropertyPojoGame) {
				PropertyPojoGame propertyPojoGame = (PropertyPojoGame) property;
				bagType = propertyPojoGame.getBagClass();
				price = propertyPojoGame.getBuyPrice();
				currency = propertyPojoGame.getCurrency();
				
			} else if (property instanceof EquipmentPojoGame) {
				EquipmentPojoGame equipmentPojoGame = (EquipmentPojoGame) property;
				bagType = equipmentPojoGame.getBagClass();
				price = equipmentPojoGame.getPrice();
				currency = equipmentPojoGame.getCurrency();
				isEquip = true;
				
			}
			if (!MathUtil.isBagType(bagType)) {
				log.error("物品背包配置类型错误");
				return;
			}
			byte isGridEnough = serverPlayer.getBag().getSubBags()[bagType].onlyCanAddBuyItem(itemTypeId, number);
			if (isGridEnough == -1) {//
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_E), serverPlayer);
			} else {
				if (serverPlayer.isDebitSuccess(currency, number, price)) {
					if (isGridEnough > 0) {
						// 创建物品
						Item item = ItemManage.createItem(itemTypeId);
						if (item != null) {
							// 添加物品到背包
							List<PackItem> resultLongAL = new ArrayList<PackItem>();
							resultLongAL = serverPlayer.getBag().putItemInBag(item, number, roleId, dao);
							if (resultLongAL != null && resultLongAL.size() > 0) {
								if (!isEquip) {// 普通物品
									if (resultLongAL.get(0) != null && resultLongAL.get(0).getAddType() >= 0) {
										result = 1;
										BuyItemResponse.Builder responseBuidler = BuyItemResponse.newBuilder();
										responseBuidler.setResult(result);
										MessageSend.sendToGate(responseBuidler.build(), serverPlayer);
										for (int i = 1; i < resultLongAL.size(); i++) {
											if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
												// 发送更新道具消息
												MessageSend.sendToGate(itemLogicManage.sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), bagType), serverPlayer);
											} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_ADD) {
												item = resultLongAL.get(i).getItem();
												// 发送放置物品到背包消息
												MessageSend.sendToGate(itemLogicManage.sendAddItemToBagResponse(item), serverPlayer);
											}
										}
										
									}
								} else {// 装备
									for (int i = 1; i < resultLongAL.size(); i++) {
										item = resultLongAL.get(i).getItem();
										// 发送放置物品到背包消息
										MessageSend.sendToGate(itemLogicManage.sendAddItemToBagResponse(item), serverPlayer);
									}
								}
							}
							
						}
					} else if (isGridEnough == 0) {// 更新背包信息
					
						long itemdataid = serverPlayer.getBag().getSubBags()[bagType].queryItemDataByConfigId(itemTypeId);
						Item item = serverPlayer.getBag().queryItem(itemdataid, serverPlayer.getRole().getId());
						serverPlayer.getBag().updateItemInbag(item, number, roleId, dao);
						// 发送更新道具消息
						MessageSend.sendToGate(itemLogicManage.sendUpdateItemToBagResponse(itemdataid, number, bagType), serverPlayer);
					}
					
				} else {
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_12), serverPlayer);
				}
			}
			
		}
		
	}
	
}
