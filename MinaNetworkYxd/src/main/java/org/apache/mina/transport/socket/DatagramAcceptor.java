/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSessionRecycler;

public abstract interface DatagramAcceptor extends IoAcceptor {
	public abstract InetSocketAddress getLocalAddress();

	public abstract InetSocketAddress getDefaultLocalAddress();

	public abstract void setDefaultLocalAddress(
			InetSocketAddress paramInetSocketAddress);

	public abstract IoSessionRecycler getSessionRecycler();

	public abstract void setSessionRecycler(
			IoSessionRecycler paramIoSessionRecycler);

	public abstract DatagramSessionConfig getSessionConfig();
}