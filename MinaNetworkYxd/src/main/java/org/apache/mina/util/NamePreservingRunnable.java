/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamePreservingRunnable implements Runnable {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(NamePreservingRunnable.class);
	private final String newName;
	private final Runnable runnable;

	public NamePreservingRunnable(Runnable runnable, String newName) {
		this.runnable = runnable;
		this.newName = newName;
	}

	public void run() {
		Thread currentThread = Thread.currentThread();
		String oldName = currentThread.getName();

		if (this.newName != null) {
			setName(currentThread, this.newName);
		}
		try {
			this.runnable.run();
		} finally {
			setName(currentThread, oldName);
		}
	}

	private void setName(Thread thread, String name) {
		try {
			thread.setName(name);
		} catch (SecurityException se) {
			if (LOGGER.isWarnEnabled())
				LOGGER.warn("Failed to set the thread name.", se);
		}
	}
}