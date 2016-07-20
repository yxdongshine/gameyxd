/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.session;

import org.apache.mina.core.write.WriteRequestQueue;

public abstract interface IoSessionDataStructureFactory {
	public abstract IoSessionAttributeMap getAttributeMap(
			IoSession paramIoSession) throws Exception;

	public abstract WriteRequestQueue getWriteRequestQueue(
			IoSession paramIoSession) throws Exception;
}