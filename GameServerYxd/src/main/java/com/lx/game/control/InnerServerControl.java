package com.lx.game.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.CommondClassLoader;
import com.engine.service.ConnectorManage;
import com.lib.utils.BitUitls;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.logical.GameMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:InnerServerControl <br/>
 * Function: TODO (内部服务逻辑控件). <br/>
 * Reason: TODO (暂时不用). <br/>
 * Date: 2015-7-2 上午10:15:37 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class InnerServerControl implements IBeanInvoke<InnerMessage> {
	
	// TODO Auto-generated method stub
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private CommondClassLoader commond;
	
	@Override
	public void invoke(InnerMessage msg, IConnect con) {
		long startTime = System.currentTimeMillis();
		try {
			// 协议头不要后16位再取4位二进制
			int serverType = BitUitls.bitToInt(Integer.toBinaryString((msg.getCommand() >> 16)), 4);
			IConnect clientIConnect = ConnectorManage.getMinaConnect(msg.getClientSessionId());
			// 内转外
			if (clientIConnect != null && serverType > 0) {// 发到客户端
				// 封装成iobuffer
				MinaMessage.Builder builder = MinaMessage.newBuilder();
				builder.setCommand(msg.getCommand());
				builder.setBody(msg.getBody());
				
				byte data[] = builder.build().toByteArray();
				IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
				buffer.putInt(data.length);
				buffer.put(data);
				buffer.flip();
				clientIConnect.send(buffer);
			} else {
				@SuppressWarnings("unchecked")
				GameMessage<InnerMessage> task = (GameMessage<InnerMessage>) commond.get(msg.getCommand());
				if (task != null) {
					task.doMessage(msg, con);
				} else {
					log.error(Integer.toHexString(msg.getCommand()) + ":::GameInnerServer不能处理的消息:::" + msg.getClientSessionId());
				}
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
