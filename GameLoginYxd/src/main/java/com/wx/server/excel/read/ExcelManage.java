package com.wx.server.excel.read;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wx.server.excel.model.ServerCodeModel;
import com.wx.server.excel.model.StatusCodeModel;
import com.wx.server.utils.LogUtils;

/**
 * ClassName:ExcelManage <br/>
 * Function: TODO (excel文件管理器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:57:03 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ExcelManage {
	private static LogUtils log = LogUtils.getLog(ExcelManage.class);
	@Autowired
	private ServerCodeModel serverCodeModel;
	
	public void load() {
		try {
			serverCodeModel.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
