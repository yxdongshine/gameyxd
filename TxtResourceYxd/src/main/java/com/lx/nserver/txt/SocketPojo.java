package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 孔配置表
 **/
public class SocketPojo {
	
	public SocketPojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 孔编号 **/
	@TxtString
	private String socketType;
	
	/** 数量 **/
	@TxtInt
	private int count;
	
	/** 概率 **/
	@TxtInt
	private int probability;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getSocketType() {
		return socketType;
	}
	
	public void setSocketType(String _socketType) {
		socketType = _socketType;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int _count) {
		count = _count;
	}
	
	public int getProbability() {
		return probability;
	}
	
	public void setProbability(int _probability) {
		probability = _probability;
	}
	
}