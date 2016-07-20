/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.buffer;

import java.nio.ByteBuffer;

public abstract interface IoBufferAllocator {
	public abstract IoBuffer allocate(int paramInt, boolean paramBoolean);

	public abstract ByteBuffer allocateNioBuffer(int paramInt,
			boolean paramBoolean);

	public abstract IoBuffer wrap(ByteBuffer paramByteBuffer);

	public abstract void dispose();
}