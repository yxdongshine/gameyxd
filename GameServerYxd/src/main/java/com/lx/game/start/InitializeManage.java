package com.lx.game.start;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.init.GameInitializeManage;
import com.engine.thread.TimerCallbackThread;
import com.engine.thread.TimerThread;
import com.engine.timer.TimerThreadManage;
import com.engine.utils.log.LogUtils;
import com.lib.res.UserDir;
import com.lx.game.mina.InnerClient;
import com.lx.game.mina.InnerServer;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.timer.TaskTimerEvent;
import com.lx.logical.manage.BuffManage;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.SkillManage;
import com.lx.logical.manage.SpaceManage;

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
	private Log log = LogUtils.getLog(InitializeManage.class);
	@Autowired
	private InnerClient gateInnerClient;
	
	@Autowired
	private InnerServer innerServer;
	@Autowired
	private ItemConfigLoad itemConfigLoad;
	@Autowired
	private SpaceManage spaceMgr;
	@Autowired
	private TimerThread timerThread;
	@Autowired
	private TimerCallbackThread timerCallbackThread;
	@Autowired
	private FuBenManage fuBenMgr;
	/** 读取配置 **/
	// @Autowired
	// private TxtModelManage txtModelManage;
	
	@Override
	public void gameInit() {
		// TODO Auto-generated method stub
		log.error("user_dir:::" + UserDir.USER_DIR);
		
		/** 初始化空间管理器 **/
		// spaceMgr.init();
		// 初始化世界副本
		fuBenMgr.initWorldFuBen();
		gateInnerClient.startInnerClient();
		innerServer.startInnerServer();
		// 加载道具
		itemConfigLoad.load();
		
		timerThread.startThread();
		timerCallbackThread.startThread();
		//TaskTimerEvent ttv = new TaskTimerEvent(0,1000 * 60 + System.currentTimeMillis(),-1);
	}
	
}
