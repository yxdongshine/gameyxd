package com.lx.game.start;

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
public class GameStart {
	
	public static final String resPath = "EngineYxd\\src\\main\\resources\\conf";

	/**
	 * main:(). <br/>
	 * TODO().<br/>
	 * `
	 * 
	 * @author lyh
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File(System.getProperty("user.dir"));
		ServerStart.main(new String[] { "31", f.getParent() + File.separator + resPath, "GameLog" });
		// ServerInfoManage.loadContext(null);
	}
	
}
