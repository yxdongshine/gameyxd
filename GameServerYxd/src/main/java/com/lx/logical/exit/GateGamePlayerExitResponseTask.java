package com.lx.logical.exit;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.InnerToMessage.GateGamePlayerExitRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.control.InnerClientControl;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.BuffManage;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.GamePlayerManage;
import com.lx.logical.manage.SpaceManage;
import com.lx.logical.manage.TeamGameManager;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:GateWorldPlayerExitResponseTask <br/>
 * Function: TODO (处理角色退出). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午10:19:10 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.GATE_GAME_PLAYER_EXIT_REQUEST_VALUE)
public class GateGamePlayerExitResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private GamePlayerManage gamePlayerManage;
	@Autowired
	private FuBenManage fuBenManage;
	@Autowired
	private TeamGameManager teamGameManager;
	@Autowired
	private BuffManage buffMange;
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 角色退出
		GateGamePlayerExitRequest req = GateGamePlayerExitRequest.parseFrom(msg.getBody());
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().remove(req.getSessionId());
		if (sp != null) {
			GameGlogalContainer.getRolesMap().remove(sp.getId());
			Role role = sp.getRole();
			InnerClientControl.clientQueuePool.removeFromPool(role.getId());
			// 角色离线
			fuBenManage.roleOffline(sp);
			// 保存buff数据
			buffMange.saveBuffToDb(sp);
			
			gamePlayerManage.sendGameWorldUpdatePlayerRequest(sp);
			// 保存背包数据
			sp.offIineSaveBagData(dao);
			// 保存任务数据
			sp.saveTaskData(dao);
			//下线判断是否是小队中最后一个
			teamGameManager.roleOffline(sp, sp.getRole().getId());
			
			sp.getRole().setLoginOutTime(new Timestamp(System.currentTimeMillis()));
			dao.updateFinal(role);
			log.error(role.getRoleName() + ":角色保存成功::" + role.getFuBenId());
		}
		
	}
	
}
