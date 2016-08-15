package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*NPC配置表
**/
public class NPCConfigPojo{

	public NPCConfigPojo(){
	}

	/**NPCId**/
	@TxtKey
	@TxtInt
	private int id;

	/**NPC名字**/
	@TxtString
	private String name;

	/**NPC描述**/
	@TxtString
	private String description;

	/**NPC配音**/
	@TxtString
	private String configureMuisc;

	/**功能图标**/
	@TxtString
	private String functionPic;

	/**NPC头像**/
	@TxtString
	private String headPic;

	/**客户端预设ID**/
	@TxtInt
	private int CPID;

	/**绑定任务集合**/
	@TxtString
	private String bindTaskIds;

	/**绑定功能集合**/
	@TxtString
	private String bindFuns;

	/**隐藏功能**/
	@TxtString
	private String hideFuns;

	/**阵营**/
	@TxtInt
	private int campId;

	/**地图ID**/
	@TxtInt
	private int MapId;



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

	public String getConfigureMuisc(){
		return configureMuisc;
	}

	public void setConfigureMuisc(String _configureMuisc){
		configureMuisc= _configureMuisc;
	}

	public String getFunctionPic(){
		return functionPic;
	}

	public void setFunctionPic(String _functionPic){
		functionPic= _functionPic;
	}

	public String getHeadPic(){
		return headPic;
	}

	public void setHeadPic(String _headPic){
		headPic= _headPic;
	}

	public int getCPID(){
		return CPID;
	}

	public void setCPID(int _CPID){
		CPID= _CPID;
	}

	public String getBindTaskIds(){
		return bindTaskIds;
	}

	public void setBindTaskIds(String _bindTaskIds){
		bindTaskIds= _bindTaskIds;
	}

	public String getBindFuns(){
		return bindFuns;
	}

	public void setBindFuns(String _bindFuns){
		bindFuns= _bindFuns;
	}

	public String getHideFuns(){
		return hideFuns;
	}

	public void setHideFuns(String _hideFuns){
		hideFuns= _hideFuns;
	}

	public int getCampId(){
		return campId;
	}

	public void setCampId(int _campId){
		campId= _campId;
	}

	public int getMapId(){
		return MapId;
	}

	public void setMapId(int _MapId){
		MapId= _MapId;
	}



}