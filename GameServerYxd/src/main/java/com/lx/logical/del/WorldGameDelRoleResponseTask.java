package com.lx.logical.del;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.InnerToMessage.WorldGameDelRoleRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:WorldGameDelRoleResponseTask <br/>
 * Function: TODO (世界服通知游戏服删除角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午11:46:42 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.WORLD_GAME_DEL_ROLE_REQUEST_VALUE)
public class WorldGameDelRoleResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		WorldGameDelRoleRequest req = WorldGameDelRoleRequest.parseFrom(msg.getBody());
		
	}
	
}
