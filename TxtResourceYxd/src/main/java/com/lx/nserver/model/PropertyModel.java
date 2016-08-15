package com.lx.nserver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.PropertyPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:PropertyModel <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午10:32:23 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class PropertyModel extends ResourceModelImpl<PropertyPojo> {
	
	private static Log log = LogFactory.getLog(PropertyModel.class);
	List<PropertyPojo> propertyPojoList;
	private ConcurrentHashMap<Integer, List<PropertyPojo>> propertyPojoHM;
	
	/**
	 * Creates a new instance of PropertyModel.
	 * 
	 */
	public PropertyModel() {
		// TODO Auto-generated constructor stub
		super(TxtRes.PROPERTY_CONFIG, PropertyPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		// 根绝类型分类缓存 不同类型的道具
		propertyPojoHM = new ConcurrentHashMap<Integer, List<PropertyPojo>>();
		for (int i = 0; i < this.getReslList().size(); i++) {
			PropertyPojo propertyPojo = this.getReslList().get(i);
			if (propertyPojo != null) {
				if (propertyPojoHM.contains(propertyPojo.getItemType())) {// 如果含有
					propertyPojoHM.get(propertyPojo.getItemType()).add(propertyPojo);
				} else {
					propertyPojoList = new ArrayList<PropertyPojo>();
					propertyPojoList.add(propertyPojo);
					propertyPojoHM.put(propertyPojo.getItemType(), propertyPojoList);
				}
			}
		}
		log.error("加载道具配置属性表完成::" + this.getClass().getSimpleName());
	}
	
	public ConcurrentHashMap<Integer, List<PropertyPojo>> getPropertyPojoHM() {
		return propertyPojoHM;
	}
	
	public void setPropertyPojoHM(ConcurrentHashMap<Integer, List<PropertyPojo>> propertyPojoHM) {
		this.propertyPojoHM = propertyPojoHM;
	}
	
}
