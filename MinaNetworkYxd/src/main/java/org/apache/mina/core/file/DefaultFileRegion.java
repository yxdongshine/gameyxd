/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.file;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class DefaultFileRegion implements FileRegion {
	private final FileChannel channel;
	private final long originalPosition;
	private long position;
	private long remainingBytes;

	public DefaultFileRegion(FileChannel channel) throws IOException {
		this(channel, 0L, channel.size());
	}

	public DefaultFileRegion(FileChannel channel, long position,
			long remainingBytes) {
		if (channel == null) {
			throw new IllegalArgumentException("channel can not be null");
		}
		if (position < 0L) {
			throw new IllegalArgumentException(
					"position may not be less than 0");
		}
		if (remainingBytes < 0L) {
			throw new IllegalArgumentException(
					"remainingBytes may not be less than 0");
		}
		this.channel = channel;
		this.originalPosition = position;
		this.position = position;
		this.remainingBytes = remainingBytes;
	}

	public long getWrittenBytes() {
		return (this.position - this.originalPosition);
	}

	public long getRemainingBytes() {
		return this.remainingBytes;
	}

	public FileChannel getFileChannel() {
		return this.channel;
	}

	public long getPosition() {
		return this.position;
	}

	public void update(long value) {
		this.position += value;
		this.remainingBytes -= value;
	}

	public String getFilename() {
		return null;
	}
}