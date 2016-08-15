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
import com.lx.nserver.txt.AttributeDescPojo;
import com.lx.nserver.txt.AttributePojo;
import com.lx.nserver.txt.CareerPojo;
import com.lx.nserver.txt.CareerTalentPojo;
import com.lx.nserver.txt.MonsterGroupPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (怪物组模型模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MonsterGroupModel extends ResourceModelImpl<MonsterGroupPojo> {
	// private Log log = LogUtils.getLog(CareeModel.class);
	/** 职业 id， 属性容器 * AttributePojo */
	// private ConcurrentHashMap<Integer, List<AttributePojo>> attributeMap = new ConcurrentHashMap<Integer,List<AttributePojo>>() ;
	public MonsterGroupModel() {
		super(TxtRes.MONSTER_GROUP_TXT, MonsterGroupPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		// attributeMap = new ConcurrentHashMap<Integer,List<AttributePojo>>();
		// for (AttributePojo pojo : this.getReslList()){
		// List<AttributePojo> list = attributeMap.get(pojo.getCareerId());
		// if (list == null){
		// list = new ArrayList<AttributePojo>();
		// attributeMap.put(pojo.getCareerId(), list);
		// }
		// list.add(pojo);
		// }
		log.error("加载怪物组完成!!!!!!!!!!!" + this.getClass().getSimpleName());
	}
}
