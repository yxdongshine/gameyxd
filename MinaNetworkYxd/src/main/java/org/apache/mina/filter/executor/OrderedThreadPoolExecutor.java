/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.executor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.DummySession;
import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderedThreadPoolExecutor extends ThreadPoolExecutor
{
  private static Logger LOGGER = LoggerFactory.getLogger(OrderedThreadPoolExecutor.class);
  private static final int DEFAULT_INITIAL_THREAD_POOL_SIZE = 0;
  private static final int DEFAULT_MAX_THREAD_POOL = 16;
  private static final int DEFAULT_KEEP_ALIVE = 30;
  private static final IoSession EXIT_SIGNAL = new DummySession();
  private final AttributeKey TASKS_QUEUE;
  private final BlockingQueue<IoSession> waitingSessions;
  private final Set<Worker> workers;
  private volatile int largestPoolSize;
  private final AtomicInteger idleWorkers;
  private long completedTaskCount;
  private volatile boolean shutdown;
  private final IoEventQueueHandler eventQueueHandler;

  public OrderedThreadPoolExecutor()
  {
    this(0, 16, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
  }

  public OrderedThreadPoolExecutor(int maximumPoolSize)
  {
    this(0, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
  }

  public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize)
  {
    this(corePoolSize, maximumPoolSize, 30L, TimeUnit.SECONDS, Executors.defaultThreadFactory(), null);
  }

  public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit)
  {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), null);
  }

  public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, IoEventQueueHandler eventQueueHandler)
  {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), eventQueueHandler);
  }

  public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory)
  {
    this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, null);
  }

  public OrderedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, IoEventQueueHandler eventQueueHandler)
  {
    super(0, 1, keepAliveTime, unit, new SynchronousQueue(), threadFactory, new ThreadPoolExecutor.AbortPolicy());

    this.TASKS_QUEUE = new AttributeKey(super.getClass(), "tasksQueue");

    this.waitingSessions = new LinkedBlockingQueue();

    this.workers = new HashSet();

    this.idleWorkers = new AtomicInteger();

    if (corePoolSize < 0) {
      throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
    }

    if ((maximumPoolSize == 0) || (maximumPoolSize < corePoolSize)) {
      throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
    }

    super.setCorePoolSize(corePoolSize);
    super.setMaximumPoolSize(maximumPoolSize);

    if (eventQueueHandler == null)
      this.eventQueueHandler = IoEventQueueHandler.NOOP;
    else
      this.eventQueueHandler = eventQueueHandler;
  }

  private SessionTasksQueue getSessionTasksQueue(IoSession session)
  {
    SessionTasksQueue queue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);

    if (queue == null) {
      queue = new SessionTasksQueue();
      SessionTasksQueue oldQueue = (SessionTasksQueue)session.setAttributeIfAbsent(this.TASKS_QUEUE, queue);

      if (oldQueue != null) {
        queue = oldQueue;
      }
    }

    return queue;
  }

  public IoEventQueueHandler getQueueHandler()
  {
    return this.eventQueueHandler;
  }

  public void setRejectedExecutionHandler(RejectedExecutionHandler handler)
  {
  }

  private void addWorker()
  {
    synchronized (this.workers) {
      if (this.workers.size() >= super.getMaximumPoolSize()) {
        return;
      }

      Worker worker = new Worker();
      Thread thread = getThreadFactory().newThread(worker);

      this.idleWorkers.incrementAndGet();

      thread.start();
      this.workers.add(worker);

      if (this.workers.size() > this.largestPoolSize)
        this.largestPoolSize = this.workers.size();
    }
  }

  private void addWorkerIfNecessary()
  {
    if (this.idleWorkers.get() == 0)
      synchronized (this.workers) {
        if ((this.workers.isEmpty()) || (this.idleWorkers.get() == 0))
          addWorker();
      }
  }

  private void removeWorker()
  {
    synchronized (this.workers) {
      if (this.workers.size() <= super.getCorePoolSize()) {
        return;
      }
      this.waitingSessions.offer(EXIT_SIGNAL);
    }
  }

  public int getMaximumPoolSize()
  {
    return super.getMaximumPoolSize();
  }

  public void setMaximumPoolSize(int maximumPoolSize)
  {
    if ((maximumPoolSize <= 0) || (maximumPoolSize < super.getCorePoolSize())) {
      throw new IllegalArgumentException("maximumPoolSize: " + maximumPoolSize);
    }

    synchronized (this.workers) {
      super.setMaximumPoolSize(maximumPoolSize);
      int difference = this.workers.size() - maximumPoolSize;
      while (difference > 0) {
        removeWorker();
        --difference;
      }
    }
  }

  public boolean awaitTermination(long timeout, TimeUnit unit)
    throws InterruptedException
  {
    long deadline = System.currentTimeMillis() + unit.toMillis(timeout);

    synchronized (this.workers) {
      while (!(isTerminated())) {
        long waitTime = deadline - System.currentTimeMillis();
        if (waitTime <= 0L) {
          break;
        }

        this.workers.wait(waitTime);
      }
    }
    return isTerminated();
  }

  public boolean isShutdown()
  {
    return this.shutdown;
  }

  public boolean isTerminated()
  {
    if (!(this.shutdown)) {
      return false;
    }

    synchronized (this.workers) {
      return this.workers.isEmpty();
    }
  }

  public void shutdown()
  {
    if (this.shutdown) {
      return;
    }

    this.shutdown = true;

    synchronized (this.workers) {
      for (int i = this.workers.size(); i > 0; --i)
        this.waitingSessions.offer(EXIT_SIGNAL);
    }
  }

  public List<Runnable> shutdownNow()
  {
    shutdown();

    List answer = new ArrayList();
    IoSession session;
    while ((session = (IoSession)this.waitingSessions.poll()) != null)
    {
      if (session == EXIT_SIGNAL) {
        this.waitingSessions.offer(EXIT_SIGNAL);
        Thread.yield();
      }
      else
      {
        SessionTasksQueue sessionTasksQueue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);

        synchronized (sessionTasksQueue.tasksQueue)
        {
          for (Runnable task : sessionTasksQueue.tasksQueue) {
            getQueueHandler().polled(this, (IoEvent)task);
            answer.add(task);
          }

          sessionTasksQueue.tasksQueue.clear();
        }
      }
    }
    return answer;
  }

  private void print(Queue<Runnable> queue, IoEvent event)
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Adding event ").append(event.getType()).append(" to session ").append(event.getSession().getId());
    boolean first = true;
    sb.append("\nQueue : [");
    for (Runnable elem : queue) {
      if (first)
        first = false;
      else {
        sb.append(", ");
      }

      sb.append(((IoEvent)elem).getType()).append(", ");
    }
    sb.append("]\n");
    LOGGER.debug(sb.toString());
  }

  public void execute(Runnable task)
  {
    if (this.shutdown) {
      rejectTask(task);
    }

    checkTaskType(task);

    IoEvent event = (IoEvent)task;

    IoSession session = event.getSession();

    SessionTasksQueue sessionTasksQueue = getSessionTasksQueue(session);
    Queue tasksQueue = sessionTasksQueue.tasksQueue;

    boolean offerEvent = this.eventQueueHandler.accept(this, event);
    boolean offerSession;
    if (offerEvent)
    {
      synchronized (tasksQueue)
      {
        tasksQueue.offer(event);
        if (sessionTasksQueue.processingCompleted) {
          sessionTasksQueue.processingCompleted = false;
          offerSession = true;
        } else {
          offerSession = false;
        }

        if (LOGGER.isDebugEnabled())
          print(tasksQueue, event);
      }
    }
    else {
      offerSession = false;
    }

    if (offerSession)
    {
      this.waitingSessions.offer(session);
    }

    addWorkerIfNecessary();

    if (offerEvent)
      this.eventQueueHandler.offered(this, event);
  }

  private void rejectTask(Runnable task)
  {
    getRejectedExecutionHandler().rejectedExecution(task, this);
  }

  private void checkTaskType(Runnable task) {
    if (!(task instanceof IoEvent))
      throw new IllegalArgumentException("task must be an IoEvent or its subclass.");
  }

  public int getActiveCount()
  {
    synchronized (this.workers) {
      return (this.workers.size() - this.idleWorkers.get());
    }
  }

  public long getCompletedTaskCount()
  {
    synchronized (this.workers) {
      long answer = this.completedTaskCount;
      for (Worker w : this.workers) {
        answer += w.completedTaskCount;
      }

      return answer;
    }
  }

  public int getLargestPoolSize()
  {
    return this.largestPoolSize;
  }

  public int getPoolSize()
  {
    synchronized (this.workers) {
      return this.workers.size();
    }
  }

  public long getTaskCount()
  {
    return getCompletedTaskCount();
  }

  public boolean isTerminating()
  {
    synchronized (this.workers) {
      return ((isShutdown()) && (!(isTerminated())));
    }
  }

  public int prestartAllCoreThreads()
  {
    int answer = 0;
    synchronized (this.workers) {
      for (int i = super.getCorePoolSize() - this.workers.size(); i > 0; --i) {
        addWorker();
        ++answer;
      }
    }
    return answer;
  }

  public boolean prestartCoreThread()
  {
    synchronized (this.workers) {
      if (this.workers.size() < super.getCorePoolSize()) {
        addWorker();
        return true;
      }
      return false;
    }
  }

  public BlockingQueue<Runnable> getQueue()
  {
    throw new UnsupportedOperationException();
  }

  public void purge()
  {
  }

  public boolean remove(Runnable task)
  {
    checkTaskType(task);
    IoEvent event = (IoEvent)task;
    IoSession session = event.getSession();
    SessionTasksQueue sessionTasksQueue = (SessionTasksQueue)session.getAttribute(this.TASKS_QUEUE);
    Queue tasksQueue = sessionTasksQueue.tasksQueue;

    if (sessionTasksQueue == null)
      return false;
    boolean removed;
    synchronized (tasksQueue) {
      removed = tasksQueue.remove(task);
    }
    if (removed) {
      getQueueHandler().polled(this, event);
    }

    return removed;
  }

  public int getCorePoolSize()
  {
    return super.getCorePoolSize();
  }

  public void setCorePoolSize(int corePoolSize)
  {
    if (corePoolSize < 0) {
      throw new IllegalArgumentException("corePoolSize: " + corePoolSize);
    }
    if (corePoolSize > super.getMaximumPoolSize()) {
      throw new IllegalArgumentException("corePoolSize exceeds maximumPoolSize");
    }

    synchronized (this.workers) {
      if (super.getCorePoolSize() > corePoolSize) {
        for (int i = super.getCorePoolSize() - corePoolSize; i > 0; --i) {
          removeWorker();
        }
      }
      super.setCorePoolSize(corePoolSize);
    }
  }

  private class SessionTasksQueue
  {
    private final Queue<Runnable> tasksQueue;
    private boolean processingCompleted;

    private SessionTasksQueue()
    {
      this.tasksQueue = new ConcurrentLinkedQueue();

      this.processingCompleted = true;
    }
  }

  private class Worker
    implements Runnable
  {
    private volatile long completedTaskCount;
    private Thread thread;

    public void run()
    {
      this.thread = Thread.currentThread();
      while (true)
        try
        {
          IoSession session = fetchSession();

          OrderedThreadPoolExecutor.this.idleWorkers.decrementAndGet();

          if (session == null) {
            synchronized (OrderedThreadPoolExecutor.this.workers) {
              if (OrderedThreadPoolExecutor.this.workers.size() > OrderedThreadPoolExecutor.this.getCorePoolSize())
              {
                OrderedThreadPoolExecutor.this.workers.remove(this);
              }
            }
          }

          if (session == OrderedThreadPoolExecutor.EXIT_SIGNAL) {
          }
          try
          {
            if (session == null) 
             runTasks(OrderedThreadPoolExecutor.this.getSessionTasksQueue(session));
          }
          finally {
            OrderedThreadPoolExecutor.this.idleWorkers.incrementAndGet();
          }
        }
        finally {
          synchronized (OrderedThreadPoolExecutor.this.workers) {
            OrderedThreadPoolExecutor.this.workers.remove(this);
            OrderedThreadPoolExecutor.this.completedTaskCount += this.completedTaskCount;
            OrderedThreadPoolExecutor.this.workers.notifyAll(); }  }   } 
    // ERROR //
    private IoSession fetchSession() {
		return null;
    	
    } 
    private void runTasks(OrderedThreadPoolExecutor.SessionTasksQueue sessionTasksQueue) { while (true) { Queue tasksQueue = sessionTasksQueue.tasksQueue;

        synchronized (tasksQueue) {
          Runnable task = (Runnable)tasksQueue.poll();

          if (task == null) {
            sessionTasksQueue.processingCompleted = true;
            return;
          }
        }
        Runnable task = null;
        OrderedThreadPoolExecutor.this.eventQueueHandler.polled(OrderedThreadPoolExecutor.this, (IoEvent)task);

        runTask(task);
      }
    }

    private void runTask(Runnable task) {
      OrderedThreadPoolExecutor.this.beforeExecute(this.thread, task);
      boolean ran = false;
      try {
        task.run();
        ran = true;
        OrderedThreadPoolExecutor.this.afterExecute(task, null);
        this.completedTaskCount += 1L;
      } catch (RuntimeException e) {
        if (!(ran)) {
          OrderedThreadPoolExecutor.this.afterExecute(task, e);
        }
        throw e;
      }
    }
  }
}