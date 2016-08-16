package com.lx.logical.world.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.Role;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.game.login.LoginGameServer.CreateRoleResponse;
import com.loncent.protocol.game.login.LoginGameServer.RoleListResponse;
import com.loncent.protocol.game.login.LoginGameServer.RoleMessageData;
import com.loncent.protocol.inner.CheckToken.CheckTokenRequest;
import com.lx.server.mina.session.IConnect;
import com.lx.world.send.MessageSend;

/**
 * ClassName:LoginManage <br/>
 * Function: TODO (登录管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午9:24:04 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class LoginManage {
	
	@Autowired
	private EntityDAO dao;
	
	/** 最大角色数量 **/
	public final static int MAX_ROLE_NUM = 3;
	
	/**
	 * sendCheckTokenRequest:(). <br/>
	 * TODO().<br/>
	 * 发送令牌验证消息
	 * 
	 * @author lyh
	 * @param userName
	 * @param token
	 * @param con
	 */
	public void sendCheckTokenRequest(String userName, long token, IConnect con) {
		CheckTokenRequest.Builder request = CheckTokenRequest.newBuilder();
		request.setAccountName(userName);
		request.setToken(token);
		con.send(request.build());
		
	}
	
	/**
	 * createRoleResult:(). <br/>
	 * TODO().<br/>
	 * 创建角色结果
	 * 
	 * @author lyh
	 * @param result
	 * @return
	 */
	public CreateRoleResponse createRoleResult(int result) {
		CreateRoleResponse.Builder build = CreateRoleResponse.newBuilder();
		build.setResult(result);
		return build.build();
	}
	
	/**
	 * processRoleList:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param userName
	 * @param innerMsg
	 * @param con
	 */
	public void processRoleList(String userName, InnerMessage innerMsg, IConnect con) {
		RoleListResponse.Builder builder = RoleListResponse.newBuilder();
		// 账号从据库找到角色数据
		List<Role> roleList = dao.findByProperty(Role.class, "userName", userName);
		if (roleList.size() > 0) {
			
			for (Role role : roleList) {
				
				RoleMessageData bmData = createRoleMessageData(role);
				builder.addData(bmData);
			}
		}
		MessageSend.sendToGate(builder.build(), con, innerMsg.getClientSessionId(), innerMsg.getGateTypeId());
	}
	
	/**
	 * createRoleMessageData:(). <br/>
	 * TODO().<br/>
	 * 创建角色数据
	 * 
	 * @author lyh
	 * @param role
	 * @return
	 */
	private RoleMessageData createRoleMessageData(Role role) {
		RoleMessageData.Builder data = RoleMessageData.newBuilder();
		// data.setRoleCareer(role.)
		data.setRoleId(role.getId());
		data.setCareerConfigId(role.getCareerConfigId());// 暂定了1
		data.setRoleName(role.getRoleName());
		data.setRoleLevel(role.getRoleLevel());
		data.setRoleTalent("天下无双");//
		return data.build();
	}
	
	/**
	 * enterGame:(). <br/>
	 * TODO().<br/>
	 * 进入游戏
	 * 
	 * @author lyh
	 * @param role
	 * @param innerMsg
	 */
	public void enterGame(Role role, InnerMessage innerMsg) {
		
	}
}
