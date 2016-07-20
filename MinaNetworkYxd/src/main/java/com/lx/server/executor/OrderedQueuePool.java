/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.executor;

import java.util.HashMap;

public class OrderedQueuePool<K>
{
  private HashMap<K, TasksQueue> map = new HashMap();

  public TasksQueue getTasksQueue(K key)
  {
    synchronized (this.map) {
      TasksQueue queue = (TasksQueue)this.map.get(key);
      if (queue == null) {
        queue = new TasksQueue();
        this.map.put(key, queue);
      }
      return queue;
    }
  }

  public HashMap<K, TasksQueue> getTasksQueues()
  {
    return this.map;
  }

  public TasksQueue removeTasksQueue(K key)
  {
    synchronized (this.map) {
      TasksQueue tq = (TasksQueue)this.map.remove(key);
      return tq;
    }
  }
}