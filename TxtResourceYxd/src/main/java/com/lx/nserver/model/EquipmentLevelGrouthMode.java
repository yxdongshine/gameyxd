package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.EquipmentLevelGrouthPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:EquipmentLevelGrouthMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-9 上午10:12:27 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class EquipmentLevelGrouthMode extends ResourceModelImpl<EquipmentLevelGrouthPojo> {
	
	private static final Log log = LogFactory.getLog(EquipmentLevelGrouthMode.class);
	
	/**
	 * Creates a new instance of EquipmentLevelGrouthMode.
	 * 
	 */
	public EquipmentLevelGrouthMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.EQUIPMENT_LEVEL_GROPU_CONFIG, EquipmentLevelGrouthPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备成长配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
