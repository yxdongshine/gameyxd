package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*任务系统设置表
**/
public class TaskConfigPojo{

	public TaskConfigPojo(){
	}

	/**最多任务数**/
	@TxtKey
	@TxtInt
	private int maxTaskNumber;

	/**初始化任务编号**/
	@TxtInt
	private int initTaskId;



	public int getMaxTaskNumber(){
		return maxTaskNumber;
	}

	public void setMaxTaskNumber(int _maxTaskNumber){
		maxTaskNumber= _maxTaskNumber;
	}

	public int getInitTaskId(){
		return initTaskId;
	}

	public void setInitTaskId(int _initTaskId){
		initTaskId= _initTaskId;
	}



}