package com.lx.game.item;

import com.engine.entityobj.ServerPlayer;
import com.lx.game.item.resConfig.Socket;
import com.lx.game.item.util.Const;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.EquipmentPojoGame;

/**
 * ClassName:EquitItem <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-3 上午11:53:53 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class EquitItem extends Item {
	
	private EquipmentPojoGame equipmentProperty;
	
	private long id;
	
	/**
	 * 装备的孔
	 */
	private int socketEquip;
	
	/**
	 * 分数
	 */
	private int score;
	
	/**
	 * 装备成长值
	 */
	private int growthAddValue;
	
	/**
	 * Creates a new instance of EquitItem.
	 * 
	 */
	public EquitItem(EquipmentPojoGame equipmentProperty, long uuid) {
		// TODO Auto-generated constructor stub
		this.equipmentProperty = equipmentProperty;
		this.id = uuid;
		// 初始化孔位
		this.score = getBornSocket();
		// 初始化分数
		this.score = ItemConfigLoad.getEquipQualityAL().get(this.equipmentProperty.getQuality()).getStandard();
		// 初始化成长值
		this.growthAddValue = 0;
		
	}
	
	/**
	 * 获取天生孔数
	 * 
	 * @return
	 */
	public int getBornSocket() {
		Socket s = ItemConfigLoad.getSocketHashMap().get(equipmentProperty.getSocket());
		int[] arr = s.getProbability();
		int index = getRandomByArr(arr);
		return s.getCount(index);
	}
	
	/**
	 * 必有一个
	 * 
	 * @param arr
	 * @return
	 */
	public int getRandomByArr(int[] probability) {
		int rand = Const.random.nextInt(Const.DENOMINATOR);
		int sum = 0;
		for (int i = 0; i < probability.length; i++) {
			sum += probability[i];
			if (rand < sum) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * 装备的使用效果 effect:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param target
	 */
	public void effect(ServerPlayer sp) {
		// gust 20101110 修改原主属性结构
		// main Attributes
		int[][] mainAtts = equipmentProperty.getMainAttributeData();
		for (int i = 0; i < mainAtts.length; i++) {
			int value = mainAtts[i][1];
			// sp.increaseAttributeByValue((byte) mainAtts[i][0], value + growthAddValue);
			sp.addBaseAttribute(sp.getAttribute(), mainAtts[i][0], value);
			sp.getAttribute().refreshRelatedAttribute(mainAtts[i][0]);
		}
		
	}
	
	/**
	 * 脱掉装备的效果 unEffect:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 */
	public void unEffect(ServerPlayer sp) {
		
		// class Attributes
		int[][] mainAtts = equipmentProperty.getMainAttributeData();
		for (int i = 0; i < mainAtts.length; i++) {
			int value = mainAtts[i][1];
			// target.decreaseAttributeByValue((byte) mainAtts[i][0], value + growthAddValue);
			sp.addBaseAttribute(sp.getAttribute(), mainAtts[i][0], -value);
			sp.getAttribute().refreshRelatedAttribute(mainAtts[i][0]);
		}
		
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	
	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	
	@Override
	public EquipmentPojoGame getProperty() {
		return equipmentProperty;
	}
	
	public void setEquipmentProperty(EquipmentPojoGame equipmentProperty) {
		this.equipmentProperty = equipmentProperty;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
}
