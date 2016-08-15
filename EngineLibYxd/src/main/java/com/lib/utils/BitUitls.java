package com.lib.utils;

/**
 * ClassName:BitUitls <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 上午9:06:49 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class BitUitls {
	/**
	 * bitToInt:(). <br/>
	 * TODO().<br/>
	 * 把二进制转换成10进制(int)
	 * 
	 * @author lyh
	 * @param bi二进制字符串
	 * @param _len转化的二进制长度
	 * @return
	 */
	public static int bitToInt(String bi, int _len) {
		// int len = bi.length();
		int len = _len;
		int sum = 0;
		int tmp, max = (bi.length() < len ? bi.length() : len) - 1;
		int index = (bi.length() - len) < 0 ? 0 : (bi.length() - len);
		for (int i = index; i < bi.length(); ++i) {
			tmp = bi.charAt(i) - '0';
			sum += tmp * Math.pow(2, max--);
		}
		return sum;
	}
}
