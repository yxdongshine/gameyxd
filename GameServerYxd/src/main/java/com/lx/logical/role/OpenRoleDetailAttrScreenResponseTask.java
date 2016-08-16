package com.lx.logical.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.OpenRoleDetailAttrScreenRequest;
import com.loncent.protocol.game.player.Role.OpenRoleDetailAttrScreenResponse;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.GlobalMsgManage;
import com.lx.nserver.manage.TxtModelManage;
import com.lx.nserver.model.AttributeDescModel;
import com.lx.nserver.txt.AttributeDescPojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenRoleDetailAttrScreenResponseTask <br/>
 * Function: TODO (查看角色详细属性). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-15 下午8:20:17 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_OPEN_ROLE_DETAIL_ATTR_SCREEN_REQUEST_VALUE)
public class OpenRoleDetailAttrScreenResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private AttributeDescModel attributeDescModel;
	
	/** 伤害类型 **/
	private int damageType = 86;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		OpenRoleDetailAttrScreenRequest req = OpenRoleDetailAttrScreenRequest.parseFrom(msg.getBody());
		ServerPlayer sp = GlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		// 发送详细数据
		if (sp != null) {
			sendOpenRoleDetailAttrScreenResponse(sp);
		} else {
			log.error("角色对象为空!!!!");
		}
	}
	
	/**
	 * sendOpenRoleDetailAttrScreenResponse:(). <br/>
	 * TODO().<br/>
	 * 发送角色属性详细消息
	 * 
	 * @author lyh
	 * @param sp
	 */
	public void sendOpenRoleDetailAttrScreenResponse(ServerPlayer sp) {
		Attribute attr = sp.getAttribute();
		OpenRoleDetailAttrScreenResponse.Builder resp = OpenRoleDetailAttrScreenResponse.newBuilder();
		for (AttributeDescPojo pojo : attributeDescModel.getReslList()) {
			if (pojo.getBDisplay() == 1) {
				if (pojo.getAttrType() == damageType) {
					AttributeData data = GlobalMsgManage.createAttributeData(damageType, attr.damage());
					resp.addData(data);
				} else {
					resp.addData(GlobalMsgManage.createAttributeData(pojo.getAttrType(), attr.getAttribute(pojo.getAttrType())));
				}
			}
		}
		
		MessageSend.sendToGate(resp.build(), sp);
	}
	
}
