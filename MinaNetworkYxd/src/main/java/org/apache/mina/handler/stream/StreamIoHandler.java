/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.handler.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamIoHandler extends IoHandlerAdapter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StreamIoHandler.class);

	private static final AttributeKey KEY_IN = new AttributeKey(
			StreamIoHandler.class, "in");

	private static final AttributeKey KEY_OUT = new AttributeKey(
			StreamIoHandler.class, "out");
	private int readTimeout;
	private int writeTimeout;

	protected abstract void processStreamIo(IoSession paramIoSession,
			InputStream paramInputStream, OutputStream paramOutputStream);

	public int getReadTimeout() {
		return this.readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public int getWriteTimeout() {
		return this.writeTimeout;
	}

	public void setWriteTimeout(int writeTimeout) {
		this.writeTimeout = writeTimeout;
	}

	public void sessionOpened(IoSession session) {
		session.getConfig().setWriteTimeout(this.writeTimeout);
		session.getConfig().setIdleTime(IdleStatus.READER_IDLE,
				this.readTimeout);

		InputStream in = new IoSessionInputStream();
		OutputStream out = new IoSessionOutputStream(session);
		session.setAttribute(KEY_IN, in);
		session.setAttribute(KEY_OUT, out);
		processStreamIo(session, in, out);
	}

	public void sessionClosed(IoSession session) throws Exception {
		InputStream in = (InputStream) session.getAttribute(KEY_IN);
		OutputStream out = (OutputStream) session.getAttribute(KEY_OUT);
		try {
			in.close();
		} finally {
			out.close();
		}
	}

	public void messageReceived(IoSession session, Object buf) {
		IoSessionInputStream in = (IoSessionInputStream) session
				.getAttribute(KEY_IN);
		in.write((IoBuffer) buf);
	}

	public void exceptionCaught(IoSession session, Throwable cause) {
		IoSessionInputStream in = (IoSessionInputStream) session
				.getAttribute(KEY_IN);

		IOException e = null;
		if (cause instanceof StreamIoException)
			e = (IOException) cause.getCause();
		else if (cause instanceof IOException) {
			e = (IOException) cause;
		}

		if ((e != null) && (in != null)) {
			in.throwException(e);
		} else {
			LOGGER.warn("Unexpected exception.", cause);
			session.close(true);
		}
	}

	public void sessionIdle(IoSession session, IdleStatus status) {
		if (status == IdleStatus.READER_IDLE)
			throw new StreamIoException(new SocketTimeoutException(
					"Read timeout"));
	}

	private static class StreamIoException extends RuntimeException {
		private static final long serialVersionUID = 3976736960742503222L;

		public StreamIoException(IOException cause) {
			super(cause);
		}
	}
}