/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.proxy.event;

import java.util.LinkedList;
import java.util.Queue;
import org.apache.mina.proxy.ProxyConnector;
import org.apache.mina.proxy.ProxyLogicHandler;
import org.apache.mina.proxy.handlers.socks.SocksProxyRequest;
import org.apache.mina.proxy.session.ProxyIoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IoSessionEventQueue
{
  private static final Logger logger = LoggerFactory.getLogger(IoSessionEventQueue.class);
  private ProxyIoSession proxyIoSession;
  private Queue<IoSessionEvent> sessionEventsQueue = new LinkedList();

  public IoSessionEventQueue(ProxyIoSession proxyIoSession) {
    this.proxyIoSession = proxyIoSession;
  }

  private void discardSessionQueueEvents()
  {
    synchronized (this.sessionEventsQueue)
    {
      this.sessionEventsQueue.clear();
      logger.debug("Event queue CLEARED");
    }
  }

  public void enqueueEventIfNecessary(IoSessionEvent evt)
  {
    logger.debug("??? >> Enqueue {}", evt);

    if (this.proxyIoSession.getRequest() instanceof SocksProxyRequest)
    {
      evt.deliverEvent();
      return;
    }

    if (this.proxyIoSession.getHandler().isHandshakeComplete()) {
      evt.deliverEvent();
    }
    else if (evt.getType() == IoSessionEventType.CLOSED) {
      if (this.proxyIoSession.isAuthenticationFailed()) {
        this.proxyIoSession.getConnector().cancelConnectFuture();
        discardSessionQueueEvents();
        evt.deliverEvent();
      } else {
        discardSessionQueueEvents();
      }
    } else if (evt.getType() == IoSessionEventType.OPENED)
    {
      enqueueSessionEvent(evt);
      evt.deliverEvent();
    } else {
      enqueueSessionEvent(evt);
    }
  }

  public void flushPendingSessionEvents()
    throws Exception
  {
    synchronized (this.sessionEventsQueue)
    {
      IoSessionEvent evt;
      while ((evt = (IoSessionEvent)this.sessionEventsQueue.poll()) != null)
      {
        logger.debug(" Flushing buffered event: {}", evt);
        evt.deliverEvent();
      }
    }
  }

  private void enqueueSessionEvent(IoSessionEvent evt)
  {
    synchronized (this.sessionEventsQueue) {
      logger.debug("Enqueuing event: {}", evt);
      this.sessionEventsQueue.offer(evt);
    }
  }
}