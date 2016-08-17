package com.lx.client.start;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.main.Start;

/**
 * ClassName:GateStart <br/>
 * Function: TODO (开启网关游戏服务). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午2:17:23 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerStart extends Start {
	
	/**
	 * Creates a new instance of GateStart.
	 * 
	 */
	public ServerStart() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String arg[]) {
		new ServerStart().start(arg);
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		loadGateInitializeManage();
	}
	
	/**
	 * loadGateInitializeManage:(). <br/>
	 * TODO().<br/>
	 * 加载网关初始化数据
	 * 
	 * @author lyh
	 */
	private void loadGateInitializeManage() {
		InitializeManage gim = (InitializeManage) SpringBeanFactory.getSpringBean("initializeManage");
		gim.init();
	}
	
}
