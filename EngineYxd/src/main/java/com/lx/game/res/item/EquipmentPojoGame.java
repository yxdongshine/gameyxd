package com.lx.game.res.item;

import com.lx.game.item.EquitItem;
import com.lx.nserver.txt.EquipmentPojo;
import com.read.tool.txt.*;

/**
 * 装备配置表
 **/
public class EquipmentPojoGame extends Property {
	
	public EquipmentPojoGame(EquipmentPojo equipmentProperty) {
		this.id = equipmentProperty.getId();
		this.name = equipmentProperty.getName();
		this.level = equipmentProperty.getLevel();
		this.itemClass = equipmentProperty.getItemClass();
		this.mainAttribute = equipmentProperty.getMainAttribute();
		this.quality = equipmentProperty.getQuality();
		this.price = equipmentProperty.getPrice();
		this.currency = equipmentProperty.getCurrency();
		this.career = equipmentProperty.getCareer();
		this.bind = equipmentProperty.getBind();
		this.bagClass = equipmentProperty.getBagClass();
		this.socket = equipmentProperty.getSocket();
		this.pic = equipmentProperty.getPic();
		this.model = equipmentProperty.getModel();
		this.dropModel = equipmentProperty.getDropModel();
		this.bonus = equipmentProperty.getBonus();
		this.growthbonus = equipmentProperty.getGrowthbonus();
		this.growth = equipmentProperty.getGrowth();
		this.cantfenjie = equipmentProperty.getCantfenjie();
		this.cantSold = equipmentProperty.getCantSold();
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 名称 **/
	@TxtString
	private String name;
	
	/** 级别 **/
	@TxtInt
	private int level;
	
	/** 物品等级 **/
	@TxtInt
	private int itemClass;
	
	/** 物品属性 **/
	@TxtInt
	private int mainAttribute;
	
	/** 物品品质 **/
	@TxtInt
	private int quality;
	
	/** 价格数量 **/
	@TxtInt
	private int price;
	
	/** 货币类型 **/
	@TxtInt
	private int currency;
	
	/** 职业名称 **/
	@TxtString
	private int career;
	
	/** 绑定类型 **/
	@TxtInt
	private int bind;
	
	/** 背包类型 **/
	@TxtInt
	private int bagClass;
	
	/** 宝石孔 **/
	@TxtString
	private String socket;
	
	/** 图片 **/
	@TxtString
	private String pic;
	
	/** 模型 **/
	@TxtString
	private String model;
	
	/** 掉落模型 **/
	@TxtString
	private String dropModel;
	
	/** 加成 **/
	@TxtInt
	private int bonus;
	
	/** 加成规则 **/
	@TxtInt
	private int growthbonus;
	
	/** 成长 **/
	@TxtInt
	private int growth;
	
	/** 是否能分解 **/
	@TxtByte
	private byte cantfenjie;
	
	/** 是否能卖（0,不能；1.能） **/
	@TxtByte
	private byte cantSold;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int _level) {
		level = _level;
	}
	
	public int getItemClass() {
		return itemClass;
	}
	
	public void setItemClass(int _itemClass) {
		itemClass = _itemClass;
	}
	
	public int getMainAttribute() {
		return mainAttribute;
	}
	
	public void setMainAttribute(int _mainAttribute) {
		mainAttribute = _mainAttribute;
	}
	
	public int getQuality() {
		return quality;
	}
	
	public void setQuality(int _quality) {
		quality = _quality;
	}
	
	public int getPrice() {
		return price;
	}
	
	public void setPrice(int _price) {
		price = _price;
	}
	
	public int getCurrency() {
		return currency;
	}
	
	public void setCurrency(int _currency) {
		currency = _currency;
	}
	
	public int getCareer() {
		return career;
	}
	
	public void setCareer(int _career) {
		career = _career;
	}
	
	public int getBind() {
		return bind;
	}
	
	public void setBind(int _bind) {
		bind = _bind;
	}
	
	public int getBagClass() {
		return bagClass;
	}
	
	public void setBagClass(int _bagClass) {
		bagClass = _bagClass;
	}
	
	public String getSocket() {
		return socket;
	}
	
	public void setSocket(String _socket) {
		socket = _socket;
	}
	
	public String getPic() {
		return pic;
	}
	
	public void setPic(String _pic) {
		pic = _pic;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String _model) {
		model = _model;
	}
	
	public String getDropModel() {
		return dropModel;
	}
	
	public void setDropModel(String _dropModel) {
		dropModel = _dropModel;
	}
	
	public int getBonus() {
		return bonus;
	}
	
	public void setBonus(int _bonus) {
		bonus = _bonus;
	}
	
	public int getGrowthbonus() {
		return growthbonus;
	}
	
	public void setGrowthbonus(int _growthbonus) {
		growthbonus = _growthbonus;
	}
	
	public int getGrowth() {
		return growth;
	}
	
	public void setGrowth(int _growth) {
		growth = _growth;
	}
	
	public byte getCantfenjie() {
		return cantfenjie;
	}
	
	public void setCantfenjie(byte _cantfenjie) {
		cantfenjie = _cantfenjie;
	}
	
	@Override
	public byte getCantSold() {
		return cantSold;
	}
	
	public void setCantSold(byte cantSold) {
		this.cantSold = cantSold;
	}
	
	@Override
	Item subCreate(long uuid) {
		// TODO Auto-generated method stub
		return new EquitItem(this, uuid);
	}
	
	@Override
	public int getFolderableNum() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	/**
	 * 
	 * @return 装备的主属性
	 */
	public int[][] getMainAttributeData() {
		if (ItemConfigLoad.getMainattributeHashMap().get(this.mainAttribute) == null) {
			return new int[][] {};
		}
		return ItemConfigLoad.getMainattributeHashMap().get(this.mainAttribute).getMainAttribute(level);
	}
	
}