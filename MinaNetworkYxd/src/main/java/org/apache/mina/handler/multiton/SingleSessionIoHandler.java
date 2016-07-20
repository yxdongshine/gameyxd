/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.multiton;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

@Deprecated
public abstract interface SingleSessionIoHandler
{
  public abstract void sessionCreated()
    throws Exception;

  public abstract void sessionOpened()
    throws Exception;

  public abstract void sessionClosed()
    throws Exception;

  public abstract void sessionIdle(IdleStatus paramIdleStatus)
    throws Exception;

  public abstract void exceptionCaught(Throwable paramThrowable)
    throws Exception;

  public abstract void inputClosed(IoSession paramIoSession);

  public abstract void messageReceived(Object paramObject)
    throws Exception;

  public abstract void messageSent(Object paramObject)
    throws Exception;
}