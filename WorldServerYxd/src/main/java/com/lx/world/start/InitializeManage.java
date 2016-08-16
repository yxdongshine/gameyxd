package com.lx.world.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.init.GameInitializeManage;
import com.lx.nserver.manage.TxtModelManage;
import com.lx.world.mina.InnerServer;
import com.lx.world.mina.MinaClient;

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
	private InnerServer gateInnerServer;
	
	/** 读取配置 **/
	@Autowired
	private TxtModelManage txtModelManage;
	
	@Override
	public void gameInit() {
		// TODO Auto-generated method stub
		// 开启网关的网络服务
		gateInnerClient.startInnerClient();
		gateInnerServer.startInnerServer();
	}
	
}
