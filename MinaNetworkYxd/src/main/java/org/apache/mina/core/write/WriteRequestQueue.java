/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.write;

import org.apache.mina.core.session.IoSession;

public abstract interface WriteRequestQueue {
	public abstract WriteRequest poll(IoSession paramIoSession);

	public abstract void offer(IoSession paramIoSession,
			WriteRequest paramWriteRequest);

	public abstract boolean isEmpty(IoSession paramIoSession);

	public abstract void clear(IoSession paramIoSession);

	public abstract void dispose(IoSession paramIoSession);

	public abstract int size();
}