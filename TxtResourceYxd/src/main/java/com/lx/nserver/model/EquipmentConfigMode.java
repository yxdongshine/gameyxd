package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.EquipmentConfigPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:EquipmentConfigMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午6:05:42 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class EquipmentConfigMode extends ResourceModelImpl<EquipmentConfigPojo> {
	
	private static final Log log = LogFactory.getLog(EquipmentPropertyModel.class);
	
	/**
	 * Creates a new instance of EquipmentConfigMode.
	 * 
	 */
	public EquipmentConfigMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.EQUIPMENT_QUALITY_CONFIG, EquipmentConfigPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
