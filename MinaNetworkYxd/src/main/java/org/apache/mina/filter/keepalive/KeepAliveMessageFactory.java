/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.keepalive;

import org.apache.mina.core.session.IoSession;

public abstract interface KeepAliveMessageFactory {
	public abstract boolean isRequest(IoSession paramIoSession,
			Object paramObject);

	public abstract boolean isResponse(IoSession paramIoSession,
			Object paramObject);

	public abstract Object getRequest(IoSession paramIoSession);

	public abstract Object getResponse(IoSession paramIoSession,
			Object paramObject);
}