/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;

public abstract interface SocketAcceptor extends IoAcceptor {
	public abstract InetSocketAddress getLocalAddress();

	public abstract InetSocketAddress getDefaultLocalAddress();

	public abstract void setDefaultLocalAddress(
			InetSocketAddress paramInetSocketAddress);

	public abstract boolean isReuseAddress();

	public abstract void setReuseAddress(boolean paramBoolean);

	public abstract int getBacklog();

	public abstract void setBacklog(int paramInt);

	public abstract SocketSessionConfig getSessionConfig();
}