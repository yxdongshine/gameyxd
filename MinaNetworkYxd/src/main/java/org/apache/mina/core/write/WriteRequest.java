/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.write;

import java.net.SocketAddress;
import org.apache.mina.core.future.WriteFuture;

public abstract interface WriteRequest {
	public abstract WriteRequest getOriginalRequest();

	public abstract WriteFuture getFuture();

	public abstract Object getMessage();

	public abstract SocketAddress getDestination();

	public abstract boolean isEncoded();
}