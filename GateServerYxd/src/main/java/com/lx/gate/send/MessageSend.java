package com.lx.gate.send;

import org.apache.commons.logging.Log;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.engine.config.xml.ServerInfoManage;
import com.engine.entityobj.*;
import com.engine.entityobj.ServerPlayer;
import com.engine.service.ConnectorManage;
import com.engine.utils.log.LogUtils;
import com.google.protobuf.Message;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.lx.gate.mina.InnerClient;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:MessageSend <br/>
 * Function: TODO (网关服转发送到各服的消息). <br/>
 * Reason: TODO (内部消息都要转成 InnerMessage,Iobuffer). <br/>
 * Date: 2015-7-3 上午11:41:53 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class MessageSend {
	private static Log log = LogUtils.getLog(MessageSend.class);
	
	/**
	 * sendToWorld:(). <br/>
	 * TODO().<br/>
	 * 游戏服发送消息到世界服
	 * 
	 * @author lyh
	 * @param msg
	 * @param con网关的客户端连接
	 * @param sp
	 */
	public static void sendToWorld(Message msg, IConnect con) {
		IConnect send = ConnectorManage.getConnectFromList(ServerType.WORLD_SERVER);
		if (send == null) {
			log.error("没有世界服务器的连接::" + msg.getDescriptorForType().getName());
			return;
		}
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			InnerMessage.Builder im = InnerMessage.newBuilder();
			im.setClientSessionId(con.getId());
			im.setBody(msg.toByteString());
			im.setGateTypeId(ServerInfoManage.curServerInfo.serverTypeId);
			im.setCommand(head);
			byte data[] = im.build().toByteArray();
			IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.putInt(data.length);
			buffer.put(data);
			buffer.flip();
			send.send(buffer);
		} else {
			log.error("发送到世界服务器有错误::" + msg.getDescriptorForType().getName());
		}
	}
	
	/**
	 * sendToGate:(). <br/>
	 * TODO().<br/>
	 * 发送到游戏服务器
	 * 
	 * @author lyh
	 * @param msg
	 * @param con
	 * @param sp
	 */
	public static void sendToGame(Message msg, IConnect con, ServerPlayer sp) {
		IConnect send = ConnectorManage.getConnectFromList(ServerType.GAME_SERVER);
		if (send == null) {
			log.error("没有游戏服务器的连接::" + msg.getDescriptorForType().getName());
			return;
		}
		
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			InnerMessage.Builder im = InnerMessage.newBuilder();
			im.setClientSessionId(con.getId());
			im.setBody(msg.toByteString());
			im.setGateTypeId(ServerInfoManage.curServerInfo.serverTypeId);
			im.setCommand(head);
			byte data[] = im.build().toByteArray();
			IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.putInt(data.length);
			buffer.put(data);
			buffer.flip();
			send.send(buffer);
		} else {
			log.error("发送到游戏服务器有错误::" + msg.getDescriptorForType().getName());
		}
	}
	
	/**
	 * sendGateToClient:(). <br/>
	 * TODO().<br/>
	 * 发送到客户端
	 * 
	 * @author lyh
	 * @param msg
	 * @param sessionClientId
	 */
	public static void sendGateToClient(Message msg, long sessionClientId) {
		IConnect send = ConnectorManage.getMinaConnect(sessionClientId);
		if (send == null) {
			log.error("没有sessionid连接::" + sessionClientId);
			return;
		}
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			MinaMessage.Builder im = MinaMessage.newBuilder();
			im.setBody(msg.toByteString());
			im.setCommand(head);
			byte data[] = im.build().toByteArray();
			IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.putInt(data.length);
			buffer.put(data);
			buffer.flip();
			send.send(buffer);
		} else {
			log.error("发送客户端有错误::" + msg.getDescriptorForType().getName());
		}
	}
}
