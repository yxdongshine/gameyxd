/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.session.IoSession;

public abstract interface ProtocolDecoderOutput {
	public abstract void write(Object paramObject);

	public abstract void flush(IoFilter.NextFilter paramNextFilter,
			IoSession paramIoSession);
}