/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public abstract interface ProtocolCodecFactory {
	public abstract ProtocolEncoder getEncoder(IoSession paramIoSession)
			throws Exception;

	public abstract ProtocolDecoder getDecoder(IoSession paramIoSession)
			throws Exception;
}