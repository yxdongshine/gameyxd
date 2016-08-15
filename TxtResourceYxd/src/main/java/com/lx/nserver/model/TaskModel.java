package com.lx.nserver.model;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.lx.nserver.res.TxtRes;
import com.lx.nserver.txt.TaskPojo;
import com.read.txt.ResourceModelImpl;

/**
 * ClassName:TaskModel <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午2:39:25 <br/>
 * 
 * @author yxd
 * @version
 */
@Component
public class TaskModel extends ResourceModelImpl<TaskPojo> {
	
	private ConcurrentHashMap<Integer, TaskPojo> TaskPojoHM;
	
	@Override
	public void printLog() {
		// TODO Auto-generated method stub
		TaskPojoHM = new ConcurrentHashMap<Integer, TaskPojo>();
		if (this.getReslList() != null) {
			for (int i = 0; i < this.getReslList().size(); i++) {
				TaskPojo tp = this.getReslList().get(i);
				if (tp != null) {
					TaskPojoHM.put(tp.getId(), tp);
				}
			}
		}
		
		log.error("任务配置加载完成");
	}
	
	public TaskModel() {
		// TODO Auto-generated constructor stub
		super(TxtRes.TASK_CONFIG, TaskPojo.class);
	}
	
	public ConcurrentHashMap<Integer, TaskPojo> getTaskPojoHM() {
		return TaskPojoHM;
	}
	
	public TaskPojo getTaskPojoByTaskId(int id) {
		return TaskPojoHM.get(id);
	}
}
