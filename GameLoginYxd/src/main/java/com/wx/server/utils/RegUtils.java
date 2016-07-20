package com.wx.server.utils;

/**
 * ClassName:RegUtils <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午6:16:01 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class RegUtils {
	
	/**
	 * validPassword:(). <br/>
	 * TODO().<br/>
	 * 验证密码
	 * 
	 * @author lyh
	 * @param text
	 * @param game
	 * @return
	 */
	public static boolean validPassword(String text) {
		String regstr = "[a-z0-9A-Z]{6,16}";
		return text != null && text.matches(regstr);
	}
}
