package com.lx.logical.role;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.OpenRoleDetailAttrScreenResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenRoleDetailAttrScreenResponseTask <br/>
 * Function: TODO (角色属性详情). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-16 下午2:59:38 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_OPEN_ROLE_DETAIL_ATTR_SCREEN_RESPONSE_VALUE)
public class OpenRoleDetailAttrScreenResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		OpenRoleDetailAttrScreenResponse resp = OpenRoleDetailAttrScreenResponse.parseFrom(msg.getBody());
		for (AttributeData data : resp.getDataList()) {
			log.error("属性类型::" + data.getAttrType() + "::" + data.getAttrVal());
		}
	}
	
}
