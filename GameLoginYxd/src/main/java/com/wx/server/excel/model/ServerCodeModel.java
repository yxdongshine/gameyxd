package com.wx.server.excel.model;

import org.springframework.stereotype.Component;

import com.read.excel.ResourceModelImpl;
import com.wx.server.excel.pojo.ServerCodePojo;
import com.wx.server.res.ExcelPath;

/**
 * ClassName:StatusCodeModel <br/>
 * Function: TODO (服务器状态码(处理类)). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:46:05 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ServerCodeModel extends ResourceModelImpl<ServerCodePojo> {
	
	public ServerCodeModel() {
		super(ExcelPath.SERVER_CODE, ServerCodePojo.class);
	}
	
	@Override
	public void load() throws Exception {
		// TODO Auto-generated method stub
		String[] sheet = new String[] { "ServerCode" };
		this.load(sheet);
	}
	
	/**
	 * findStatusCodePojoById:(). <br/>
	 * TODO().<br/>
	 * 查找服务器状态码
	 * 
	 * @author lyh
	 * @param id
	 * @return
	 */
	public ServerCodePojo findServerCodePojoByCount(int count) {
		for (ServerCodePojo pojo : getReslList()) {
			if (count >= pojo.getMin() && count <= pojo.getMax()) {
				return pojo;
			}
		}
		return null;
	}
	
}
