/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.statemachine;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract interface DecodingState {
	public abstract DecodingState decode(IoBuffer paramIoBuffer,
			ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;

	public abstract DecodingState finishDecode(
			ProtocolDecoderOutput paramProtocolDecoderOutput) throws Exception;
}