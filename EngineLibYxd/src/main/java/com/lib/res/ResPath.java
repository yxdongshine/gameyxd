package com.lib.res;

/**
 * ClassName:ResPath <br/>
 * Function: TODO (资源文件的路径). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-25 下午4:52:10 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface ResPath {
	
	/** 分隔符 **/
	public static final String SEPARATE = "/";
	
	public static final String RES = UserDir.USER_DIR.replace("\\conf", "\\");
}
