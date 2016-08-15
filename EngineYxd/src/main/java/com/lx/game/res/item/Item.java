package com.lx.game.res.item;

import com.engine.container.GlogalContainer;
import com.engine.domain.ItemData;
import com.engine.entityobj.ServerPlayer;
import com.lx.game.item.CommonToolItem;
import com.lx.game.item.EquitItem;

/**
 * ClassName:Item <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-3 上午11:33:28 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class Item implements Comparable<Item> {
	
	private long id;
	
	private Property property;
	
	private ItemData itemData;
	
	public Property getProperty() {
		return property;
	}
	
	public void setProperty(Property property) {
		this.property = property;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public ItemData getItemData() {
		return itemData;
	}
	
	public void setItemData(ItemData itemData) {
		this.itemData = itemData;
	}
	
	/**
	 * 
	 * 开始功能方法
	 * 
	 * 
	 */
	
	public byte canUse(ServerPlayer me, ServerPlayer target) {
		
		return -1;
	}
	
	public static final byte USE_ITEM_SUCESS_WITH_REMOVE = 0;
	public static final byte USE_ITEM_SUCESS_WITH_OUT_REMOVE = 1;
	public static final byte USE_ITEM_FAIL = -1;
	public static final byte USE_ITEM_FAIL_REMOVE_ALL = -2;
	
	/**
	 * 使用物品
	 * 
	 * @param obj 使用对象--人物
	 * @return 0 使用成功 1 使用成功，不需要移除 -1 使用失败 -2 武器和lucky物品全消失
	 * 
	 *         使用物品 use:(). <br/>
	 *         TODO().<br/>
	 * 
	 * @author yxd
	 * @param me
	 * @param target
	 * @return
	 */
	public byte use(ServerPlayer me, ServerPlayer target) {
		
		return -1;
	}
	
	@Override
	public int compareTo(Item o) {
		// TODO Auto-generated method stub
		ServerPlayer sp = GlogalContainer.getRolesMap().get(this.itemData.getRoleId());
		if (o instanceof EquitItem) {// 如果是装备
			EquitItem equipItemThis = (EquitItem) this;
			EquitItem equipItemO = (EquitItem) o;
			if (sp == null) {// 表示该物品没有持有者
				return 0;
			} else {
				if (equipItemThis.getProperty().getCareer() == sp.getRole().getCareerConfigId()) {// 表示当前对象为该职业装备
					return 1;
				} else if (equipItemO.getProperty().getCareer() == sp.getRole().getCareerConfigId()) {
					return -1;
				} else {// 表示都不相等 或者都相等 则比较 2. 头，身，肩，披风，武器，项链，戒指，时装。
					EquipmentPojoGame equipPropertyThis = (EquipmentPojoGame) this.getProperty();
					EquipmentPojoGame equipPropertyO = (EquipmentPojoGame) o.getProperty();
					if (equipPropertyThis.getBind() > equipPropertyO.getBind()) {
						return 1;
					} else if (equipPropertyThis.getBind() < equipPropertyO.getBind()) {
						return -1;
					} else if (equipPropertyThis.getBind() == equipPropertyO.getBind()) {// 如果相等 则比较等级
						if (equipPropertyThis.getLevel() > equipPropertyO.getLevel()) {
							return 1;
							
						} else if (equipPropertyThis.getLevel() < equipPropertyO.getLevel()) {
							return -1;
						} else if (equipPropertyThis.getLevel() == equipPropertyO.getLevel()) {// 等级相等 比较质量
							if (equipPropertyThis.getQuality() > equipPropertyO.getQuality()) {
								return 1;
							} else if (equipPropertyThis.getQuality() < equipPropertyO.getQuality()) {
								return -1;
							} else {// 等级相等 比较得分
								if (equipItemThis.getScore() > equipItemO.getScore()) {
									return 1;
								} else if (equipItemThis.getScore() < equipItemO.getScore()) {
									return -1;
								} else {// 等级相等 比较名称
									return equipItemThis.getProperty().getName().compareTo(equipItemO.getProperty().getName());
								}
							}
						}
					}
				}
			}
		} else if (o instanceof CommonToolItem) {
			CommonToolItem commonItemThis = (CommonToolItem) this;
			CommonToolItem commonItemO = (CommonToolItem) o;
			PropertyPojoGame propertyPojoGameCommono = commonItemO.getProperty();
			PropertyPojoGame propertyPojoGameCommonThis = commonItemThis.getProperty();
			if (propertyPojoGameCommonThis.getItemType() > propertyPojoGameCommono.getItemType()) {
				return 1;
			} else if (propertyPojoGameCommonThis.getItemType() < propertyPojoGameCommono.getItemType()) {
				return -1;
			} else {// 比较品质
				if (propertyPojoGameCommonThis.getQuality() > propertyPojoGameCommono.getQuality()) {
					return 1;
				} else if (propertyPojoGameCommonThis.getQuality() < propertyPojoGameCommono.getQuality()) {
					return -1;
				} else {// 比较等级
					if (propertyPojoGameCommonThis.getLevel() > propertyPojoGameCommono.getLevel()) {
						return 1;
					} else if (propertyPojoGameCommonThis.getLevel() > propertyPojoGameCommono.getLevel()) {
						return -1;
					} else {// 最后比较名称
						return propertyPojoGameCommonThis.getName().compareTo(propertyPojoGameCommono.getName());
					}
				}
			}
			
		}
		return 0;
	}
}
