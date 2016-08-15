package com.engine.entityobj;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.engine.domain.SkillData;
import com.engine.interfaces.ISkill;
import com.lx.nserver.txt.SkillTemplatePojo;

/**
 * ClassName:Skill <br/>
 * Function: (技能实体). <br/>
 * Date: 2015-8-13 上午10:59:38 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class ServerSkill implements ISkill {
	
	/** 技能模板 **/
	protected SkillTemplatePojo template;
	
	protected SkillData skillData;
	
	/**
	 * 怪物是配置文件id,角色为数据库的id
	 */
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return skillData != null ? skillData.getId() : template.getId();
	}
	
	@Override
	public SkillTemplatePojo getSkillTemplatePojo() {
		// TODO Auto-generated method stub
		return template;
	}
	
	@Override
	public void setSkillTemplatePojo(SkillTemplatePojo pSkillTemplatePojo) {
		// TODO Auto-generated method stub
		template = pSkillTemplatePojo;
	}
	
	public SkillData getSkillData() {
		return skillData;
	}
	
	public void setSkillData(SkillData skillData) {
		this.skillData = skillData;
	}
	
}