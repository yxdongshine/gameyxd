/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.multiton;

import org.apache.mina.core.session.IoSession;

@Deprecated
public abstract interface SingleSessionIoHandlerFactory
{
  public abstract SingleSessionIoHandler getHandler(IoSession paramIoSession)
    throws Exception;
}