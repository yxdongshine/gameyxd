package com.engine.executor;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;

import com.engine.pool.IWorker;
import com.engine.pool.OrderedQueuePool;
import com.engine.pool.TasksQueue;
import com.engine.utils.log.LogUtils;
import com.engine.worker.AbstractWork;

/**
 * ClassName:OrderedQueuePoolExecutor <br/>
 * Function: TODO (对象顺序连接池). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-26 上午10:11:11 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class OrderedQueuePoolExecutor extends ThreadPoolExecutor {
	protected static Log log = LogUtils.getLog(OrderedQueuePoolExecutor.class);
	
	/** 每一个key,对应着一个相应的列表 **/
	private OrderedQueuePool<Long> pool = new OrderedQueuePool<>();
	
	/** 线程池名称 **/
	private String name;
	
	/** 核心线程池 **/
	private int corePoolSize;
	
	/** 线程池最大队列 **/
	private int maxQueueSize;
	
	public OrderedQueuePoolExecutor(String name, int corePoolSize, int maxQueueSize) {
		super(corePoolSize, maxQueueSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		this.name = name;
		this.corePoolSize = corePoolSize;
		this.maxQueueSize = maxQueueSize;
	}
	
	public OrderedQueuePoolExecutor(String name, int corePoolSize) {
		this(name, corePoolSize, Integer.MAX_VALUE);
	}
	
	/**
	 * 增加执行任务
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean addTask(Long key, AbstractWork task) {
		// key = key % corePoolSize;
		TasksQueue queue = pool.getTasksQueue(key);
		boolean run = false;
		boolean result = false;
		synchronized (queue) {
			if (maxQueueSize > 0) {
				if (queue.size() > maxQueueSize) {
					log.error("队列" + name + "(" + key + ")" + "抛弃指令!");
					queue.clear();
				}
			}
			result = queue.add(task);
			if (result) {
				task.setTasksQueue(queue);
				{
					if (queue.isProcessingCompleted()) {
						queue.setProcessingCompleted(false);
						run = true;
					}
				}
				
			} else {
				log.error("队列添加任务失败");
			}
		}
		if (run) {
			execute(queue);
		}
		return result;
	}
	
	// /**
	// * 获取任务执行队列
	// * @param key
	// * @return
	// */
	// public TasksQueue<AbstractWork> getTasksQueue(Long key){
	// key = key % corePoolSize;
	// return pool.getTasksQueue(key);
	// }
	
	/**
	 * 获取剩余任务数量
	 */
	public int getTaskCounts() {
		int count = super.getActiveCount();
		
		Iterator<Entry<Long, TasksQueue>> iter = pool.getTasksQueues().entrySet().iterator();
		while (iter.hasNext()) {
			Entry<Long, TasksQueue> entry = (Entry<Long, TasksQueue>) iter.next();
			TasksQueue tasksQueue = entry.getValue();
			// if(tasksQueue.size() > 0){
			// log.error(entry.getKey() + " queue size:" + tasksQueue.size() + ", is run:" + tasksQueue.isProcessingCompleted());
			// }
			count += tasksQueue.size();
		}
		return count;
	}
	
	/**
	 * removeFromPool:(). <br/>
	 * TODO().<br/>
	 * 删除队列
	 * 
	 * @author lyh
	 * @param key
	 */
	public void removeFromPool(Long key) {
		// TasksQueue
		TasksQueue queue = pool.removeTasksQueue(key);
		if (queue != null) {
			queue.clear();
		} else {
			log.error("没有删除消息队列::" + key);
		}
		
	}
}
