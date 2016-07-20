package com.wx.server.start;

import java.io.File;

/**
 * ClassName:StartGame <br/>
 * Function: TODO (启动游戏服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:28:19 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class LoginStart {
	
	/**
	 * main:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File(System.getProperty("user.dir"));
		LoginServer.main(new String[] { "11" });
		// ServerInfoManage.loadContext(null);
	}
	
}
