package com.wx.server.utils;

/**
 * ClassName:StatusCode <br/>
 * Function: TODO (状态码). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:06:40 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class StatusCode {
	/** 用户名密码不能为空 **/
	public static final int STATUS_1 = 0x0001;
	/** 你输入的账号或密码不合法 **/
	public static final int STATUS_2 = 0x0002;
	/** 该用户名已存在 **/
	public static final int STATUS_3 = 0x0003;
	/** 用户名或者密码错误 **/
	public static final int STATUS_4 = 0x0004;
	/** 密码修改成功 **/
	public static final int STATUS_5 = 0x0005;
	/** 用户不存在 **/
	public static final int STATUS_6 = 0x0006;
	
	/** 服务器正在维护 **/
	public static final int STATUS_7 = 0x0007;
	
}
