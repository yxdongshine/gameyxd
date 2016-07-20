/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.file;

import java.nio.channels.FileChannel;

public abstract interface FileRegion {
	public abstract FileChannel getFileChannel();

	public abstract long getPosition();

	public abstract void update(long paramLong);

	public abstract long getRemainingBytes();

	public abstract long getWrittenBytes();

	public abstract String getFilename();
}