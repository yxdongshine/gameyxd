/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.util.EventListener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public abstract interface IoServiceListener extends EventListener {
	public abstract void serviceActivated(IoService paramIoService)
			throws Exception;

	public abstract void serviceIdle(IoService paramIoService,
			IdleStatus paramIdleStatus) throws Exception;

	public abstract void serviceDeactivated(IoService paramIoService)
			throws Exception;

	public abstract void sessionCreated(IoSession paramIoSession)
			throws Exception;

	public abstract void sessionClosed(IoSession paramIoSession)
			throws Exception;

	public abstract void sessionDestroyed(IoSession paramIoSession)
			throws Exception;
}