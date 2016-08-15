package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.RoleLevelUpPojo;
import com.lx.nserver.txt.SkillUnitPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (技能伤害单元模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class SkillUnitModel extends ResourceModelImpl<SkillUnitPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	
	public SkillUnitModel() {
		super(TxtRes.SKILL_UNIT_TXT, SkillUnitPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("技能伤害单元加载完成::" + this.getClass().getSimpleName());
		
	}
	
}
