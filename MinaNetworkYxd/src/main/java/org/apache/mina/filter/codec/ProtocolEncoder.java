/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import org.apache.mina.core.session.IoSession;

public abstract interface ProtocolEncoder {
	public abstract void encode(IoSession paramIoSession, Object paramObject,
			ProtocolEncoderOutput paramProtocolEncoderOutput) throws Exception;

	public abstract void dispose(IoSession paramIoSession) throws Exception;
}