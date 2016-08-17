package com.lx.client.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.init.GameInitializeManage;
import com.engine.msgloader.CommondClassLoader;
import com.lx.client.mina.MinaClient;
import com.lx.client.timethread.TimeThreadManage;

/**
 * ClassName:GateInitializeManage <br/>
 * Function: TODO (网关数据初始化). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-2 上午9:48:48 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class InitializeManage extends GameInitializeManage {
	
	@Autowired
	private MinaClient gateInnerClient;
	
	@Autowired
	private CommondClassLoader commondClassLoader;
	@Autowired
	private TimeThreadManage timeThreadManage;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		gameInit();
	}
	
	@Override
	public void gameInit() {
		
		// TODO Auto-generated method stub
		commondClassLoader.setPackageName("com.lx.logical");
		commondClassLoader.initialize(SpringBeanFactory.factory);
		gateInnerClient.startInnerClient();
		timeThreadManage.load();
		
	}
	
}
