package com.lx.nserver.model;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.TaskConfigPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:TaskConfigModel <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午2:36:55 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class TaskConfigModel extends ResourceModelImpl<TaskConfigPojo> {
	private byte number;
	private int initTaskId;
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		CopyOnWriteArrayList<TaskConfigPojo> TaskConfigList = this.getReslList();
		if (TaskConfigList != null) {
			for (int i = 0; i < TaskConfigList.size(); i++) {
				TaskConfigPojo taskConfigPojo = TaskConfigList.get(i);
				if (taskConfigPojo != null) {
					number = (byte) taskConfigPojo.getMaxTaskNumber();
					initTaskId = taskConfigPojo.getInitTaskId();
				}
			}
		}
		log.error("任务系统配置加载完成");
	}
	
	public TaskConfigModel() {
		// TODO Auto-generated constructor stub
		super(TxtRes.TASK_ALL_SET_CONFIG, TaskConfigPojo.class);
	}
	
	public byte getNumber() {
		return number;
	}
	
	public int getInitTaskId() {
		return initTaskId;
	}
	
	/**
	 * 获取最大配置任务数 getTaskMaxNumberConfig:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	/* public byte getTaskMaxNumberConfig(){ byte number=0; CopyOnWriteArrayList<TaskConfigPojo> TaskConfigList= this.getReslList(); if(TaskConfigList!=null){ for (int i = 0; i <
	 * TaskConfigList.size(); i++) { TaskConfigPojo taskConfigPojo=TaskConfigList.get(i); if(taskConfigPojo!=null){ number=(byte) taskConfigPojo.getMaxTaskNumber(); } } } return number; } */
	
}
