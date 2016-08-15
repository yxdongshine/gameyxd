package com.engine.pool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * ClassName: TasksQueue <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-26 下午1:55:27 <br/>
 * 任务队列
 * 
 * @author lyh
 * @version @param <V>
 */

public class TasksQueue implements Runnable {
	
	// private Lock lock = new ReentrantLock();
	
	/**
	 * 命令队列
	 */
	private final Queue<IWorker> tasksQueue = new ConcurrentLinkedQueue<IWorker>();
	
	private boolean processingCompleted = true;
	
	/** 正在工作 **/
	private boolean isWorking = true;
	
	/**
	 * 下一执行命令
	 * 
	 * @return
	 */
	public IWorker poll() {
		// try {
		// lock.lock();
		return tasksQueue.poll();
		// } finally {
		// lock.unlock();
		// }
	}
	
	/**
	 * 增加执行指令
	 * 
	 * @param command
	 * @return
	 */
	public boolean add(IWorker value) {
		// try {
		// lock.lock();
		return tasksQueue.offer(value);
		// } finally {
		// lock.unlock();
		// }
	}
	
	/**
	 * 清理
	 */
	public void clear() {
		// try {
		// lock.lock();
		tasksQueue.clear();
		// } finally {
		// lock.unlock();
		// }
	}
	
	/**
	 * 获取指令数量
	 * 
	 * @return
	 */
	public int size() {
		return tasksQueue.size();
	}
	
	public boolean isProcessingCompleted() {
		return processingCompleted;
	}
	
	public void setProcessingCompleted(boolean processingCompleted) {
		this.processingCompleted = processingCompleted;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isWorking) {
			IWorker w = null;
			synchronized (tasksQueue) {
				w = tasksQueue.poll();
			}
			
			if (w == null) {
				processingCompleted = true;
				break;
			}
			// 处理业务
			w.doWork();
		}
	}
	
	public void stop() {
		isWorking = false;
	}
	
}
