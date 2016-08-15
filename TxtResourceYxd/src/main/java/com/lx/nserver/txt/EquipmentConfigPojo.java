package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 装备配置表
 **/
public class EquipmentConfigPojo {
	
	public EquipmentConfigPojo() {
	}
	
	/** 编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 装备质量编号 **/
	@TxtString
	private String equipQualityId;
	
	/** 装备类别编号 **/
	@TxtString
	private String EquipClassId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getEquipQualityId() {
		return equipQualityId;
	}
	
	public void setEquipQualityId(String _equipQualityId) {
		equipQualityId = _equipQualityId;
	}
	
	public String getEquipClassId() {
		return EquipClassId;
	}
	
	public void setEquipClassId(String _EquipClassId) {
		EquipClassId = _EquipClassId;
	}
	
}