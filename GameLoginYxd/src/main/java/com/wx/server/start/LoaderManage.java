package com.wx.server.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wx.server.beanfactory.SpringBeanFactory;
import com.wx.server.config.properties.ServerGameConfig;
import com.wx.server.config.xml.ServerInfoManage;
import com.wx.server.excel.read.ExcelManage;
import com.wx.server.httpserver.GameHttpServer;
import com.wx.server.logical.manage.LoginManage;
import com.wx.server.mina.MinaServer;
import com.wx.server.msgloader.CommondClassLoader;

/**
 * ClassName:LoaderManage <br/>
 * Function: TODO (全部的初始化加载都在这里). <br/>
 * Reason: TODO (加载管理器). <br/>
 * Date: 2015-6-6 上午9:56:30 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class LoaderManage {
	@Autowired
	private CommondClassLoader cml;
	@Autowired
	private ExcelManage excelManage;
	@Autowired
	private LoginManage loginManage;
	@Autowired
	private GameHttpServer pGameHttpServer;
	@Autowired
	private MinaServer minaServer;
	
	public void load() {
		
		ServerGameConfig.loadGameConfig();// 加载游戏配置
		cml.setPackageName(ServerGameConfig.LOGICAL_PACKAGE);
		cml.initialize(SpringBeanFactory.factory);// 逻辑加载
		excelManage.load();// 加载excel配置文件
		loginManage.load();// 登录加载
		
		minaServer.startServer();// 加载网络服务
		pGameHttpServer.start();
	}
}
