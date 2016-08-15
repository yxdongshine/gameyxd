package com.lx.game.item;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lib.utils.ServerUUID;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.Property;

/**
 * 
 * 道具物品管理类 ClassName:ItemManga <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-2 下午2:54:44 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class ItemManage {
	
	private static final Log log = LogFactory.getLog(ItemManage.class);
	
	/**
	 * Creates a new instance of ItemManage.
	 * 
	 */
	
	/**
	 * 此保存的是临时创建物品 方便持久层 和数据层通过id 查看item的原型数据
	 * 
	 * 物品的销毁和创建 一定记得remove put
	 */
	private static final ConcurrentHashMap<Long, Item> temporaryItemHashMap = new ConcurrentHashMap<Long, Item>();
	
	/**
	 * 创建物品 createBagItem:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param roleId 角色编号
	 * @param itemConfigId 道具模板编号
	 * @return
	 */
	public static Item createItem(int itemConfigId) {
		long itemId = 0;// 创建背包物品id
		// 测试数据
		itemId = ServerUUID.getUUID();
		Property property = ItemConfigLoad.getPrototype(itemConfigId);
		Item item = null;
		if (property != null) {
			item = property.create(itemId);
			log.error("******item.getId()*******" + item.getId());
			
		}
		return item;
	}
	
	public static ConcurrentHashMap<Long, Item> getTemporaryitemhashmap() {
		return temporaryItemHashMap;
	}
	
}
