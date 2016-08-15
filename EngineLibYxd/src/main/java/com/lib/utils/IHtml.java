package com.lib.utils;

import java.util.Properties;

/**
 * ClassName:IHtml <br/>
 * Function: TODO (Html模板). <br/>
 * Reason: TODO (). <br/>
 * Date: 2014-3-11 上午10:39:30 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IHtml {
	// Properties properties =
	// ServerProperties.loadGameProperties(IPropertiesResPath.LANGUAGE_PACK_PATH);
	
	String FONT = "<font color='%s'>%s</font>";
	
	String HREF = "<a href='event:%s@%s'>%s</a>";
	String A_HREF = "<a href= '%s'>%s</a>";
}
