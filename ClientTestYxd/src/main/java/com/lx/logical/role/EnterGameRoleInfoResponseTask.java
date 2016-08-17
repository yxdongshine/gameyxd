package com.lx.logical.role;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.EnterGameRoleInfoResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:EnterGameRoleInfoResponseTask <br/>
 * Function: TODO (角色属性). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-15 下午8:16:39 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_ENTER_GAME_ROLE_INFO_RESPONSE_VALUE)
public class EnterGameRoleInfoResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		EnterGameRoleInfoResponse resp = EnterGameRoleInfoResponse.parseFrom(msg.getBody());
		log.error(resp.getRoleName() + ":::收到人物属性:::" + resp.getRoleLevel());
	}
	
}
