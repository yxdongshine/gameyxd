/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.vmpipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoServiceListenerSupport;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.apache.mina.core.write.WriteToClosedSessionException;

class VmPipeFilterChain extends DefaultIoFilterChain
{
  private final Queue<IoEvent> eventQueue = new ConcurrentLinkedQueue();

  private final IoProcessor<VmPipeSession> processor = new VmPipeIoProcessor();
  private volatile boolean flushEnabled;
  private volatile boolean sessionOpened;

  VmPipeFilterChain(AbstractIoSession session)
  {
    super(session);
  }

  IoProcessor<VmPipeSession> getProcessor() {
    return this.processor;
  }

  public void start() {
    this.flushEnabled = true;
    flushEvents();
    flushPendingDataQueues((VmPipeSession)getSession());
  }

  private void pushEvent(IoEvent e) {
    pushEvent(e, this.flushEnabled);
  }

  private void pushEvent(IoEvent e, boolean flushNow) {
    this.eventQueue.add(e);
    if (flushNow)
      flushEvents();
  }

  private void flushEvents()
  {
    IoEvent e;
    while ((e = (IoEvent)this.eventQueue.poll()) != null)
    {
      fireEvent(e);
    }
  }

  private void fireEvent(IoEvent e) {
    VmPipeSession session = (VmPipeSession)getSession();
    IoEventType type = e.getType();
    Object data = e.getParameter();

    if (type == IoEventType.MESSAGE_RECEIVED) {
      if ((this.sessionOpened) && (!(session.isReadSuspended())) && (session.getLock().tryLock()))
        try {
          if (session.isReadSuspended()) {
            session.receivedMessageQueue.add(data); 
          }
          label97: super.fireMessageReceived(data);
        }
        finally {
          session.getLock().unlock();
        }
      else
        session.receivedMessageQueue.add(data);
    }
    else if (type == IoEventType.WRITE) {
      super.fireFilterWrite((WriteRequest)data);
    } else if (type == IoEventType.MESSAGE_SENT) {
      super.fireMessageSent((WriteRequest)data);
    } else if (type == IoEventType.EXCEPTION_CAUGHT) {
      super.fireExceptionCaught((Throwable)data);
    } else if (type == IoEventType.SESSION_IDLE) {
      super.fireSessionIdle((IdleStatus)data);
    } else if (type == IoEventType.SESSION_OPENED) {
      super.fireSessionOpened();
      this.sessionOpened = true;
    } else if (type == IoEventType.SESSION_CREATED) {
      session.getLock().lock();
      try {
        super.fireSessionCreated();
      } finally {
        session.getLock().unlock();
      }
    } else if (type == IoEventType.SESSION_CLOSED) {
      flushPendingDataQueues(session);
      super.fireSessionClosed();
    } else if (type == IoEventType.CLOSE) {
      super.fireFilterClose();
    }
  }

  private static void flushPendingDataQueues(VmPipeSession s) {
    s.getProcessor().updateTrafficControl(s);
    s.getRemoteSession().getProcessor().updateTrafficControl(s);
  }

  public void fireFilterClose()
  {
    pushEvent(new IoEvent(IoEventType.CLOSE, getSession(), null));
  }

  public void fireFilterWrite(WriteRequest writeRequest)
  {
    pushEvent(new IoEvent(IoEventType.WRITE, getSession(), writeRequest));
  }

  public void fireExceptionCaught(Throwable cause)
  {
    pushEvent(new IoEvent(IoEventType.EXCEPTION_CAUGHT, getSession(), cause));
  }

  public void fireMessageSent(WriteRequest request)
  {
    pushEvent(new IoEvent(IoEventType.MESSAGE_SENT, getSession(), request));
  }

  public void fireSessionClosed()
  {
    pushEvent(new IoEvent(IoEventType.SESSION_CLOSED, getSession(), null));
  }

  public void fireSessionCreated()
  {
    pushEvent(new IoEvent(IoEventType.SESSION_CREATED, getSession(), null));
  }

