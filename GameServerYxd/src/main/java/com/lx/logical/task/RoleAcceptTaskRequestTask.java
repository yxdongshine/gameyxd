package com.lx.logical.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.TaskData;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.task.Task.RoleAcceptTaskRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TaskManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RoleAcceptTaskRequestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-4 下午7:56:28 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_ROLE_ACCEPT_TASK_REQUEST_VALUE)
public class RoleAcceptTaskRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private TaskManage taskManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleAcceptTaskRequest builder = RoleAcceptTaskRequest.parseFrom(msg.getBody());
			int taskId = builder.getTaskId();
			if (!serverPlayer.getTaskModular().getTaskHashmap().containsKey(taskId)) {
				TaskData taskData = taskManage.acceptTask(serverPlayer, taskId);
				if (taskData != null) {// 接收了
					MessageSend.sendToGate(taskManage.buildRoleAcceptTaskResponse(taskManage.buildTaskData(taskData)), serverPlayer);
					// 发送可见或者可接的任务列表
					MessageSend.sendToGate(taskManage.buildRoleCanAcceptTaskResponse(serverPlayer), serverPlayer);
				} else {// 背包空间不足
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_E), serverPlayer);
				}
			} else {
				log.error(taskId + "===任务已经接了");
			}
		}
	}
	
}
