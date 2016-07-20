/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket;

import org.apache.mina.core.session.IoSessionConfig;

public abstract interface DatagramSessionConfig extends IoSessionConfig {
	public abstract boolean isBroadcast();

	public abstract void setBroadcast(boolean paramBoolean);

	public abstract boolean isReuseAddress();

	public abstract void setReuseAddress(boolean paramBoolean);

	public abstract int getReceiveBufferSize();

	public abstract void setReceiveBufferSize(int paramInt);

	public abstract int getSendBufferSize();

	public abstract void setSendBufferSize(int paramInt);

	public abstract int getTrafficClass();

	public abstract void setTrafficClass(int paramInt);

	public abstract boolean isCloseOnPortUnreachable();

	public abstract void setCloseOnPortUnreachable(boolean paramBoolean);
}