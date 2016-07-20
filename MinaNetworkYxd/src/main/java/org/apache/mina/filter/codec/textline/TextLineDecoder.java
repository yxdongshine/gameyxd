/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.textline;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderException;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.RecoverableProtocolDecoderException;

public class TextLineDecoder implements ProtocolDecoder {
	private final AttributeKey CONTEXT;
	private final Charset charset;
	private final LineDelimiter delimiter;
	private IoBuffer delimBuf;
	private int maxLineLength;
	private int bufferLength;

	public TextLineDecoder() {
		this(LineDelimiter.AUTO);
	}

	public TextLineDecoder(String delimiter) {
		this(new LineDelimiter(delimiter));
	}

	public TextLineDecoder(LineDelimiter delimiter) {
		this(Charset.defaultCharset(), delimiter);
	}

	public TextLineDecoder(Charset charset) {
		this(charset, LineDelimiter.AUTO);
	}

	public TextLineDecoder(Charset charset, String delimiter) {
		this(charset, new LineDelimiter(delimiter));
	}

	public TextLineDecoder(Charset charset, LineDelimiter delimiter) {
		this.CONTEXT = new AttributeKey(super.getClass(), "context");

		this.maxLineLength = 1024;

		this.bufferLength = 128;

		if (charset == null) {
			throw new IllegalArgumentException(
					"charset parameter shuld not be null");
		}

		if (delimiter == null) {
			throw new IllegalArgumentException(
					"delimiter parameter should not be null");
		}

		this.charset = charset;
		this.delimiter = delimiter;

		if (this.delimBuf == null) {
			IoBuffer tmp = IoBuffer.allocate(2).setAutoExpand(true);
			try {
				tmp.putString(delimiter.getValue(), charset.newEncoder());
			} catch (CharacterCodingException localCharacterCodingException) {
			}
			tmp.flip();
			this.delimBuf = tmp;
		}
	}

	public int getMaxLineLength() {
		return this.maxLineLength;
	}

	public void setMaxLineLength(int maxLineLength) {
		if (maxLineLength <= 0) {
			throw new IllegalArgumentException("maxLineLength ("
					+ maxLineLength + ") should be a positive value");
		}

		this.maxLineLength = maxLineLength;
	}

	public void setBufferLength(int bufferLength) {
		if (bufferLength <= 0) {
			throw new IllegalArgumentException("bufferLength ("
					+ this.maxLineLength + ") should be a positive value");
		}

		this.bufferLength = bufferLength;
	}

	public int getBufferLength() {
		return this.bufferLength;
	}

	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		Context ctx = getContext(session);

		if (LineDelimiter.AUTO.equals(this.delimiter))
			decodeAuto(ctx, session, in, out);
		else
			decodeNormal(ctx, session, in, out);
	}

	private Context getContext(IoSession session) {
		Context ctx = (Context) session.getAttribute(this.CONTEXT);

		if (ctx == null) {
			ctx = new Context(this.bufferLength);
			session.setAttribute(this.CONTEXT, ctx);
		}

		return ctx;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out)
			throws Exception {
	}

	public void dispose(IoSession session) throws Exception {
		Context ctx = (Context) session.getAttribute(this.CONTEXT);

		if (ctx != null)
			session.removeAttribute(this.CONTEXT);
	}

	private void decodeAuto(Context ctx, IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws CharacterCodingException,
			ProtocolDecoderException {
		int matchCount = ctx.getMatchCount();

		int oldPos = in.position();
		int oldLimit = in.limit();

		while (in.hasRemaining()) {
			byte b = in.get();
			boolean matched = false;

			switch (b) {
			case 13:
				++matchCount;
				break;
			case 10:
				++matchCount;
				matched = true;
				break;
			case 11:
			case 12:
			default:
				matchCount = 0;
			}

			if (!(matched))
				continue;
			int pos = in.position();
			in.limit(pos);
			in.position(oldPos);

			ctx.append(in);

			in.limit(oldLimit);
			in.position(pos);

			if (ctx.getOverflowPosition() == 0) {
				IoBuffer buf = ctx.getBuffer();
				buf.flip();
				buf.limit(buf.limit() - matchCount);
				try {
					byte[] data = new byte[buf.limit()];
					buf.get(data);
					CharsetDecoder decoder = ctx.getDecoder();

					CharBuffer buffer = decoder.decode(ByteBuffer.wrap(data));
					String str = buffer.toString();
					writeText(session, str, out);
				} finally {
					buf.clear();
				}
			} else {
				int overflowPosition = ctx.getOverflowPosition();
				ctx.reset();
				throw new RecoverableProtocolDecoderException(
						"Line is too long: " + overflowPosition);
			}

			oldPos = pos;
			matchCount = 0;
		}

		in.position(oldPos);
		ctx.append(in);

		ctx.setMatchCount(matchCount);
	}

	private void decodeNormal(Context ctx, IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws CharacterCodingException,
			ProtocolDecoderException {
		int matchCount = ctx.getMatchCount();

		int oldPos = in.position();
		int oldLimit = in.limit();

		while (in.hasRemaining()) {
			byte b = in.get();

			if (this.delimBuf.get(matchCount) == b) {
				++matchCount;

				if (matchCount != this.delimBuf.limit())
					continue;
				int pos = in.position();
				in.limit(pos);
				in.position(oldPos);

				ctx.append(in);

				in.limit(oldLimit);
				in.position(pos);

				if (ctx.getOverflowPosition() == 0) {
					IoBuffer buf = ctx.getBuffer();
					buf.flip();
					buf.limit(buf.limit() - matchCount);
					try {
						writeText(session, buf.getString(ctx.getDecoder()), out);
					} finally {
						buf.clear();
					}
				} else {
					int overflowPosition = ctx.getOverflowPosition();
					ctx.reset();
					throw new RecoverableProtocolDecoderException(
							"Line is too long: " + overflowPosition);
				}

				oldPos = pos;
				matchCount = 0;
			} else {
				in.position(Math.max(0, in.position() - matchCount));
				matchCount = 0;
			}

		}

		in.position(oldPos);
		ctx.append(in);

		ctx.setMatchCount(matchCount);
	}

	protected void writeText(IoSession session, String text,
			ProtocolDecoderOutput out) {
		out.write(text);
	}

	private class Context {
		private final CharsetDecoder decoder;
		private final IoBuffer buf;
		private int matchCount = 0;

		private int overflowPosition = 0;

		private Context(int paramInt) {
			this.decoder = TextLineDecoder.this.charset.newDecoder();
			this.buf = IoBuffer.allocate(paramInt).setAutoExpand(true);
		}

		public CharsetDecoder getDecoder() {
			return this.decoder;
		}

		public IoBuffer getBuffer() {
			return this.buf;
		}

		public int getOverflowPosition() {
			return this.overflowPosition;
		}

		public int getMatchCount() {
			return this.matchCount;
		}

		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}

		public void reset() {
			this.overflowPosition = 0;
			this.matchCount = 0;
			this.decoder.reset();
		}

		public void append(IoBuffer in) {
			if (this.overflowPosition != 0) {
				discard(in);
			} else if (this.buf.position() > TextLineDecoder.this.maxLineLength
					- in.remaining()) {
				this.overflowPosition = this.buf.position();
				this.buf.clear();
				discard(in);
			} else {
				getBuffer().put(in);
			}
		}

		private void discard(IoBuffer in) {
			if (2147483647 - in.remaining() < this.overflowPosition)
				this.overflowPosition = 2147483647;
			else {
				this.overflowPosition += in.remaining();
			}

			in.position(in.limit());
		}
	}
}