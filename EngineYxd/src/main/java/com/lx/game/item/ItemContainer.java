package com.lx.game.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.ItemData;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.res.item.Property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ClassName:ItemContainer <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午9:34:50 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class ItemContainer {
	
	/**
	 * 日志文件
	 */
	private static Log log = LogFactory.getLog(ItemContainer.class);
	
	public boolean lock;
	/**
	 * 创建的 初始容量
	 */
	public int capacity;
	
	/**
	 * 该类型背包中格子的索引数 当前应该放第几格了
	 */
	public int index = 0;
	
	/**
	 * 该类型的最大格子数
	 */
	public int maxGrid;
	
	/**
	 * 背包类型
	 */
	public int bagType;
	
	/**
	 * 该类型背包开格子数规则
	 */
	public ArrayList<BagOpenGridConfig> bagOpenGridConfigAL = new ArrayList<BagOpenGridConfig>();
	/**
	 * 持有itemdata对象
	 */
	public List<Cell> cells;
	
	/**
	 * 创建固定大小 类型限制的背包
	 * 
	 * @param capacity
	 */
	public ItemContainer(int capacity) {
		this.capacity = capacity;
		initCells(capacity);
		
	}
	
	/**
	 * 初始化背包格子数 initCells:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param capacity
	 */
	public void initCells(int capacity) {
		this.cells = new ArrayList<Cell>();
		for (int i = 0; i < capacity; i++) {
			this.cells.add(new Cell());
		}
	}
	
	/**
	 * 
	 * AddCells:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param AddCapacity
	 */
	public void AddCells(int AddCapacity) {
		for (int i = 0; i < AddCapacity; i++) {
			this.cells.add(new Cell());
		}
	}
	
	/**
	 * 每个格子对象 ClassName: Cell <br/>
	 * Function: TODO (). <br/>
	 * Reason: TODO (). <br/>
	 * date: 2015-7-1 上午11:19:01 <br/>
	 * 
	 * @author yxd
	 * @version ItemContainer
	 */
	public class Cell {
		private Item item;
		
		public Item getItem() {
			return item;
		}
		
		public void setItem(Item item) {
			this.item = item;
		}
		
		public boolean isEmpty() {
			return item == null;
		}
		
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static final Comparator<Cell>[] cellCompartors = new Comparator[] { new Comparator<ItemContainer.Cell>() {
		
		@Override
		public int compare(Cell o1, Cell o2) {
			if (o1.isEmpty()) {
				return 1;
			} else if (o2.isEmpty()) {
				return -1;
			} else {
				return o1.getItem().compareTo(o2.getItem());
			}
		}
	}, };
	
	/**
	 * 
	 * arrange:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void arrange() {
		Cell[] array = new Cell[this.cells.size()];
		array = this.cells.toArray(array);
		Arrays.sort(array, cellCompartors[0]);
		this.cells = Arrays.asList(array);
		// 修改在背包中的索引
		for (int i = 0; i < this.cells.size(); i++) {
			if (this.cells.get(i) != null && this.cells.get(i).getItem() != null && this.cells.get(i).getItem().getItemData() != null) {
				this.cells.get(i).getItem().getItemData().setIndexInBag(i);
			}
			
		}
	}
	
	/**
	 * 装备类数据 ClassName: equipmentCell <br/>
	 * Function: TODO (). <br/>
	 * Reason: TODO (). <br/>
	 * date: 2015-7-3 下午4:06:56 <br/>
	 * 
	 * @author yxd
	 * @version ItemContainer
	 * 
	 *          public class equipmentCell{ private EquipmentData equipmentData;
	 * 
	 *          public EquipmentData getEquipmentData() { return equipmentData; }
	 * 
	 *          public void setEquipmentData(EquipmentData equipmentData) { this.equipmentData = equipmentData; }
	 * 
	 * 
	 *          }
	 */
	/**
	 * 仅仅判断能不能添加物品 onlyCanAddItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemTypeId
	 * @param count
	 * @return
	 */
	public byte onlyCanAddBuyItem(int itemTypeId, int count) {
		byte addItemType = -1;
		Property property = ItemConfigLoad.getPrototype(itemTypeId);
		ItemData itemData = null;
		if (property.getFolderableNum() > 1) {// 表示可以折叠
			for (int i = 0; i < this.cells.size(); i++) {
				if (this.cells.get(i) != null && this.cells.get(i).getItem() != null && this.cells.get(i).getItem().getItemData() != null && this.cells.get(i).getItem().getItemData().getConfigId() == itemTypeId && this.cells.get(i).getItem().getItemData().getNumber() < property.getFolderableNum()) {// 表示找到
					itemData = this.cells.get(i).getItem().getItemData();
					addItemType = this.addGridNumber(property, itemData, count, true);
					
					break;
				}
				itemData = null;
				// 没有找到的情况下
				if (this.cells.size() - 1 == i) {
					// itemData = this.cells.get(this.index).getItem().getItemData();
					addItemType = this.addGridNumber(property, itemData, count, true);
					
				}
			}
		} else {// 表示不能被折叠
		
			addItemType = this.addGridNumber(property, itemData, count, false);
		}
		
		return addItemType;
		
	}
	
	/**
	 * 该id物品是否能添加到该类型背包中
	 * 
	 * 先看该物品是否可以折叠 可以折叠则 先看物品是否存在 存在则加上已经存在的数量 否 就看折叠数与格子数之间的关系 不可折叠 就直接看添加的数量 与格子之间的关系
	 * 
	 * canAddItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemId
	 * @return 返回0
	 */
	public List<PackItem> canAddItem(Item temporaryItem, int count, long roleId, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		int addItemType = -1;
		if (temporaryItem == null || temporaryItem.getProperty() == null) {
			log.error("item不存在");
			PackItem pi = new PackItem(addItemType, 0, null);
			resultLongAL.add(pi);
			return resultLongAL;
		}
		ItemData itemData = null;
		if (temporaryItem.getProperty().getFolderableNum() > 1) {// 表示可以折叠
			if (temporaryItem.getItemData() == null) {// 表示新建的物品 itemdata 为空
				itemData = this.queryItemBagByConfigId(temporaryItem.getProperty().getId(), temporaryItem.getProperty().getFolderableNum());
				addItemType = this.addGridNumber(temporaryItem.getProperty(), itemData, count, true);
				if (addItemType == -1) {// 表示添加失败 需要买格子
					PackItem pi = new PackItem(addItemType, 0, null);
					resultLongAL.add(pi);
					return resultLongAL;
				} else {
					PackItem pi = new PackItem(addItemType, 0, null);
					resultLongAL.add(pi);
					resultLongAL.addAll(addItem(addItemType, temporaryItem, itemData, count, roleId, dao));
				}
			} else {
				addItemType = this.addGridNumber(temporaryItem.getProperty(), null, count, true);
				if (addItemType == -1) {// 表示添加失败 需要买格子
					PackItem pi = new PackItem(addItemType, 0, null);
					resultLongAL.add(pi);
					return resultLongAL;
				} else {
					PackItem pi = new PackItem(addItemType, 0, null);
					resultLongAL.add(pi);
					resultLongAL.addAll(addItem(addItemType, temporaryItem, null, count, roleId, dao));
				}
			}
			
		} else {// 表示不能被折叠
			addItemType = this.addGridNumber(temporaryItem.getProperty(), itemData, count, false);
			if (addItemType == -1) {// 表示添加失败 需要买格子
				PackItem pi = new PackItem(addItemType, 0, null);
				resultLongAL.add(pi);
				return resultLongAL;
			} else {
				PackItem pi = new PackItem(addItemType, 0, null);
				resultLongAL.add(pi);
				Item item = null;
				for (int i = 0; i < addItemType; i++) {
					item = openNewGrid(temporaryItem, 1, roleId, dao, 0);
					PackItem pi2 = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_ADD, 0, item);
					resultLongAL.add(pi2);
				}
			}
		}
		return resultLongAL;
	}
	
	/**
	 * 
	 * addGridNumber:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @param itemData
	 * @param count
	 * @param canFolderable
	 * @return -1 表示格子数不够需要添加格子;0 表示不需要添加
	 */
	public byte addGridNumber(Property item, ItemData itemData, int count, boolean canFolderable) {
		byte gridNumber = -1;
		if (canFolderable) {// 可以折叠
			if (itemData != null) {
				if (itemData.getNumber() + count > item.getFolderableNum()) {// 开始计算格子数
					// 最后格子 物品数量
					int lastNumber = (itemData.getNumber() + count - item.getFolderableNum()) % item.getFolderableNum();
					// 中间新开的格子数量 为 配置折叠数
					int openGridNumber = (itemData.getNumber() + count - item.getFolderableNum()) / item.getFolderableNum();
					gridNumber = (byte) ((lastNumber == 0) ? openGridNumber : openGridNumber + 1);
					gridNumber = (gridNumber + index <= capacity) ? gridNumber : -1;
				} else if (itemData.getNumber() + count <= item.getFolderableNum()) {
					// 表示不需要格子
					gridNumber = 0;
				}
			} else {
				// 不存在该物品 直接新增一个
				// 最后格子 物品数量
				int lastNumber = count % item.getFolderableNum();
				// 中间新开的格子数量 为 配置折叠数
				int openGridNumber = count / item.getFolderableNum();
				gridNumber = (byte) ((lastNumber == 0) ? openGridNumber : openGridNumber + 1);
				gridNumber = (gridNumber + index <= capacity) ? gridNumber : -1;
			}
			
		} else {// 不能被折叠的情况
			gridNumber = (byte) ((count + index <= capacity) ? count : -1);
		}
		return gridNumber;
	}
	
	/**
	 * 
	 * addItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param itemId 道具编号
	 * @param count 道具数量
	 * @return
	 */
	public List<PackItem> addItem(long addItemType, Item temporaryItem, ItemData itemData, int count, long roleId, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		if (addItemType == 0) {// 不需要添加 直接修改数字
			if (itemData != null) {
				if (this.cells.get(itemData.getIndexInBag()) != null && this.cells.get(itemData.getIndexInBag()).getItem() != null && this.cells.get(itemData.getIndexInBag()).getItem().getItemData() != null) {
					this.cells.get(itemData.getIndexInBag()).getItem().getItemData().setNumber(itemData.getNumber() + count);
					dao.saveOrUpdate(itemData);
					PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_UPDATE, count, this.cells.get(itemData.getIndexInBag()).getItem());
					resultLongAL.add(pi);
				}
			}
		} else {
			// 都先填满第一个格子
			Item item = null;
			if (itemData != null) {
				int teapNumber = itemData.getNumber();
				if (this.cells.get(itemData.getIndexInBag()) != null && this.cells.get(itemData.getIndexInBag()).getItem() != null && this.cells.get(itemData.getIndexInBag()).getItem().getItemData() != null) {
					this.cells.get(itemData.getIndexInBag()).getItem().getItemData().setNumber(temporaryItem.getProperty().getFolderableNum());
					dao.saveOrUpdate(itemData);
					PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_UPDATE, temporaryItem.getProperty().getFolderableNum() - teapNumber, this.cells.get(itemData.getIndexInBag()).getItem());
					resultLongAL.add(pi);
				}
				
				// 最后格子 物品数量
				int lastNumber = (teapNumber + count - temporaryItem.getProperty().getFolderableNum()) % temporaryItem.getProperty().getFolderableNum();
				lastNumber = lastNumber == 0 ? temporaryItem.getProperty().getFolderableNum() : lastNumber;
				
				for (int j = 0; j < addItemType; j++) {
					
					if (j == addItemType - 1) {// 最后一个给子
						item = openNewGrid(temporaryItem, lastNumber, roleId, dao, 1);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_ADD, 0, item);
						resultLongAL.add(pi);
					} else {
						item = openNewGrid(temporaryItem, temporaryItem.getProperty().getFolderableNum(), roleId, dao, 1);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_ADD, 0, item);
						resultLongAL.add(pi);
					}
				}
			} else {
				// 最后格子 物品数量
				int lastNumber = count % temporaryItem.getProperty().getFolderableNum();
				lastNumber = lastNumber == 0 ? temporaryItem.getProperty().getFolderableNum() : lastNumber;
				long id = 0;
				for (int j = 0; j < addItemType; j++) {
					
					if (j == addItemType - 1) {// 最后一个给子
						item = openNewGrid(temporaryItem, lastNumber, roleId, dao, 1);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_ADD, 0, item);
						resultLongAL.add(pi);
					} else {
						item = openNewGrid(temporaryItem, temporaryItem.getProperty().getFolderableNum(), roleId, dao, 1);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_ADD, 0, item);
						resultLongAL.add(pi);
					}
				}
			}
			
		}
		return resultLongAL;
	}
	
	/**
	 * 看背包中是否有空格子 isHasNullCell:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public int isHasNullCell() {
		int hasIndex = -1;
		for (int i = 0; i < index; i++) {
			if (this.cells.get(i).getItem() == null) {
				hasIndex = i;
				break;
			}
		}
		
		return hasIndex;
	}
	
	/**
	 * 创建一个新的格子对象数据
	 * 
	 * addOrUpdate 0表示添加 1表示是新增进来的 openNewGrid:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public Item openNewGrid(Item temporaryItem1, int count, long roleId, EntityDAO dao, int addOrUpdate) {
		ItemData itemTemporaryData = new ItemData();
		// 根据模板编号创建新道具
		Item item = null;
		int itemDateCellIndex = -1;
		if (isHasNullCell() == -1) {
			itemDateCellIndex = index;
		} else {
			itemDateCellIndex = isHasNullCell();
		}
		long id = 0;
		if (temporaryItem1 instanceof EquitItem) {// 如果是装备
			EquitItem equitItem = (EquitItem) temporaryItem1;
			
			if (addOrUpdate == 0) {// 普通添加
				item = temporaryItem1;
				id = temporaryItem1.getId();
			} else if (addOrUpdate == 1) {// 超过格子添加
				item = ItemManage.createItem(equitItem.getProperty().getId());
				id = item.getId();
			}
			
			itemTemporaryData.setId(id);
			itemTemporaryData.setConfigId(equitItem.getProperty().getId());
			itemTemporaryData.setItemType(equitItem.getProperty().getBagClass());
			itemTemporaryData.setNumber(count);
			itemTemporaryData.setQuality(equitItem.getProperty().getQuality());
			itemTemporaryData.setIndexInBag(itemDateCellIndex);
			itemTemporaryData.setRoleId(roleId);
			// 设置装备的固有属性
			itemTemporaryData.setPos(-1);// 装备绑定位
			itemTemporaryData.setScore(equitItem.getScore());// 初始化分数
			itemTemporaryData.setSocket(equitItem.getBornSocket());// 初始化孔
			item.setItemData(itemTemporaryData);
			cells.get(itemDateCellIndex).setItem(item);
			if (isHasNullCell() == -1) {
				index++;
			}
			dao.saveOrUpdate(item.getItemData());
			
		} else if (temporaryItem1 instanceof CommonToolItem) {// 如果是普通道具
			CommonToolItem temporaryItem = (CommonToolItem) temporaryItem1;
			
			if (addOrUpdate == 0) {// 普通添加
				item = temporaryItem1;
				id = temporaryItem1.getId();
			} else if (addOrUpdate == 1) {// 超过格子添加
				item = ItemManage.createItem(temporaryItem.getProperty().getId());
				id = item.getId();
			}
			
			itemTemporaryData.setId(id);
			itemTemporaryData.setConfigId(temporaryItem.getProperty().getId());
			itemTemporaryData.setItemType(temporaryItem.getProperty().getBagClass());
			itemTemporaryData.setNumber(count);
			itemTemporaryData.setQuality(temporaryItem.getProperty().getQuality());
			itemTemporaryData.setIndexInBag(itemDateCellIndex);
			itemTemporaryData.setRoleId(roleId);
			item.setItemData(itemTemporaryData);
			cells.get(itemDateCellIndex).setItem(item);
			if (isHasNullCell() == -1) {
				index++;
			}
			dao.saveOrUpdate(item.getItemData());
		}
		
		return item;
	}
	
	/**
	 * 删除角色背包物品 delItemBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ItemDataId
	 * @param RoleId
	 * @return
	 */
	public boolean delItemBag(Item item, long RoleId) {
		if (item == null || RoleId <= 0) {
			log.error("物品不存在");
			return false;
		}
		
		if (item.getItemData() != null) {
			int index = item.getItemData().getIndexInBag();
			if (this.cells.get(index) != null && this.cells.get(index).getItem() != null && this.cells.get(index).getItem().getItemData() != null && this.cells.get(index).getItem().getItemData().getId() == item.getItemData().getId()) {// 表示找到
				// 删除背包数据itemData
				this.getCells().get(index).setItem(null);
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 修改背包数据 updateItemBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ItemDataId
	 * @param roleId
	 * @return -2表示异常 ；-1 表示使用减少量小于0 不够用；1表示需要购买格子数 ;111表示不需要添加格子直接修改数量
	 */
	public List<PackItem> updateItemBag(Item item, int count, long roleId, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		byte result = -2;
		if (item == null || roleId <= 0) {
			log.debug("物品编号不存在" + "角色编号：" + roleId);
			result = -2;
			PackItem pi = new PackItem(result, 0, null);
			resultLongAL.add(pi);
			return resultLongAL;
		}
		if (item != null && item.getItemData() != null && item.getProperty() != null) {
			if (this.getCells().get(item.getItemData().getIndexInBag()) != null && this.getCells().get(item.getItemData().getIndexInBag()).getItem() != null) {
				ItemData itemData = this.getCells().get(item.getItemData().getIndexInBag()).getItem().getItemData();
				int folderableNum = item.getProperty().getFolderableNum();
				if (itemData != null && itemData.getId() == item.getItemData().getId()) {// 找到该物品
					if (this.summerItemDataByConfigId(itemData.getConfigId()) + count < 0) {// 数量不够
						result = -1;
						PackItem pi = new PackItem(result, 0, null);
						resultLongAL.add(pi);
						return resultLongAL;
					} else if (itemData.getNumber() + count >= 0 && itemData.getNumber() + count <= folderableNum) {// 修改数量
						dao.saveOrUpdate(itemData);
						this.getCells().get(item.getItemData().getIndexInBag()).getItem().getItemData().setNumber(itemData.getNumber() + count);
						result = 111;
						PackItem pi0 = new PackItem(result, 0, null);
						resultLongAL.add(pi0);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_UPDATE, count, this.cells.get(itemData.getIndexInBag()).getItem());
						resultLongAL.add(pi);
						if (itemData.getNumber() == 0) {
							this.getCells().get(item.getItemData().getIndexInBag()).setItem(null);
							// 删掉数据库
							dao.delete(itemData);
							PackItem pi2 = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_DEL, 0, item);
							resultLongAL.add(pi2);
						}
						
					} else if (itemData.getNumber() + count > folderableNum) {
						boolean isCanfolderable = false;
						if (folderableNum > 1) {
							isCanfolderable = true;
						} else {
							isCanfolderable = false;
						}
						byte addItemType = this.addGridNumber(item.getProperty(), itemData, count, isCanfolderable);
						if (addItemType == -1) {// 表示添加失败 需要买格子
							result = -1;
							PackItem pi = new PackItem(result, 0, null);
							resultLongAL.add(pi);
							return resultLongAL;
						} else {
							result = 111;
							PackItem pi = new PackItem(result, 0, null);
							resultLongAL.add(pi);
							resultLongAL.addAll(addItem(addItemType, item, itemData, count, roleId, dao));
							return resultLongAL;
						}
					} else if (this.summerItemDataByConfigId(itemData.getConfigId()) + count > 0 && itemData.getNumber() + count < 0) {// 这种就是要计算删除和修改
						result = 111;
						PackItem pi0 = new PackItem(result, 0, null);
						resultLongAL.add(pi0);
						int delNumner = item.getItemData().getNumber();
						// 首先就是删除item
						this.getCells().get(item.getItemData().getIndexInBag()).setItem(null);
						// 删掉数据库
						dao.delete(itemData);
						PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_DEL, 0, item);
						resultLongAL.add(pi);
						
						// 计算剩下的数量与格子删除关系
						resultLongAL.addAll(delOrUpList(-(count + delNumner), itemData.getConfigId(), folderableNum, dao));
					}
					
				} else {
					log.error("itemdata 不存在");
				}
			} else {
				log.error("itemData不存在");
			}
			
		} else {
			log.error("数据异常");
		}
		
		return resultLongAL;
	}
	
	/**
	 * 根据itemData查找物品编号 queryItemBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ItemDataId
	 * @return
	 */
	public ItemData queryItemBag(long ItemDataId) {
		ItemData itemData = null;
		for (int i = 0; i < this.cells.size(); i++) {
			if (this.getCells().get(i) != null && this.getCells().get(i).getItem() != null) {
				itemData = this.getCells().get(i).getItem().getItemData();
				if (itemData != null && itemData.getId() == ItemDataId) {
					return itemData;
				}
			}
		}
		
		return itemData;
	}
	
	/**
	 * 
	 * queryItemBagByConfigId:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param configId
	 * @return
	 */
	public ItemData queryItemBagByConfigId(long configId, int fordernumber) {
		ItemData itemData = null;
		for (int i = 0; i < this.cells.size(); i++) {
			if (this.getCells().get(i) != null && this.getCells().get(i).getItem() != null) {
				itemData = this.getCells().get(i).getItem().getItemData();
				if (itemData != null && itemData.getConfigId() == configId) {
					if (fordernumber > 1 && itemData.getNumber() < fordernumber) {
						return itemData;
					}
					
				}
			}
		}
		
		return itemData;
	}
	
	/**
	 * @return the cells
	 */
	public final List<Cell> getCells() {
		return cells;
	}
	
	/**
	 * @return the lock
	 */
	public final boolean isLock() {
		return lock;
	}
	
	/**
	 * @param lock the lock to set
	 */
	public final void setLock(boolean lock) {
		this.lock = lock;
	}
	
	public int getMaxGrid() {
		return maxGrid;
	}
	
	public void setMaxGrid(int maxGrid) {
		this.maxGrid = maxGrid;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public ArrayList<BagOpenGridConfig> getBagOpenGridConfigAL() {
		return bagOpenGridConfigAL;
	}
	
	public void setBagOpenGridConfigAL(ArrayList<BagOpenGridConfig> bagOpenGridConfigAL) {
		this.bagOpenGridConfigAL = bagOpenGridConfigAL;
	}
	
	/**
	 * 
	 * queryItemDataByConfigId:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ConfigId
	 * @return
	 */
	public long queryItemDataByConfigId(int ConfigId) {
		long itemdataId = -1;
		for (int i = 0; i < this.cells.size(); i++) {
			if (this.cells.get(i) != null && this.cells.get(i).getItem() != null && this.cells.get(i).getItem().getItemData() != null && this.cells.get(i).getItem().getItemData().getConfigId() == ConfigId) {// 表示找到
				Item item = this.cells.get(i).getItem();
				ItemData itemData = this.cells.get(i).getItem().getItemData();
				if (itemData.getNumber() < item.getProperty().getFolderableNum()) {
					itemdataId = itemData.getId();
					break;
				}
			}
			
		}
		return itemdataId;
	}
	
	/**
	 * 返回该类型的物品所有数量 summerItemDataByConfigId:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param ConfigId
	 * @return
	 */
	public int summerItemDataByConfigId(int ConfigId) {
		int itemdataId = -1;
		for (int i = 0; i < this.cells.size(); i++) {
			if (this.cells.get(i) != null && this.cells.get(i).getItem() != null && this.cells.get(i).getItem().getItemData() != null && this.cells.get(i).getItem().getItemData().getConfigId() == ConfigId) {// 表示找到
				Item item = this.cells.get(i).getItem();
				ItemData itemData = item.getItemData();
				itemdataId += itemData.getNumber();
			}
			
		}
		return itemdataId;
	}
	
	/**
	 * 
	 * delOrUpList:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sumNumber
	 * @param configId
	 * @param forderNum
	 * @return
	 */
	public List<PackItem> delOrUpList(int sumNumber, int configId, int forderNum, EntityDAO dao) {
		List<PackItem> resultLongAL = new ArrayList<PackItem>();
		// 最后格子 物品数量
		int lastNumber = sumNumber % forderNum;
		// 中间新开的格子数量 为 配置折叠数
		int openGridNumber = sumNumber / forderNum;
		for (int i = 0; i < this.cells.size() && openGridNumber >= 0; i++) {
			if (this.cells.get(i) != null && this.cells.get(i).getItem() != null && this.cells.get(i).getItem().getItemData() != null && this.cells.get(i).getItem().getItemData().getConfigId() == configId) {// 表示找到
				Item item = this.cells.get(i).getItem();
				if (openGridNumber == 0) {// 更新
					this.getCells().get(i).getItem().getItemData().setNumber(this.getCells().get(i).getItem().getItemData().getNumber() - lastNumber);
					dao.saveOrUpdate(item.getItemData());
					PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_UPDATE, -lastNumber, item);
					resultLongAL.add(pi);
				} else {// 删除
					// 首先就是删除item
					this.getCells().get(item.getItemData().getIndexInBag()).setItem(null);
					// 删掉数据库
					dao.delete(item.getItemData());
					PackItem pi = new PackItem(ItemStaticConfig.BAG_ADD_TYPE_DEL, 0, item);
					resultLongAL.add(pi);
				}
				openGridNumber--;
			}
			
		}
		
		return resultLongAL;
	}
}
