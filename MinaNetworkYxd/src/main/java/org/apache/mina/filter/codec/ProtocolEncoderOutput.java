/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import org.apache.mina.core.future.WriteFuture;

public abstract interface ProtocolEncoderOutput {
	public abstract void write(Object paramObject);

	public abstract void mergeAll();

	public abstract WriteFuture flush();
}