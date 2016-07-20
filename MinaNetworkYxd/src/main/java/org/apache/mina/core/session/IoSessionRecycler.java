/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.session;

import java.net.SocketAddress;

public abstract interface IoSessionRecycler {
	public static final IoSessionRecycler NOOP = new IoSessionRecycler() {
		public void put(IoSession session) {
		}

		public IoSession recycle(SocketAddress remoteAddress) {
			return null;
		}

		public void remove(IoSession session) {
		}
	};

	public abstract void put(IoSession paramIoSession);

	public abstract IoSession recycle(SocketAddress paramSocketAddress);

	public abstract void remove(IoSession paramIoSession);
}