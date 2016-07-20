/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.future;

import java.util.EventListener;
import org.apache.mina.core.session.IoSession;

public abstract interface IoFutureListener<F extends IoFuture> extends EventListener
{
  public static final IoFutureListener<IoFuture> CLOSE = new IoFutureListener() {
    public void operationComplete(IoFuture future) {
      future.getSession().close(true);
    }
  };

  public abstract void operationComplete(F paramF);
}