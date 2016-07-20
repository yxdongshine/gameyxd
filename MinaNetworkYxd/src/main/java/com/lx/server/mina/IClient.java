/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina;

import java.net.InetSocketAddress;
import org.apache.mina.core.session.IoSession;

public abstract interface IClient {
	public abstract IoSession connect(String paramString, int paramInt)
			throws Exception;

	public abstract IoSession connect(InetSocketAddress paramInetSocketAddress)
			throws Exception;
}