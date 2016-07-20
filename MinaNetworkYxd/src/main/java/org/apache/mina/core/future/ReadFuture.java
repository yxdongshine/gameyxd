/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.future;

public abstract interface ReadFuture extends IoFuture
{
  public abstract Object getMessage();

  public abstract boolean isRead();

  public abstract boolean isClosed();

  public abstract Throwable getException();

  public abstract void setRead(Object paramObject);

  public abstract void setClosed();

  public abstract void setException(Throwable paramThrowable);

  public abstract ReadFuture await()
    throws InterruptedException;

  public abstract ReadFuture awaitUninterruptibly();

  public abstract ReadFuture addListener(IoFutureListener<?> paramIoFutureListener);

  public abstract ReadFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}