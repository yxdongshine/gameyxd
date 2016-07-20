/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.Set;
import org.apache.mina.core.session.IoSession;

public abstract interface IoAcceptor extends IoService
{
  public abstract SocketAddress getLocalAddress();

  public abstract Set<SocketAddress> getLocalAddresses();

  public abstract SocketAddress getDefaultLocalAddress();

  public abstract List<SocketAddress> getDefaultLocalAddresses();

  public abstract void setDefaultLocalAddress(SocketAddress paramSocketAddress);

  public abstract void setDefaultLocalAddresses(SocketAddress paramSocketAddress, SocketAddress[] paramArrayOfSocketAddress);

  public abstract void setDefaultLocalAddresses(Iterable<? extends SocketAddress> paramIterable);

  public abstract void setDefaultLocalAddresses(List<? extends SocketAddress> paramList);

  public abstract boolean isCloseOnDeactivation();

  public abstract void setCloseOnDeactivation(boolean paramBoolean);

  public abstract void bind()
    throws IOException;

  public abstract void bind(SocketAddress paramSocketAddress)
    throws IOException;

  public abstract void bind(SocketAddress paramSocketAddress, SocketAddress[] paramArrayOfSocketAddress)
    throws IOException;

  public abstract void bind(SocketAddress[] paramArrayOfSocketAddress)
    throws IOException;

  public abstract void bind(Iterable<? extends SocketAddress> paramIterable)
    throws IOException;

  public abstract void unbind();

  public abstract void unbind(SocketAddress paramSocketAddress);

  public abstract void unbind(SocketAddress paramSocketAddress, SocketAddress[] paramArrayOfSocketAddress);

  public abstract void unbind(Iterable<? extends SocketAddress> paramIterable);

  public abstract IoSession newSession(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);
}