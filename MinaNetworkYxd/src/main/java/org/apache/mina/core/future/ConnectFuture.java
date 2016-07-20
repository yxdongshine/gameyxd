/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.future;

import org.apache.mina.core.session.IoSession;

public abstract interface ConnectFuture extends IoFuture
{
  public abstract IoSession getSession();

  public abstract Throwable getException();

  public abstract boolean isConnected();

  public abstract boolean isCanceled();

  public abstract void setSession(IoSession paramIoSession);

  public abstract void setException(Throwable paramThrowable);

  public abstract void cancel();

  public abstract ConnectFuture await()
    throws InterruptedException;

  public abstract ConnectFuture awaitUninterruptibly();

  public abstract ConnectFuture addListener(IoFutureListener<?> paramIoFutureListener);

  public abstract ConnectFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}