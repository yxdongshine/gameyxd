/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.statemachine;

public abstract class ConsumeToLinearWhitespaceDecodingState extends
		ConsumeToDynamicTerminatorDecodingState {
	protected boolean isTerminator(byte b) {
		return ((b == 32) || (b == 9));
	}
}