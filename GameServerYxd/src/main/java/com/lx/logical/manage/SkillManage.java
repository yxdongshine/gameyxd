package com.lx.logical.manage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.engine.dbdao.EntityDAO;
import com.engine.domain.Role;
import com.engine.domain.SkillData;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.ServerSkill;
import com.lib.utils.ServerUUID;
import com.lx.nserver.model.SkillTemplateModel;
import com.lx.nserver.txt.SkillTemplatePojo;

/**
 * ClassName:SkillManage <br/>
 * Function: (技能管理类). <br/>
 * Date: 2015-8-12 下午5:39:12 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class SkillManage {
	
	@Autowired
	private SkillTemplateModel skillTemplateModel;
	
	// /***
	// * 加载角色技能
	// * loadRoleSkill:(). <br/>
	// */
	// public void loadRoleSkill(ServerPlayer sp, EntityDAO dao)
	// {
	// Role role = sp.getRole();
	// List<SkillData> skillDatas = dao.findByProperties(SkillData.class,
	// new String[]{"roleId"}, new Object[]{role.getId()});
	// //解析角色技能数据
	// for(SkillData data : skillDatas)
	// {
	// ServerSkill skill = JSON.parseObject(data.getSkillData(), ServerSkill.class);
	// sp.getSkillList().put(skill.getUid(), skill);
	//
	// if(skill.getSequence() != -1)//装配到按钮映射
	// {
	// sp.getSkillBtnMap().put(skill.getSequence(), skill);
	// }
	// }
	// int i = 0 ;
	// }
	
	// /**
	// * 保存角色技能
	// * saveRoleSkill:(). <br/>
	// */
	// public void saveRoleSkill(ServerPlayer sp, EntityDAO dao)
	// {
	// Map<Long, ServerSkill> skills = sp.getSkillList();
	// for(Map.Entry<Long, ServerSkill> entry : skills.entrySet())
	// {
	// ServerSkill skill = entry.getValue();
	// String skillStr = JSON.toJSONString(skill);
	// long roleId = sp.getRole().getId();
	//
	// // 查询数据库中是否已有
	// SkillData dbData = dao.findById(SkillData.class, skill.getUid());
	// if(dbData == null)
	// {
	// SkillData data = new SkillData();
	// data.setId(skill.getUid());
	// data.setRoleId(roleId);
	// data.setSkillData(skillStr);
	// dao.save(data);
	// }else
	// {
	// dbData.setSkillData(skillStr);
	// dao.updateFinal(dbData);
	// }
	// }
	// }
	
	// /**
	// * saveRoleSkill:(). <br/>
	// */
	// public void saveRoleSkill(ServerPlayer sp, EntityDAO dao, long skillUid)
	// {
	// Map<Long, ServerSkill> skills = sp.getSkillList();
	// if(!skills.containsKey(skillUid))
	// return;
	// ServerSkill skill = skills.get(skillUid);
	// String skillStr = JSON.toJSONString(skill);
	//
	// // 查询数据库中是否已有
	// SkillData dbData = dao.findById(SkillData.class, skill.getUid());
	// if(dbData != null)
	// {
	// dbData.setSkillData(skillStr);
	// dao.updateFinal(dbData);
	// }
	// }
	
	// public ServerSkill addNewSkill(ServerPlayer sp, EntityDAO dao, int skillTid)
	// {
	// SkillTemplatePojo template = skillTemplateModel.get(skillTid);
	// if(template == null)
	// return null;
	//
	// ServerSkill skill = new ServerSkill();
	// skill.setUid(ServerUUID.getUUID());
	// skill.setTid(skillTid);
	// skill.setHitRate(template.getHitRate());
	// skill.setHurtMax(template.getHurtMax());
	// skill.setHurtMin(template.getHurtMin());
	// // 初始等级(区别于装备等级)
	// skill.setLevel(1);
	// // 初始不安装到按钮
	// skill.setSequence(-1);
	//
	// //加载作用属性
	// ArrayList<Integer> workAttr = new ArrayList<Integer>();
	// for(int i =0; i < template.getWorkAttrs().length; i += 3)
	// {
	// int attrId = template.getWorkAttrs()[0][i];
	// int type = template.getWorkAttrs()[0][i + 1];
	// int value = template.getWorkAttrs()[0][i + 2];
	// workAttr.add(attrId);
	// workAttr.add(type);
	// workAttr.add(value);
	// }
	// skill.setWorkAttr(workAttr);
	//
	// // 加载技能模板
	// Map<Integer, Integer> fireCost = new HashMap<Integer, Integer>();
	//
	// for( int i =0; i < template.getFireCosts().length; i += 2)
	// {
	// int key = template.getFireCosts()[0][i];
	// int value = template.getFireCosts()[0][i+1];
	// fireCost.put(key, value);
	// }
	// skill.setFireCosts(fireCost);
	// sp.getSkillList().put(skill.getUid(), skill);
	// return skill;
	// }
}
