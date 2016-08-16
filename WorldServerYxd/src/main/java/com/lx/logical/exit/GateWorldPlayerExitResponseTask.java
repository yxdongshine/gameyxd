package com.lx.logical.exit;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.InnerToMessage.GateWorldPlayerExitRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;
import com.lx.world.container.WorldGlogalContainer;
import com.lx.world.control.InnerServerControl;

/**
 * ClassName:GateWorldPlayerExitResponseTask <br/>
 * Function: TODO (处理角色退出). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午10:19:10 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.GATE_WORLD_PLAYER_EXIT_REQUEST_VALUE)
public class GateWorldPlayerExitResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		GateWorldPlayerExitRequest req = GateWorldPlayerExitRequest.parseFrom(msg.getBody());
		WorldGlogalContainer.getCareertalentmap().remove(req.getSessionId());
		WorldGlogalContainer.getRolesMap().remove(req.getSessionId());
		ServerPlayer sp = WorldGlogalContainer.getSessionServerPlayerMap().remove(req.getSessionId());
		if (sp != null) {
			InnerServerControl.serverQueuePool.removeFromPool(sp.getRole().getId());
		}
	}
	
}
