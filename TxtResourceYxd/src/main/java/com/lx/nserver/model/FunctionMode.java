package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.FunctionPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:FunctionMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午11:09:51 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class FunctionMode extends ResourceModelImpl<FunctionPojo> {
	
	private static Log log = LogFactory.getLog(PropertyModel.class);
	
	/**
	 * Creates a new instance of FunctionMode.
	 * 
	 */
	public FunctionMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.FUNCTION_CONFIG, FunctionPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("道具使用配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
