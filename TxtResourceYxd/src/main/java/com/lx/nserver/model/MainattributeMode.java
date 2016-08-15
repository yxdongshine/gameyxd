package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.MainattributePojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:MainattributeMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-9 上午10:10:24 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class MainattributeMode extends ResourceModelImpl<MainattributePojo> {
	
	private static final Log log = LogFactory.getLog(MainattributeMode.class);
	
	/**
	 * Creates a new instance of MainattributeMode.
	 * 
	 */
	public MainattributeMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.MAINATTRIBUTE_CONFIG, MainattributePojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备主要属性配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
