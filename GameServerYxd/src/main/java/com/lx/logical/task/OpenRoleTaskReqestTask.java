package com.lx.logical.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TaskManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenRoleTaskReqestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午6:44:08 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_OPEN_ROLE_TASK_REQUEST_VALUE)
public class OpenRoleTaskReqestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private TaskManage taskManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			// 发送已经接没有做完的任务
			MessageSend.sendToGate(taskManage.buildOpenRoleTaskResponse(serverPlayer), serverPlayer);
			// 发送可见或者可接的任务列表
			MessageSend.sendToGate(taskManage.buildRoleCanAcceptTaskResponse(serverPlayer), serverPlayer);
		}
	}
}
