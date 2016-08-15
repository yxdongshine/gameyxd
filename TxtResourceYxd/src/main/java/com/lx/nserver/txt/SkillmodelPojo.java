package com.lx.nserver.txt;

import com.read.tool.txt.*;

/**
 * 技能模型配置表
 **/
public class SkillmodelPojo {
	
	public SkillmodelPojo() {
	}
	
	/** 技能编号 **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 模型名字 **/
	@TxtString
	private String name;
	
	/** 模型路径 **/
	@TxtString
	private String modelPath;
	
	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		name = _name;
	}
	
	public String getModelPath() {
		return modelPath;
	}
	
	public void setModelPath(String _modelPath) {
		modelPath = _modelPath;
	}
	
}