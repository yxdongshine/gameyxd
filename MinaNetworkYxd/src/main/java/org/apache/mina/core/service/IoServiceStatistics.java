/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class IoServiceStatistics {
	private AbstractIoService service;
	private double readBytesThroughput;
	private double writtenBytesThroughput;
	private double readMessagesThroughput;
	private double writtenMessagesThroughput;
	private double largestReadBytesThroughput;
	private double largestWrittenBytesThroughput;
	private double largestReadMessagesThroughput;
	private double largestWrittenMessagesThroughput;
	private long readBytes;
	private long writtenBytes;
	private long readMessages;
	private long writtenMessages;
	private long lastReadTime;
	private long lastWriteTime;
	private long lastReadBytes;
	private long lastWrittenBytes;
	private long lastReadMessages;
	private long lastWrittenMessages;
	private long lastThroughputCalculationTime;
	private int scheduledWriteBytes;
	private int scheduledWriteMessages;
	private final AtomicInteger throughputCalculationInterval = new AtomicInteger(
			3);

	private final Lock throughputCalculationLock = new ReentrantLock();

	public IoServiceStatistics(AbstractIoService service) {
		this.service = service;
	}

	public final int getLargestManagedSessionCount() {
		return this.service.getListeners().getLargestManagedSessionCount();
	}

	public final long getCumulativeManagedSessionCount() {
		return this.service.getListeners().getCumulativeManagedSessionCount();
	}

	public final long getLastIoTime() {
		this.throughputCalculationLock.lock();
		try {
			return Math.max(this.lastReadTime, this.lastWriteTime);
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getLastReadTime() {
		this.throughputCalculationLock.lock();
		try {
			return this.lastReadTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getLastWriteTime() {
		this.throughputCalculationLock.lock();
		try {
			return this.lastWriteTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getReadBytes() {
		this.throughputCalculationLock.lock();
		try {
			return this.readBytes;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getWrittenBytes() {
		this.throughputCalculationLock.lock();
		try {
			return this.writtenBytes;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getReadMessages() {
		this.throughputCalculationLock.lock();
		try {
			return this.readMessages;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final long getWrittenMessages() {
		this.throughputCalculationLock.lock();
		try {
			return this.writtenMessages;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getReadBytesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			resetThroughput();
			return this.readBytesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getWrittenBytesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			resetThroughput();
			return this.writtenBytesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getReadMessagesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			resetThroughput();
			return this.readMessagesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getWrittenMessagesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			resetThroughput();
			return this.writtenMessagesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getLargestReadBytesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			return this.largestReadBytesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getLargestWrittenBytesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			return this.largestWrittenBytesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getLargestReadMessagesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			return this.largestReadMessagesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final double getLargestWrittenMessagesThroughput() {
		this.throughputCalculationLock.lock();
		try {
			return this.largestWrittenMessagesThroughput;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final int getThroughputCalculationInterval() {
		return this.throughputCalculationInterval.get();
	}

	public final long getThroughputCalculationIntervalInMillis() {
		return (this.throughputCalculationInterval.get() * 1000L);
	}

	public final void setThroughputCalculationInterval(
			int throughputCalculationInterval) {
		if (throughputCalculationInterval < 0) {
			throw new IllegalArgumentException(
					"throughputCalculationInterval: "
							+ throughputCalculationInterval);
		}

		this.throughputCalculationInterval.set(throughputCalculationInterval);
	}

	protected final void setLastReadTime(long lastReadTime) {
		this.throughputCalculationLock.lock();
		try {
			this.lastReadTime = lastReadTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	protected final void setLastWriteTime(long lastWriteTime) {
		this.throughputCalculationLock.lock();
		try {
			this.lastWriteTime = lastWriteTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	private void resetThroughput() {
		if (this.service.getManagedSessionCount() == 0) {
			this.readBytesThroughput = 0.0D;
			this.writtenBytesThroughput = 0.0D;
			this.readMessagesThroughput = 0.0D;
			this.writtenMessagesThroughput = 0.0D;
		}
	}

	public void updateThroughput(long currentTime) {
		this.throughputCalculationLock.lock();
		try {
			int interval = (int) (currentTime - this.lastThroughputCalculationTime);
			long minInterval = getThroughputCalculationIntervalInMillis();

			if ((minInterval == 0L) || (interval < minInterval)) {
				return;
			}

			long readBytes = this.readBytes;
			long writtenBytes = this.writtenBytes;
			long readMessages = this.readMessages;
			long writtenMessages = this.writtenMessages;

			this.readBytesThroughput = ((readBytes - this.lastReadBytes) * 1000.0D / interval);
			this.writtenBytesThroughput = ((writtenBytes - this.lastWrittenBytes) * 1000.0D / interval);
			this.readMessagesThroughput = ((readMessages - this.lastReadMessages) * 1000.0D / interval);
			this.writtenMessagesThroughput = ((writtenMessages - this.lastWrittenMessages) * 1000.0D / interval);

			if (this.readBytesThroughput > this.largestReadBytesThroughput) {
				this.largestReadBytesThroughput = this.readBytesThroughput;
			}

			if (this.writtenBytesThroughput > this.largestWrittenBytesThroughput) {
				this.largestWrittenBytesThroughput = this.writtenBytesThroughput;
			}

			if (this.readMessagesThroughput > this.largestReadMessagesThroughput) {
				this.largestReadMessagesThroughput = this.readMessagesThroughput;
			}

			if (this.writtenMessagesThroughput > this.largestWrittenMessagesThroughput) {
				this.largestWrittenMessagesThroughput = this.writtenMessagesThroughput;
			}

			this.lastReadBytes = readBytes;
			this.lastWrittenBytes = writtenBytes;
			this.lastReadMessages = readMessages;
			this.lastWrittenMessages = writtenMessages;
		} finally {
			this.throughputCalculationLock.unlock();
		}
		this.throughputCalculationLock.unlock();
	}

	public final void increaseReadBytes(long nbBytesRead, long currentTime) {
		this.throughputCalculationLock.lock();
		try {
			this.readBytes += nbBytesRead;
			this.lastReadTime = currentTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void increaseReadMessages(long currentTime) {
		this.throughputCalculationLock.lock();
		try {
			this.readMessages += 1L;
			this.lastReadTime = currentTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void increaseWrittenBytes(int nbBytesWritten, long currentTime) {
		this.throughputCalculationLock.lock();
		try {
			this.writtenBytes += nbBytesWritten;
			this.lastWriteTime = currentTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void increaseWrittenMessages(long currentTime) {
		this.throughputCalculationLock.lock();
		try {
			this.writtenMessages += 1L;
			this.lastWriteTime = currentTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final int getScheduledWriteBytes() {
		this.throughputCalculationLock.lock();
		try {
			return this.scheduledWriteBytes;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void increaseScheduledWriteBytes(int increment) {
		this.throughputCalculationLock.lock();
		try {
			this.scheduledWriteBytes += increment;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final int getScheduledWriteMessages() {
		this.throughputCalculationLock.lock();
		try {
			return this.scheduledWriteMessages;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void increaseScheduledWriteMessages() {
		this.throughputCalculationLock.lock();
		try {
			this.scheduledWriteMessages += 1;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	public final void decreaseScheduledWriteMessages() {
		this.throughputCalculationLock.lock();
		try {
			this.scheduledWriteMessages -= 1;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}

	protected void setLastThroughputCalculationTime(
			long lastThroughputCalculationTime) {
		this.throughputCalculationLock.lock();
		try {
			this.lastThroughputCalculationTime = lastThroughputCalculationTime;
		} finally {
			this.throughputCalculationLock.unlock();
		}
	}
}