  public void fireSessionIdle(IdleStatus status)
  {
    pushEvent(new IoEvent(IoEventType.SESSION_IDLE, getSession(), status));
  }

  public void fireSessionOpened()
  {
    pushEvent(new IoEvent(IoEventType.SESSION_OPENED, getSession(), null));
  }

  public void fireMessageReceived(Object message)
  {
    pushEvent(new IoEvent(IoEventType.MESSAGE_RECEIVED, getSession(), message));
  }

  private class VmPipeIoProcessor implements IoProcessor<VmPipeSession> {
    public void flush(VmPipeSession session) {
      WriteRequestQueue queue = session.getWriteRequestQueue0();
      if (!(session.isClosing())) {
        session.getLock().lock();
        try {
          if (queue.isEmpty(session)) {
            return;
          }

          long currentTime = System.currentTimeMillis();
          WriteRequest req = null;
          Object m = req.getMessage();
          VmPipeFilterChain.this.pushEvent(new IoEvent(IoEventType.MESSAGE_SENT, session, req), false);
          session.getRemoteSession().getFilterChain().fireMessageReceived(getMessageCopy(m));
          label134: if (m instanceof IoBuffer)
            session.increaseWrittenBytes0(((IoBuffer)m).remaining(), currentTime);
        }
        finally
        {
          WriteRequest req;
          if (VmPipeFilterChain.this.flushEnabled) {
            VmPipeFilterChain.this.flushEvents();
          }
          session.getLock().unlock();
        }
        if (VmPipeFilterChain.this.flushEnabled) {
          VmPipeFilterChain.this.flushEvents();
        }
        session.getLock().unlock();

      } else {
        List failedRequests = new ArrayList();
        WriteRequest req;
        while ((req = queue.poll(session)) != null)
        {
          failedRequests.add(req);
        }

        if (!(failedRequests.isEmpty())) {
          WriteToClosedSessionException cause = new WriteToClosedSessionException(failedRequests);
          for (Iterator iterator = failedRequests.iterator(); iterator.hasNext();) {
        	  WriteRequest r =  (WriteRequest) iterator.next();
              r.getFuture().setException(cause);
          }
          session.getFilterChain().fireExceptionCaught(cause);
        }
      }
    }

    public void write(VmPipeSession session, WriteRequest writeRequest)
    {
      WriteRequestQueue writeRequestQueue = session.getWriteRequestQueue();

      writeRequestQueue.offer(session, writeRequest);

      if (!(session.isWriteSuspended()))
        flush(session);
    }

    private Object getMessageCopy(Object message)
    {
      Object messageCopy = message;
      if (message instanceof IoBuffer) {
        IoBuffer rb = (IoBuffer)message;
        rb.mark();
        IoBuffer wb = IoBuffer.allocate(rb.remaining());
        wb.put(rb);
        wb.flip();
        rb.reset();
        messageCopy = wb;
      }
      return messageCopy;
    }

    public void remove(VmPipeSession session) {
      try {
        session.getLock().lock();
        if (session.getCloseFuture().isClosed()) return;
        session.getServiceListeners().fireSessionDestroyed(session);
        label41: session.getRemoteSession().close(true);
      }
      finally {
        session.getLock().unlock();
      }
    }

    public void add(VmPipeSession session)
    {
    }

    public void updateTrafficControl(VmPipeSession session) {
      if (!(session.isReadSuspended())) {
        List data = new ArrayList();
        session.receivedMessageQueue.drainTo(data);
        for (Iterator localIterator = data.iterator(); localIterator.hasNext(); ) { Object aData = localIterator.next();
          VmPipeFilterChain.this.fireMessageReceived(aData);
        }
      }

      if (!(session.isWriteSuspended()))
        flush(session);
    }

    public void dispose()
    {
    }

    public boolean isDisposed()
    {
      return false;
    }

    public boolean isDisposing() {
      return false;
    }
  }
}