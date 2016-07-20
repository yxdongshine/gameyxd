package com.wx.server.excel.pojo;

import com.read.tool.excel.Sheet;
import com.read.tool.excel.XlsInt;
import com.read.tool.excel.XlsKey;
import com.read.tool.excel.XlsString;

/**
 * ClassName:StatusCodePojo <br/>
 * Function: TODO (状态码的配置文件). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午5:42:26 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Sheet("状态码的配置文件")
public class StatusCodePojo {
	
	@XlsKey(name = "id")
	@XlsInt(name = "id")
	private int id;
	/** 状态码类型 **/
	@XlsInt(name = "type")
	private int type;
	
	/** 状态码内容 **/
	@XlsString(name = "content")
	private String content;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
