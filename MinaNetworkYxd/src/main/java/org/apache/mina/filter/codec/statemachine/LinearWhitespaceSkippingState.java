/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.statemachine;

public abstract class LinearWhitespaceSkippingState extends SkippingState {
	protected boolean canSkip(byte b) {
		return ((b == 32) || (b == 9));
	}
}