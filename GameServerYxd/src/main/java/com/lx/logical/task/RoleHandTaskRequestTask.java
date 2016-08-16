package com.lx.logical.task;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.task.Task.RoleHandTaskRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.send.MessageSend;
import com.lx.game.task.TaskStaticDataConfig;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.TaskManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:RoleHandTaskRequestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-5 下午2:36:26 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_ROLE_HAND_TASK_REQUEST_VALUE)
public class RoleHandTaskRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	TaskManage taskManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			RoleHandTaskRequest builder = RoleHandTaskRequest.parseFrom(msg.getBody());
			int taskId = builder.getTaskId();
			if (serverPlayer.getTaskModular().getTaskHashmap().containsKey(taskId)) {
				int reslut = taskManage.handTaskBalance(taskId, serverPlayer);
				if (reslut >= 0) {
					// 删除该任务
					taskManage.delTask(serverPlayer, taskId);
					// 发送可见或者可接的任务列表
					MessageSend.sendToGate(taskManage.buildRoleCanAcceptTaskResponse(serverPlayer), serverPlayer);
					// 发送客户端完成该任务
					MessageSend.sendToGate(taskManage.buildRoleHandTaskResponse(taskId, TaskStaticDataConfig.TASK_STATE_FINSHED_HANDED), serverPlayer);
					
				} else {// 背包没有装下 提示任务提交失败
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_E), serverPlayer);
				}
			} else {
				log.error("没有该任务");
			}
		}
	}
	
}
