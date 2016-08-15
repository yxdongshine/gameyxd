package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.CareerPojo;
import com.lx.nserver.txt.CareerTalentPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (职业模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class CareerModel extends ResourceModelImpl<CareerPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	
	public CareerModel() {
		super(TxtRes.CAREER_TXT, CareerPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("加载职业完成:" + this.getClass().getSimpleName());
		
	}
	
}
