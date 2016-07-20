/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

public class EncryptAndDecryptCode {
	public static byte[] encrypt(byte[] input, int[] sign) {
		if ((input == null) || (input.length < 1) || (sign == null)
				|| (sign.length < 3)) {
			return null;
		}

		byte k = (byte) sign[0];
		int c1 = sign[1];
		int c2 = sign[2];
		for (int i = 0; i < input.length; ++i) {
			input[i] = (byte) (input[i] ^ k);
		}

		return input;
	}

	public static byte[] decrypt(byte[] input, int[] sign) {
		if ((input == null) || (input.length < 1) || (sign == null)
				|| (sign.length < 3)) {
			return null;
		}

		byte k = (byte) sign[0];
		int c1 = sign[1];
		int c2 = sign[2];
		int previous = 0;
		for (int i = 0; i < input.length; ++i) {
			previous = input[i];

			input[i] = (byte) (input[i] ^ k);
		}

		return input;
	}
}