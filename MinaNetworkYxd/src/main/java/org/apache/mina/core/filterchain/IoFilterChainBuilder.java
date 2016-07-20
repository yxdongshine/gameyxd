/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.filterchain;

public abstract interface IoFilterChainBuilder {
	public static final IoFilterChainBuilder NOOP = new IoFilterChainBuilder() {
		public void buildFilterChain(IoFilterChain chain) throws Exception {
		}

		public String toString() {
			return "NOOP";
		}
	};

	public abstract void buildFilterChain(IoFilterChain paramIoFilterChain)
			throws Exception;
}