package com.lx.logical.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.lib.utils.LockObject;
import com.lib.utils.LockUtils;
import com.lib.utils.ServerUUID;
import com.lib.utils.ToolUtils;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.CreateRoleRequest;
import com.loncent.protocol.game.login.LoginGameServer.RoleListResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.world.manage.LoginManage;
import com.lx.logical.world.manage.ServerPlayerManage;
import com.lx.server.mina.session.IConnect;
import com.lx.world.container.WorldGlogalContainer;
import com.lx.world.data.CareerTalentData;
import com.lx.world.send.MessageSend;

/**
 * ClassName:CreateRoleResponseTask <br/>
 * Function: TODO (创建角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午3:13:07 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_CREATE_ROLE_REQUEST_VALUE)
public class CreateRoleResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private LoginManage loginManage;
	@Autowired
	private ServerPlayerManage serverPlayerManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		CreateRoleRequest request = CreateRoleRequest.parseFrom(msg.getBody());
		
		if (ToolUtils.isStringNull(request.getRoleName())) {
			MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_9), session, msg.getClientSessionId(), msg.getGateTypeId());
			MessageSend.sendToGate(loginManage.createRoleResult(0), session, msg.getClientSessionId(), msg.getGateTypeId());
			return;
		}
		LockObject lockObj = LockUtils.getLockObject(request.getUserName());
		try {
			lockObj.lock();
			// 有可能有并发
			List<Role> list = dao.findByProperty(Role.class, "userName", request.getUserName());
			if (list.size() >= LoginManage.MAX_ROLE_NUM) {
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_C), session, msg.getClientSessionId(), msg.getGateTypeId());
				MessageSend.sendToGate(loginManage.createRoleResult(0), session, msg.getClientSessionId(), msg.getGateTypeId());
				return;
			}
			
			// 判断角色名称是否重复
			List<Role> roleNamelist = dao.findByProperty(Role.class, "roleName", request.getRoleName());
			for (Role role : roleNamelist) {
				if (role.getRoleName().equals(request.getRoleName())) {
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_8), session, msg.getClientSessionId(), msg.getGateTypeId());
					MessageSend.sendToGate(loginManage.createRoleResult(0), session, msg.getClientSessionId(), msg.getGateTypeId());
					return;
				}
			}
			
			int namelen = 0;
			boolean hasIllegalChar = false;
			String name = request.getRoleName();
			for (int i = 0, ilen = name.length(); i < ilen; i++) {
				char namechar = name.charAt(i);
				if (namechar == '\n' || namechar == '\r' || namechar == '\t') {
					// 名字中有换行字符
					hasIllegalChar = true;
				}
				if (namechar <= 0x2000) {// 中文占两个宽度,英文等只占一个宽度
					namelen++;
				} else {
					namelen += 2;
				}
			}
			if (name == null || name.length() == 0 || namelen > 12) {
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_A), session, msg.getClientSessionId(), msg.getGateTypeId());
				MessageSend.sendToGate(loginManage.createRoleResult(0), session, msg.getClientSessionId(), msg.getGateTypeId());
			} else if (hasIllegalChar/* || BadWords.containsBadName(name) */
			    || name.indexOf(' ') >= 0 || name.indexOf('　') >= 0) {// 有空格
				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_B), session, msg.getClientSessionId(), msg.getGateTypeId());
				MessageSend.sendToGate(loginManage.createRoleResult(0), session, msg.getClientSessionId(), msg.getGateTypeId());
			}
			
			// 没有判断角名是否合法
			CareerTalentData data = WorldGlogalContainer.getCareertalentmap().remove(msg.getClientSessionId());
			if (data != null) {
				Role role = createRole(request, data);
				
				// 创建角色
				ServerPlayer sp = serverPlayerManage.createServerPlayer(msg, role, true);
				sp.setDir(sp.getRoleInitPojo().getDir());
				sp.setPosition3D(sp.getRoleInitPojo().getX(), sp.getRoleInitPojo().getY(), sp.getRoleInitPojo().getZ());
				role.setX(sp.getRoleInitPojo().getX());
				role.setY(sp.getRoleInitPojo().getY());
				role.setZ(sp.getRoleInitPojo().getZ());
				role.setFuBenId(sp.getRoleInitPojo().getMapId());
				role.setMapUid(sp.getRoleInitPojo().getMapId());
				
				
				dao.save(role);
				// 进入游戏
				MessageSend.sendToGate(loginManage.createRoleResult(1), session, msg.getClientSessionId(), msg.getGateTypeId());
				// 创建角色
				MessageSend.sendToGame(serverPlayerManage.createWorldGameEnterGameRequest(role), sp, false);
			} else {
				log.error("职业资质没有数据");
			}
		} catch (Exception e) {
			log.error("登录出异常了:", e);
		} finally {
			lockObj.unlock();
		}
	}
	
	/**
	 * createRole:(). <br/>
	 * TODO().<br/>
	 * 创建角色
	 * 
	 * @author lyh
	 * @param request
	 * @return
	 */
	private Role createRole(CreateRoleRequest request, CareerTalentData data) {
		Role role = new Role();
		role.setId(ServerUUID.getUUID());
		role.setCareerConfigId(data.getCareerConfigId());
		role.setRoleName(request.getRoleName());
		role.setUserName(request.getUserName());
		role.setServerId("" + request.getServerId());
		role.setRoleLevel(1);
		role.setAgility(data.getAgility());
		role.setAir(data.getAir());
		role.setControl(data.getControl());
		role.setDefence(data.getDefence());
		role.setInnerForce(data.getInnerForce());
		role.setTenacity(data.getTenacity());
		role.setGuideInfo(0);
		return role;
	}
	
}
