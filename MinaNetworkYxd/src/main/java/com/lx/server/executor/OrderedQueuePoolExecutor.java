/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.executor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderedQueuePoolExecutor extends ThreadPoolExecutor
{
  protected Logger log;
  private OrderedQueuePool<Long> pool;
  private String name;
  private int corePoolSize;
  private int maxQueueSize;

  public OrderedQueuePoolExecutor(String name, int corePoolSize, int maxQueueSize)
  {
    super(corePoolSize, maxQueueSize, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());

    this.log = LoggerFactory.getLogger(OrderedQueuePoolExecutor.class);

    this.pool = new OrderedQueuePool();

    this.name = name;
    this.corePoolSize = corePoolSize;
    this.maxQueueSize = maxQueueSize;
  }

  public OrderedQueuePoolExecutor(String name, int corePoolSize) {
    this(name, corePoolSize, 2147483647);
  }

  public boolean addTask(Long key, AbstractWork task)
  {
    TasksQueue queue = this.pool.getTasksQueue(key);
    boolean run = false;
    boolean result = false;
    synchronized (queue) {
      if ((this.maxQueueSize > 0) && 
        (queue.size() > this.maxQueueSize)) {
        this.log.error("����" + this.name + "(" + key + ")" + "����ָ��!");
        queue.clear();
      }

      result = queue.add(task);
      if (result) {
        task.setTasksQueue(queue);

        if (queue.isProcessingCompleted()) {
          queue.setProcessingCompleted(false);
          run = true;
        }
      }
      else
      {
        this.log.error("�����������ʧ��");
      }
    }
    if (run) {
      execute(queue);
    }
    return result;
  }

  public int getTaskCounts()
  {
    int count = super.getActiveCount();

    Iterator iter = this.pool.getTasksQueues().entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      TasksQueue tasksQueue = (TasksQueue)entry.getValue();

      count += tasksQueue.size();
    }
    return count;
  }

  public TasksQueue removeFromPool(Long key)
  {
    TasksQueue queue = this.pool.removeTasksQueue(key);
    if (queue != null) {
      queue.clear();
    }

    return queue;
  }
}