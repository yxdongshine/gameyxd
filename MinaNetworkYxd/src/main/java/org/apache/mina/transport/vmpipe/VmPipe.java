/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.vmpipe;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoServiceListenerSupport;

class VmPipe {
	private final VmPipeAcceptor acceptor;
	private final VmPipeAddress address;
	private final IoHandler handler;
	private final IoServiceListenerSupport listeners;

	VmPipe(VmPipeAcceptor acceptor, VmPipeAddress address, IoHandler handler,
			IoServiceListenerSupport listeners) {
		this.acceptor = acceptor;
		this.address = address;
		this.handler = handler;
		this.listeners = listeners;
	}

	public VmPipeAcceptor getAcceptor() {
		return this.acceptor;
	}

	public VmPipeAddress getAddress() {
		return this.address;
	}

	public IoHandler getHandler() {
		return this.handler;
	}

	public IoServiceListenerSupport getListeners() {
		return this.listeners;
	}
}