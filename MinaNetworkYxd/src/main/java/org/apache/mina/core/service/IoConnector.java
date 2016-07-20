/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.net.SocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSessionInitializer;

public abstract interface IoConnector extends IoService
{
  /** @deprecated */
  public abstract int getConnectTimeout();

  public abstract long getConnectTimeoutMillis();

  /** @deprecated */
  public abstract void setConnectTimeout(int paramInt);

  public abstract void setConnectTimeoutMillis(long paramLong);

  public abstract SocketAddress getDefaultRemoteAddress();

  public abstract void setDefaultRemoteAddress(SocketAddress paramSocketAddress);

  public abstract SocketAddress getDefaultLocalAddress();

  public abstract void setDefaultLocalAddress(SocketAddress paramSocketAddress);

  public abstract ConnectFuture connect();

  public abstract ConnectFuture connect(IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);

  public abstract ConnectFuture connect(SocketAddress paramSocketAddress);

  public abstract ConnectFuture connect(SocketAddress paramSocketAddress, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);

  public abstract ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);

  public abstract ConnectFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, IoSessionInitializer<? extends ConnectFuture> paramIoSessionInitializer);
}