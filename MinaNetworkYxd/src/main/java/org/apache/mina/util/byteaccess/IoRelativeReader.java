/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util.byteaccess;

import java.nio.ByteOrder;
import org.apache.mina.core.buffer.IoBuffer;

public abstract interface IoRelativeReader {
	public abstract int getRemaining();

	public abstract boolean hasRemaining();

	public abstract void skip(int paramInt);

	public abstract ByteArray slice(int paramInt);

	public abstract ByteOrder order();

	public abstract byte get();

	public abstract void get(IoBuffer paramIoBuffer);

	public abstract short getShort();

	public abstract int getInt();

	public abstract long getLong();

	public abstract float getFloat();

	public abstract double getDouble();

	public abstract char getChar();
}