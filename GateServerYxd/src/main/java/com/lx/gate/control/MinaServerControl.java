package com.lx.gate.control;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.ServerInfoManage;
import com.engine.msgloader.CommondClassLoader;
import com.engine.service.ConnectorManage;
import com.lib.utils.BitUitls;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.logical.GameMessage;
import com.lx.server.mina.codec.IBeanInvoke;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:InnerServerControl <br/>
 * Function: TODO (mina服务逻辑控件). <br/>
 * Reason: TODO (职责: 1:转发到相关的服务器(带上sessionid). 2:处理与客户端的心跳等消息. ). <br/>
 * Date: 2015-7-2 上午10:15:37 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MinaServerControl implements IBeanInvoke<MinaMessage> {
	
	// TODO Auto-generated method stub
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private CommondClassLoader commond;
	
	@SuppressWarnings("unchecked")
	@Override
	public void invoke(MinaMessage msg, IConnect con) {
		long startTime = System.currentTimeMillis();
		try {
			// 协议头不要后16位再取个位数
			// 协议头不要后16位再取4位二进制
			int serverType = BitUitls.bitToInt(Integer.toBinaryString((msg.getCommand() >> 16)), 4);
			// log.error("gateServerType::" + serverType);
			// 外网转内网
			if (serverType == ServerType.WORLD_SERVER || serverType == ServerType.GAME_SERVER) {// 转发世界服,游戏服务器
				IConnect sCon = ConnectorManage.getConnectFromList(serverType);
				if (sCon == null) {
					log.error("gateServerType::" + serverType);
					return;
				}
				InnerMessage.Builder builder = InnerMessage.newBuilder();
				builder.setCommand(msg.getCommand());
				builder.setBody(msg.getBody());
				builder.setGateTypeId(ServerInfoManage.curServerInfo.serverTypeId);
				builder.setRoleId(con.getRoleId());
				builder.setClientSessionId(con.getId());
				byte data[] = builder.build().toByteArray();
				IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
				buffer.putInt(data.length);
				buffer.put(data);
				buffer.flip();
				
				sCon.send(buffer);
			} else {
				
				GameMessage<MinaMessage> task = (GameMessage<MinaMessage>) commond.get(msg.getCommand());
				if (task != null) {
					task.doMessage(msg, con);
				} else {
					log.error(Integer.toHexString(msg.getCommand()) + ":::GateMinaServer不能处理的消息:::");
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
