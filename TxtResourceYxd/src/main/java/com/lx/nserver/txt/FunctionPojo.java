package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*功能触发表
**/
public class FunctionPojo{

	public FunctionPojo(){
	}

	/**编号**/
	@TxtKey
	@TxtInt
	private int id;

	/**类型**/
	@TxtInt
	private int type;

	/**货币类型**/
	@TxtInt
	private int currency;

	/**物品编号**/
	@TxtInt
	private int item;

	/**数量**/
	@TxtInt
	private int count;

	/**概率值**/
	@TxtInt
	private int ratio;

	/**血值**/
	@TxtInt
	private int valueh;

	/**血值百分比**/
	@TxtInt
	private int percenth;

	/**魔值**/
	@TxtInt
	private int valuem;

	/**魔值百分比**/
	@TxtInt
	private int percentm;

	/**有效时间**/
	@TxtInt
	private int effectiveTime;

	/**药瓶使用cd时间**/
	@TxtInt
	private int useCdTime;



	public int getId(){
		return id;
	}

	public void setId(int _id){
		id= _id;
	}

	public int getType(){
		return type;
	}

	public void setType(int _type){
		type= _type;
	}

	public int getCurrency(){
		return currency;
	}

	public void setCurrency(int _currency){
		currency= _currency;
	}

	public int getItem(){
		return item;
	}

	public void setItem(int _item){
		item= _item;
	}

	public int getCount(){
		return count;
	}

	public void setCount(int _count){
		count= _count;
	}

	public int getRatio(){
		return ratio;
	}

	public void setRatio(int _ratio){
		ratio= _ratio;
	}

	public int getValueh(){
		return valueh;
	}

	public void setValueh(int _valueh){
		valueh= _valueh;
	}

	public int getPercenth(){
		return percenth;
	}

	public void setPercenth(int _percenth){
		percenth= _percenth;
	}

	public int getValuem(){
		return valuem;
	}

	public void setValuem(int _valuem){
		valuem= _valuem;
	}

	public int getPercentm(){
		return percentm;
	}

	public void setPercentm(int _percentm){
		percentm= _percentm;
	}

	public int getEffectiveTime(){
		return effectiveTime;
	}

	public void setEffectiveTime(int _effectiveTime){
		effectiveTime= _effectiveTime;
	}

	public int getUseCdTime(){
		return useCdTime;
	}

	public void setUseCdTime(int _useCdTime){
		useCdTime= _useCdTime;
	}



}