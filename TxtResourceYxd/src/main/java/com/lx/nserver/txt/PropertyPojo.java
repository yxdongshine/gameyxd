package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*道具配置表
**/
public class PropertyPojo{

	public PropertyPojo(){
	}

	/**道具编号**/
	@TxtKey
	@TxtInt
	private int id;

	/**名称**/
	@TxtString
	private String name;

	/**描述**/
	@TxtString
	private String description;

	/**图片id**/
	@TxtString
	private String pic;

	/**购入价格**/
	@TxtInt
	private int buyPrice;

	/**卖出价格**/
	@TxtInt
	private int solePrice;

	/**金钱类型(0金币，1钻石)**/
	@TxtInt
	private int currency;

	/**绑定类型(获取时绑定0、装备后绑定1和不绑定2  ,3种)**/
	@TxtByte
	private byte bind;

	/**品质(白色0，绿色1，蓝色2，金色3，紫色4，橙色5，红色6)**/
	@TxtInt
	private int quality;

	/**级别**/
	@TxtInt
	private int level;

	/**分类ID(为任务道具0和非任务道具1, 2类)**/
	@TxtByte
	private byte typeID;

	/**是否能卖（0,不能；1.能）**/
	@TxtByte
	private byte cantSold;

	/**掉在地上显示的模型**/
	@TxtString
	private String dropModel;

	/**可折叠数**/
	@TxtInt
	private int folderableNum;

	/**道具类型（0 装备，1 普通，2特殊）**/
	@TxtInt
	private int toolClass;

	/**触发索引**/
	@TxtString
	private String function;

	/**物品类型**/
	@TxtInt
	private int itemType;

	/**是否只能用1次**/
	@TxtByte
	private byte useatonce;

	/**用时是否删除**/
	@TxtByte
	private byte useWithRemove;

	/**药瓶补满价格**/
	@TxtInt
	private int fullBottle;



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

	public String getDescription(){
		return description;
	}

	public void setDescription(String _description){
		description= _description;
	}

	public String getPic(){
		return pic;
	}

	public void setPic(String _pic){
		pic= _pic;
	}

	public int getBuyPrice(){
		return buyPrice;
	}

	public void setBuyPrice(int _buyPrice){
		buyPrice= _buyPrice;
	}

	public int getSolePrice(){
		return solePrice;
	}

	public void setSolePrice(int _solePrice){
		solePrice= _solePrice;
	}

	public int getCurrency(){
		return currency;
	}

	public void setCurrency(int _currency){
		currency= _currency;
	}

	public byte getBind(){
		return bind;
	}

	public void setBind(byte _bind){
		bind= _bind;
	}

	public int getQuality(){
		return quality;
	}

	public void setQuality(int _quality){
		quality= _quality;
	}

	public int getLevel(){
		return level;
	}

	public void setLevel(int _level){
		level= _level;
	}

	public byte getTypeID(){
		return typeID;
	}

	public void setTypeID(byte _typeID){
		typeID= _typeID;
	}

	public byte getCantSold(){
		return cantSold;
	}

	public void setCantSold(byte _cantSold){
		cantSold= _cantSold;
	}

	public String getDropModel(){
		return dropModel;
	}

	public void setDropModel(String _dropModel){
		dropModel= _dropModel;
	}

	public int getFolderableNum(){
		return folderableNum;
	}

	public void setFolderableNum(int _folderableNum){
		folderableNum= _folderableNum;
	}

	public int getToolClass(){
		return toolClass;
	}

	public void setToolClass(int _toolClass){
		toolClass= _toolClass;
	}

	public String getFunction(){
		return function;
	}

	public void setFunction(String _function){
		function= _function;
	}

	public int getItemType(){
		return itemType;
	}

	public void setItemType(int _itemType){
		itemType= _itemType;
	}

	public byte getUseatonce(){
		return useatonce;
	}

	public void setUseatonce(byte _useatonce){
		useatonce= _useatonce;
	}

	public byte getUseWithRemove(){
		return useWithRemove;
	}

	public void setUseWithRemove(byte _useWithRemove){
		useWithRemove= _useWithRemove;
	}

	public int getFullBottle(){
		return fullBottle;
	}

	public void setFullBottle(int _fullBottle){
		fullBottle= _fullBottle;
	}



}