package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.EquipQualityPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:EquipQualityMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午6:06:19 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class EquipQualityMode extends ResourceModelImpl<EquipQualityPojo> {
	
	private static final Log log = LogFactory.getLog(EquipQualityMode.class);
	
	/**
	 * Creates a new instance of EquipQualityMode.
	 * 
	 */
	public EquipQualityMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.EQUIPMENT_QUALITY_CONFIG, EquipQualityPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备质量配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
