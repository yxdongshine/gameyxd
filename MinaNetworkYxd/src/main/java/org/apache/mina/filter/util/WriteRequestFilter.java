/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.util;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestWrapper;

public abstract class WriteRequestFilter extends IoFilterAdapter {
	public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		Object filteredMessage = doFilterWrite(nextFilter, session,
				writeRequest);
		if ((filteredMessage != null)
				&& (filteredMessage != writeRequest.getMessage()))
			nextFilter.filterWrite(session, new FilteredWriteRequest(
					filteredMessage, writeRequest));
		else
			nextFilter.filterWrite(session, writeRequest);
	}

	public void messageSent(IoFilter.NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		if (writeRequest instanceof FilteredWriteRequest) {
			FilteredWriteRequest req = (FilteredWriteRequest) writeRequest;
			if (req.getParent() == this) {
				nextFilter.messageSent(session, req.getParentRequest());
				return;
			}
		}

		nextFilter.messageSent(session, writeRequest);
	}

	protected abstract Object doFilterWrite(
			IoFilter.NextFilter paramNextFilter, IoSession paramIoSession,
			WriteRequest paramWriteRequest) throws Exception;

	private class FilteredWriteRequest extends WriteRequestWrapper {
		private final Object filteredMessage;

		public FilteredWriteRequest(Object paramObject,
				WriteRequest paramWriteRequest) {
			super(paramWriteRequest);

			if (paramObject == null) {
				throw new IllegalArgumentException("filteredMessage");
			}
			this.filteredMessage = paramObject;
		}

		public WriteRequestFilter getParent() {
			return WriteRequestFilter.this;
		}

		public Object getMessage() {
			return this.filteredMessage;
		}
	}
}