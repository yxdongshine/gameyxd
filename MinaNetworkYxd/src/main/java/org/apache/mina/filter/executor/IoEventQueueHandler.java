/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.executor;

import java.util.EventListener;
import org.apache.mina.core.session.IoEvent;

public abstract interface IoEventQueueHandler extends EventListener {
	public static final IoEventQueueHandler NOOP = new IoEventQueueHandler() {
		public boolean accept(Object source, IoEvent event) {
			return true;
		}

		public void offered(Object source, IoEvent event) {
		}

		public void polled(Object source, IoEvent event) {
		}
	};

	public abstract boolean accept(Object paramObject, IoEvent paramIoEvent);

	public abstract void offered(Object paramObject, IoEvent paramIoEvent);

	public abstract void polled(Object paramObject, IoEvent paramIoEvent);
}