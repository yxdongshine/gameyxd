/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

public abstract interface IoProcessor<S extends IoSession>
{
  public abstract boolean isDisposing();

  public abstract boolean isDisposed();

  public abstract void dispose();

  public abstract void add(S paramS);

  public abstract void flush(S paramS);

  public abstract void write(S paramS, WriteRequest paramWriteRequest);

  public abstract void updateTrafficControl(S paramS);

  public abstract void remove(S paramS);
}