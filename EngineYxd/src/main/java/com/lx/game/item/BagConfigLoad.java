package com.lx.game.item;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lx.nserver.model.BagConfigMode;
import com.lx.nserver.txt.BagConfigPojo;

/**
 * ClassName:BagConfigLoad <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:28:12 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class BagConfigLoad {
	/**
	 * Creates a new instance of BagConfigLoad.
	 * 
	 */
	private static Log log = LogFactory.getLog(BagConfigLoad.class);
	
	/**
	 * 道具类型
	 */
	public static final byte BAG_EQUIP = 0;// 装备类
	public static final byte BAG_COMMON = 1;// 普通物品类
	public static final byte BAG_SPECIAL = 2;// 特殊物品类
	
	@Autowired
	BagConfigMode bagConfigMOde;
	
	CopyOnWriteArrayList<BagConfigPojo> bagConfigPojoAL;
	/**
	 * 存储配置数组
	 */
	public static StoreLvConfig[] bagCfg;
	
	private BagConfigLoad() {
		// TODO Auto-generated constructor stub
		bagConfigPojoAL = new CopyOnWriteArrayList<BagConfigPojo>();
	}
	
	public void load() {
		
		bagConfigPojoAL = (CopyOnWriteArrayList<BagConfigPojo>) bagConfigMOde.getReslList();
		log.error("******************" + bagConfigMOde.getReslList().size());
		initBagConfig();
		
	}
	
	/**
	 * 实例化存储配置 initBagConfig:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void initBagConfig() {
		bagCfg = new StoreLvConfig[bagConfigPojoAL.size()];
		/**
		 * 实例化
		 */
		for (int i = 0; i < bagCfg.length; i++) {
			BagConfigPojo bagConfigPojo = bagConfigPojoAL.get(i);
			if (bagConfigPojo != null) {
				StoreLvConfig storeLvConfig = new StoreLvConfig();
				storeLvConfig.setDefaultGrid(bagConfigPojo.getDedaultGrid());
				storeLvConfig.setMaxGrid(bagConfigPojo.getMaxGrid());
				String buyGridFee = bagConfigPojo.getBuyGridFee();
				ArrayList<BagOpenGridConfig> openGridAL = new ArrayList<BagOpenGridConfig>();
				if (buyGridFee != null && buyGridFee.trim().length() > 0) {// 花费配置是以#分开为数组;*分隔为对象内属性
					String[] bagOpenGridConfigArray = buyGridFee.split("#");
					for (int j = 0; j < bagOpenGridConfigArray.length; j++) {
						String bagOpenGridConfigStr = bagOpenGridConfigArray[i];
						String[] bagOpenGridConfigStrArray = bagOpenGridConfigStr.split("\\*");
						for (int k = 0; k < bagOpenGridConfigStrArray.length; k++) {
							BagOpenGridConfig bagOpenGridConfig = new BagOpenGridConfig();
							bagOpenGridConfig.setCount(Integer.parseInt(bagOpenGridConfigStrArray[k++]));
							bagOpenGridConfig.setCurrency(Integer.parseInt(bagOpenGridConfigStrArray[k++]));
							bagOpenGridConfig.setFee(Integer.parseInt(bagOpenGridConfigStrArray[k++]));
							openGridAL.add(bagOpenGridConfig);
						}
						
					}
					
				}
				
				storeLvConfig.setOpenGridAL(openGridAL);// 添加该类型背包打开格子规则
				bagCfg[i] = storeLvConfig;// 背包配置组
			}
		}
	}
	
}
