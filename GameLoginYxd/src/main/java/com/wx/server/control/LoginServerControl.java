package com.wx.server.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;
import com.wx.server.logical.GameMessage;
import com.wx.server.msgloader.CommondClassLoader;
import com.wx.server.utils.LogUtils;

/**
 * ClassName:LoginServerControl <br/>
 * Function: TODO (逻辑控制总类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-6 下午3:54:30 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class LoginServerControl implements IBeanInvoke<MinaMessage> {
	private LogUtils log = LogUtils.getLog(LoginServerControl.class);
	
	@Autowired
	private CommondClassLoader commond;
	
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(MinaMessage msg, IConnect net) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		try {
			
			GameMessage<MinaMessage> task = (GameMessage<MinaMessage>) commond.get(msg.getCommand());
			if (task != null) {
				task.doMessage(msg, net);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(Integer.toHexString(msg.getCommand()) + ":::错误消息:::" + e, e);
		}
		
		finally {
			long endTime = System.currentTimeMillis();
			// 处理时间大于1秒有问题
			if (endTime - startTime >= 1000) {
				log.error((endTime - startTime) + "处理时间超过1秒的是:::" + Integer.toHexString(msg.getCommand()));
			}
		}
	}
	
}
