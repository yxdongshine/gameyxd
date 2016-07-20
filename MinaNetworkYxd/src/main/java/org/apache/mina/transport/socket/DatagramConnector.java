/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket;

import java.net.InetSocketAddress;
import org.apache.mina.core.service.IoConnector;

public abstract interface DatagramConnector extends IoConnector {
	public abstract InetSocketAddress getDefaultRemoteAddress();

	public abstract DatagramSessionConfig getSessionConfig();

	public abstract void setDefaultRemoteAddress(
			InetSocketAddress paramInetSocketAddress);
}