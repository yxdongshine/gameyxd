package com.lx.world.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.entityobj.ServerPlayer;
import com.engine.executor.OrderedQueuePoolExecutor;
import com.engine.msgloader.CommondClassLoader;
import com.engine.worker.AbstractWork;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.logical.GameMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:InnerServerControl <br/>
 * Function: TODO (内部服务逻辑控件). <br/>
 * Reason: TODO (职责: 游戏服务器逻辑对同一角色id的逻辑同步). <br/>
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
	public static OrderedQueuePoolExecutor serverQueuePool = new OrderedQueuePoolExecutor("worldQueuePool", 10);
	
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(InnerMessage msg, IConnect con) {
		long startTime = System.currentTimeMillis();
		try {
			// int serverType = (msg.getCommand() >> 16) % 10;
			// IConnect clientIConnect = connectorManage.getMinaConnect(msg.getClientSessionId());
			// //内转外
			// if (clientIConnect != null && serverType > 0) {// 发到客户端
			// // 封装成iobuffer
			// MinaMessage.Builder builder = MinaMessage.newBuilder();
			// builder.setCommand(msg.getCommand());
			// builder.setBody(msg.getBody());
			//
			// byte data[] = builder.build().toByteArray();
			// IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			// buffer.putInt(data.length);
			// buffer.put(data);
			// buffer.flip();
			// clientIConnect.send(buffer);
			// } else {
			// 对roleid同步
			
			if (msg.getRoleId() > 0) {
				// 判断是否在角色容器里
				ServerPlayer sp = GlogalContainer.getRolesMap().get(msg.getRoleId());
				if (sp == null) {
					log.error(Integer.toHexString(msg.getCommand()) + ":::InnerServer不能处理的消息:::" + msg.getClientSessionId());
					return;
				}
				final InnerMessage im = msg;
				final IConnect connect = con;
				serverQueuePool.addTask(msg.getRoleId(), new AbstractWork() {
					
					@Override
					public void doWork() {
						// TODO Auto-generated method stub
						GameMessage<InnerMessage> task = (GameMessage<InnerMessage>) commond.get(im.getCommand());
						if (task != null) {
							try {
								task.doMessage(im, connect);
							} catch (Exception e) {
								e.printStackTrace();
								log.error(Integer.toHexString(im.getCommand()) + ":::错误消息:::" + e, e);
							}
						} else {
							log.error(Integer.toHexString(im.getCommand()) + ":::InnerServer不能处理的消息:::" + im.getClientSessionId());
						}
					}
				});
			} else {
				
				GameMessage<InnerMessage> task = (GameMessage<InnerMessage>) commond.get(msg.getCommand());
				if (task != null) {
					task.doMessage(msg, con);
				} else {
					log.error(Integer.toHexString(msg.getCommand()) + ":::InnerServer不能处理的消息:::" + msg.getClientSessionId());
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
