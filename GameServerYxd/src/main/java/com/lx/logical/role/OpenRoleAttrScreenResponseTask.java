package com.lx.logical.role;

import org.springframework.stereotype.Component;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.OpenRoleAttrScreenRequest;
import com.loncent.protocol.game.player.Role.OpenRoleAttrScreenResponse;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenRoleAttrScreenResponseTask <br/>
 * Function: TODO (打开角色属性界面). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-14 下午8:35:48 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_OPEN_ROLE_ATTR_SCREEN_REQUEST_VALUE)
public class OpenRoleAttrScreenResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		OpenRoleAttrScreenRequest request = OpenRoleAttrScreenRequest.parseFrom(msg.getBody());
		ServerPlayer sp = getServerPlayerByteSessionId(msg.getClientSessionId());
		if (sp != null) {
			sendOpenRoleAttrScreenResponse(sp);
		}
	}
	
	/**
	 * sendOpenRoleAttrScreenResponse:(). <br/>
	 * TODO().<br/>
	 * 返回角色属性界面
	 * 
	 * @author lyh
	 * @param sp
	 */
	private void sendOpenRoleAttrScreenResponse(ServerPlayer sp) {
		Attribute attr = sp.getAttribute();
		OpenRoleAttrScreenResponse.Builder resp = OpenRoleAttrScreenResponse.newBuilder();
		resp.setAir(attr.getAttribute(Attribute.AIR));
		resp.setAgility(attr.getAttribute(Attribute.AGILITY));
		resp.setDamage(attr.damage());
		resp.setTenacity(attr.getAttribute(Attribute.TENACITY));
		resp.setDefence(attr.getAttribute(Attribute.DEFENCE));
		resp.setExternalForceAttack(attr.getAttribute(Attribute.EXTERNAL_FORCE_ATTACK));
		resp.setInnerForceAttack(attr.getAttribute(Attribute.INNER_FORCE_ATTACK));
		resp.setInnerForce(attr.getAttribute(Attribute.INNER_FORCE));
		resp.setWarlordWine(1000);
		resp.setEnlightenmentTea(10000);
		MessageSend.sendToGate(resp.build(), sp);
	}
}
