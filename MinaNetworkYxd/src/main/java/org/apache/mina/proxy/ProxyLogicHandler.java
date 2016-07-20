/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.proxy;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.proxy.session.ProxyIoSession;

public abstract interface ProxyLogicHandler {
	public abstract boolean isHandshakeComplete();

	public abstract void messageReceived(IoFilter.NextFilter paramNextFilter,
			IoBuffer paramIoBuffer) throws ProxyAuthException;

	public abstract void doHandshake(IoFilter.NextFilter paramNextFilter)
			throws ProxyAuthException;

	public abstract ProxyIoSession getProxyIoSession();

	public abstract void enqueueWriteRequest(
			IoFilter.NextFilter paramNextFilter, WriteRequest paramWriteRequest);
}