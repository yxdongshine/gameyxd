package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*装备配置表
**/
public class EquipmentPojo{

	public EquipmentPojo(){
	}

	/**编号**/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int id;

	/**名称**/
	@TxtString(name = "")
	private String name;

	/**要求人物级别**/
	@TxtInt(name = "")
	private int level;

	/**物品等级**/
	@TxtInt(name = "")
	private int itemClass;

	/**物品属性**/
	@TxtInt(name = "")
	private int mainAttribute;

	/**物品品质**/
	@TxtInt(name = "")
	private int quality;

	/**价格数量**/
	@TxtInt(name = "")
	private int price;

	/**货币类型**/
	@TxtInt(name = "")
	private int currency;

	/**职业名称索引**/
	@TxtInt(name = "")
	private int career;

	/**绑定位置**/
	@TxtInt(name = "")
	private int bind;

	/**背包类型**/
	@TxtInt(name = "")
	private int bagClass;

	/**宝石孔**/
	@TxtString(name = "")
	private String socket;

	/**图片**/
	@TxtString(name = "")
	private String pic;

	/**模型**/
	@TxtString(name = "")
	private String model;

	/**掉落模型**/
	@TxtString(name = "")
	private String dropModel;

	/**加成**/
	@TxtInt(name = "")
	private int bonus;

	/**加成规则**/
	@TxtInt(name = "")
	private int growthbonus;

	/**成长**/
	@TxtInt(name = "")
	private int growth;

	/**是否能分解**/
	@TxtByte(name = "")
	private byte cantfenjie;

	/**是否能卖（0,不能；1.能）**/
	@TxtByte(name = "")
	private byte cantSold;



	public int getId(){
		return id;
	}

	public void setId(int _id){
		id= _id;
	}

	public String getName(){
		return name;
	}

	public void setName(String _name){
		name= _name;
	}

	public int getLevel(){
		return level;
	}

	public void setLevel(int _level){
		level= _level;
	}

	public int getItemClass(){
		return itemClass;
	}

	public void setItemClass(int _itemClass){
		itemClass= _itemClass;
	}

	public int getMainAttribute(){
		return mainAttribute;
	}

	public void setMainAttribute(int _mainAttribute){
		mainAttribute= _mainAttribute;
	}

	public int getQuality(){
		return quality;
	}

	public void setQuality(int _quality){
		quality= _quality;
	}

	public int getPrice(){
		return price;
	}

	public void setPrice(int _price){
		price= _price;
	}

	public int getCurrency(){
		return currency;
	}

	public void setCurrency(int _currency){
		currency= _currency;
	}

	public int getCareer(){
		return career;
	}

	public void setCareer(int _career){
		career= _career;
	}

	public int getBind(){
		return bind;
	}

	public void setBind(int _bind){
		bind= _bind;
	}

	public int getBagClass(){
		return bagClass;
	}

	public void setBagClass(int _bagClass){
		bagClass= _bagClass;
	}

	public String getSocket(){
		return socket;
	}

	public void setSocket(String _socket){
		socket= _socket;
	}

	public String getPic(){
		return pic;
	}

	public void setPic(String _pic){
		pic= _pic;
	}

	public String getModel(){
		return model;
	}

	public void setModel(String _model){
		model= _model;
	}

	public String getDropModel(){
		return dropModel;
	}

	public void setDropModel(String _dropModel){
		dropModel= _dropModel;
	}

	public int getBonus(){
		return bonus;
	}

	public void setBonus(int _bonus){
		bonus= _bonus;
	}

	public int getGrowthbonus(){
		return growthbonus;
	}

	public void setGrowthbonus(int _growthbonus){
		growthbonus= _growthbonus;
	}

	public int getGrowth(){
		return growth;
	}

	public void setGrowth(int _growth){
		growth= _growth;
	}

	public byte getCantfenjie(){
		return cantfenjie;
	}

	public void setCantfenjie(byte _cantfenjie){
		cantfenjie= _cantfenjie;
	}

	public byte getCantSold(){
		return cantSold;
	}

	public void setCantSold(byte _cantSold){
		cantSold= _cantSold;
	}



}