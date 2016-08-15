package com.engine.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.config.xml.map.SpaceInfoManage;
import com.engine.msgloader.CommondClassLoader;
import com.engine.properties.ServerGameConfig;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.nserver.manage.TxtModelManage;

/**
 * ClassName:GameLoadManage <br/>
 * Function: TODO (游戏数据初始加载管理类). <br/>
 * Reason: TODO (全部的初始加载都在这儿). <br/>
 * Date: 2015-7-2 上午9:40:29 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public abstract class GameInitializeManage implements Initialize {
	
	@Autowired
	private CommondClassLoader commondClassLoader;
	@Autowired
	private ConfigFactory configFactory;
	@Autowired
	private SpaceInfoManage spaceInfoManage;
	
	@Autowired
	private TxtModelManage txtModelManage;
	
	public void init() {
		configFactory.init();
		
		/** 加载地图数据 **/
		spaceInfoManage.init();
		
		// 加载功能模块
		commondClassLoader.setPackageName(ServerGameConfig.PACKAGE);
		commondClassLoader.initialize(SpringBeanFactory.factory);
		txtModelManage.loadTxt(SpringBeanFactory.factory, ServerGameConfig.TXT_PACKAGE);
		gameInit();
	}
	
	public abstract void gameInit();
	
}
