package com.lx.world.start;

import java.io.File;

/**
 * ClassName:StartGame <br/>
 * Function: TODO (启动世界服务器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午11:28:19 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class WorldStart {
	
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
		ServerStart.main(new String[] { "41", f.getParent() + File.separator + "res", "WorldLog" });
		// ServerInfoManage.loadContext(null);
	}
	
}
