package com.lx.client.start;

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
public class ClientStart {
	
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
		ServerStart.main(new String[] { "61", f.getParent() + File.separator + "Engine", "clientlog" });
		// ServerInfoManage.loadContext(null);
	}
	
}
