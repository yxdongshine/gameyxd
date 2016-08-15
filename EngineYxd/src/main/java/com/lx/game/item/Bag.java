package com.lx.game.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.ItemData;
import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.lx.game.item.ItemContainer.Cell;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.PropertyPojoGame;

/**
 * ClassName:Bag <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午9:34:50 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */

public class Bag {
	
	private static final Log log = LogFactory.getLog(Bag.class);
	/**
	 * 背包组
	 * 
	 * 默认统一化：0 装备类 ；1普通道具类 ；2特殊道具
	 */
	ItemContainer[] subBags;
	
	/**
	 * 
	 */
	Item[] equipData = new Item[8];
	/**
	 * 装备物品
	 */
	List<ItemData> EquipmentData;
	
	/**
	 * 保存药瓶 0和 复活币 1
	 */
	Item[] bottleAndRevive = new Item[2];
	
	/**
	 * 是否下线了？ 下线为true，则从数据库从新加载 ;如果没有下线直接冲缓存中取
	 */
	boolean idOffline = false;
	
	public Bag() {
	}
	
	/**
	 * 创建角色时 初始化背包 initBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param bagData
	 */
	public void initBag(ServerPlayer sp) {
		// 新角色, 初始化默认背包数据
		subBags = new ItemContainer[BagConfigLoad.bagCfg.length];
		for (int i = 0; i < subBags.length; i++) {
			if (i == 0) {// 装备
				sp.getRole().setEquitGridNumber(BagConfigLoad.bagCfg[i].getDefaultGrid());
			} else if (i == 2) {// 普通
				sp.getRole().setCommonGridNumber(BagConfigLoad.bagCfg[i].getDefaultGrid());
			}
			subBags[i] = new ItemContainer(BagConfigLoad.bagCfg[i].getDefaultGrid());
			subBags[i].setMaxGrid(BagConfigLoad.bagCfg[i].getMaxGrid());
			// subBags[i].ownerGridNumber=BagConfigLoad.bagCfg[i].getDefaultGrid();
			subBags[i].bagType = i;// 背包类型
			subBags[i].setBagOpenGridConfigAL(BagConfigLoad.bagCfg[i].getOpenGridAL());// 背包格子数规则
		}
	}
	
