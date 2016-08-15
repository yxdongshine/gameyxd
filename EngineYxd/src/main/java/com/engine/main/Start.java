package com.engine.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.engine.beanfactory.SpringBeanFactory;
import com.engine.config.xml.ServerInfo;
import com.engine.config.xml.ServerInfoManage;
import com.engine.shutdown.ShutDownThread;
import com.lib.res.UserDir;

/**
 * ClassName:Start <br/>
 * Function: TODO (游戏入口类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午9:19:06 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class Start {
	
	/**
	 * start:(). <br/>
	 * TODO().<br/>
	 * 游戏开始
	 * 
	 * @author lyh
	 * @param ars
	 */
	public void start(String ars[]) {
		if (ars.length < 1) {
			return;
		}
		if (ars.length > 1) {
			UserDir.USER_DIR = ars[1];
		}
		
		setLog4jPath(ars[2], UserDir.USER_DIR + "/" + "log4j.properties");
		// PropertyConfigurator.configure(UserDir.USER_DIR + "/" + "log4j.properties");
		SpringBeanFactory.springStart();
		// ars[0]自己服务的标志
		ServerInfoManage.curServerInfo = ServerInfoManage.getSConfig(Integer.parseInt(ars[0]));
		
		initialize();
		
		// 加挂退出线程
		Runtime.getRuntime().addShutdownHook(new ShutDownThread());
	}
	
	/**
	 * initialize:(). <br/>
	 * TODO().<br/>
	 * 初始化一些数据
	 * 
	 * @author lyh
	 */
	public abstract void initialize();
	
	private Properties setLog4jPath(String path, String logFile) {
		Properties p = new Properties();
		try {
			FileInputStream fis = new FileInputStream(logFile);
			try {
				p.load(fis);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String logPath = p.getProperty("log4j.appender.TRACE.File");
			p.setProperty("log4j.appender.TRACE.File", UserDir.USER_DIR + File.separator + path + File.separator + logPath);
			PropertyConfigurator.configure(p);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return p;
	}
}
