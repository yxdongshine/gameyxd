package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.SkillTemplatePojo;
import com.lx.nserver.txt.SkillUnitPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:SkillTemplateModel <br/>
 * Function: (技能配置总表模型). <br/>
 * Date: 2015-8-12 下午4:54:40 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class SkillTemplateModel extends ResourceModelImpl<SkillTemplatePojo> {
	
	public SkillTemplateModel() {
		super(TxtRes.SKILL_TEMPLATE_TXT, SkillTemplatePojo.class);
	}
	
	@Override
	public void printLog() {
		log.info("加载技能配置表完成!" + this.getClass().getSimpleName());
		
	}
	
}
