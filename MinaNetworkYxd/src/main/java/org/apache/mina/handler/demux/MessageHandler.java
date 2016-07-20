/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.demux;

import org.apache.mina.core.session.IoSession;

public abstract interface MessageHandler<E>
{
  public static final MessageHandler<Object> NOOP = new MessageHandler() {
    public void handleMessage(IoSession session, Object message) {  } } ;

  public abstract void handleMessage(IoSession paramIoSession, E paramE)
    throws Exception;
}