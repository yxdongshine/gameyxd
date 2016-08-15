/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:ProtocolClassLoader.java 
 * Package Name:com.sj.game.msgloader 
 * Date:2013-8-6下午2:00:24 
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.engine.msgloader;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;

import com.lx.logical.GameMessage;
import com.sj.world.utils.ClassUtils;

/**
 * ClassName:CommondClassLoader <br/>
 * Function: TODO (消息处理类加载). <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2013-8-6 下午2:00:24 <br/>
 * 
 * @author Administrator
 * @version
 * @param <GameMessage>
 * @since JDK 1.6
 * @see
 */
@Component
public class CommondClassLoader {
	/**
	 * 指令表
	 * <P>
	 * 指令号--指令类
	 */
	private Map<Long, GameMessage<?>> commandMap = new ConcurrentHashMap<Long, GameMessage<?>>();
	
	/** 指令工作包 */
	private String packageName;
	
	private static Log log = LogFactory.getLog(CommondClassLoader.class);
	
	/* (non-Javadoc)
	 * 
	 * @see com.sirius.core.Poolable#contains(long) */
	public boolean contains(long commandID) {
		return commandMap.containsKey(Long.valueOf(commandID));
	}
	
	/* (non-Javadoc)
	 * 
	 * @see com.sirius.core.Poolable#destory() */
	public void destory() {
		commandMap.clear();
	}
	
	/**
	 * 获取某个指令
	 * 
	 * @param commandID
	 * @return
	 */
	public GameMessage<?> get(long commandID) {
		// 查找指令表
		return commandMap.get(commandID);
	}
	
	/* (non-Javadoc)
	 * 
	 * @see com.sirius.game.core.kernel.GameObject#initialize() */
	//
	public void initialize(BeanFactory beanFactory) {
		this.loadCommands(beanFactory);
	}
	
	/** 加载指令集 */
	@SuppressWarnings("unchecked")
	private void loadCommands(BeanFactory beanFactory) {
		try {
			// 定位指令表
			// packageName = "com.sirius.game.command";
			// String commandTable = packageName + ".GameMessage";
			//
			// Class<?> tmp = Class.forName(commandTable);
			Package workPackage = Package.getPackage(this.packageName);
			// System.out.println("packageName:::" + workPackage.getName());
			Set<Class<?>> _classes = ClassUtils.getClasses(workPackage);
			for (Class<?> _class : _classes) {
				if (_class.isInterface()) {
					continue;
				}
				// 指令逻辑类
				int index = _class.getName().lastIndexOf(".");
				String beanId = _class.getName().substring(index + 1);
				String firstC = ("" + beanId.charAt(0)).toLowerCase();// 转成小写
				beanId = firstC + beanId.substring(1);
				Object obj = beanFactory.getBean(beanId);
				GameMessage<?> commandClass = null;
				if ((obj instanceof GameMessage)) {
					commandClass = (GameMessage<?>) obj;
				}
				// else if (obj instanceof HttpGameMessage) {
				// commandClass = (HttpGameMessage<Proxy>) obj;
				// }
				else {
					continue;
				}
				
				// 指令号
				Long commandID = Long.valueOf(0);
				Annotation defCommandID = _class.getAnnotation(Head.class);
				if (defCommandID != null) {
					Head info = (Head) defCommandID;
					commandID = Long.valueOf(info.value());
				} else {
					if (defCommandID == null) {
						log.error("指令有错误:::" + _class);
					}
				}
				
				// 添加到指令表
				commandMap.put(commandID, commandClass);
				String msg = "[加载指令] 指令号=" + commandID + ", 处理类=" + commandClass.getClass().getSimpleName();
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("加载有错误:::", ex);
		}
	}
	
	/* (non-Javadoc)
	 * 
	 * @see com.sirius.game.core.ManageObject#wrapAsByteArray() */
	//
	public byte[] wrapAsByteArray() {
		return null;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
