/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.future;

public abstract interface CloseFuture extends IoFuture
{
  public abstract boolean isClosed();

  public abstract void setClosed();

  public abstract CloseFuture await()
    throws InterruptedException;

  public abstract CloseFuture awaitUninterruptibly();

  public abstract CloseFuture addListener(IoFutureListener<?> paramIoFutureListener);

  public abstract CloseFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}