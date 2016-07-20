/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.lx.server.mina.session.IConnect;

public abstract interface IBeanInvoke<T>
{
  public abstract void invoke(T paramT, IConnect paramIConnect);
}