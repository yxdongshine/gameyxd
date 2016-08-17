package com.lx.logical.role;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.OpenRoleAttrScreenResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenRoleAttrScreenResponseTask <br/>
 * Function: TODO (打开角色属性界面). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-15 上午10:47:28 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_OPEN_ROLE_ATTR_SCREEN_RESPONSE_VALUE)
public class OpenRoleAttrScreenResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		OpenRoleAttrScreenResponse resp = OpenRoleAttrScreenResponse.parseFrom(msg.getBody());
		log.error("武神酒:::" + resp.getWarlordWine());
		log.error("悟道茶:::" + resp.getEnlightenmentTea());
		
	}
	
}
