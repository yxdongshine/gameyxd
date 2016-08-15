package com.engine.domain;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ClassName:SkillData <br/>
 * Function: (技能数据). <br/>
 * Date: 2015-8-13 下午3:26:52 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Entity
@Table(name = "skillData")
public class SkillData extends DbEntity {
	
	/** UID **/
	private static final long serialVersionUID = -532311660845861439L;
	
	/** 角色ID **/
	private long roleId;
	
	/** 技能数据 **/
	private String skillData;
	
	public long getRoleId() {
		return roleId;
	}
	
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
	public String getSkillData() {
		return skillData;
	}
	
	public void setSkillData(String skillData) {
		this.skillData = skillData;
	}
}
