package com.wx.server.start;

import org.apache.commons.logging.Log;
import org.apache.log4j.PropertyConfigurator;

import com.wx.server.beanfactory.SpringBeanFactory;
import com.wx.server.config.xml.ServerInfoManage;
import com.wx.server.mina.MinaServer;
import com.wx.server.thread.ShutDownThread;
import com.wx.server.utils.LogUtils;

/***
 * 
 * ClassName: LoginServer <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-10 下午3:22:18 <br/>
 * 游戏入口类
 * 
 * @author lyh
 * @version
 */
public class LoginServer {
	
	private static LogUtils log = LogUtils.getLog(LoginServer.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 0) {
			log.error("哥你的参数不足,谢谢!");
			return;
		}
		// TODO Auto-generated method stub
		ServerInfoManage.curServerInfo = ServerInfoManage.getSConfig(Integer.parseInt(args[0]));
		new LoginServer().load();
	}
	
	public void load() {
		PropertyConfigurator.configure(com.wx.server.res.ResPath.USER_DIR + "/src/main/resources/config/log4j.properties");
		SpringBeanFactory.springStart();// spring加载
		this.loadLoaderManage();
		
		Runtime.getRuntime().addShutdownHook(new ShutDownThread());// 加一个钩子关闭时调用
		
	}
	
	/**
	 * loadLoaderManage:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 */
	private void loadLoaderManage() {
		LoaderManage lm = (LoaderManage) SpringBeanFactory.getSpringBean("loaderManage");
		lm.load();
	}
}
