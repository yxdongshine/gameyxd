package com.lx.logical.space;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.PlayerStatus;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.SpaceManage;
import com.lx.server.mina.session.IConnect;
import com.loncent.protocol.game.space.Space.SpaceClientMoveRequest;

/**
 * ClassName:SpaceClientMoveResponseTask <br/>
 * Function: (客户端移动响应). <br/>
 * Date: 2015-7-16 上午11:36:56 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_MOVE_REQUEST_VALUE)
public class SpaceClientMoveResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private SpaceManage spaceManage;
	
	private Log log = LogFactory.getLog(SpaceClientMoveResponseTask.class);
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		SpaceClientMoveRequest reqMsg = SpaceClientMoveRequest.parseFrom(msg.getBody());
		
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (sp == null)
			return;
		if (sp.getStatus() == PlayerStatus.STATUS_CHANGE_MAP) {
			log.error("change map no move!!!");
			return;
		}
		if (sp.getSpace() == null)
			return;
		//死亡不能移动
		if (sp.isDie()){
			return;
		}
		// Role role = sp.getRole();
		spaceManage.clientMove(sp, reqMsg.getMoveEntity());
	}
	
}
