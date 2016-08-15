package com.lib.utils;

/**
 * ClassName:TheadUtils <br/>
 * Function: TODO (线程模型工具类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 上午9:54:17 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class TheadUtils {
	
	/**
	 * sleep:(). <br/>
	 * TODO().<br/>
	 * 停止多少时间(毫秒)
	 * 
	 * @author lyh
	 * @param millis 毫秒
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
