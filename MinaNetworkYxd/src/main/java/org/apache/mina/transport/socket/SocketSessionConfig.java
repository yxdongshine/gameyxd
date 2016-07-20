/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket;

import org.apache.mina.core.session.IoSessionConfig;

public abstract interface SocketSessionConfig extends IoSessionConfig {
	public abstract boolean isReuseAddress();

	public abstract void setReuseAddress(boolean paramBoolean);

	public abstract int getReceiveBufferSize();

	public abstract void setReceiveBufferSize(int paramInt);

	public abstract int getSendBufferSize();

	public abstract void setSendBufferSize(int paramInt);

	public abstract int getTrafficClass();

	public abstract void setTrafficClass(int paramInt);

	public abstract boolean isKeepAlive();

	public abstract void setKeepAlive(boolean paramBoolean);

	public abstract boolean isOobInline();

	public abstract void setOobInline(boolean paramBoolean);

	public abstract int getSoLinger();

	public abstract void setSoLinger(int paramInt);

	public abstract boolean isTcpNoDelay();

	public abstract void setTcpNoDelay(boolean paramBoolean);
}