/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.chain;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ChainedIoHandler extends IoHandlerAdapter {
	private final IoHandlerChain chain;

	public ChainedIoHandler() {
		this.chain = new IoHandlerChain();
	}

	public ChainedIoHandler(IoHandlerChain chain) {
		if (chain == null) {
			throw new IllegalArgumentException("chain");
		}
		this.chain = chain;
	}

	public IoHandlerChain getChain() {
		return this.chain;
	}

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		this.chain.execute(null, session, message);
	}
}