package com.lx.nserver.txt;
import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.read.tool.txt.*;
/**
*BuffType
**/
public class BuffTypePojo{

	public BuffTypePojo(){
	}

	/**buffTypeId**/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int buffTypeId;

	/**buff类名**/
	@TxtString(name = "")
	private String buffTypeName;

	/**是否可叠加**/
	@TxtInt(name = "")
	private int canAdd;

	/**清除buff类别**/
	@TxtString(name = "")
	private String clearBuffTypes;
	
	/**清除buff类别**/  
	private int[][] clearBuffTypeList = new int[0][];

	/**免疫buff类别**/
	@TxtString(name = "")
	private String immuneBuffTypes;
	
	/**清除buff类别**/  
	private int[][] immuneBuffTypeList = new int[0][];

	/**生效时长**/
	@TxtInt(name = "")
	private int workTime;

	/**生效间隔**/
	@TxtInt(name = "")
	private int workTick;

	/**作用属性及类型**/
	@TxtString(name = "")
	private String workAttrValue;
	
	/** 作用属性及类型 **/  
	private int[][] workAttrValues = new int[0][];

	/**是否保存到数据库**/
	@TxtInt(name = "")
	private int canSave;

	/**buff描述**/
	@TxtString(name = "")
	private String desc;



	public int getBuffTypeId(){
		return buffTypeId;
	}

	public void setBuffTypeId(int _buffTypeId){
		buffTypeId= _buffTypeId;
	}

	public String getBuffTypeName(){
		return buffTypeName;
	}

	public void setBuffTypeName(String _buffTypeName){
		buffTypeName= _buffTypeName;
	}

	public int getCanAdd(){
		return canAdd;
	}

	public void setCanAdd(int _canAdd){
		canAdd= _canAdd;
	}

	public void setClearBuffTypes(String _clearBuffTypes){
		clearBuffTypes= _clearBuffTypes;
		if(!ToolUtils.isStringNull(_clearBuffTypes))
		{
			this.clearBuffTypeList = ToolUtils.splitStr(_clearBuffTypes, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getClearBuffTypeList()
	{
		return this.clearBuffTypeList;
	}

	public void setImmuneBuffTypes(String _immuneBuffTypes){
		immuneBuffTypes= _immuneBuffTypes;
		if(!ToolUtils.isStringNull(_immuneBuffTypes))
		{
			this.immuneBuffTypeList = ToolUtils.splitStr(_immuneBuffTypes, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getImmuneBuffTypeList()
	{
		return this.immuneBuffTypeList;
	}

	public int getWorkTime(){
		return workTime;
	}

	public void setWorkTime(int _workTime){
		workTime= _workTime;
	}

	public int getWorkTick(){
		return workTick;
	}

	public void setWorkTick(int _workTick){
		workTick= _workTick;
	}

	public void setWorkAttrValue(String _workAttrValue){
		workAttrValue= _workAttrValue;
		if(!ToolUtils.isStringNull(_workAttrValue))
		{
			this.workAttrValues = ToolUtils.splitStr(_workAttrValue, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getWorkAttrValues()
	{
		return this.workAttrValues;
	}

	public int getCanSave(){
		return canSave;
	}

	public void setCanSave(int _canSave){
		canSave= _canSave;
	}

	public String getDesc(){
		return desc;
	}

	public void setDesc(String _desc){
		desc= _desc;
	}



}