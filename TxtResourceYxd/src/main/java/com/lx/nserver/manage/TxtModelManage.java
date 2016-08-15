package com.lx.nserver.manage;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.utils.log.LogUtils;
import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.lx.nserver.model.SkillTemplateModel;
import com.lx.nserver.model.SkillUnitModel;
import com.lx.nserver.txt.SkillTemplatePojo;
import com.lx.nserver.txt.SkillUnitPojo;
import com.read.txt.ResourceModelImpl;
import com.sj.world.utils.ClassUtils;

/**
 * ClassName:TxtModelManage <br/>
 * Function: TODO (配置文件管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-16 下午2:17:01 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class TxtModelManage {
	private static Log log = LogUtils.getLog(TxtModelManage.class);
	@Autowired
	private SkillTemplateModel skillTemplateModel;
	@Autowired
	private SkillUnitModel skillUnitModel;
	
	/**
	 * checkTxt:(). <br/>
	 * TODO().<br/>
	 * 加载文件
	 * 
	 * @author lyh
	 * @param beanFactory
	 * @param packageName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean loadTxt(BeanFactory beanFactory, String packageName) {
		try {
			log.error("把资源全部读一遍开始!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			Package workPackage = Package.getPackage(packageName);
			// System.out.println("packageName:::" + workPackage.getName());
			Set<Class<?>> _classes = ClassUtils.getClasses(workPackage);
			for (Class<?> _class : _classes) {
				if (_class.isInterface()) {
					continue;
				}
				// 指令逻辑类
				int index = _class.getName().lastIndexOf(".");
				String beanId = _class.getName().substring(index + 1);
				String firstC = ("" + beanId.charAt(0)).toLowerCase();// 转成小写
				beanId = firstC + beanId.substring(1);
				ResourceModelImpl obj = (ResourceModelImpl) beanFactory.getBean(beanId);
				log.error("读到文本配置::" + obj.getClass().getSimpleName());
			}
			// 设置技能伤害单元
			setSkillUnit();
			log.error("把资源全部读一遍完成!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("加载有错误:::", ex);
			System.exit(0);
			return false;
		}
	}
	
	/**
	 * setSkillUnit:(). <br/>
	 * TODO().<br/>
	 * 伤害单元
	 * 
	 * @author lyh
	 */
	private void setSkillUnit() {
		for (SkillTemplatePojo pojo : skillTemplateModel.getReslList()) {
			
			if (!ToolUtils.isStringNull(pojo.getUnitId())) {
				int[] idArray = ToolUtils.splitStringToInt(pojo.getUnitId(), IConst.WELL);
				SkillUnitPojo skillUnitPojo[] = new SkillUnitPojo[idArray.length];
				int index = 0;
				for (int k : idArray) {
					SkillUnitPojo suPojo = skillUnitModel.get(k);
					if (suPojo != null) {
						skillUnitPojo[index] = suPojo;
					}
					index++;
				}
				pojo.setSkillUnitPojo(skillUnitPojo);
			}
		}
	}
	
}
