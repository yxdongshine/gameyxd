package com.lx.logical.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.game.login.LoginGameServer.NewPlayerRoleGuideResponse;
import com.loncent.protocol.inner.InnerToMessage.GameGateEnterGameSuccessResponse;
import com.loncent.protocol.inner.InnerToMessage.WorldGameEnterGameRequest;
import com.lx.game.fight.FightManage;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.BuffManage;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.GamePlayerManage;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.logical.manage.PlayerManage;
import com.lx.logical.manage.SpaceManage;
import com.lx.logical.manage.TaskManage;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:WorldGameEnterGameResponseTask <br/>
 * Function: TODO (角色进入游戏). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午10:32:31 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.WORLD_GAME_ENTER_GAME_REQUEST_VALUE)
public class WorldGameEnterGameResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private PlayerManage playerManage;
	@Autowired
	private FightManage fightManage;
	@Autowired
	private ItemLogicManage itemLogicManage;
	@Autowired
	private GamePlayerManage gamePlayerManage;
	@Autowired
	private TaskManage taskManage;
	@Autowired
	private FuBenManage fuBenManage;
	@Autowired
	private BuffManage buffManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		WorldGameEnterGameRequest request = WorldGameEnterGameRequest.parseFrom(msg.getBody());
		Role role = JSON.parseObject(request.getRoleInfo(), Role.class);
		// Role role = JsonObjectMapper.readMapperObjectByContent(request.getRoleInfo(), Role.class);
		ServerPlayer sp = playerManage.createServerPlayer(msg, role, false);
		fuBenManage.willEnterFuBenMaps.put(role.getId(), role.getFuBenId());
		// TOD 角色上线成功后续操作
		sp.setIFightListener(fightManage);
		// 通知网关服务器进入游戏成功
		GameGateEnterGameSuccessResponse resp = GameGateEnterGameSuccessResponse.newBuilder().setRoleId(role.getId()).build();
		MessageSend.sendToGate(resp, sp);
		// 加载buff数据
		buffManage.loadRoleBuffFromDb(sp);
		// 发送角色属性
		gamePlayerManage.sendEnterGameRoleInfoResponse(sp);
		// 初始化玩家信息
		itemLogicManage.loadBagData(sp, dao);
		// 发送背包数据
		MessageSend.sendToGate(itemLogicManage.getBagItemInfo(sp.getBag()).build(), sp);
		// 初始化玩家任务模块
		taskManage.loadTaskData(sp);
		// 发送已经接没有做完的任务
		MessageSend.sendToGate(taskManage.buildOpenRoleTaskResponse(sp), sp);
		// 发送可见或者可接的任务列表
		MessageSend.sendToGate(taskManage.buildRoleCanAcceptTaskResponse(sp), sp);
		// 发送新手引导信息
		MessageSend.sendToGate(buildNewPlayerRoleGuideResponse(sp).build(), sp);
		
		
	}
	
	/**
	 * 
	 * buildNewPlayerRoleGuideResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 * @return
	 */
	public NewPlayerRoleGuideResponse.Builder buildNewPlayerRoleGuideResponse(ServerPlayer sp) {
		NewPlayerRoleGuideResponse.Builder responseBulider = NewPlayerRoleGuideResponse.newBuilder();
		responseBulider.setAllGuidePos(sp.getRole().getGuideInfo());
		return responseBulider;
	}
}
