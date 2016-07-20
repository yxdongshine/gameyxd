/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.mina.core.IoUtil;
import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultIoFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.DefaultIoSessionDataStructureFactory;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionDataStructureFactory;
import org.apache.mina.core.session.IoSessionInitializationException;
import org.apache.mina.core.session.IoSessionInitializer;
import org.apache.mina.util.ExceptionMonitor;
import org.apache.mina.util.NamePreservingRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIoService
  implements IoService
{
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIoService.class);

  private static final AtomicInteger id = new AtomicInteger();
  private final String threadName;
  private final Executor executor;
  private final boolean createdExecutor;
  private IoHandler handler;
  protected final IoSessionConfig sessionConfig;
  private final IoServiceListener serviceActivationListener = new IoServiceListener()
  {
    public void serviceActivated(IoService service) {
      AbstractIoService s = (AbstractIoService)service;
      IoServiceStatistics _stats = s.getStatistics();
      _stats.setLastReadTime(s.getActivationTime());
      _stats.setLastWriteTime(s.getActivationTime());
      _stats.setLastThroughputCalculationTime(s.getActivationTime());
    }

    public void serviceDeactivated(IoService service)
      throws Exception
    {
    }

    public void serviceIdle(IoService service, IdleStatus idleStatus)
      throws Exception
    {
    }

    public void sessionCreated(IoSession session)
      throws Exception
    {
    }

    public void sessionClosed(IoSession session)
      throws Exception
    {
    }

    public void sessionDestroyed(IoSession session)
      throws Exception
    {
    }
  };

  private IoFilterChainBuilder filterChainBuilder = new DefaultIoFilterChainBuilder();

  private IoSessionDataStructureFactory sessionDataStructureFactory = new DefaultIoSessionDataStructureFactory();
  private final IoServiceListenerSupport listeners;
  protected final Object disposalLock = new Object();
  private volatile boolean disposing;
  private volatile boolean disposed;
  private IoServiceStatistics stats = new IoServiceStatistics(null);

  protected AbstractIoService(IoSessionConfig sessionConfig, Executor executor)
  {
    if (sessionConfig == null) {
      throw new IllegalArgumentException("sessionConfig");
    }

    if (getTransportMetadata() == null) {
      throw new IllegalArgumentException("TransportMetadata");
    }

    if (!(getTransportMetadata().getSessionConfigType().isAssignableFrom(sessionConfig.getClass()))) {
      throw new IllegalArgumentException("sessionConfig type: " + sessionConfig.getClass() + " (expected: " + getTransportMetadata().getSessionConfigType() + ")");
    }

    this.listeners = new IoServiceListenerSupport(this);
    this.listeners.add(this.serviceActivationListener);

    this.sessionConfig = sessionConfig;

    ExceptionMonitor.getInstance();

    if (executor == null) {
      this.executor = Executors.newCachedThreadPool();
      this.createdExecutor = true;
    } else {
      this.executor = executor;
      this.createdExecutor = false;
    }

    this.threadName = super.getClass().getSimpleName() + '-' + id.incrementAndGet();
  }

  public final IoFilterChainBuilder getFilterChainBuilder()
  {
    return this.filterChainBuilder;
  }

  public final void setFilterChainBuilder(IoFilterChainBuilder builder)
  {
    if (builder == null) {
      builder = new DefaultIoFilterChainBuilder();
    }
    this.filterChainBuilder = builder;
  }

  public final DefaultIoFilterChainBuilder getFilterChain()
  {
    if (this.filterChainBuilder instanceof DefaultIoFilterChainBuilder) {
      return ((DefaultIoFilterChainBuilder)this.filterChainBuilder);
    }

    throw new IllegalStateException("Current filter chain builder is not a DefaultIoFilterChainBuilder.");
  }

  public final void addListener(IoServiceListener listener)
  {
    this.listeners.add(listener);
  }

  public final void removeListener(IoServiceListener listener)
  {
    this.listeners.remove(listener);
  }

  public final boolean isActive()
  {
    return this.listeners.isActive();
  }

  public final boolean isDisposing()
  {
    return this.disposing;
  }

  public final boolean isDisposed()
  {
    return this.disposed;
  }

  public final void dispose()
  {
    dispose(false);
  }

  public final void dispose(boolean awaitTermination)
  {
    if (this.disposed) {
      return;
    }

    synchronized (this.disposalLock) {
      if (!(this.disposing)) {
        this.disposing = true;
        try
        {
          dispose0();
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
        }
      }
    }

    if (this.createdExecutor) {
      ExecutorService e = (ExecutorService)this.executor;
      e.shutdownNow();
      if (awaitTermination) {
        try
        {
          LOGGER.debug("awaitTermination on {} called by thread=[{}]", this, Thread.currentThread().getName());
          e.awaitTermination(2147483647L, TimeUnit.SECONDS);
          LOGGER.debug("awaitTermination on {} finished", this);
        } catch (InterruptedException e1) {
          LOGGER.warn("awaitTermination on [{}] was interrupted", this);

          Thread.currentThread().interrupt();
        }
      }
    }
    this.disposed = true;
  }

  protected abstract void dispose0()
    throws Exception;

  public final Map<Long, IoSession> getManagedSessions()
  {
    return this.listeners.getManagedSessions();
  }

  public final int getManagedSessionCount()
  {
    return this.listeners.getManagedSessionCount();
  }

  public final IoHandler getHandler()
  {
    return this.handler;
  }

  public final void setHandler(IoHandler handler)
  {
    if (handler == null) {
      throw new IllegalArgumentException("handler cannot be null");
    }

    if (isActive()) {
      throw new IllegalStateException("handler cannot be set while the service is active.");
    }

    this.handler = handler;
  }

  public final IoSessionDataStructureFactory getSessionDataStructureFactory()
  {
    return this.sessionDataStructureFactory;
  }

  public final void setSessionDataStructureFactory(IoSessionDataStructureFactory sessionDataStructureFactory)
  {
    if (sessionDataStructureFactory == null) {
      throw new IllegalArgumentException("sessionDataStructureFactory");
    }

    if (isActive()) {
      throw new IllegalStateException("sessionDataStructureFactory cannot be set while the service is active.");
    }

    this.sessionDataStructureFactory = sessionDataStructureFactory;
  }

  public IoServiceStatistics getStatistics()
  {
    return this.stats;
  }

  public final long getActivationTime()
  {
    return this.listeners.getActivationTime();
  }

  public final Set<WriteFuture> broadcast(Object message)
  {
    List futures = IoUtil.broadcast(message, getManagedSessions().values());
    return new AbstractSet()
    {
      public Iterator<WriteFuture> iterator() {
        return null;
      }

      public int size()
      {
        return 0;
      }
    };
  }

  public final IoServiceListenerSupport getListeners() {
    return this.listeners;
  }

  protected final void executeWorker(Runnable worker) {
    executeWorker(worker, null);
  }

  protected final void executeWorker(Runnable worker, String suffix) {
    String actualThreadName = this.threadName;
    if (suffix != null) {
      actualThreadName = actualThreadName + '-' + suffix;
    }
    this.executor.execute(new NamePreservingRunnable(worker, actualThreadName));
  }

  protected final void initSession(IoSession session, IoFuture future, IoSessionInitializer sessionInitializer)
  {
    if (this.stats.getLastReadTime() == 0L) {
      this.stats.setLastReadTime(getActivationTime());
    }

    if (this.stats.getLastWriteTime() == 0L) {
      this.stats.setLastWriteTime(getActivationTime());
    }

    try
    {
      ((AbstractIoSession)session).setAttributeMap(session.getService().getSessionDataStructureFactory().getAttributeMap(session));
    } catch (IoSessionInitializationException e) {
      throw e;
    } catch (Exception e) {
      throw new IoSessionInitializationException("Failed to initialize an attributeMap.", e);
    }
    try
    {
      ((AbstractIoSession)session).setWriteRequestQueue(session.getService().getSessionDataStructureFactory().getWriteRequestQueue(session));
    } catch (IoSessionInitializationException e) {
      throw e;
    } catch (Exception e) {
      throw new IoSessionInitializationException("Failed to initialize a writeRequestQueue.", e);
    }

    if ((future != null) && (future instanceof ConnectFuture))
    {
      session.setAttribute(DefaultIoFilterChain.SESSION_CREATED_FUTURE, future);
    }

    if (sessionInitializer != null) {
      sessionInitializer.initializeSession(session, future);
    }

    finishSessionInitialization0(session, future);
  }

  protected void finishSessionInitialization0(IoSession session, IoFuture future)
  {
  }

  public int getScheduledWriteBytes()
  {
    return this.stats.getScheduledWriteBytes();
  }

  public int getScheduledWriteMessages()
  {
    return this.stats.getScheduledWriteMessages();
  }

  public static class ServiceOperationFuture extends DefaultIoFuture
  {
    public ServiceOperationFuture()
    {
      super(null);
    }

    public final boolean isDone() {
      return (getValue() == Boolean.TRUE);
    }

    public final void setDone() {
      setValue(Boolean.TRUE);
    }

    public final Exception getException() {
      if (getValue() instanceof Exception) {
        return ((Exception)getValue());
      }

      return null;
    }

    public final void setException(Exception exception) {
      if (exception == null) {
        throw new IllegalArgumentException("exception");
      }
      setValue(exception);
    }
  }
}