	/**
	 * 角色存在 加载背包数据 loadBagData:(). <br/>
	 * TODO().<br/>
	 * 
	 * 
	 * @author yxd
	 * @param commonItemData
	 * @param specialItemData
	 */
	public void loadBagData(Role role, EntityDAO dao) {
		// 先判断格子是否扩展过
		if (role.getEquitGridNumber() > 16) {
			subBags[0].capacity = role.getEquitGridNumber();
			subBags[0].AddCells(role.getEquitGridNumber() - 16);
		}
		if (role.getCommonGridNumber() > 16) {
			subBags[1].capacity = role.getCommonGridNumber();
			subBags[1].AddCells(role.getCommonGridNumber() - 16);
		}
		// 装备
		EquipmentData = dao.findByProperties(ItemData.class, new String[] { "roleId", "itemType" }, new Object[] { role.getId(), 0 });
		// 道具
		List<ItemData> commonItemData = dao.findByProperties(ItemData.class, new String[] { "roleId", "itemType" }, new Object[] { role.getId(), 1 });
		List<ItemData> specialItemData = dao.findByProperties(ItemData.class, new String[] { "roleId", "itemType" }, new Object[] { role.getId(), 2 });
		List<ItemData> bottleAndReviveData = dao.findByProperties(ItemData.class, new String[] { "roleId", "itemType" }, new Object[] { role.getId(), 4 });
		int maxIndex = 0;
		if (EquipmentData != null && EquipmentData.size() > 0) {
			// 加载装备
			for (int i = 0; i < EquipmentData.size(); i++) {
				ItemData equipmentData = EquipmentData.get(i);
				if (equipmentData.getPos() <= 0) {// 如果不是装备栏数据
					if (equipmentData.getIndexInBag() > -1 && equipmentData.getIndexInBag() <= subBags[0].capacity) {// 必须在背包数组界中
						if (subBags[0].getCells().get(equipmentData.getIndexInBag()) != null) {
							// 创建物品
							Item item = ItemManage.createItem(equipmentData.getConfigId());
							item.setId(equipmentData.getId());
							item.setItemData(equipmentData);
							subBags[0].getCells().get(equipmentData.getIndexInBag()).setItem(item);
							if (equipmentData.getIndexInBag() > maxIndex) {
								maxIndex = equipmentData.getIndexInBag();
							}
						}
					}
					
				} else {
					// 创建物品
					Item item = ItemManage.createItem(equipmentData.getConfigId());
					item.setId(equipmentData.getId());
					item.setItemData(equipmentData);
					equipData[equipmentData.getPos() - 1] = item;
				}
				
			}
			subBags[0].index = ++maxIndex;
		}
		maxIndex = 0;
		if (commonItemData != null && commonItemData.size() > 0) {
			// 加载普通道具
			for (int i = 0; i < commonItemData.size(); i++) {
				ItemData itemData = commonItemData.get(i);
				if (itemData.getIndexInBag() > -1 && itemData.getIndexInBag() <= subBags[1].capacity) {// 必须在背包数组界中
					if (subBags[1].getCells().get(itemData.getIndexInBag()) != null) {
						// 创建物品
						Item item = ItemManage.createItem(itemData.getConfigId());
						item.setId(itemData.getId());
						item.setItemData(itemData);
						subBags[1].getCells().get(itemData.getIndexInBag()).setItem(item);
						if (itemData.getIndexInBag() > maxIndex) {
							maxIndex = itemData.getIndexInBag();
						}
					}
				}
				
			}
			subBags[1].index = ++maxIndex;
		}
		maxIndex = 0;
		if (specialItemData != null && specialItemData.size() > 0) {
			// 加载特殊道具
			for (int i = 0; i < specialItemData.size(); i++) {
				ItemData itemData = specialItemData.get(i);
				if (itemData.getIndexInBag() > -1 && itemData.getIndexInBag() <= subBags[2].capacity) {// 必须在背包数组界中
					if (subBags[2].getCells().get(itemData.getIndexInBag()) != null) {
						// 创建物品
						Item item = ItemManage.createItem(itemData.getConfigId());
						item.setId(itemData.getId());
						item.setItemData(itemData);
						subBags[2].getCells().get(itemData.getIndexInBag()).setItem(item);
						if (itemData.getIndexInBag() > maxIndex) {
							maxIndex = itemData.getIndexInBag();
						}
					}
				}
			}
			subBags[2].index = ++maxIndex;
		}
		
		// 加载药瓶
		List<PropertyPojoGame> bottlePropList = ItemConfigLoad.getPropertyPojoHM().get(ItemStaticConfig.PROP_TYPE_BOTTLE);
		if (bottleAndReviveData != null && bottleAndReviveData.size() <= 0) {// 表示第一次创建人物
			// 默认按照从小到大排序 取出
			if (bottlePropList != null && bottlePropList.size() > 0) {
				PropertyPojoGame ppg = bottlePropList.get(0);
				// 创建一个物品
				// 创建物品
				Item item = ItemManage.createItem(ppg.getId());
				item.setItemData(buildItemData(ppg, role, item));
				bottleAndRevive[0] = item;// 药瓶
			}
		} else if (bottleAndReviveData.size() > 0) {// 表示已经存在药瓶
			ItemData itemdata = bottleAndReviveData.get(0);
			for (int i = 0; i < bottlePropList.size(); i++) {
				PropertyPojoGame ppg = bottlePropList.get(i);
				if (ppg.getId() == itemdata.getConfigId()) {
					// 创建物品
					Item item = ItemManage.createItem(ppg.getId());
					item.setId(itemdata.getId());
					item.setItemData(buildItemData(ppg, role, item));
					bottleAndRevive[0] = item;// 药瓶
					break;
				}
			}
		}
	}
	
	/**
	 * 创建背包 角色创建时 createBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param roleId
	 */
	public void createBag(ServerPlayer sp) {
		this.initBag(sp);
		
	}
	
	/**
	 * 保存角色的道具编号 saveBagData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param roleId
	 */
	public void saveBagData(EntityDAO dao) {
		// 保存装备栏中数据
		for (int i = 0; i < this.equipData.length; i++) {
			if (this.equipData[i] != null && this.equipData[i].getItemData() != null) {
				dao.saveOrUpdate(this.equipData[i].getItemData());
			}
		}
		
		// 保存背包中的数据
		for (int i = 0; i < this.subBags.length; i++) {
			for (int j = 0; j < this.subBags[i].getCells().size(); j++) {
				Cell cell = this.subBags[i].getCells().get(j);
				if (cell != null) {
					Item item = (Item) cell.getItem();
					if (item != null) {
						dao.saveOrUpdate(item.getItemData());
					}
				}
			}
			
		}
		
		// 保存复活币和药瓶
		for (int i = 0; i < bottleAndRevive.length; i++) {
			if (this.bottleAndRevive[i] != null && this.bottleAndRevive[i].getItemData() != null) {
				dao.saveOrUpdate(this.bottleAndRevive[i].getItemData());
			}
		}
	}
	
