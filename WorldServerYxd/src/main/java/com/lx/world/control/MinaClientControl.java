package com.lx.world.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.CommondClassLoader;
import com.engine.service.ConnectorManage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.logical.GameMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:InnerServerControl <br/>
 * Function: TODO (这个是连接登录服用的). <br/>
 * Reason: TODO (暂时不用). <br/>
 * Date: 2015-7-2 上午10:15:37 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaClientControl implements IBeanInvoke<MinaMessage> {
	
	// TODO Auto-generated method stub
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private CommondClassLoader commond;
	
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(MinaMessage msg, IConnect con) {
		long startTime = System.currentTimeMillis();
		try {
			
			GameMessage<MinaMessage> task = (GameMessage<MinaMessage>) commond.get(msg.getCommand());
			if (task != null) {
				task.doMessage(msg, con);
			} else {
				log.error(Integer.toHexString(msg.getCommand()) + ":::worldMinaClient不能处理的消息:::");
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
