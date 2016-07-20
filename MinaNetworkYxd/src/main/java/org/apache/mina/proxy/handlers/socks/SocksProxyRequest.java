/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.proxy.handlers.socks;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.mina.proxy.handlers.ProxyRequest;

public class SocksProxyRequest extends ProxyRequest {
	private byte protocolVersion;
	private byte commandCode;
	private String userName;
	private String password;
	private String host;
	private int port;
	private String serviceKerberosName;

	public SocksProxyRequest(byte protocolVersion, byte commandCode,
			InetSocketAddress endpointAddress, String userName) {
		super(endpointAddress);
		this.protocolVersion = protocolVersion;
		this.commandCode = commandCode;
		this.userName = userName;
	}

	public SocksProxyRequest(byte commandCode, String host, int port,
			String userName) {
		this.protocolVersion = 4;
		this.commandCode = commandCode;
		this.userName = userName;
		this.host = host;
		this.port = port;
	}

	public byte[] getIpAddress() {
		if (getEndpointAddress() == null) {
			return SocksProxyConstants.FAKE_IP;
		}

		return getEndpointAddress().getAddress().getAddress();
	}

	public byte[] getPort() {
		byte[] port = new byte[2];
		int p = (getEndpointAddress() == null) ? this.port
				: getEndpointAddress().getPort();
		port[1] = (byte) p;
		port[0] = (byte) (p >> 8);
		return port;
	}

	public byte getCommandCode() {
		return this.commandCode;
	}

	public byte getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getUserName() {
		return this.userName;
	}

	public final synchronized String getHost() {
		if (this.host == null) {
			InetSocketAddress adr = getEndpointAddress();

			if ((adr != null) && (!(adr.isUnresolved()))) {
				this.host = getEndpointAddress().getHostName();
			}
		}

		return this.host;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceKerberosName() {
		return this.serviceKerberosName;
	}

	public void setServiceKerberosName(String serviceKerberosName) {
		this.serviceKerberosName = serviceKerberosName;
	}
}