	/**
	 * 将创建好的背包物品放入背包 putItemInBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param roleId
	 * @return
	 */
	public List<PackItem> putItemInBag(Item item, int count, long roleId, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		int bagType = -1;
		if (item instanceof CommonToolItem) {
			CommonToolItem commonToolItem = (CommonToolItem) item;
			bagType = commonToolItem.getProperty().getBagClass();
		} else if (item instanceof EquitItem) {
			EquitItem equitItem = (EquitItem) item;
			bagType = equitItem.getProperty().getBagClass();
		}
		// 添加道具到背包
		resultLongAL = this.getSubBags()[bagType].canAddItem(item, count, roleId, dao);
		
		return resultLongAL;
	}
	
	/**
	 * 删除背包数据 delItemInBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemId
	 * @param roleId
	 * @return
	 */
	public Item delItemInBag(long ItemDataId, long roleId, EntityDAO dao, int pos) {
		
		Item item = this.queryItem(ItemDataId, roleId);
		if (item != null) {
			int bagType = -1;
			if (item instanceof CommonToolItem) {
				CommonToolItem commonToolItem = (CommonToolItem) item;
				bagType = commonToolItem.getProperty().getBagClass();
			} else if (item instanceof EquitItem) {
				EquitItem equitItem = (EquitItem) item;
				bagType = equitItem.getProperty().getBagClass();
			}
			if (pos > 0) {
				ItemData teapItemData = (ItemData) item.getItemData().clone();
				teapItemData.setPos(pos);
				teapItemData.setIndexInBag(-1);
				dao.saveOrUpdate(teapItemData);
			} else {
				// 先删数据库 物品
				dao.delete(item.getItemData());
			}
			
			this.getSubBags()[bagType].delItemBag(item, roleId);
			
		}
		return item;
	}
	
	/**
	 * 
	 * updateItemInbag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public List<PackItem> updateItemInbag(Item item, int count, long roleId, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		if (item != null) {
			int bagType = -1;
			if (item instanceof CommonToolItem) {
				CommonToolItem commonToolItem = (CommonToolItem) item;
				bagType = commonToolItem.getProperty().getBagClass();
			} else if (item instanceof EquitItem) {
				EquitItem equitItem = (EquitItem) item;
				bagType = equitItem.getProperty().getBagClass();
			}
			resultLongAL = getSubBags()[bagType].updateItemBag(item, count, roleId, dao);
			
		}
		return resultLongAL;
	}
	
	/**
	 * 查看道具背包数据
	 * 
	 * 遍历所有背包数据
	 * 
	 * queryItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ItemDataId
	 * @param roleId
	 * @return
	 */
	public Item queryItem(long ItemDataId, long roleId) {
		Item item = null;
		item = this.queryInEquipBar(ItemDataId);
		if (item != null)
			return item;
		for (int i = 0; i < this.subBags.length; i++) {
			for (int j = 0; j < subBags[i].getCells().size(); j++) {// 遍历背包中数据
				if (subBags[i].getCells().get(j) != null) {
					Item tempItem = subBags[i].getCells().get(j).getItem();
					if (tempItem != null && tempItem.getItemData() != null && tempItem.getItemData().getId() == ItemDataId) {// 如果不为空
						item = tempItem;
						return item;
					}
				}
			}
		}
		return item;
	}
	
	/**
	 * 装备栏中寻找 queryInEquipBar:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ItemDataId
	 * @return
	 */
	public Item queryInEquipBar(long ItemDataId) {
		Item item = null;
		for (int i = 0; i < this.equipData.length; i++) {// 遍历装备栏中的数据
			if (this.equipData[i] != null && this.equipData[i].getItemData().getId() == ItemDataId) {// 找到
				item = this.equipData[i];
				return item;
			}
		}
		return item;
	}
	
	/**
	 * 
	 * buildItemData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ppg
	 * @param role
	 * @param item
	 * @return
	 */
	public ItemData buildItemData(PropertyPojoGame ppg, Role role, Item item) {
		ItemData itemdata = new ItemData();
		itemdata.setConfigId(ppg.getId());
		itemdata.setId(item.getId());
		itemdata.setIndexInBag(-1);
		itemdata.setNumber(ppg.getFolderableNum());
		itemdata.setItemType(ppg.getItemType());
		itemdata.setRoleId(role.getId());
		return itemdata;
	}
	
	public ItemContainer[] getSubBags() {
		return subBags;
	}
	
	public List<ItemData> getEquipmentData() {
		return EquipmentData;
	}
	
	public void setEquipmentData(List<ItemData> equipmentData) {
		EquipmentData = equipmentData;
	}
	
	public Item[] getEquipData() {
		return equipData;
	}
	
	public void setEquipData(Item[] equipData) {
		this.equipData = equipData;
	}
	
	public Item[] getBottleAndRevive() {
		return bottleAndRevive;
	}
	
}
