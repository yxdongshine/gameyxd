/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util.byteaccess;

import org.apache.mina.core.buffer.IoBuffer;

public class SimpleByteArrayFactory implements ByteArrayFactory {
	public ByteArray create(int size) {
		if (size < 0) {
			throw new IllegalArgumentException(
					"Buffer size must not be negative:" + size);
		}
		IoBuffer bb = IoBuffer.allocate(size);
		ByteArray ba = new BufferByteArray(bb) {
			public void free() {
			}
		};
		return ba;
	}
}