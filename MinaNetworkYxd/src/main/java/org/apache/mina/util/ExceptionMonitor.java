/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util;

public abstract class ExceptionMonitor {
	private static ExceptionMonitor instance = new DefaultExceptionMonitor();

	public static ExceptionMonitor getInstance() {
		return instance;
	}

	public static void setInstance(ExceptionMonitor monitor) {
		if (monitor == null) {
			monitor = new DefaultExceptionMonitor();
		}

		instance = monitor;
	}

	public abstract void exceptionCaught(Throwable paramThrowable);
}