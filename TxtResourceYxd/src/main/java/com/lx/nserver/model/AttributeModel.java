package com.lx.nserver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.AttributePojo;
import com.lx.nserver.txt.CareerPojo;
import com.lx.nserver.txt.CareerTalentPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (属性模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class AttributeModel extends ResourceModelImpl<AttributePojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	/** 职业 id， 属性容器 * AttributePojo */
	private ConcurrentHashMap<Integer, List<AttributePojo>> attributeMap;
	
	public AttributeModel() {
		super(TxtRes.ATTRIBUTE_TXT, AttributePojo.class);
		
		// System.err.println("ssssss::" + attributeMap);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		attributeMap = new ConcurrentHashMap<Integer, List<AttributePojo>>();
		for (AttributePojo pojo : this.getReslList()) {
			List<AttributePojo> list = attributeMap.get(pojo.getCareerId());
			if (list == null) {
				list = new ArrayList<AttributePojo>();
				attributeMap.put(pojo.getCareerId(), list);
			}
			list.add(pojo);
		}
		log.error("加载属性完成!!!!!!!!!!!" + this.getClass().getSimpleName());
	}
	
	/**
	 * findAttributeMapByCareerId:(). <br/>
	 * TODO().<br/>
	 * 用职业id找到属性
	 * 
	 * @author lyh
	 * @param careerId
	 * @return
	 */
	public List<AttributePojo> findAttributeMapByCareerId(int careerId) {
		List<AttributePojo> list = attributeMap.get(careerId);
		return list;
	}
}
