package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*Buff
**/
public class BuffPojo{

	public BuffPojo(){
	}

	/**buffId**/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int buffId;

	/**buff名称**/
	@TxtString(name = "")
	private String buffName;

	/**buff等级**/
	@TxtInt(name = "")
	private int buffLevel;

	/**buff类别**/
	@TxtInt(name = "")
	private int buffType;

	/**数值**/
	@TxtInt(name = "")
	private int workValue;



	public int getBuffId(){
		return buffId;
	}

	public void setBuffId(int _buffId){
		buffId= _buffId;
	}

	public String getBuffName(){
		return buffName;
	}

	public void setBuffName(String _buffName){
		buffName= _buffName;
	}

	public int getBuffLevel(){
		return buffLevel;
	}

	public void setBuffLevel(int _buffLevel){
		buffLevel= _buffLevel;
	}

	public int getBuffType(){
		return buffType;
	}

	public void setBuffType(int _buffType){
		buffType= _buffType;
	}

	public int getWorkValue(){
		return workValue;
	}

	public void setWorkValue(int _workValue){
		workValue= _workValue;
	}



}