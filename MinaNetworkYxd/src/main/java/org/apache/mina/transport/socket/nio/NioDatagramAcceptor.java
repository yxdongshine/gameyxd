/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket.nio;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.service.AbstractIoAcceptor;
import org.apache.mina.core.service.AbstractIoAcceptor.AcceptorOperationFuture;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.service.AbstractIoService.ServiceOperationFuture;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoServiceListenerSupport;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.ExpiringSessionRecycler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.core.session.IoSessionRecycler;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.apache.mina.transport.socket.DatagramAcceptor;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.DefaultDatagramSessionConfig;
import org.apache.mina.util.ExceptionMonitor;

public final class NioDatagramAcceptor extends AbstractIoAcceptor
  implements DatagramAcceptor, IoProcessor<NioSession>
{
  private static final IoSessionRecycler DEFAULT_RECYCLER = new ExpiringSessionRecycler();
  private static final long SELECT_TIMEOUT = 1000L;
  private final Semaphore lock;
  private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> registerQueue;
  private final Queue<AbstractIoAcceptor.AcceptorOperationFuture> cancelQueue;
  private final Queue<NioSession> flushingSessions;
  private final Map<SocketAddress, DatagramChannel> boundHandles;
  private IoSessionRecycler sessionRecycler;
  private final AbstractIoService.ServiceOperationFuture disposalFuture;
  private volatile boolean selectable;
  private Acceptor acceptor;
  private long lastIdleCheckTime;
  private volatile Selector selector;

  public NioDatagramAcceptor()
  {
    this(new DefaultDatagramSessionConfig(), null);
  }

  public NioDatagramAcceptor(Executor executor)
  {
    this(new DefaultDatagramSessionConfig(), executor);
  }

  private NioDatagramAcceptor(IoSessionConfig sessionConfig, Executor executor)
  {
    super(sessionConfig, executor);

    this.lock = new Semaphore(1);

    this.registerQueue = new ConcurrentLinkedQueue();

    this.cancelQueue = new ConcurrentLinkedQueue();

    this.flushingSessions = new ConcurrentLinkedQueue();

    this.boundHandles = Collections.synchronizedMap(new HashMap());

    this.sessionRecycler = DEFAULT_RECYCLER;

    this.disposalFuture = new AbstractIoService.ServiceOperationFuture();
    try
    {
      init();
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

  private int registerHandles()
  {
    while (true)
    {
      AbstractIoAcceptor.AcceptorOperationFuture req = (AbstractIoAcceptor.AcceptorOperationFuture)this.registerQueue.poll();

      if (req == null) {
        break;
      }

      Map newHandles = new HashMap();
      List localAddresses = req.getLocalAddresses();
      Iterator localIterator2;
      try {
    	for (Iterator iterator = localAddresses.iterator(); iterator.hasNext();) {
			SocketAddress socketAddress = (SocketAddress) iterator.next();
			
          DatagramChannel handle = open(socketAddress);
          newHandles.put(localAddress(handle), handle);
        }

        this.boundHandles.putAll(newHandles);

        getListeners().fireServiceActivated();
        req.setDone();
        DatagramChannel handle;
        return newHandles.size();
      } catch (Exception e) {
        req.setException(e);
      }
      finally
      {
        DatagramChannel handle;
        if (req.getException() != null) {
          for (Iterator iterator = newHandles.values().iterator(); iterator.hasNext();) {
			 handle = (DatagramChannel) iterator.next();
            try {
              close(handle);
            } catch (Exception e) {
              ExceptionMonitor.getInstance().exceptionCaught(e);
            }
          }

          wakeup();
        }
      }
    }

    return 0;
  }

  private void processReadySessions(Set<SelectionKey> handles) {
    Iterator iterator = handles.iterator();

    while (iterator.hasNext()) {
      SelectionKey key = (SelectionKey)iterator.next();
      DatagramChannel handle = (DatagramChannel)key.channel();
      iterator.remove();
      try
      {
        if ((key != null) && (key.isValid()) && (key.isReadable())) {
          readHandle(handle);
        }

        if ((key != null) && (key.isValid()) && (key.isWritable()))
          for (IoSession session : getManagedSessions().values())
            scheduleFlush((NioSession)session);
      }
      catch (Exception e)
      {
        ExceptionMonitor.getInstance().exceptionCaught(e);
      }
    }
  }

  private boolean scheduleFlush(NioSession session)
  {
    if (session.setScheduledForFlush(true)) {
      this.flushingSessions.add(session);
      return true;
    }
    return false;
  }

  private void readHandle(DatagramChannel handle) throws Exception
  {
    IoBuffer readBuf = IoBuffer.allocate(getSessionConfig().getReadBufferSize());

    SocketAddress remoteAddress = receive(handle, readBuf);

    if (remoteAddress != null) {
      IoSession session = newSessionWithoutLock(remoteAddress, localAddress(handle));

      readBuf.flip();

      session.getFilterChain().fireMessageReceived(readBuf);
    }
  }

  private IoSession newSessionWithoutLock(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception {
    DatagramChannel handle = (DatagramChannel)this.boundHandles.get(localAddress);

    if (handle == null) {
      throw new IllegalArgumentException("Unknown local address: " + localAddress);
    }

    synchronized (this.sessionRecycler) {
      IoSession session = this.sessionRecycler.recycle(remoteAddress);

      if (session != null) {
        return session;
      }

      NioSession newSession = newSession(this, handle, remoteAddress);
      getSessionRecycler().put(newSession);
      session = newSession;
    }
    IoSession session = null;
    initSession(session, null, null);
    try
    {
      getFilterChainBuilder().buildFilterChain(session.getFilterChain());
      getListeners().fireSessionCreated(session);
    } catch (Exception e) {
      ExceptionMonitor.getInstance().exceptionCaught(e);
    }

    return session;
  }

  private void flushSessions(long currentTime) {
    while (true) {
      NioSession session = (NioSession)this.flushingSessions.poll();

      if (session == null)
      {
        return;
      }

      session.unscheduledForFlush();
      try
      {
        boolean flushedAll = flush(session, currentTime);

        if ((flushedAll) && (!(session.getWriteRequestQueue().isEmpty(session))) && (!(session.isScheduledForFlush())));
        scheduleFlush(session);
      }
      catch (Exception e) {
        session.getFilterChain().fireExceptionCaught(e);
      }
    }
  }

  private boolean flush(NioSession session, long currentTime) throws Exception {
    WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
    int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);

    int writtenBytes = 0;
    while (true) {
      WriteRequest req;
      try { IoBuffer buf;
        while (true) { req = session.getCurrentWriteRequest();

          if (req == null) {
            req = writeRequestQueue.poll(session);

            if (req == null) {
              setInterestedInWrite(session, false);
            }

            session.setCurrentWriteRequest(req);
          }

          buf = (IoBuffer)req.getMessage();

          if (buf.remaining() != 0)
            break;
          session.setCurrentWriteRequest(null);
          buf.reset();
          session.getFilterChain().fireMessageSent(req);
        }

        SocketAddress destination = req.getDestination();

        if (destination == null) {
          destination = session.getRemoteAddress();
        }

        int localWrittenBytes = send(session, buf, destination);

        if ((localWrittenBytes == 0) || (writtenBytes >= maxWrittenBytes))
        {
          setInterestedInWrite(session, true);
          return false;
        }
        setInterestedInWrite(session, false);

        session.setCurrentWriteRequest(null);
        writtenBytes += localWrittenBytes;
        buf.reset();
      }
      finally
      {
        session.increaseWrittenBytes(writtenBytes, currentTime); }  }
  }

  private int unregisterHandles() {
    int nHandles = 0;
    while (true)
    {
      AbstractIoAcceptor.AcceptorOperationFuture request = (AbstractIoAcceptor.AcceptorOperationFuture)this.cancelQueue.poll();
      if (request == null)
      {
        break;
      }

      for (SocketAddress socketAddress : request.getLocalAddresses()) {
        DatagramChannel handle = (DatagramChannel)this.boundHandles.remove(socketAddress);

        if (handle == null) {
          continue;
        }
        try
        {
          close(handle);
          wakeup();
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
        } finally {
          ++nHandles;
        }
      }

      request.setDone();
    }

    return nHandles;
  }

  private void notifyIdleSessions(long currentTime)
  {
    if (currentTime - this.lastIdleCheckTime >= 1000L) {
      this.lastIdleCheckTime = currentTime;
      AbstractIoSession.notifyIdleness(getListeners().getManagedSessions().values().iterator(), currentTime);
    }
  }

  private void startupAcceptor()
    throws InterruptedException
  {
    if (!(this.selectable)) {
      this.registerQueue.clear();
      this.cancelQueue.clear();
      this.flushingSessions.clear();
    }

    this.lock.acquire();

    if (this.acceptor == null) {
      this.acceptor = new Acceptor();
      executeWorker(this.acceptor);
    } else {
      this.lock.release();
    }
  }

  protected void init() throws Exception {
    this.selector = Selector.open();
  }

  public void add(NioSession session)
  {
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

    for (DatagramChannel handle : this.boundHandles.values()) {
      ((Set)newLocalAddresses).add(localAddress(handle));
    }

    return ((Set<SocketAddress>)newLocalAddresses);
  }

  protected void close(DatagramChannel handle) throws Exception {
    SelectionKey key = handle.keyFor(this.selector);

    if (key != null) {
      key.cancel();
    }

    handle.disconnect();
    handle.close();
  }

  protected void destroy() throws Exception {
    if (this.selector != null)
      this.selector.close();
  }

  protected void dispose0()
    throws Exception
  {
    unbind();
    startupAcceptor();
    wakeup();
  }

  public void flush(NioSession session)
  {
    if (scheduleFlush(session))
      wakeup();
  }

  public InetSocketAddress getDefaultLocalAddress()
  {
    return ((InetSocketAddress)super.getDefaultLocalAddress());
  }

  public InetSocketAddress getLocalAddress()
  {
    return ((InetSocketAddress)super.getLocalAddress());
  }

  public DatagramSessionConfig getSessionConfig()
  {
    return ((DatagramSessionConfig)this.sessionConfig);
  }

  public final IoSessionRecycler getSessionRecycler() {
    return this.sessionRecycler;
  }

  public TransportMetadata getTransportMetadata() {
    return NioDatagramSession.METADATA;
  }

  protected boolean isReadable(DatagramChannel handle) {
    SelectionKey key = handle.keyFor(this.selector);

    if ((key == null) || (!(key.isValid()))) {
      return false;
    }

    return key.isReadable();
  }

  protected boolean isWritable(DatagramChannel handle) {
    SelectionKey key = handle.keyFor(this.selector);

    if ((key == null) || (!(key.isValid()))) {
      return false;
    }

    return key.isWritable();
  }

  protected SocketAddress localAddress(DatagramChannel handle) throws Exception {
    InetSocketAddress inetSocketAddress = (InetSocketAddress)handle.socket().getLocalSocketAddress();
    InetAddress inetAddress = inetSocketAddress.getAddress();

    if ((inetAddress instanceof Inet6Address) && (((Inet6Address)inetAddress).isIPv4CompatibleAddress()))
    {
      byte[] ipV6Address = ((Inet6Address)inetAddress).getAddress();
      byte[] ipV4Address = new byte[4];

      for (int i = 0; i < 4; ++i) {
        ipV4Address[i] = ipV6Address[(12 + i)];
      }

      InetAddress inet4Adress = Inet4Address.getByAddress(ipV4Address);
      return new InetSocketAddress(inet4Adress, inetSocketAddress.getPort());
    }
    return inetSocketAddress;
  }

  protected NioSession newSession(IoProcessor<NioSession> processor, DatagramChannel handle, SocketAddress remoteAddress)
  {
    SelectionKey key = handle.keyFor(this.selector);

    if ((key == null) || (!(key.isValid()))) {
      return null;
    }

    NioDatagramSession newSession = new NioDatagramSession(this, handle, processor, remoteAddress);
    newSession.setSelectionKey(key);

    return newSession;
  }

  public final IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress)
  {
    if (isDisposing()) {
      throw new IllegalStateException("Already disposed.");
    }

    if (remoteAddress == null) {
      throw new IllegalArgumentException("remoteAddress");
    }

    synchronized (this.bindLock) {
      if (!(isActive())) {
        throw new IllegalStateException("Can't create a session from a unbound service.");
      }
      try
      {
         return newSessionWithoutLock(remoteAddress, localAddress);
      } catch (RuntimeException e) {
        throw e;
      } catch (Error e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeIoException("Failed to create a session.", e);
      }
    }
  }

  protected DatagramChannel open(SocketAddress localAddress) throws Exception {
    DatagramChannel ch = DatagramChannel.open();
    boolean success = false;
    try {
      new NioDatagramSessionConfig(ch).setAll(getSessionConfig());
      ch.configureBlocking(false);
      try
      {
        ch.socket().bind(localAddress);
      }
      catch (IOException ioe)
      {
        String newMessage = "Error while binding on " + localAddress + "\n" + "original message : " + ioe.getMessage();
        Exception e = new IOException(newMessage);
        e.initCause(ioe.getCause());

        ch.close();

        throw e;
      }

      ch.register(this.selector, 1);
      success = true;
    } finally {
      if (!(success)) {
        close(ch);
      }
    }

    return ch;
  }

  protected SocketAddress receive(DatagramChannel handle, IoBuffer buffer) throws Exception {
    return handle.receive(buffer.buf());
  }

  public void remove(NioSession session)
  {
    getSessionRecycler().remove(session);
    getListeners().fireSessionDestroyed(session);
  }

  protected int select() throws Exception {
    return this.selector.select();
  }

  protected int select(long timeout) throws Exception {
    return this.selector.select(timeout);
  }

  protected Set<SelectionKey> selectedHandles() {
    return this.selector.selectedKeys();
  }

  protected int send(NioSession session, IoBuffer buffer, SocketAddress remoteAddress) throws Exception {
    return ((DatagramChannel)session.getChannel()).send(buffer.buf(), remoteAddress);
  }

  public void setDefaultLocalAddress(InetSocketAddress localAddress) {
    setDefaultLocalAddress(localAddress);
  }

  protected void setInterestedInWrite(NioSession session, boolean isInterested) throws Exception {
    SelectionKey key = session.getSelectionKey();

    if (key == null) {
      return;
    }

    int newInterestOps = key.interestOps();

    if (isInterested)
      newInterestOps |= 4;
    else {
      newInterestOps &= -5;
    }

    key.interestOps(newInterestOps);
  }

  public final void setSessionRecycler(IoSessionRecycler sessionRecycler) {
    synchronized (this.bindLock) {
      if (isActive()) {
        throw new IllegalStateException("sessionRecycler can't be set while the acceptor is bound.");
      }

      if (sessionRecycler == null) {
        sessionRecycler = DEFAULT_RECYCLER;
      }

      this.sessionRecycler = sessionRecycler;
    }
  }

  protected final void unbind0(List<? extends SocketAddress> localAddresses)
    throws Exception
  {
    AbstractIoAcceptor.AcceptorOperationFuture request = new AbstractIoAcceptor.AcceptorOperationFuture(localAddresses);

    this.cancelQueue.add(request);
    startupAcceptor();
    wakeup();

    request.awaitUninterruptibly();

    if (request.getException() != null)
      throw request.getException();
  }

  public void updateTrafficControl(NioSession session)
  {
    throw new UnsupportedOperationException();
  }

  protected void wakeup() {
    this.selector.wakeup();
  }

  public void write(NioSession session, WriteRequest writeRequest)
  {
    long currentTime = System.currentTimeMillis();
    WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();
    int maxWrittenBytes = session.getConfig().getMaxReadBufferSize() + (session.getConfig().getMaxReadBufferSize() >>> 1);

    int writtenBytes = 0;

    IoBuffer buf = (IoBuffer)writeRequest.getMessage();

    if (buf.remaining() == 0)
    {
      session.setCurrentWriteRequest(null);
      buf.reset();
      session.getFilterChain().fireMessageSent(writeRequest);
      return;
    }
    try
    {
      int localWrittenBytes;
      while (true) {
        if (writeRequest == null) {
          writeRequest = writeRequestQueue.poll(session);

          if (writeRequest == null) {
            setInterestedInWrite(session, false);
          }

          session.setCurrentWriteRequest(writeRequest);
        }

        buf = (IoBuffer)writeRequest.getMessage();

        if (buf.remaining() == 0)
        {
          session.setCurrentWriteRequest(null);
          buf.reset();
          session.getFilterChain().fireMessageSent(writeRequest);
        }

        SocketAddress destination = writeRequest.getDestination();

        if (destination == null) {
          destination = session.getRemoteAddress();
        }

        localWrittenBytes = send(session, buf, destination);

        if ((localWrittenBytes != 0) && (writtenBytes < maxWrittenBytes))
          break;
        setInterestedInWrite(session, true);

        session.getWriteRequestQueue().offer(session, writeRequest);
        scheduleFlush(session);
      }
      setInterestedInWrite(session, false);

      session.setCurrentWriteRequest(null);
      writtenBytes += localWrittenBytes;
      buf.reset();
      label291: session.getFilterChain().fireMessageSent(writeRequest);
    }
    catch (Exception e)
    {
      session.getFilterChain().fireExceptionCaught(e);
    } finally {
      session.increaseWrittenBytes(writtenBytes, currentTime);
    }
  }

  private class Acceptor
    implements Runnable
  {
    public void run()
    {
      int nHandles = 0;
      NioDatagramAcceptor.this.lastIdleCheckTime = System.currentTimeMillis();

      NioDatagramAcceptor.this.lock.release();

      while (NioDatagramAcceptor.this.selectable)
        try {
          int selected = NioDatagramAcceptor.this.select(1000L);

          nHandles += NioDatagramAcceptor.this.registerHandles();

          if (nHandles == 0) {
            try {
              NioDatagramAcceptor.this.lock.acquire();

              if ((!(NioDatagramAcceptor.this.registerQueue.isEmpty())) || (!(NioDatagramAcceptor.this.cancelQueue.isEmpty()))) 
              NioDatagramAcceptor.this.acceptor = null;

              label111: NioDatagramAcceptor.this.lock.release(); } finally { NioDatagramAcceptor.this.lock.release();
            }
          }

          if (selected > 0) {
            NioDatagramAcceptor.this.processReadySessions(NioDatagramAcceptor.this.selectedHandles());
          }

          long currentTime = System.currentTimeMillis();
          NioDatagramAcceptor.this.flushSessions(currentTime);
          nHandles -= NioDatagramAcceptor.this.unregisterHandles();

          NioDatagramAcceptor.this.notifyIdleSessions(currentTime);
        }
        catch (ClosedSelectorException cse) {
          ExceptionMonitor.getInstance().exceptionCaught(cse);
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
          try
          {
            Thread.sleep(1000L);
          }
          catch (InterruptedException localInterruptedException1)
          {
          }
        }
      if ((NioDatagramAcceptor.this.selectable) && (NioDatagramAcceptor.this.isDisposing())) {
        label224: NioDatagramAcceptor.this.selectable = false;
        try {
          NioDatagramAcceptor.this.destroy();
        } catch (Exception e) {
          ExceptionMonitor.getInstance().exceptionCaught(e);
        } finally {
          NioDatagramAcceptor.this.disposalFuture.setValue(Boolean.valueOf(true));
        }
      }
    }
  }
}