/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.executor;

import org.apache.mina.core.session.IoEvent;

public abstract interface IoEventSizeEstimator {
	public abstract int estimateSize(IoEvent paramIoEvent);
}