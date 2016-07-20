/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.loncent.protocol.BaseMessage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerProtocolDecoder extends CumulativeProtocolDecoder {
	private static final Logger log = LoggerFactory
			.getLogger(InnerProtocolDecoder.class);
	private ProtocolCodec codec;

	public InnerProtocolDecoder(ProtocolCodec codec) {
		this.codec = codec;
	}

	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() > 4) {
			in.mark();
			int size = in.getInt();

			if (size == 1952937823) {
				return readQQProtocol(in);
			}
			if (size > in.remaining()) {
				in.reset();
				return false;
			}

			byte[] data = new byte[size];
			in.get(data);
			BaseMessage.InnerMessage msg = null;
			try {
				msg = this.codec.decode(data);
			} catch (Exception e) {
				log.error("decode error::", e);
			}
			if (msg != null)
				out.write(msg);
			else {
				return false;
			}
			return true;
		}

		return false;
	}

	private boolean readQQProtocol(IoBuffer in) {
		int matchCount = 0;

		while (in.hasRemaining()) {
			byte b = in.get();
			if (b == 13) {
				byte next = in.get();
				if (next == 10) {
					++matchCount;
					if (matchCount >= 3) {
						return true;
					}
				}
			}
		}
		return false;
	}
}