package com.lx.logical.task;

import javax.print.attribute.standard.Severity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.task.Task.RoleCheckTaskRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TaskManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RoleCheckTaskRequestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-6 上午11:47:32 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_ROLE_CHECK_TASK_REQUEST_VALUE)
public class RoleCheckTaskRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	TaskManage taskManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleCheckTaskRequest builder = RoleCheckTaskRequest.parseFrom(msg.getBody());
			int taskId = builder.getTaskId();
			if (serverPlayer.getTaskModular().getTaskHashmap().containsKey(taskId)) {
				MessageSend.sendToGate(taskManage.buildTaskData(serverPlayer.getTaskModular().getTaskHashmap().get(taskId)), serverPlayer);
			}
		}
	}
	
}
