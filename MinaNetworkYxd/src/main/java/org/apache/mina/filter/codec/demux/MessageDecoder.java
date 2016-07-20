/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract interface MessageDecoder {
	public static final MessageDecoderResult OK = MessageDecoderResult.OK;

	public static final MessageDecoderResult NEED_DATA = MessageDecoderResult.NEED_DATA;

	public static final MessageDecoderResult NOT_OK = MessageDecoderResult.NOT_OK;

	public abstract MessageDecoderResult decodable(IoSession paramIoSession,
			IoBuffer paramIoBuffer);

	public abstract MessageDecoderResult decode(IoSession paramIoSession,
			IoBuffer paramIoBuffer,
			ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

	public abstract void finishDecode(IoSession paramIoSession,
			ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}