/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.demux;

import org.apache.mina.core.session.IoSession;

public abstract interface ExceptionHandler<E extends Throwable>
{
  public static final ExceptionHandler<Throwable> NOOP = new ExceptionHandler() {
    public void exceptionCaught(IoSession session, Throwable cause) {  } } ;

  public static final ExceptionHandler<Throwable> CLOSE = new ExceptionHandler() {
    public void exceptionCaught(IoSession session, Throwable cause) {
      session.close(true);
    }
  };

  public abstract void exceptionCaught(IoSession paramIoSession, E paramE)
    throws Exception;
}