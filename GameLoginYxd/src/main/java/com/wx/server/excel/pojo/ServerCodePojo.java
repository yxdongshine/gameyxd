package com.wx.server.excel.pojo;

import com.read.tool.excel.Sheet;
import com.read.tool.excel.XlsInt;
import com.read.tool.excel.XlsKey;
import com.read.tool.excel.XlsString;

/**
 * ClassName:ServerCodePojo <br/>
 * Function: TODO (服务器状态码的配置文件). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:42:26 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Sheet("服务器的配置文件")
public class ServerCodePojo {
	
	@XlsKey(name = "id")
	@XlsInt(name = "id")
	private int id;
	@XlsInt(name = "min")
	private int min;
	
	@XlsInt(name = "max")
	private int max;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getMin() {
		return min;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	public int getMax() {
		return max;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
}
