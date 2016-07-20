/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.net.SocketAddress;
import java.util.Set;
import org.apache.mina.core.session.IoSessionConfig;

public abstract interface TransportMetadata
{
  public abstract String getProviderName();

  public abstract String getName();

  public abstract boolean isConnectionless();

  public abstract boolean hasFragmentation();

  public abstract Class<? extends SocketAddress> getAddressType();

  public abstract Set<Class<? extends Object>> getEnvelopeTypes();

  public abstract Class<? extends IoSessionConfig> getSessionConfigType();
}