/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.handler;

import com.loncent.protocol.BaseMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;
import java.io.IOException;
import java.util.Map;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaHandlerAdapter extends IoHandlerAdapter
{
  private static final Logger log = LoggerFactory.getLogger(MinaHandlerAdapter.class);
  protected Map<String, ProxyXmlBean> _proxyList;
  private boolean _isLongConn = false;
  protected INetApdapterLisener netDispatch;

  public MinaHandlerAdapter(Map<String, ProxyXmlBean> proxyList)
  {
    this._proxyList = proxyList;
    this._isLongConn = false;
  }

  public MinaHandlerAdapter(Map<String, ProxyXmlBean> proxyList, Boolean isLongConn) {
    this._proxyList = proxyList;
    this._isLongConn = isLongConn.booleanValue();
  }

  public void setProxyDispatch(INetApdapterLisener net)
  {
    if (net != null)
      this.netDispatch = net;
  }

  public void sessionCreated(IoSession session) throws Exception {
    log.info("sessionCreated......" + session.getRemoteAddress());
  }

  public void sessionOpened(IoSession session) throws Exception {
    log.info("sessionOpened......" + session.getId());

    this.netDispatch.sessionOpen(session);
    log.info(System.currentTimeMillis() + " : sessionCreated : " + session + " : " + session.getCreationTime());
  }

  public void sessionClosed(IoSession session) throws Exception {
    log.info(session.getLocalAddress() + "[close]");
    IConnect net = (IConnect)session.getAttribute("netsession");
    if (net != null) {
      session.removeAttribute("netsession");
      this.netDispatch.sessionClose(net);
      net.close();
    }
  }

  public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
    if ((!(this._isLongConn)) && (session != null)) {
      Integer in = (Integer)session.getAttribute("idle");
      int idle = 0;
      if (in != null) {
        idle = ((Integer)session.getAttribute("idle")).intValue();
        if (idle++ > NetConst.SYSTEM_SOCKET_MAX_IDLE_TIMES) {
          log.info("�ﵽϵͳ������ô���");
          IConnect net = (IConnect)session.getAttribute("netsession");
          if (net != null) {
            this.netDispatch.sessionTimeOut(net);
          }
          session.close(true);
        } else {
          if (idle == 5) {
            idle = 1;
          }
          session.setAttribute("idle", Integer.valueOf(idle));
        }
      } else {
        session.setAttribute("idle", Integer.valueOf(idle));
      }
    }
  }

  public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
    IConnect net = (IConnect)session.getAttribute("netsession");

    if (net == null) {
      return;
    }

    if ((cause.getMessage() == null) || ((!(cause.getMessage().contains("Զ������ǿ�ȹر���һ�����е�����"))) && (!(cause.getMessage().contains("Connection"))) && (!(cause.getClass().equals(IOException.class)))))
    {
      log.error("��Ϣ����----", cause);
    }

    net.close();
  }

  public void messageReceived(IoSession session, Object message)
  {
    BaseMessage.MinaMessage msg = (BaseMessage.MinaMessage)message;
    try
    {
      IConnect net = (IConnect)session.getAttribute("netsession");
      if (net == null) {
        log.error("û��connect::::" + Integer.toHexString(msg.getCommand()));
        message = null;
        return;
      }

      ProxyXmlBean xml = (ProxyXmlBean)this._proxyList.get(Integer.valueOf(msg.getCommand()));
      if (((XMLNetApdapterLisener)this.netDispatch != null) && (xml != null))
      {
        this.netDispatch.doProxy(net, msg, xml);
      } else {
        log.error("������::::" + Integer.toHexString(msg.getCommand()));
        this.netDispatch.doProxy(net, msg);
      }

      message = null;
    } catch (Exception e) {
      log.error("������::::" + Integer.toHexString(msg.getCommand()), e);
    }
  }

  public void messageSent(IoSession session, Object message)
    throws Exception
  {
  }
}