/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.session;

import org.apache.mina.core.future.IoFuture;

public abstract interface IoSessionInitializer<T extends IoFuture>
{
  public abstract void initializeSession(IoSession paramIoSession, T paramT);
}