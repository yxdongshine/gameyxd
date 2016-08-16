package com.lx.game.send;

import org.apache.commons.logging.Log;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.engine.container.GlogalContainer;
import com.engine.entityobj.ServerPlayer;
import com.engine.service.ConnectorManage;
import com.engine.utils.log.LogUtils;
import com.google.protobuf.Message;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.game.mina.InnerClient;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * ClassName:MessageSend <br/>
 * Function: TODO (游戏服务器发送到各服的消息). <br/>
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
	 * @param con
	 * @param sp
	 */
	public static void sendToWorld(Message msg, ServerPlayer sp) {
		IConnect send = ConnectorManage.getConnectFromList(ServerType.WORLD_SERVER);
		if (send == null) {
			log.error("没有世界服务器的连接::" + msg.getDescriptorForType().getName());
			return;
		}
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			InnerMessage.Builder im = InnerMessage.newBuilder();
			im.setClientSessionId(sp.getClientSessionId());
			im.setBody(msg.toByteString());
			im.setGateTypeId(sp.getGateServerTypeId());
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
	 * sendToLog:(). <br/>
	 * TODO().<br/>
	 * 发送到Log
	 * 
	 * @author lyh
	 * @param msg
	 * @param sp
	 */
	public static void sendToLog(Message msg, ServerPlayer sp) {
		IConnect con = ConnectorManage.getConnectFromList(ServerType.LOG_SERVER);
		if (con == null) {
			log.error("没有log服务器的连接::" + msg.getDescriptorForType().getName());
			return;
		}
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			InnerMessage.Builder im = InnerMessage.newBuilder();
			im.setClientSessionId(sp.getClientSessionId());
			im.setBody(msg.toByteString());
			im.setGateTypeId(sp.getGateServerTypeId());
			im.setRoleId(sp.getRole().getId());
			
			im.setCommand(head);
			byte data[] = im.build().toByteArray();
			IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.putInt(data.length);
			buffer.put(data);
			buffer.flip();
			con.send(buffer);
		} else {
			log.error("发送到log服务器有错误::" + msg.getDescriptorForType().getName());
		}
	}
	
	/**
	 * sendToGate:(). <br/>
	 * TODO().<br/>
	 * 发送到网关有两种情况，一种是通过网关转发到客户端一种是在网关处理
	 * 
	 * @author lyh
	 * @param msg
	 * @param con
	 * @param sp
	 */
	public static void sendToGate(Message msg, ServerPlayer sp) {
		IConnect con = ConnectorManage.getConnectFromListTypeId(sp.getGateServerTypeId());
		if (con == null) {
			log.error("没有log服务器的连接::" + msg.getDescriptorForType().getName());
			return;
		}
		ServerPlayer ssp = GlogalContainer.getRolesMap().get(sp.getId());
		if (ssp == null){
			log.error("角色不存在!,不能发送!"+sp.getId());
			return;
		}
		Integer head = InnerClient.protocolMap.get(msg.getDescriptorForType().getName());
		if (head != null) {
			InnerMessage.Builder im = InnerMessage.newBuilder();
			im.setClientSessionId(sp.getClientSessionId());
			im.setBody(msg.toByteString());
			im.setGateTypeId(sp.getGateServerTypeId());
			im.setCommand(head);
			byte data[] = im.build().toByteArray();
			IoBuffer buffer = IoBuffer.allocate(data.length + NetConst.CODE_LENGTH);
			buffer.putInt(data.length);
			buffer.put(data);
			buffer.flip();
			con.send(buffer);
		} else {
			log.error("发送到网关服务器有错误::" + msg.getDescriptorForType().getName());
		}
	}
}
