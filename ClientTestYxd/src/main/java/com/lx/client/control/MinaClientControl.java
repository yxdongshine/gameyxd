package com.lx.client.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.entityobj.ServerPlayer;
import com.engine.executor.OrderedQueuePoolExecutor;
import com.engine.msgloader.CommondClassLoader;
import com.engine.worker.AbstractWork;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.logical.GameMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:InnerServerControl <br/>
 * Function: TODO (内部客户端逻辑控件). <br/>
 * Reason: TODO (). <br/>
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
	
	private OrderedQueuePoolExecutor queuePool = new OrderedQueuePoolExecutor("gameQueuePool", 10);
	
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(MinaMessage msg, IConnect con) {
		long startTime = System.currentTimeMillis();
		try {
			
			GameMessage<MinaMessage> task = (GameMessage<MinaMessage>) commond.get(msg.getCommand());
			if (task != null) {
				task.doMessage(msg, con);
			} else {
				log.error(Integer.toHexString(msg.getCommand()) + ":::client不能处理的消息:::");
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
