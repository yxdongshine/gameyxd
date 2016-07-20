/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.handler;

import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.session.IConnect;
import org.apache.mina.core.session.IoSession;

public abstract interface INetApdapterLisener<T>
{
  public abstract void doProxy(IConnect paramIConnect, T paramT);

  public abstract void doProxy(IConnect paramIConnect, T paramT, ProxyXmlBean paramProxyXmlBean);

  public abstract void sessionClose(IConnect paramIConnect);

  public abstract void sessionOpen(IoSession paramIoSession);

  public abstract void haveNoProxyError(IConnect paramIConnect);

  public abstract void decodeProxyError(IConnect paramIConnect);

  public abstract void sessionTimeOut(IConnect paramIConnect);
}