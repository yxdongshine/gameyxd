/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.session;

import com.google.protobuf.Message;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequestQueue;

public class MinaConnect implements IConnect {
	protected IoSession session;
	private InetSocketAddress remouteAddress;
	private int remoteType;
	private String remoteName;
	private int remoteSid;
	private String remoteGroup;
	private long ping;
	private long roleId;
	private boolean connected;
	private long id = -1L;

	public String getRemoteName() {
		return this.remoteName;
	}

	public void setRemoteName(String remoteName) {
		this.remoteName = remoteName;
	}

	public MinaConnect() {
		ping(System.currentTimeMillis());
	}

	public MinaConnect(IoSession session) {
		this.session = session;
	}

	public InetSocketAddress getRemouteAdress() {
		return this.remouteAddress;
	}

	public String getRemouteIp() {
		if ((this.remouteAddress == null) && (this.session != null)) {
			this.remouteAddress = ((InetSocketAddress) this.session
					.getRemoteAddress());
		}
		if (this.remouteAddress == null) {
			return null;
		}
		return this.remouteAddress.getAddress().getHostAddress();
	}

	public void setRemouteAdress(InetSocketAddress soketAddress) {
		this.remouteAddress = soketAddress;
	}

	public String getRemoteGroup() {
		return this.remoteGroup;
	}

	public void setRemoteGroup(String remoteGroup) {
		this.remoteGroup = remoteGroup;
	}

	public int getRemoteSTypeid() {
		return this.remoteSid;
	}

	public void setRemoteSTypeid(int remoteSid) {
		this.remoteSid = remoteSid;
	}

	public int getRemoteSType() {
		return this.remoteType;
	}

	public void setRemoteSType(int type) {
		this.remoteType = type;
	}

	public boolean isConnected() {
		return this.connected;
	}

	public boolean isConnect() {
		return this.connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public void send(IoBuffer buf) {
		if (this.session == null) {
			return;
		}
		if ((isConnect()) && (this.session.isConnected()))
			this.session.write(buf);
	}

	public void send(Message protobufMsg) {
		if (this.session == null) {
			return;
		}
		if ((isConnect()) && (this.session.isConnected()))
			this.session.write(protobufMsg);
	}

	public void setAttachment(Object obj) {
		if (obj == null) {
			this.session = null;
		} else if (obj instanceof IoSession) {
			this.session = ((IoSession) obj);
			setId(this.session.getId());
		} else {
			throw new IllegalArgumentException(
					"minaConnect ,obj is not IoSession ,obj="
							+ obj.getClass().getName());
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public Object getAttachment() {
		return this.session;
	}

	public long getId() {
		return this.id;
	}

	public void close() {
		if (this.session != null)
			this.session.close(true);
	}

	public void ping(long time) {
		this.ping = time;
	}

	public long getPing() {
		return this.ping;
	}

	public int getWriteSize() {
		if (this.session != null) {
			return this.session.getWriteRequestQueue().size();
		}
		return 0;
	}

	public String getInfo() {
		StringBuffer sb = new StringBuffer();
		if (this.session == null) {
			return "";
		}
		this.session.updateThroughput(System.currentTimeMillis(), false);
		sb.append("\n" + getRemoteName() + "\n");
		sb.append("\t[WriteRequestQueue()] :"
				+ this.session.getWriteRequestQueue().size() + " ");
		sb.append("[WrittenBytes] :" + this.session.getWrittenBytes() + " ");
		sb.append("[WrittenBytesThroughput] :"
				+ (int) this.session.getWrittenBytesThroughput() + " ");
		sb.append("[WrittenMessages] :"
				+ (int) this.session.getWrittenMessages() + " ");
		sb.append("[WrittenMessagesThroughput] :"
				+ (int) this.session.getWrittenMessagesThroughput() + " ");
		sb.append("\n");
		sb.append("\t[ReadBytes] :" + this.session.getReadBytes() + " ");
		sb.append("[ReadBytesThroughput] :"
				+ (int) this.session.getReadBytesThroughput() + " ");
		sb.append("[ReadMessage]s :" + this.session.getReadMessages() + " ");
		sb.append("[ReadMessagesThroughput] :"
				+ this.session.getReadMessagesThroughput() + " ");
		sb.append("[ScheduledWriteBytes] :"
				+ this.session.getScheduledWriteBytes() + " ");
		sb.append("[ScheduledWriteMessages] :"
				+ this.session.getScheduledWriteMessages() + "\n");

		return sb.toString();
	}

	public long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
}