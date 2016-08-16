package com.lx.logical.world.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.lib.utils.JsonObjectMapper;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.inner.InnerToMessage.WorldGameEnterGameRequest;
import com.lx.nserver.model.AttributeModel;
import com.lx.nserver.model.CareerModel;
import com.lx.nserver.model.RoleInitModel;
import com.lx.nserver.txt.CareerPojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ServerPlayerManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (角色管理类). <br/>
 * Date: 2015-7-9 下午5:35:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class ServerPlayerManage {
	
	@Autowired
	private CareerModel careerModel;
	
	@Autowired
	private RoleInitModel roleInitModel;
	
	/**
	 * sendWorldGameEnterGameRequest:(). <br/>
	 * TODO().<br/>
	 * 通知游戏服角色进入游戏
	 * 
	 * @author lyh
	 * @param role
	 * @param con
	 */
	public WorldGameEnterGameRequest createWorldGameEnterGameRequest(Role role) {
		WorldGameEnterGameRequest.Builder build = WorldGameEnterGameRequest.newBuilder();
		String json = JSON.toJSONString(role);
		// String json = JsonObjectMapper.writeMapperObject(role);
		build.setRoleInfo(json);
		return build.build();
	}
	
	/**
	 * createServerPlayer:(). <br/>
	 * TODO().<br/>
	 * 创建角色
	 * 
	 * @author lyh
	 * @param msg
	 * @param role
	 * @param bCreate
	 * @return
	 */
	public ServerPlayer createServerPlayer(InnerMessage msg, Role role, boolean bCreate) {
		ServerPlayer sp = new ServerPlayer();
		sp.setClientSessionId(msg.getClientSessionId());
		sp.setGateServerTypeId(msg.getGateTypeId());
		sp.setRole(role);
		CareerPojo pojo = careerModel.get(role.getCareerConfigId());
		sp.setCareerPojo(pojo);
		sp.setRoleInitPojo(roleInitModel.get(1));
		sp.setUsePoint(role.getRoleLevel() * sp.getRoleInitPojo().getUpPoint());
		role.setHp(pojo.getInitHp());
		role.setMp(pojo.getInitMp());
		return sp;
	}
}
