package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 背包配置表
 **/
public class BagConfigPojo {
	
	public BagConfigPojo() {
	}
	
	/** 编号 **/
	@TxtKey(name = "")
	@TxtInt(name = "")
	private int id;
	
	/** 默认格子数 **/
	@TxtInt(name = "")
	private int dedaultGrid;
	
	/** 最大格子数 **/
	@TxtInt(name = "")
	private int maxGrid;
	
	/** 格子花费（count:一次性开的格子数；currency：货币类型;fee:花费） **/
	@TxtString(name = "")
	private String buyGridFee;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public int getDedaultGrid() {
		return dedaultGrid;
	}
	
	public void setDedaultGrid(int _dedaultGrid) {
		dedaultGrid = _dedaultGrid;
	}
	
	public int getMaxGrid() {
		return maxGrid;
	}
	
	public void setMaxGrid(int _maxGrid) {
		maxGrid = _maxGrid;
	}
	
	public String getBuyGridFee() {
		return buyGridFee;
	}
	
	public void setBuyGridFee(String _buyGridFee) {
		buyGridFee = _buyGridFee;
	}
	
}