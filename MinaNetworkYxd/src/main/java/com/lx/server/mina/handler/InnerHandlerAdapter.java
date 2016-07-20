/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.handler;

import com.loncent.protocol.BaseMessage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.server.executor.AbstractWork;
import com.lx.server.executor.OrderedQueuePoolExecutor;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.session.IConnect;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerHandlerAdapter extends MinaHandlerAdapter
{
  private Logger log = LoggerFactory.getLogger(InnerHandlerAdapter.class);
  private OrderedQueuePoolExecutor queuePool = null;

  public InnerHandlerAdapter(Map<String, ProxyXmlBean> proxyList, OrderedQueuePoolExecutor queuePool)
  {
    super(proxyList);
    this.queuePool = queuePool;
  }

  public void sessionOpened(IoSession session)
    throws Exception
  {
    super.sessionOpened(session);

    this.log.error("�Ѵ�:::" + session);
  }

  public void messageReceived(IoSession session, Object message)
  {
    BaseMessage.InnerMessage msg = (BaseMessage.InnerMessage)message;
    try
    {
      IConnect net = (IConnect)session.getAttribute("netsession");
      if (net == null) {
        message = null;
        return;
      }

      ProxyXmlBean xml = (ProxyXmlBean)this._proxyList.get(Integer.valueOf(msg.getCommand()));
      if (((XMLNetApdapterLisener)this.netDispatch != null) && (xml != null))
      {
        if ((this.queuePool != null) && (msg.getClientSessionId() > 0L))
        {
          final IConnect cNet = net;
          final BaseMessage.InnerMessage im = msg;
          final ProxyXmlBean pxb = xml;

          this.queuePool.addTask(Long.valueOf(msg.getClientSessionId()), new AbstractWork(cNet, im, pxb)
          {
            public void doWork()
            {
              InnerHandlerAdapter.this.netDispatch.doProxy(cNet, im, pxb);
            }
          });
        }
        else {
          this.netDispatch.doProxy(net, msg, xml);
        }
      }
      else {
        this.log.error("������::::" + Integer.toHexString(msg.getCommand()));
        this.netDispatch.doProxy(net, msg);
      }

      message = null;
    } catch (Exception e) {
      this.log.error("������::::" + Integer.toHexString(msg.getCommand()), e);
    }
  }

  public void messageSent(IoSession session, Object message)
    throws Exception
  {
  }

  public void sessionClosed(IoSession session) throws Exception
  {
    super.sessionClosed(session);
  }

  public OrderedQueuePoolExecutor getQueuePool() {
    return this.queuePool;
  }

  public void setQueuePool(OrderedQueuePoolExecutor queuePool) {
    this.queuePool = queuePool;
  }
}