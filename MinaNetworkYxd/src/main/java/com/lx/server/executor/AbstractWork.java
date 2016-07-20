/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.executor;

import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.session.IConnect;

public abstract class AbstractWork implements IWorker {
	private TasksQueue tasksQueue;
	private boolean isWorking;

	public AbstractWork(IConnect cNet, InnerMessage im, ProxyXmlBean pxb) {
		// TODO Auto-generated constructor stub
	}

	public TasksQueue getTasksQueue() {
		return this.tasksQueue;
	}

	public void setTasksQueue(TasksQueue tasksQueue) {
		this.tasksQueue = tasksQueue;
	}

	public boolean isWorking() {
		return this.isWorking;
	}

	public void setWorking(boolean isWorking) {
		this.isWorking = isWorking;
	}
}