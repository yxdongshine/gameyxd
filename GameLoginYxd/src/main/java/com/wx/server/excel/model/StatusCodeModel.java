package com.wx.server.excel.model;

import org.springframework.stereotype.Component;

import com.read.excel.ResourceModelImpl;
import com.wx.server.excel.pojo.StatusCodePojo;
import com.wx.server.res.ExcelPath;

/**
 * ClassName:StatusCodeModel <br/>
 * Function: TODO (状态码(处理类)). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:46:05 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class StatusCodeModel extends ResourceModelImpl<StatusCodePojo> {
	
	public StatusCodeModel() {
		super(ExcelPath.STATUT_CODE, StatusCodePojo.class);
	}
	
	@Override
	public void load() throws Exception {
		// TODO Auto-generated method stub
		String[] sheet = new String[] { "StatusCode" };
		this.load(sheet);
		
	}
	
	/**
	 * findStatusCodePojoById:(). <br/>
	 * TODO().<br/>
	 * 查找状态码
	 * 
	 * @author lyh
	 * @param id
	 * @return
	 */
	public StatusCodePojo findStatusCodePojoById(int id) {
		StatusCodePojo pojo = lookupEntry(id);
		return pojo;
	}
	
}
