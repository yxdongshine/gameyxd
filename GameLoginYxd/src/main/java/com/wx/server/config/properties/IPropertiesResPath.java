package com.wx.server.config.properties;

import com.wx.server.res.ResPath;

/**
 * ClassName:IPropertiesResPath <br/>
 * Function: TODO (配置文件properties路径). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-5-15 上午10:46:49 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IPropertiesResPath {
	String PROPERITES = ".properties";
	String TXT = ".txt";
	String GAME_P = ResPath.USER_DIR + "/game" + PROPERITES;
}
