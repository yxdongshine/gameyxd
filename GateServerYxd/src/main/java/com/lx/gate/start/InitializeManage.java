package com.lx.gate.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.init.GameInitializeManage;
import com.lx.gate.heart.HeartManage;
import com.lx.gate.mina.InnerClient;
import com.lx.gate.mina.InnerServer;
import com.lx.gate.mina.MinaServer;

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
	private InnerClient gateInnerClient;
	
	@Autowired
	private InnerServer gateInnerServer;
	
	@Autowired
	private MinaServer gateMinaServer;
	
	@Autowired
	private HeartManage heartManage;
	
	@Override
	public void gameInit() {
		// TODO Auto-generated method stub
		gateInnerClient.startInnerClient();
		gateInnerServer.startInnerServer();
		gateMinaServer.startMinaServer();
		// heartManage.heartTick();
	}
	
}
