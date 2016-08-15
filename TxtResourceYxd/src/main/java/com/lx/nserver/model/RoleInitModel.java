package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.RoleInitPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (角色初始化数据模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class RoleInitModel extends ResourceModelImpl<RoleInitPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	
	public RoleInitModel() {
		super(TxtRes.ROLE_INIT_TXT, RoleInitPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("角色初始化数据完成::" + this.getClass().getSimpleName());
		
	}
	
}
