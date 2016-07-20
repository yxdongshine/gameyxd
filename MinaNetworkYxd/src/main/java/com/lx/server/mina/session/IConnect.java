/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.session;

import com.google.protobuf.Message;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;

public abstract interface IConnect {
	public abstract InetSocketAddress getRemouteAdress();

	public abstract int getRemoteSTypeid();

	public abstract void setRemoteSTypeid(int paramInt);

	public abstract int getRemoteSType();

	public abstract String getRemoteGroup();

	public abstract String getRemoteName();

	public abstract void ping(long paramLong);

	public abstract long getPing();

	public abstract void setConnected(boolean paramBoolean);

	public abstract boolean isConnect();

	public abstract void send(IoBuffer paramIoBuffer);

	public abstract void send(Message paramMessage);

	public abstract void setAttachment(Object paramObject);

	public abstract Object getAttachment();

	public abstract long getId();

	public abstract long getRoleId();

	public abstract void setId(long paramLong);

	public abstract void close();

	public abstract void setRemoteSType(int paramInt);

	public abstract void setRemoteName(String paramString);

	public abstract void setRemoteGroup(String paramString);

	public abstract void setRemouteAdress(
			InetSocketAddress paramInetSocketAddress);

	public abstract String getRemouteIp();

	public abstract int getWriteSize();

	public abstract String getInfo();

	public abstract void setRoleId(long paramLong);
}