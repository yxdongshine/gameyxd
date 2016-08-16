package com.lx.logical.manage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.ItemData;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.utils.ErrorCode;
import com.lib.utils.ServerUUID;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.game.item.Item.AddItemToBagResponse;
import com.loncent.protocol.game.item.Item.ArrangeTypeBagResponse;
import com.loncent.protocol.game.item.Item.BottleRestResponse;
import com.loncent.protocol.game.item.Item.BuyItemResponse;
import com.loncent.protocol.game.item.Item.DelDropItemResponse;
import com.loncent.protocol.game.item.Item.DelItemFromBagResponse;
import com.loncent.protocol.game.item.Item.FillBottleResponse;
import com.loncent.protocol.game.item.Item.OpenRoleBagResponse;
import com.loncent.protocol.game.item.Item.PutOnEquipToBodyResponse;
import com.loncent.protocol.game.item.Item.RenewBottleResponse;
import com.loncent.protocol.game.item.Item.UpdateItemToBagResponse;
import com.loncent.protocol.game.item.Item.OneKeySaleResponse;
import com.loncent.protocol.game.item.Item.UpgradeBottleResponse;
import com.loncent.protocol.game.player.Role.AttrType;
import com.loncent.protocol.stauscode.StatusCode.StatusCodeResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.DropItem;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.MapObjectMessage;
import com.lx.game.entity.listener.IDropItemListener;
import com.lx.game.item.Bag;
import com.lx.game.item.EquitItem;
import com.lx.game.item.ItemManage;
import com.lx.game.item.PackItem;
import com.lx.game.item.ItemContainer.Cell;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.EquipmentPojoGame;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.Property;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;

