/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.polling;

import java.net.SocketAddress;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.AbstractIoAcceptor;
import org.apache.mina.core.service.AbstractIoAcceptor.AcceptorOperationFuture;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.service.AbstractIoService.ServiceOperationFuture;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.SimpleIoProcessorPool;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.util.ExceptionMonitor;

public abstract class AbstractPollingIoAcceptor<S extends AbstractIoSession, H> extends AbstractIoAcceptor
{
  private final Semaphore lock;
  private final IoProcessor<S> processor;
  private final boolean createdProcessor;
  private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> registerQueue;
  private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> cancelQueue;
  private final Map<SocketAddress, H> boundHandles;
  private final AbstractIoService.ServiceOperationFuture disposalFuture;
  private volatile boolean selectable;
  private AtomicReference<AbstractPollingIoAcceptor<S, H>.Acceptor> acceptorRef;
  protected boolean reuseAddress;
  protected int backlog;

  protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass)
  {
    this(sessionConfig, null, new SimpleIoProcessorPool(processorClass), true, null);
  }

  protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount)
  {
    this(sessionConfig, null, new SimpleIoProcessorPool(processorClass, processorCount), true, null);
  }

  protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Class<? extends IoProcessor<S>> processorClass, int processorCount, SelectorProvider selectorProvider)
  {
    this(sessionConfig, null, new SimpleIoProcessorPool(processorClass, processorCount, selectorProvider), true, selectorProvider);
  }

  protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, IoProcessor<S> processor)
  {
    this(sessionConfig, null, processor, false, null);
  }

  protected AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor)
  {
    this(sessionConfig, executor, processor, false, null);
  }

  private AbstractPollingIoAcceptor(IoSessionConfig sessionConfig, Executor executor, IoProcessor<S> processor, boolean createdProcessor, SelectorProvider selectorProvider)
  {
    super(sessionConfig, executor);

    this.lock = new Semaphore(1);

    this.registerQueue = new ConcurrentLinkedQueue();

    this.cancelQueue = new ConcurrentLinkedQueue();

    this.boundHandles = Collections.synchronizedMap(new HashMap());

    this.disposalFuture = new AbstractIoService.ServiceOperationFuture();

    this.acceptorRef = new AtomicReference();

    this.reuseAddress = false;

    this.backlog = 50;

    if (processor == null) {
      throw new IllegalArgumentException("processor");
    }

    this.processor = processor;
    this.createdProcessor = createdProcessor;
    try
    {
      init(selectorProvider);

      this.selectable = true;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeIoException("Failed to initialize.", e);
    } finally {
      if (!(this.selectable))
        try {
          destroy();
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
        }
    }
  }

  protected abstract void init()
    throws Exception;

  protected abstract void init(SelectorProvider paramSelectorProvider)
    throws Exception;

  protected abstract void destroy()
    throws Exception;

  protected abstract int select()
    throws Exception;

  protected abstract void wakeup();

  protected abstract Iterator<H> selectedHandles();

  protected abstract H open(SocketAddress paramSocketAddress)
    throws Exception;

  protected abstract SocketAddress localAddress(H paramH)
    throws Exception;

  protected abstract S accept(IoProcessor<S> paramIoProcessor, H paramH)
    throws Exception;

  protected abstract void close(H paramH)
    throws Exception;

  protected void dispose0()
    throws Exception
  {
    unbind();

    startupAcceptor();
    wakeup();
  }

  protected final Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses)
    throws Exception
  {
    AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);

    this.registerQueue.add(request);

    startupAcceptor();
    try
    {
      this.lock.acquire();

      Thread.sleep(10L);
      wakeup();
    } finally {
      this.lock.release();
    }

    request.awaitUninterruptibly();

    if (request.getException() != null) {
      throw request.getException();
    }

    Object newLocalAddresses = new HashSet();

    for (Object handle : this.boundHandles.values()) {
      ((Set)newLocalAddresses).add(localAddress((H) handle));
    }

    return ((Set<SocketAddress>)newLocalAddresses);
  }

  private void startupAcceptor()
    throws InterruptedException
  {
    if (!(this.selectable)) {
      this.registerQueue.clear();
      this.cancelQueue.clear();
    }

    Acceptor acceptor = (Acceptor)this.acceptorRef.get();

    if (acceptor == null) {
      this.lock.acquire();
      acceptor = new Acceptor();

      if (this.acceptorRef.compareAndSet(null, acceptor))
        executeWorker(acceptor);
      else
        this.lock.release();
    }
  }

  protected final void unbind0(List<? extends SocketAddress> localAddresses)
    throws Exception
  {
    AbstractIoAcceptor.AcceptorOperationFuture future = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);

    this.cancelQueue.add(future);
    startupAcceptor();
    wakeup();

    future.awaitUninterruptibly();
    if (future.getException() != null)
      throw future.getException();
  }

  private int registerHandles()
  {
    while (true)
    {
      AbstractIoAcceptor.AcceptorOperationFuture future = (AbstractIoAcceptor.AcceptorOperationFuture)this.registerQueue.poll();

      if (future == null) {
        return 0;
      }

      Map newHandles = new ConcurrentHashMap();
      List localAddresses = future.getLocalAddresses();
      Iterator localIterator2;
      try
      {
    	for (Iterator iterator = localAddresses.iterator(); iterator.hasNext();) {
			SocketAddress a = (SocketAddress) iterator.next();
          Object handle = open(a);
          newHandles.put(localAddress((H) handle), handle);
        }

        this.boundHandles.putAll(newHandles);

        future.setDone();
        Object handle;
        return newHandles.size();
      }
      catch (Exception e) {
        future.setException(e);
      }
      finally
      {
        if (future.getException() != null) {
          for (Object handle : newHandles.values()) {
            try {
              close((H) handle);
            } catch (Exception e) {
              ExceptionMonitor.getInstance().exceptionCaught(e);
            }

          }

          wakeup();
        }
      }
    }
  }

  private int unregisterHandles()
  {
    int cancelledHandles = 0;
    while (true) {
      AbstractIoAcceptor.AcceptorOperationFuture future = (AbstractIoAcceptor.AcceptorOperationFuture)this.cancelQueue.poll();
      if (future == null)
      {
        break;
      }

      for (SocketAddress a : future.getLocalAddresses()) {
        Object handle = this.boundHandles.remove(a);

        if (handle == null) {
          continue;
        }
        try
        {
          close((H) handle);
          wakeup();
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
        } finally {
          ++cancelledHandles;
        }
      }

      future.setDone();
    }

    return cancelledHandles;
  }

  public final IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress)
  {
    throw new UnsupportedOperationException();
  }

  public int getBacklog()
  {
    return this.backlog;
  }

  public void setBacklog(int backlog)
  {
    synchronized (this.bindLock) {
      if (isActive()) {
        throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
      }

      this.backlog = backlog;
    }
  }

  public boolean isReuseAddress()
  {
    return this.reuseAddress;
  }

  public void setReuseAddress(boolean reuseAddress)
  {
    synchronized (this.bindLock) {
      if (isActive()) {
        throw new IllegalStateException("backlog can't be set while the acceptor is bound.");
      }

      this.reuseAddress = reuseAddress;
    }
  }

  public SocketSessionConfig getSessionConfig()
  {
    return ((SocketSessionConfig)this.sessionConfig);
  }

  private class Acceptor
    implements Runnable
  {
    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: getstatic 22	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:$assertionsDisabled	Z
      //   3: ifne +25 -> 28
      //   6: aload_0
      //   7: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   10: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   13: invokevirtual 40	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
      //   16: aload_0
      //   17: if_acmpeq +11 -> 28
      //   20: new 46	java/lang/AssertionError
      //   23: dup
      //   24: invokespecial 48	java/lang/AssertionError:<init>	()V
      //   27: athrow
      //   28: iconst_0
      //   29: istore_1
      //   30: aload_0
      //   31: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   34: invokestatic 49	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$2	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/Semaphore;
      //   37: invokevirtual 53	java/util/concurrent/Semaphore:release	()V
      //   40: goto +229 -> 269
      //   43: aload_0
      //   44: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   47: invokevirtual 58	org/apache/mina/core/polling/AbstractPollingIoAcceptor:select	()I
      //   50: istore_2
      //   51: iload_1
      //   52: aload_0
      //   53: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   56: invokestatic 62	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$4	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)I
      //   59: iadd
      //   60: istore_1
      //   61: iload_1
      //   62: ifne +143 -> 205
      //   65: aload_0
      //   66: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   69: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   72: aconst_null
      //   73: invokevirtual 66	java/util/concurrent/atomic/AtomicReference:set	(Ljava/lang/Object;)V
      //   76: aload_0
      //   77: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   80: invokestatic 70	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$5	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/Queue;
      //   83: invokeinterface 74 1 0
      //   88: ifeq +46 -> 134
      //   91: aload_0
      //   92: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   95: invokestatic 79	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$6	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/Queue;
      //   98: invokeinterface 74 1 0
      //   103: ifeq +31 -> 134
      //   106: getstatic 22	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:$assertionsDisabled	Z
      //   109: ifne +170 -> 279
      //   112: aload_0
      //   113: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   116: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   119: invokevirtual 40	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
      //   122: aload_0
      //   123: if_acmpne +156 -> 279
      //   126: new 46	java/lang/AssertionError
      //   129: dup
      //   130: invokespecial 48	java/lang/AssertionError:<init>	()V
      //   133: athrow
      //   134: aload_0
      //   135: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   138: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   141: aconst_null
      //   142: aload_0
      //   143: invokevirtual 82	java/util/concurrent/atomic/AtomicReference:compareAndSet	(Ljava/lang/Object;Ljava/lang/Object;)Z
      //   146: ifne +31 -> 177
      //   149: getstatic 22	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:$assertionsDisabled	Z
      //   152: ifne +127 -> 279
      //   155: aload_0
      //   156: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   159: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   162: invokevirtual 40	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
      //   165: aload_0
      //   166: if_acmpne +113 -> 279
      //   169: new 46	java/lang/AssertionError
      //   172: dup
      //   173: invokespecial 48	java/lang/AssertionError:<init>	()V
      //   176: athrow
      //   177: getstatic 22	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:$assertionsDisabled	Z
      //   180: ifne +25 -> 205
      //   183: aload_0
      //   184: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   187: invokestatic 36	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$1	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/util/concurrent/atomic/AtomicReference;
      //   190: invokevirtual 40	java/util/concurrent/atomic/AtomicReference:get	()Ljava/lang/Object;
      //   193: aload_0
      //   194: if_acmpeq +11 -> 205
      //   197: new 46	java/lang/AssertionError
      //   200: dup
      //   201: invokespecial 48	java/lang/AssertionError:<init>	()V
      //   204: athrow
      //   205: iload_2
      //   206: ifle +14 -> 220
      //   209: aload_0
      //   210: aload_0
      //   211: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   214: invokevirtual 86	org/apache/mina/core/polling/AbstractPollingIoAcceptor:selectedHandles	()Ljava/util/Iterator;
      //   217: invokespecial 90	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:processHandles	(Ljava/util/Iterator;)V
      //   220: iload_1
      //   221: aload_0
      //   222: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   225: invokestatic 94	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$7	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)I
      //   228: isub
      //   229: istore_1
      //   230: goto +39 -> 269
      //   233: astore_2
      //   234: invokestatic 97	org/apache/mina/util/ExceptionMonitor:getInstance	()Lorg/apache/mina/util/ExceptionMonitor;
      //   237: aload_2
      //   238: invokevirtual 103	org/apache/mina/util/ExceptionMonitor:exceptionCaught	(Ljava/lang/Throwable;)V
      //   241: goto +38 -> 279
      //   244: astore_2
      //   245: invokestatic 97	org/apache/mina/util/ExceptionMonitor:getInstance	()Lorg/apache/mina/util/ExceptionMonitor;
      //   248: aload_2
      //   249: invokevirtual 103	org/apache/mina/util/ExceptionMonitor:exceptionCaught	(Ljava/lang/Throwable;)V
      //   252: ldc2_w 107
      //   255: invokestatic 109	java/lang/Thread:sleep	(J)V
      //   258: goto +11 -> 269
      //   261: astore_3
      //   262: invokestatic 97	org/apache/mina/util/ExceptionMonitor:getInstance	()Lorg/apache/mina/util/ExceptionMonitor;
      //   265: aload_3
      //   266: invokevirtual 103	org/apache/mina/util/ExceptionMonitor:exceptionCaught	(Ljava/lang/Throwable;)V
      //   269: aload_0
      //   270: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   273: invokestatic 115	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$3	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Z
      //   276: ifne -233 -> 43
      //   279: aload_0
      //   280: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   283: invokestatic 115	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$3	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Z
      //   286: ifeq +211 -> 497
      //   289: aload_0
      //   290: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   293: invokevirtual 119	org/apache/mina/core/polling/AbstractPollingIoAcceptor:isDisposing	()Z
      //   296: ifeq +201 -> 497
      //   299: aload_0
      //   300: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   303: iconst_0
      //   304: invokestatic 122	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$8	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;Z)V
      //   307: aload_0
      //   308: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   311: invokestatic 126	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$11	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Z
      //   314: ifeq +102 -> 416
      //   317: aload_0
      //   318: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   321: invokestatic 129	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$12	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/IoProcessor;
      //   324: invokeinterface 133 1 0
      //   329: goto +87 -> 416
      //   332: astore_2
      //   333: aload_0
      //   334: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   337: invokestatic 138	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$10	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/lang/Object;
      //   340: dup
      //   341: astore_3
      //   342: monitorenter
      //   343: aload_0
      //   344: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   347: invokevirtual 119	org/apache/mina/core/polling/AbstractPollingIoAcceptor:isDisposing	()Z
      //   350: ifeq +10 -> 360
      //   353: aload_0
      //   354: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   357: invokevirtual 142	org/apache/mina/core/polling/AbstractPollingIoAcceptor:destroy	()V
      //   360: aload_3
      //   361: monitorexit
      //   362: goto +42 -> 404
      //   365: aload_3
      //   366: monitorexit
      //   367: athrow
      //   368: astore_3
      //   369: invokestatic 97	org/apache/mina/util/ExceptionMonitor:getInstance	()Lorg/apache/mina/util/ExceptionMonitor;
      //   372: aload_3
      //   373: invokevirtual 103	org/apache/mina/util/ExceptionMonitor:exceptionCaught	(Ljava/lang/Throwable;)V
      //   376: aload_0
      //   377: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   380: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   383: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   386: goto +28 -> 414
      //   389: astore 4
      //   391: aload_0
      //   392: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   395: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   398: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   401: aload 4
      //   403: athrow
      //   404: aload_0
      //   405: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   408: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   411: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   414: aload_2
      //   415: athrow
      //   416: aload_0
      //   417: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   420: invokestatic 138	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$10	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Ljava/lang/Object;
      //   423: dup
      //   424: astore_3
      //   425: monitorenter
      //   426: aload_0
      //   427: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   430: invokevirtual 119	org/apache/mina/core/polling/AbstractPollingIoAcceptor:isDisposing	()Z
      //   433: ifeq +10 -> 443
      //   436: aload_0
      //   437: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   440: invokevirtual 142	org/apache/mina/core/polling/AbstractPollingIoAcceptor:destroy	()V
      //   443: aload_3
      //   444: monitorexit
      //   445: goto +42 -> 487
      //   448: aload_3
      //   449: monitorexit
      //   450: athrow
      //   451: astore_3
      //   452: invokestatic 97	org/apache/mina/util/ExceptionMonitor:getInstance	()Lorg/apache/mina/util/ExceptionMonitor;
      //   455: aload_3
      //   456: invokevirtual 103	org/apache/mina/util/ExceptionMonitor:exceptionCaught	(Ljava/lang/Throwable;)V
      //   459: aload_0
      //   460: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   463: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   466: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   469: goto +28 -> 497
      //   472: astore 4
      //   474: aload_0
      //   475: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   478: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   481: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   484: aload 4
      //   486: athrow
      //   487: aload_0
      //   488: getfield 29	org/apache/mina/core/polling/AbstractPollingIoAcceptor$Acceptor:this$0	Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;
      //   491: invokestatic 145	org/apache/mina/core/polling/AbstractPollingIoAcceptor:access$9	(Lorg/apache/mina/core/polling/AbstractPollingIoAcceptor;)Lorg/apache/mina/core/service/AbstractIoService$ServiceOperationFuture;
      //   494: invokevirtual 149	org/apache/mina/core/service/AbstractIoService$ServiceOperationFuture:setDone	()V
      //   497: return
      //
      // Exception table:
      //   from	to	target	type
      //   43	230	233	java/nio/channels/ClosedSelectorException
      //   43	230	244	java/lang/Exception
      //   252	258	261	java/lang/InterruptedException
      //   307	332	332	finally
      //   343	362	365	finally
      //   365	367	365	finally
      //   333	368	368	java/lang/Exception
      //   333	376	389	finally
      //   426	445	448	finally
      //   448	450	448	finally
      //   416	451	451	java/lang/Exception
      //   416	459	472	finally
    }

    private void processHandles(Iterator<H> handles)
      throws Exception
    {
      while (handles.hasNext()) {
        Object handle = handles.next();
        handles.remove();

        AbstractIoSession session = AbstractPollingIoAcceptor.this.accept(AbstractPollingIoAcceptor.this.processor, (H) handle);

        if (session == null) {
          continue;
        }

        AbstractPollingIoAcceptor.this.initSession(session, null, null);

        session.getProcessor().add(session);
      }
    }
  }
}