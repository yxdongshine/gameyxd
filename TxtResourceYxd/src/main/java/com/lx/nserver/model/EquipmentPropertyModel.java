package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.EquipmentPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:EquipmentPropertyModel <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-3 下午1:38:47 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class EquipmentPropertyModel extends ResourceModelImpl<EquipmentPojo> {
	
	private static final Log log = LogFactory.getLog(EquipmentPropertyModel.class);
	
	/**
	 * Creates a new instance of EquipmentPropertyModel.
	 * 
	 */
	public EquipmentPropertyModel() {
		// TODO Auto-generated constructor stub
		super(TxtRes.EQUITMENT_CONFIG, EquipmentPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备原型配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
