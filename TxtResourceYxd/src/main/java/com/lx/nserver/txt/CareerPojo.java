package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*职业
**/
public class CareerPojo{

	public CareerPojo(){
	}

	/**职业类型(编号)**/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int id;

	/**职业名称**/
	@TxtString(name = "")
	private String carrerName;

	/**在游戏框的面部图标**/
	@TxtString(name = "")
	private String faceIcon;

	/**职业模型**/
	@TxtString(name = "")
	private String careerModel;

	/**头部模型**/
	@TxtString(name = "")
	private String headModel;

	/**身体模型**/
	@TxtString(name = "")
	private String bodyModel;

	/**肩部模型**/
	@TxtString(name = "")
	private String shoulderModel;

	/**批风模型**/
	@TxtString(name = "")
	private String capeModel;

	/**武器模型**/
	@TxtString(name = "")
	private String weaponModel;

	/**职业描述**/
	@TxtString(name = "")
	private String description;

	/**初始技能id**/
	@TxtString(name = "")
	private String initSkillId;

	/**初始装备id**/
	@TxtString(name = "")
	private String initEquipId;

	/**升级后增加的气力**/
	@TxtInt(name = "")
	private int lvAir;

	/**升级后增加的内力**/
	@TxtInt(name = "")
	private int lvInnerForce;

	/**升级后增加的灵敏**/
	@TxtInt(name = "")
	private int lvAgility;

	/**升级后增加的掌控**/
	@TxtInt(name = "")
	private int lvControl;

	/**升级后增加的坚韧**/
	@TxtInt(name = "")
	private int lvTenacity;

	/**升级后增加的防御**/
	@TxtInt(name = "")
	private int lvDefence;

	/**初始气力**/
	@TxtInt(name = "")
	private int initAir;

	/**初始内力**/
	@TxtInt(name = "")
	private int initInnerForce;

	/**初始敏捷**/
	@TxtInt(name = "")
	private int initAgility;

	/**初始掌控**/
	@TxtInt(name = "")
	private int initControl;

	/**初始坚韧**/
	@TxtInt(name = "")
	private int initTenacity;

	/**初始防御**/
	@TxtInt(name = "")
	private int initDefence;

	/**初始HP**/
	@TxtInt(name = "")
	private int initHp;

	/**初始MP**/
	@TxtInt(name = "")
	private int initMp;



	public int getId(){
		return id;
	}

	public void setId(int _id){
		id= _id;
	}

	public String getCarrerName(){
		return carrerName;
	}

	public void setCarrerName(String _carrerName){
		carrerName= _carrerName;
	}

	public String getFaceIcon(){
		return faceIcon;
	}

	public void setFaceIcon(String _faceIcon){
		faceIcon= _faceIcon;
	}

	public String getCareerModel(){
		return careerModel;
	}

	public void setCareerModel(String _careerModel){
		careerModel= _careerModel;
	}

	public String getHeadModel(){
		return headModel;
	}

	public void setHeadModel(String _headModel){
		headModel= _headModel;
	}

	public String getBodyModel(){
		return bodyModel;
	}

	public void setBodyModel(String _bodyModel){
		bodyModel= _bodyModel;
	}

	public String getShoulderModel(){
		return shoulderModel;
	}

	public void setShoulderModel(String _shoulderModel){
		shoulderModel= _shoulderModel;
	}

	public String getCapeModel(){
		return capeModel;
	}

	public void setCapeModel(String _capeModel){
		capeModel= _capeModel;
	}

	public String getWeaponModel(){
		return weaponModel;
	}

	public void setWeaponModel(String _weaponModel){
		weaponModel= _weaponModel;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String _description){
		description= _description;
	}

	public String getInitSkillId(){
		return initSkillId;
	}

	public void setInitSkillId(String _initSkillId){
		initSkillId= _initSkillId;
	}

	public String getInitEquipId(){
		return initEquipId;
	}

	public void setInitEquipId(String _initEquipId){
		initEquipId= _initEquipId;
	}

	public int getLvAir(){
		return lvAir;
	}

	public void setLvAir(int _lvAir){
		lvAir= _lvAir;
	}

	public int getLvInnerForce(){
		return lvInnerForce;
	}

	public void setLvInnerForce(int _lvInnerForce){
		lvInnerForce= _lvInnerForce;
	}

	public int getLvAgility(){
		return lvAgility;
	}

	public void setLvAgility(int _lvAgility){
		lvAgility= _lvAgility;
	}

	public int getLvControl(){
		return lvControl;
	}

	public void setLvControl(int _lvControl){
		lvControl= _lvControl;
	}

	public int getLvTenacity(){
		return lvTenacity;
	}

	public void setLvTenacity(int _lvTenacity){
		lvTenacity= _lvTenacity;
	}

	public int getLvDefence(){
		return lvDefence;
	}

	public void setLvDefence(int _lvDefence){
		lvDefence= _lvDefence;
	}

	public int getInitAir(){
		return initAir;
	}

	public void setInitAir(int _initAir){
		initAir= _initAir;
	}

	public int getInitInnerForce(){
		return initInnerForce;
	}

	public void setInitInnerForce(int _initInnerForce){
		initInnerForce= _initInnerForce;
	}

	public int getInitAgility(){
		return initAgility;
	}

	public void setInitAgility(int _initAgility){
		initAgility= _initAgility;
	}

	public int getInitControl(){
		return initControl;
	}

	public void setInitControl(int _initControl){
		initControl= _initControl;
	}

	public int getInitTenacity(){
		return initTenacity;
	}

	public void setInitTenacity(int _initTenacity){
		initTenacity= _initTenacity;
	}

	public int getInitDefence(){
		return initDefence;
	}

	public void setInitDefence(int _initDefence){
		initDefence= _initDefence;
	}

	public int getInitHp(){
		return initHp;
	}

	public void setInitHp(int _initHp){
		initHp= _initHp;
	}

	public int getInitMp(){
		return initMp;
	}

	public void setInitMp(int _initMp){
		initMp= _initMp;
	}



}