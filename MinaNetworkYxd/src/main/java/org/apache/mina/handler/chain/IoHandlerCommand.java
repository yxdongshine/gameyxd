/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.chain;

import org.apache.mina.core.session.IoSession;

public abstract interface IoHandlerCommand {
	public abstract void execute(NextCommand paramNextCommand,
			IoSession paramIoSession, Object paramObject) throws Exception;

	public static abstract interface NextCommand {
		public abstract void execute(IoSession paramIoSession,
				Object paramObject) throws Exception;
	}
}