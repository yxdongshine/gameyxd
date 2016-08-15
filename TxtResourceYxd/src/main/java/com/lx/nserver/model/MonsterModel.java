package com.lx.nserver.model;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.MonsterPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:CareeModel <br/>
 * Function: TODO (怪物模型). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:36:44 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MonsterModel extends ResourceModelImpl<MonsterPojo> {
	
	public MonsterModel() {
		super(TxtRes.MONSTER_TXT, MonsterPojo.class);
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
		log.error("加载怪物完成!!!!!!!!!!!" + this.getClass().getSimpleName());
	}
}
