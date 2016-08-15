package com.engine.properties;

import com.lib.res.ResPath;

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
	
	String PATH = ResPath.RES + "/config/";
	
	String GM = PATH + "gm" + PROPERITES;
	
	String BOSS = PATH + "boss" + PROPERITES;
	
	String DAILY_BUFFER = PATH + "daily_buffer" + PROPERITES;
	
	String ENCRYPT_DECRYPT = PATH + "decrypt_encrypt" + PROPERITES;
	
	String LANGUAGE_PACK_PATH = PATH + ServerGameConfig.LANGUAGE_PACK_PATH + TXT;
	
	String FESTIVAL_ACTIVITIES_TIME = PATH + "festival_activities_time" + TXT;
	
	String ACTIIVITY_TIME = PATH + "activity" + TXT;
}
