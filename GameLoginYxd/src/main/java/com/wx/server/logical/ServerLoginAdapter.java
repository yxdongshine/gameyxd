package com.wx.server.logical;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.stauscode.StatusCode.StatusCodeResponse;
import com.lx.server.mina.session.IConnect;
import com.wx.server.container.GlobalContainer;
import com.wx.server.dbdao.EntityDAO;
import com.wx.server.dbdao.EntityDAOInterface;
import com.wx.server.utils.LogUtils;

/**
 * ClassName:ServerLogicalAdapter <br/>
 * Function: TODO (服务器逻辑适配器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-8 下午2:29:24 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
/**
 * ClassName: ServerLoginAdapter <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-10 上午11:02:20 <br/>
 * 
 * @author lyh
 * @version
 */
@Component
public class ServerLoginAdapter {
	
	protected LogUtils log = LogUtils.getLog(this.getClass());
	
	@Autowired(required=true)
	protected EntityDAOInterface entityDAOInterface;
	
	@Autowired
	protected GlobalContainer globalContainer;
	
	/**
	 * sendPopUpTip:(). <br/>
	 * TODO().<br/>
	 * 发送弹出框信息
	 * 
	 * @author lyh
	 * @param net
	 * @param statusCodeId
	 */
	public void sendPopUpTip(IConnect net, int statusCodeId) {
		// StatusCodePojo pojo = statusCodeModel.findStatusCodePojoById(statusCodeId);
		// if (pojo != null){
		StatusCodeResponse scr = StatusCodeResponse.newBuilder().setPopstr(statusCodeId).build();
		net.send(scr);
		// }
	}
}
