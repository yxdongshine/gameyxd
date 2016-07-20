/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class MessageDecoderAdapter implements MessageDecoder {
	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}
}