package com.lx.nserver.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.SocketPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:SocketMode <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午4:12:29 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class SocketMode extends ResourceModelImpl<SocketPojo> {
	
	private static final Log log = LogFactory.getLog(SocketMode.class);
	
	/**
	 * Creates a new instance of SocketMode.
	 * 
	 */
	public SocketMode() {
		// TODO Auto-generated constructor stub
		super(TxtRes.SOCKET_CONFIG, SocketPojo.class);
	}
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		log.error("装备孔配置表加载完成::" + this.getClass().getSimpleName());
	}
	
}
