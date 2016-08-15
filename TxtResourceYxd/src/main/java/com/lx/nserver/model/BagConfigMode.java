package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.BagConfigPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:BagConfig <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:20:06 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class BagConfigMode extends ResourceModelImpl<BagConfigPojo> {
	
	private static Log log = LogFactory.getLog(BagConfigMode.class);
	
	/**
	 * Creates a new instance of BagConfig.
	 * 
	 */
	public BagConfigMode() {
		// TODO Auto-generated constructor stub]
		super(TxtRes.BAG_CONFIG, BagConfigPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("背包购买设置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
