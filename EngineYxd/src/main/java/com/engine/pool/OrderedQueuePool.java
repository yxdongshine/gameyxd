package com.engine.pool;

import java.util.HashMap;

/**
 * 
 * ClassName: OrderedQueuePool <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-26 下午1:54:44 <br/>
 * 有序池
 * 
 * @author lyh
 * @version @param <K>
 * @version @param <V>
 */

public class OrderedQueuePool<K> {
	
	/** 因为用的同步所以用hashmap **/
	HashMap<K, TasksQueue> map = new HashMap<K, TasksQueue>();
	
	/**
	 * 获得任务队列
	 * 
	 * @param key
	 * @return
	 */
	public TasksQueue getTasksQueue(K key) {
		synchronized (map) {
			TasksQueue queue = map.get(key);
			if (queue == null) {
				queue = new TasksQueue();
				map.put(key, queue);
			}
			return queue;
		}
	}
	
	/**
	 * 获得全部任务队列
	 * 
	 * @param key
	 * @return
	 */
	public HashMap<K, TasksQueue> getTasksQueues() {
		return map;
	}
	
	/**
	 * 移除任务队列
	 * 
	 * @param key
	 * @return
	 */
	public TasksQueue removeTasksQueue(K key) {
		synchronized (map) {
			return map.remove(key);
		}
	}
}
