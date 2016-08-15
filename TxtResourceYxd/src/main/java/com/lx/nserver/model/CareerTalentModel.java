package com.lx.nserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.CareerPojo;
import com.lx.nserver.txt.CareerTalentPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (职业资质模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class CareerTalentModel extends ResourceModelImpl<CareerTalentPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	
	/** 职业资质容器 **/
	private ConcurrentHashMap<Integer, List<CareerTalentPojo>> careerTypeMap;
	
	public CareerTalentModel() {
		
		super(TxtRes.CAREER_TALENT_TXT, CareerTalentPojo.class);
	}
	
	@Override
	public void printLog() {
		
		// TODO Auto-generated method stub
		careerTypeMap = new ConcurrentHashMap<Integer, List<CareerTalentPojo>>();
		for (CareerTalentPojo pojo : this.getReslList()) {
			
			List<CareerTalentPojo> list = careerTypeMap.get(pojo.getCareerId());
			if (list == null) {
				list = new ArrayList<CareerTalentPojo>();
				careerTypeMap.put(pojo.getCareerId(), list);
			}
			list.add(pojo);
		}
		log.error("加载职业资质完成---" + this.getClass().getSimpleName());
	}
	
	/**
	 * findCareerTalentListByCareerId:(). <br/>
	 * TODO().<br/>
	 * 用职业id查找对应的资质
	 * 
	 * @author lyh
	 * @param careerId
	 * @return
	 */
	public List<CareerTalentPojo> findCareerTalentListByCareerId(int careerId) {
		List<CareerTalentPojo> list = careerTypeMap.get(careerId);
		return list;
	}
	
}