/**
 * ClassName:ItemLogicManage <br/>
 * Function: TODO (道具管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-9 下午3:38:30 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class ItemLogicManage implements IDropItemListener{
	
	private Log log = LogFactory.getLog(ItemLogicManage.class);
	
	@Autowired
	EntityDAO dao;
	
	@Autowired
	private TaskManage taskManage;
	
	/**
	 * 使用装备方法 useEquipment:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemEpuipDataId
	 * @return -1 表示不能使用，0 表示使用但是不减少数值，大于1表示使用成功并且减少相应的数量: [1]表示槽位
	 */
	public int putEquipment(ServerPlayer serverPlayer, long itemEpuipDataId, EntityDAO dao) {
		int reslut = -1;
		// 从装备列表中查询出来
		Item item = serverPlayer.getBag().queryItem(itemEpuipDataId, serverPlayer.getRole().getId());
		if (item != null) {
			// 获取装备原型
			EquipmentPojoGame equipProperty = (EquipmentPojoGame) item.getProperty();
			if (equipProperty != null) {
				// 获取装备位
				int slot = equipProperty.getBind();
				if (serverPlayer.getBag().getEquipData()[slot - 1] != null) {// 装备栏中存在
					EquitItem equitItemTakeoff = (EquitItem) serverPlayer.getBag().getEquipData()[slot - 1];
					// 发送删除消息
					MessageSend.sendToGate(this.sendDelItemFromBagResponse(equitItemTakeoff.getItemData().getId(), ItemStaticConfig.BAG_TYPE_ON_BODY), serverPlayer);
					// 脱下装备
					this.takeOffEquipment(equitItemTakeoff, serverPlayer);
					// 将这个装备从背包中删除
					serverPlayer.getBag().delItemInBag(item.getItemData().getId(), serverPlayer.getRole().getId(), dao, slot);
					// 发送删除消息
					MessageSend.sendToGate(this.sendDelItemFromBagResponse(item.getItemData().getId(), equipProperty.getBagClass()), serverPlayer);
					// 将这个装备放进背包
					serverPlayer.getBag().putItemInBag(equitItemTakeoff, 1, serverPlayer.getRole().getId(), dao);
					MessageSend.sendToGate(this.sendAddItemToBagResponse(equitItemTakeoff), serverPlayer);
					item.getItemData().setIndexInBag(-1);
					item.getItemData().setPos(slot);
					// 装备栏数据
					serverPlayer.getBag().getEquipData()[slot - 1] = item;
					
					// this.sendPutOnEquipToBodyResponse(item.getId(), slot, serverPlayer);
					// 生效
					this.putOnEquipment(item, serverPlayer);
				} else {// 装备位不逊在 直接穿
					// 将这个装备从背包中删除
					serverPlayer.getBag().delItemInBag(item.getItemData().getId(), serverPlayer.getRole().getId(), dao, slot);
					// 发送删除消息
					MessageSend.sendToGate(this.sendDelItemFromBagResponse(item.getItemData().getId(), equipProperty.getBagClass()), serverPlayer);
					item.getItemData().setIndexInBag(-1);
					item.getItemData().setPos(slot);
					// 装备栏数据
					serverPlayer.getBag().getEquipData()[slot - 1] = item;
					// 生效
					this.putOnEquipment(serverPlayer.getBag().getEquipData()[slot - 1], serverPlayer);
				}
				reslut = slot;
			}
		} else {
			reslut = -1;
			log.error("物品不存在");
		}
		return reslut;
	}
	
	
	public int takeoff(ServerPlayer serverPlayer, long itemEpuipDataId, EntityDAO dao) {
		int reslut = -1;
		// 从装备列表中查询出来
		Item item = serverPlayer.getBag().queryInEquipBar(itemEpuipDataId);
		if (item != null) {
			// 获取装备原型
			EquipmentPojoGame equipProperty = (EquipmentPojoGame) item.getProperty();
			if (equipProperty != null) {
				// 获取装备位
				int slot = equipProperty.getBind();
				if (serverPlayer.getBag().getEquipData()[slot - 1] != null) {// 装备栏中存在
					// 将这个装备放进背包
					serverPlayer.getBag().putItemInBag(item, 1, serverPlayer.getRole().getId(), dao);
					MessageSend.sendToGate(this.sendAddItemToBagResponse(item), serverPlayer);
					if (item instanceof EquitItem) {
						EquitItem equitItem = (EquitItem) item;
						equitItem.unEffect(serverPlayer);
					}
					serverPlayer.getBag().getEquipData()[slot - 1] = null;
					reslut = slot;
				} else {
					log.error("装备栏不存在该装备");
				}
			} else {
				log.error("装备数据异常");
			}
		} else {
			reslut = -1;
		}
		return reslut;
	}
	
	/**
	 * 脱下装备 takeOffEquipment:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param sp
	 */
	public void takeOffEquipment(Item item, ServerPlayer serverPlayer) {
		if (item instanceof EquitItem) {
			EquitItem equitItem = (EquitItem) item;
			equitItem.unEffect(serverPlayer);// 装备失效
			
		}
	}
	
	/**
	 * 穿上装备 putOnEquipment:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param serverPlayer
	 * @param dao
	 */
	public void putOnEquipment(Item item, ServerPlayer serverPlayer) {
		if (item instanceof EquitItem) {
			EquitItem equitItem = (EquitItem) item;
			equitItem.effect(serverPlayer);// 装备生效
			
		}
	}
	
	/**
	 * 删除道具消息 sendDelItemFromBagResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemDataId
	 * @return
	 */
	public DelItemFromBagResponse sendDelItemFromBagResponse(long itemDataId, int bagType) {
		DelItemFromBagResponse.Builder responseBuilder = DelItemFromBagResponse.newBuilder();
		responseBuilder.setItemId(itemDataId);
		responseBuilder.setItemBagType(bagType);
		return responseBuilder.build();
	}
	
	/**
	 * 发送跟新道具数量 sendUpdateItemToBagResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemDataId
	 * @param number
	 * @param roleId
	 * @param dao
	 */
	public UpdateItemToBagResponse sendUpdateItemToBagResponse(long itemDataId, int number, int bagType) {
		UpdateItemToBagResponse.Builder responseBuilder = UpdateItemToBagResponse.newBuilder();
		responseBuilder.setItemId(itemDataId);
		responseBuilder.setItemNum(number);
		responseBuilder.setItemBagType(bagType);
		return responseBuilder.build();
	}
	
	/**
	 * 
	 * sendPutOnEquipToBodyResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemDataId
	 * @param slot
	 * @param serverPlayer
	 */
	public void sendPutOnEquipToBodyResponse(long itemDataId, int slot, ServerPlayer serverPlayer) {
		PutOnEquipToBodyResponse.Builder responseBuilder = PutOnEquipToBodyResponse.newBuilder();
		Item item = serverPlayer.getBag().queryInEquipBar(itemDataId);
		if (item != null) {
			responseBuilder.setItemData(buildItemData(item));
		}
		MessageSend.sendToGate(responseBuilder.build(), serverPlayer);
	}
	
	/**
	 * 发送添加道具到背包消息 sendAddItemToBagResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @return
	 */
	public AddItemToBagResponse sendAddItemToBagResponse(Item item) {
		AddItemToBagResponse.Builder responseBuilder = AddItemToBagResponse.newBuilder();
		responseBuilder.setItemData(buildItemData(item).build());
		return responseBuilder.build();
	}
	
	/**
	 * 
	 * buildItemData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @return
	 */
	public static com.loncent.protocol.game.item.Item.ItemData.Builder buildItemData(Item item) {
		com.loncent.protocol.game.item.Item.ItemData.Builder responseBuliderItemData = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
		if (item != null && item.getItemData() != null) {
			ItemData itemData = item.getItemData();
			responseBuliderItemData.setItemId(itemData.getId());
			responseBuliderItemData.setItemNum(itemData.getNumber());
			responseBuliderItemData.setItemType(itemData.getItemType());
			responseBuliderItemData.setItemTypeId(itemData.getConfigId());
			responseBuliderItemData.setQuality(itemData.getQuality());
			responseBuliderItemData.setIndexInBag(itemData.getIndexInBag());
			if (item instanceof EquitItem) {
				responseBuliderItemData.setPos(itemData.getPos());
				responseBuliderItemData.setScore(itemData.getScore());
				responseBuliderItemData.setSocket(itemData.getSocket());
			}
			
		}
		return responseBuliderItemData;
	}
	
	/**
	 * 初始化背包结构 initBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 */
	public void initBag(ServerPlayer sp) {
		// 初始化背包信息
		Bag bag = sp.getBag();
		if (bag != null) {
			bag.createBag(sp);
		} else {
			bag = new Bag();// 背包容器
			sp.setBag(bag);// 对象设置背包
			bag.createBag(sp);
		}
	}
	
	/**
	 * 加载玩家背包 loadBagData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void loadBagData(ServerPlayer sp, EntityDAO dao) {
		// 初始化背包信息
		initBag(sp);
		// 加载背包数据
		sp.getBag().loadBagData(sp.getRole(), dao);
		
	}
	
	/**
	 * 封装背包数据 getBagItemInfo:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param bag
	 * @return
	 */
	public OpenRoleBagResponse.Builder getBagItemInfo(Bag bag) {
		OpenRoleBagResponse.Builder openBagBuilder = OpenRoleBagResponse.newBuilder();
		if (bag != null) {
			openBagBuilder.setEquitGridNum(bag.getSubBags()[0].capacity);
			openBagBuilder.setCommonGridNum(bag.getSubBags()[1].capacity);
			// 装备栏列表
			for (int i = 0; i < bag.getEquipData().length; i++) {
				Item item = bag.getEquipData()[i];
				if (item != null && item.getItemData() != null) {
					ItemData itemData = item.getItemData();
					if (itemData != null) {
						com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
						itemProtoBuilder.setItemId(itemData.getId());
						itemProtoBuilder.setItemNum(itemData.getNumber());
						itemProtoBuilder.setItemType(itemData.getItemType());
						itemProtoBuilder.setItemTypeId(itemData.getConfigId());
						itemProtoBuilder.setQuality(itemData.getQuality());
						itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
						itemProtoBuilder.setPos(itemData.getPos());
						itemProtoBuilder.setScore(itemData.getScore());
						itemProtoBuilder.setSocket(itemData.getSocket());
						openBagBuilder.addEquitBarItemData(itemProtoBuilder.build());
					}
				}
				
			}
		}
		
		// 装备列表
		for (int i = 0; i < bag.getSubBags()[0].getCells().size(); i++) {
			if (bag.getSubBags()[0].getCells().get(i) != null && bag.getSubBags()[0].getCells().get(i).getItem() != null) {
				ItemData itemData = bag.getSubBags()[0].getCells().get(i).getItem().getItemData();
				if (itemData != null) {
					com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
					itemProtoBuilder.setItemId(itemData.getId());
					itemProtoBuilder.setItemNum(itemData.getNumber());
					itemProtoBuilder.setItemType(itemData.getItemType());
					itemProtoBuilder.setItemTypeId(itemData.getConfigId());
					itemProtoBuilder.setQuality(itemData.getQuality());
					itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
					itemProtoBuilder.setPos(itemData.getPos());
					itemProtoBuilder.setScore(itemData.getScore());
					itemProtoBuilder.setSocket(itemData.getSocket());
					openBagBuilder.addEquitItemData(itemProtoBuilder.build());
				}
			}
			
		}
		// 普通道具列表
		for (int i = 0; i < bag.getSubBags()[1].getCells().size(); i++) {
			if (bag.getSubBags()[1].getCells().get(i) != null && bag.getSubBags()[1].getCells().get(i).getItem() != null) {
				ItemData itemData = bag.getSubBags()[1].getCells().get(i).getItem().getItemData();
				if (itemData != null) {
					com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
					itemProtoBuilder.setItemId(itemData.getId());
					itemProtoBuilder.setItemNum(itemData.getNumber());
					itemProtoBuilder.setItemType(itemData.getItemType());
					itemProtoBuilder.setItemTypeId(itemData.getConfigId());
					itemProtoBuilder.setQuality(itemData.getQuality());
					itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
					
					openBagBuilder.addCommonItemData(itemProtoBuilder.build());
				}
			}
		}
		
		// 特殊道具列表
		for (int i = 0; i < bag.getSubBags()[2].getCells().size(); i++) {
			if (bag.getSubBags()[2].getCells().get(i) != null && bag.getSubBags()[2].getCells().get(i).getItem() != null) {
				ItemData itemData = bag.getSubBags()[2].getCells().get(i).getItem().getItemData();
				if (itemData != null) {
					com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
					itemProtoBuilder.setItemId(itemData.getId());
					itemProtoBuilder.setItemNum(itemData.getNumber());
					itemProtoBuilder.setItemType(itemData.getItemType());
					itemProtoBuilder.setItemTypeId(itemData.getConfigId());
					itemProtoBuilder.setQuality(itemData.getQuality());
					itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
					
					openBagBuilder.addSpecialItemData(itemProtoBuilder.build());
				}
			}
		}
		
		// 药瓶和复活币
		for (int i = 0; i < bag.getBottleAndRevive().length; i++) {
			Item item = bag.getBottleAndRevive()[i];
			if (item != null && item.getItemData() != null) {
				ItemData itemData = item.getItemData();
				if (itemData != null) {
					com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
					itemProtoBuilder.setItemId(itemData.getId());
					itemProtoBuilder.setItemNum(itemData.getNumber());
					itemProtoBuilder.setItemType(itemData.getItemType());
					itemProtoBuilder.setItemTypeId(itemData.getConfigId());
					itemProtoBuilder.setQuality(itemData.getQuality());
					itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
					itemProtoBuilder.setPos(itemData.getPos());
					itemProtoBuilder.setScore(itemData.getScore());
					itemProtoBuilder.setSocket(itemData.getSocket());
					openBagBuilder.addBottleAndReviveItemData(itemProtoBuilder.build());
				}
			}
			
		}
		return openBagBuilder;
	}
	
	/**
	 * 
	 * oneKeySale:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param serverPlayer
	 * @param qualityType
	 * @param dao
	 * @return
	 */
	public ArrayList<Long> oneKeySale(ServerPlayer serverPlayer, int qualityType, EntityDAO dao) {
		ArrayList<Long> itemdataIdAL = new ArrayList<Long>();
		for (int i = 0; i < serverPlayer.getBag().getSubBags()[0].getCells().size(); i++) {// 遍历装备背包
			if (serverPlayer.getBag().getSubBags()[0].getCells().get(i) != null && serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem() != null) {
				Property property = serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getProperty();
				if (property != null) {
					EquipmentPojoGame equipmentPojoGame = (EquipmentPojoGame) property;
					if (qualityType == -1) {// 表示买其他职业的所有装备
						if (equipmentPojoGame.getCantSold() > 0 && equipmentPojoGame.getCareer() != serverPlayer.getRole().getCareerConfigId()) {// 不为该角色的卖出
							itemdataIdAL.add(serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getId());
							saleTheItem(equipmentPojoGame, serverPlayer, dao, serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getId());
						}
					} else {// 卖出指定质量的装备
						ItemData itemdata = serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getItemData();
						if (itemdata != null) {
							if (equipmentPojoGame.getBind() == ItemStaticConfig.EQUIP_BAR_FASHTION_DRESS) {// 时装不在此中买
								break;
							}
							if (equipmentPojoGame.getCantSold() > 0 && itemdata.getQuality() == qualityType) {// 该质量装备删除
								itemdataIdAL.add(serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getId());
								saleTheItem(equipmentPojoGame, serverPlayer, dao, serverPlayer.getBag().getSubBags()[0].getCells().get(i).getItem().getId());
							}
						}
					}
				}
			}
		}
		return itemdataIdAL;
	}
	
	public void saleTheItem(EquipmentPojoGame equipmentPojoGame, ServerPlayer serverPlayer, EntityDAO dao, long itemDataId) {
		int price = equipmentPojoGame.getPrice();
		int currency = equipmentPojoGame.getCurrency();
		serverPlayer.addCurrency(currency, 1, price);
		// 删除该位置装备
		serverPlayer.getBag().delItemInBag(itemDataId, serverPlayer.getRole().getId(), dao, -1);
	}
	
	/**
	 * 包装发送卖出的列表 sendOneKeySaleResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemdataIdAL
	 * @return
	 */
	public OneKeySaleResponse sendOneKeySaleResponse(ArrayList<Long> itemdataIdAL) {
		OneKeySaleResponse.Builder responseBuilder = OneKeySaleResponse.newBuilder();
		for (Iterator iterator = itemdataIdAL.iterator(); iterator.hasNext();) {
			Long itemDataId = (Long) iterator.next();
			responseBuilder.addItemId(itemDataId);
		}
		return responseBuilder.build();
	}
	
	/**
	 * 
	 * sendArrangeTypeBagResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param serverPlayer
	 * @param bagType
	 * @return
	 */
	public ArrangeTypeBagResponse sendArrangeTypeBagResponse(ServerPlayer serverPlayer, int bagType) {
		ArrangeTypeBagResponse.Builder responseBuilder = ArrangeTypeBagResponse.newBuilder();
		List<Cell> cellAL = serverPlayer.getBag().getSubBags()[bagType].getCells();
		if (cellAL != null) {
			for (Iterator iterator = cellAL.iterator(); iterator.hasNext();) {
				Cell cell = (Cell) iterator.next();
				if (cell != null && cell.getItem() != null && cell.getItem().getItemData() != null) {// 存在
					ItemData itemData = cell.getItem().getItemData();
					if (itemData != null) {
						com.loncent.protocol.game.item.Item.ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
						itemProtoBuilder.setItemId(itemData.getId());
						itemProtoBuilder.setItemNum(itemData.getNumber());
						itemProtoBuilder.setItemType(itemData.getItemType());
						itemProtoBuilder.setItemTypeId(itemData.getConfigId());
						itemProtoBuilder.setQuality(itemData.getQuality());
						itemProtoBuilder.setIndexInBag(itemData.getIndexInBag());
						itemProtoBuilder.setPos(itemData.getPos());
						itemProtoBuilder.setScore(itemData.getScore());
						itemProtoBuilder.setSocket(itemData.getSocket());
						responseBuilder.addItemData(itemProtoBuilder.build());
					}
				}
			}
		}
		return responseBuilder.build();
	}
	
	/**
	 * 
	 * sendAddItemMessage:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param serverPlayer
	 * @param resultLongAL
	 */
	public void sendAddItemMessage(ServerPlayer serverPlayer, List<PackItem> resultLongAL) {
		// 添加物品到背包
		Item item = null;
		if (resultLongAL != null && resultLongAL.size() > 0) {
			
			if (resultLongAL.get(0) != null && resultLongAL.get(0).getAddType() >= 0) {
				int result = 1;
				BuyItemResponse.Builder responseBuidler = BuyItemResponse.newBuilder();
				responseBuidler.setResult(result);
				MessageSend.sendToGate(responseBuidler.build(), serverPlayer);
				for (int i = 1; i < resultLongAL.size(); i++) {
					if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
						// 发送更新道具消息
						MessageSend.sendToGate(this.sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), resultLongAL.get(i).getItem().getItemData().getItemType()), serverPlayer);
					} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_ADD) {
						item = resultLongAL.get(i).getItem();
						// 发送放置物品到背包消息
						MessageSend.sendToGate(this.sendAddItemToBagResponse(item), serverPlayer);
					}
				}
				
			}
		}
	}
	
	/**
	 * 保存物品数据 药瓶 系统后 updateItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param sp
	 */
	public void updateItem(Item item) {
		if (item != null && item.getItemData() != null) {
			dao.saveOrUpdate(item.getItemData());
		}
	}
	
	/**
	 * 构建剩余时间返回消息 buildBottleRestResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param restTime
	 * @return
	 */
	public BottleRestResponse buildBottleRestResponse(long restTime) {
		BottleRestResponse.Builder builder = BottleRestResponse.newBuilder();
		builder.setRestTime(restTime);
		return builder.build();
	}
	
	/**
	 * 升级 result=1;//返回结果 optional int32 itemConfigId=2;//药瓶配置编号 optional int32 bagType=3;//背包类型 optional int32 dataNum=4;//数据数量
	 * 
	 * buildUpgradeBottleResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public UpgradeBottleResponse buildUpgradeBottleResponse(int result, int itemConfigId, int bagType, int dataNum) {
		UpgradeBottleResponse.Builder builder = UpgradeBottleResponse.newBuilder();
		builder.setResult(result);
		builder.setItemConfigId(itemConfigId);
		builder.setBagType(bagType);
		builder.setDataNum(dataNum);
		return builder.build();
	}
	
	/**
	 * 续费 buildRenewBottleResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param result
	 * @param bagType
	 * @param dataNum
	 * @return
	 */
	public RenewBottleResponse buildRenewBottleResponse(int result, int bagType, int dataNum) {
		RenewBottleResponse.Builder builder = RenewBottleResponse.newBuilder();
		builder.setBagType(bagType);
		builder.setResult(result);
		builder.setDataNum(dataNum);
		return builder.build();
	}
	
	/**
	 * 补满 buildFillBottleResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param result
	 * @param bagType
	 * @param dataNum
	 * @return
	 */
	public FillBottleResponse buildFillBottleResponse(int result, int bagType, int dataNum) {
		FillBottleResponse.Builder builder = FillBottleResponse.newBuilder();
		builder.setBagType(bagType);
		builder.setResult(result);
		builder.setDataNum(dataNum);
		return builder.build();
	}
	
	/**
	 * sendPopUpTip:(). <br/>
	 * TODO().<br/>
	 * 发送弹出框信息
	 * 
	 * @author yxd
	 * @param net
	 * @param statusCodeId
	 */
	public static StatusCodeResponse createPopUpTip(int statusCodeId) {
		StatusCodeResponse scr = StatusCodeResponse.newBuilder().setPopstr(statusCodeId).build();
		return scr;
	}
	
	/**
	 * 
	 * 填充药瓶
	 */
	public void FillBottle(ServerPlayer serverPlayer) {
		
		PropertyPojoGame ppg = (PropertyPojoGame) serverPlayer.getBag().getBottleAndRevive()[0].getProperty();
		if (ppg != null) {
			// 看是否能够满足扣掉货币
			if (serverPlayer.isDebitSuccess(ppg.getCurrency(), 1, ppg.getFullBottle())) {// 如果扣款成功
				Item item = serverPlayer.getBag().getBottleAndRevive()[0];
				item.getItemData().setNumber(ppg.getFolderableNum());
				serverPlayer.getBag().getBottleAndRevive()[0] = item;
				// 更新数据库
				this.updateItem(item);
				// 发送更新药瓶消息
				MessageSend.sendToGate(this.buildRenewBottleResponse(1, ItemStaticConfig.BAG_TYPE_BOTTLE, item.getItemData().getNumber()), serverPlayer);
				
			} else {// 提示余额不足
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_12), serverPlayer);
			}
		}
		
	}
	
	
	/**
	 * 
	 * 发送消息方法
	 */
	
	public void MessageSendAboutBag(List<PackItem> resultLongAL, Item item, ServerPlayer serverPlayer) {
		for (int i = 1; i < resultLongAL.size(); i++) {
			if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
				// 发送更新道具消息
				MessageSend.sendToGate(sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), resultLongAL.get(i).getItem().getItemData().getItemType()), serverPlayer);
			} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_DEL) {
				item = resultLongAL.get(i).getItem();
				// 发送放置物品到背包消息
				MessageSend.sendToGate(sendDelItemFromBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getItem().getItemData().getItemType()), serverPlayer);
			}
		}
	}
	
	/**
	 * 
	 * 判断是否够装
	 */
	public byte isGridEnough(int itemTypeId, int number, ServerPlayer serverPlayer) {
		byte isGridEnough = -1;
		Property property = ItemConfigLoad.getPrototype(itemTypeId);
		if (property != null) {
			isGridEnough = serverPlayer.getBag().getSubBags()[property.getBagClass()].onlyCanAddBuyItem(itemTypeId, number);
		}
		return isGridEnough;
	}
	
	/**
	 * 
	 * queryItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemDataId
	 * @param serverPlayer
	 * @return
	 */
	
	public Item queryItem(long itemDataId, ServerPlayer serverPlayer) {
		Item item = serverPlayer.getBag().queryItem(itemDataId, serverPlayer.getRole().getId());
		return item;
	}
	
	/**
	 * 更新背包 添加数量 传正数 减少数量传负数 updateItemInbag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param count
	 * @param serverPlayer
	 * @return
	 */
	public int updateItemInbag(Item item, int count, ServerPlayer serverPlayer) {
		int result = -1;
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		// 更新背包数据
		resultLongAL = serverPlayer.getBag().updateItemInbag(item, count, serverPlayer.getRole().getId(), dao);
		if (resultLongAL.get(0).getAddType() > 0) {// 减少数量成功
			MessageSendAboutBag(resultLongAL, item, serverPlayer);
		} else if (resultLongAL.get(0).getAddType() == -2) {
			MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_F), serverPlayer);
		} else if (resultLongAL.get(0).getAddType() == -1) {
			MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_10), serverPlayer);
		}
		result = resultLongAL.get(0).getAddType();
		return result;
	}
	
	/**
	 * 根据itemdataid更新
	 */
	public int updateItemInbagByItemDataId(long itemDataId, int count, ServerPlayer serverPlayer) {
		Item item = this.queryItem(itemDataId, serverPlayer);
		int result = -1;
		result = updateItemInbag(item, count, serverPlayer);
		return result;
	}
	
	/**
	 * 
	 * putItemInBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param count
	 * @param serverPlayer
	 * @return
	 */
	public int putItemInBag(Item item, int count, ServerPlayer serverPlayer) {
		int result = -2;
		if (item != null) {
			// 添加物品到背包
			List<PackItem> resultLongAL = new ArrayList<PackItem>();
			resultLongAL = serverPlayer.getBag().putItemInBag(item, count, serverPlayer.getRole().getId(), dao);
			sendAddItemMessage(serverPlayer, resultLongAL);
			result = resultLongAL.get(0).getAddType();
		}
		return result;
	}
	
	/***
	 * 更具编号放入
	 * 
	 * 
	 */
	public int putItemInBagByPropertyId(int propertyId, int count, ServerPlayer serverPlayer) {
		int result = -2;
		result = isGridEnough(propertyId, count, serverPlayer);
		if (result < 0) {
			return result;
		}
		Item item = ItemManage.createItem(propertyId);
		if (item != null) {
			result = putItemInBag(item, count, serverPlayer);
		}
		return result;
	}
	
	/**
	 * 根据编号列表添加 putItemInBagByPropertyIds:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param propertyInfo
	 * @param count
	 * @param serverPlayer
	 */
	public void putItemInBagByPropertyIds(int[][] propertyInfo, ServerPlayer serverPlayer) {
		if (propertyInfo != null && propertyInfo.length > 0) {
			for (int i = 0; i < propertyInfo.length; i++) {
				int propertyId = propertyInfo[i][0];
				int count = propertyInfo[i][i];
				putItemInBagByPropertyId(propertyId, count, serverPlayer);
			}
		}
		
	}
	
	/**
	 * 删除item
	 */
	
	public int delItemInBag(long ItemDataId, ServerPlayer serverPlayer) {
		int result = 0;
		Item item = serverPlayer.getBag().delItemInBag(ItemDataId, serverPlayer.getRole().getId(), dao, -1);
		if (item != null) {
			MessageSend.sendToGate(sendDelItemFromBagResponse(ItemDataId, item.getItemData().getItemType()), serverPlayer);
			result = 1;
		}
		return result;
	}
	
	
	/** 
	 * createDropItem:(). <br/> 
	 * TODO().<br/> 
	 *  创建掉落的道具
	 * @author lyh 
	 * @param itemConfigId
	 * @param num
	 * @param playerId
	 * @param pos
	 * @return 
	 */  
	public DropItem createDropItem(GameSpace gameSpace,int itemConfigId,int num,long playerId,Position3D pos,boolean hasProtectTime,int dropType){
		DropItem dropItem = new DropItem();
		dropItem.setId(ServerUUID.getUUID());
		dropItem.setItemConfigId(itemConfigId);
		dropItem.setOwnerId(playerId);
		dropItem.setNum(num);
		dropItem.setPosition3D(pos.getX(), pos.getY(), pos.getZ());
		MapObjectMessage mom = new MapObjectMessage();
		if (hasProtectTime){
			dropItem.setProtectTime();
		}
		dropItem.setDropType(dropType);
		dropItem.setDisappearTime();
		mom.init(dropItem);
		dropItem.setMapObjectMessage(mom);
		gameSpace.getDropItemMaps().put(dropItem.getId(), dropItem);
		gameSpace.addToMap(dropItem);
		gameSpace.doMapObjectAddView(dropItem, dropItem.getArea().getNineArea());
		dropItem.setListener(this);
		return dropItem;
	}
	
	/** 
	 * sendDelDropItemResponse:(). <br/> 
	 * TODO().<br/> 
	 * 发送删除道具
	 * @author lyh 
	 * @param dropItemId 
	 */  
	public void sendDelDropItemResponse(DropItem dropItem){
		DelDropItemResponse resp = DelDropItemResponse.newBuilder().setItemId(dropItem.getId()).build();
		for (Map.Entry<Long, IMapObject> entry : dropItem.getViewMap().entrySet()){
			 IMapObject obj = entry.getValue();
			if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER){
				MessageSend.sendToGate(resp, (ServerPlayer)obj);
			}
		}
	}


	@Override
    public void addItemToBag(DropItem dItem) {
	    // TODO Auto-generated method stub
		ServerPlayer sp = GameGlogalContainer.getRolesMap().get(dItem.getRoleId());
		if (sp == null){
			return;
		}
	    //有可能是游戏币
		if (dItem.getDropType() == DropItem.DROP_TYPE_MONEY){
			
			sp.addMoney(dItem.getNum());
			List<AttributeData> attrDataList = new ArrayList<AttributeData>();
			attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.MONEY_VALUE,sp.getRole().getMoney()));
			GlobalMsgManage.sendUpdateAttrResponse(sp, attrDataList, sp);
		}else {
			if(taskManage.taskOfCollect(dItem.getItemConfigId(), sp)==0){
				//不是任务道具 添加普通道具
				this.putItemInBagByPropertyId(dItem.getItemConfigId(), dItem.getNum(), sp);
			}
		}
    }
}
