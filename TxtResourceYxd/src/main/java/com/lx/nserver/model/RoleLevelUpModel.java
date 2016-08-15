package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.RoleLevelUpPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (角色升级模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class RoleLevelUpModel extends ResourceModelImpl<RoleLevelUpPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	
	public RoleLevelUpModel() {
		super(TxtRes.ROLE_LEVEL_UP_TXT, RoleLevelUpPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("角色升级完成::" + this.getClass().getSimpleName());
		
	}
	
}
