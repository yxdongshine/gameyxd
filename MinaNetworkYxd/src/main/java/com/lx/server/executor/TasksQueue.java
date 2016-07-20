/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.executor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TasksQueue
  implements Runnable
{
  private final Queue<IWorker> tasksQueue = new ConcurrentLinkedQueue();

  private boolean processingCompleted = true;

  private boolean isWorking = true;

  public IWorker poll()
  {
    return ((IWorker)this.tasksQueue.poll());
  }

  public boolean add(IWorker value)
  {
    return this.tasksQueue.offer(value);
  }

  public void clear()
  {
    this.tasksQueue.clear();
  }

  public int size()
  {
    return this.tasksQueue.size();
  }

  public boolean isProcessingCompleted() {
    return this.processingCompleted;
  }

  public void setProcessingCompleted(boolean processingCompleted) {
    this.processingCompleted = processingCompleted;
  }

  public void run()
  {
    while (this.isWorking) {
      IWorker w = null;
      synchronized (this.tasksQueue) {
        w = (IWorker)this.tasksQueue.poll();
      }
      if (w == null) {
        this.processingCompleted = true;
        return;
      }

      w.doWork();
    }
  }

  public void stop() {
    this.isWorking = false;
  }
}