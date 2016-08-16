package com.lx.logical.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.msgloader.Head;
import com.engine.service.ConnectorManage;
import com.lib.utils.ServerType;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.DelRoletRequest;
import com.loncent.protocol.inner.InnerToMessage.WorldGameDelRoleRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.world.manage.LoginManage;
import com.lx.server.mina.session.IConnect;
import com.lx.world.send.MessageSend;

/**
 * ClassName:DelRoletResponseTask <br/>
 * Function: TODO (删除角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午3:10:14 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_DEL_ROLE_REQUEST_VALUE)
public class DelRoleResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private LoginManage loginManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		DelRoletRequest qeuest = DelRoletRequest.parseFrom(msg.getBody());
		
		if (qeuest.getRoleId() > 0) {
			Role role = dao.findById(Role.class, qeuest.getRoleId());
			if (role != null) {
				
				dao.delete(role);
				// 公会,竞技场等
				// 返回列表
				loginManage.processRoleList(role.getUserName(), msg, session);
				IConnect con = ConnectorManage.getConnectFromList(ServerType.GATE_SERVER);
				if (con != null) {
					MessageSend.sendToGame(createWorldGameDelRoleRequest(role), con, msg.getClientSessionId(), msg.getGateTypeId());
				}
			} else {
				log.error("删除不成功:" + qeuest.getRoleId());
			}
		}
	}
	
	private WorldGameDelRoleRequest createWorldGameDelRoleRequest(Role role) {
		WorldGameDelRoleRequest req = WorldGameDelRoleRequest.newBuilder().setRoleId(role.getId()).build();
		return req;
	}
}
