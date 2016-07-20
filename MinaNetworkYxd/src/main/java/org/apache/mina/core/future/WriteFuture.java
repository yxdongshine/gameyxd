/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.future;

public abstract interface WriteFuture extends IoFuture
{
  public abstract boolean isWritten();

  public abstract Throwable getException();

  public abstract void setWritten();

  public abstract void setException(Throwable paramThrowable);

  public abstract WriteFuture await()
    throws InterruptedException;

  public abstract WriteFuture awaitUninterruptibly();

  public abstract WriteFuture addListener(IoFutureListener<?> paramIoFutureListener);

  public abstract WriteFuture removeListener(IoFutureListener<?> paramIoFutureListener);
}