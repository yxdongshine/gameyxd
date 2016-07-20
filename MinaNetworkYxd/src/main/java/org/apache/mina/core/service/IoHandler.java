/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public abstract interface IoHandler {
	public abstract void sessionCreated(IoSession paramIoSession)
			throws Exception;

	public abstract void sessionOpened(IoSession paramIoSession)
			throws Exception;

	public abstract void sessionClosed(IoSession paramIoSession)
			throws Exception;

	public abstract void sessionIdle(IoSession paramIoSession,
			IdleStatus paramIdleStatus) throws Exception;

	public abstract void exceptionCaught(IoSession paramIoSession,
			Throwable paramThrowable) throws Exception;

	public abstract void messageReceived(IoSession paramIoSession,
			Object paramObject) throws Exception;

	public abstract void messageSent(IoSession paramIoSession,
			Object paramObject) throws Exception;

	public abstract void inputClosed(IoSession paramIoSession) throws Exception;
}