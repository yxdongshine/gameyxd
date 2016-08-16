package com.lx.logical.space;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.map.GateInfo;
import com.engine.domain.Role;
import com.engine.entityobj.PlayerStatus;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.lib.utils.IConst;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceClientEnterGateRequest;
import com.loncent.protocol.game.space.Space.SpaceClientEnterGateResponse;
import com.loncent.protocol.game.space.Space.SpaceClientEnterGateUpdateResponse;
import com.loncent.protocol.game.space.Space.SpaceClientEnterGateUpdateResponse.MapStatus;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.SpaceManage;
import com.lx.nserver.txt.FuBenTemplatePojo;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SpaceClientEnterGateResponseTask <br/>
 * Function: 客户端进入传送阵. <br/>
 * Date: 2015-7-25 下午5:29:41 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_ENTER_GATE_REQUEST_VALUE)
public class SpaceClientEnterGateResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	private static Log log = LogFactory.getLog(SpaceClientEnterGateResponseTask.class);
	
	@Autowired
	private SpaceManage spaceManage;
	@Autowired
	private FuBenManage fuBenManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		SpaceClientEnterGateRequest reqMsg = SpaceClientEnterGateRequest.parseFrom(msg.getBody());
		SpaceClientEnterGateResponse.Builder repMsg = SpaceClientEnterGateResponse.newBuilder();
		
		long gateId = reqMsg.getGateId();
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		// 获取传送门类型
		GateInfo gateInfo = spaceManage.getGateInfo(role.getFuBenId(), (int) gateId);
		if (gateInfo == null) {
			log.error("error gateId = " + gateId);
			return;
		}
		if (gateInfo.type == GateInfo.GATE_TYPE_WORLD) {
			long fuBenId = gateInfo.toTargetId;
			int mapUid = (int)fuBenId;
			/*
			FuBen fuBen = fuBenManage.getFuBen(role.getFuBenId());
			
			if (fuBen.getFubenPojo().getType() != IConst.FUBEN_TYPE_WORLD) {
				// 如果是在单人副本或者组队副本中的传送门
				Role preRole = fuBenManage.getPreRoleInfo(role.getId());
				if (preRole != null) {
					fuBenId = preRole.getFuBenId();
				}
			}
			*/
			if(fuBenManage.getFuBen(fuBenId) == null)
			{
				FuBenTemplatePojo pojo = fuBenManage.getFuBenPojo(mapUid);
				if(pojo.getType() == IConst.FUBEN_TYPE_SINGLE)
				{
					// 创建副本
					FuBen fuBen = fuBenManage.createSingleFuBen(mapUid);
					fuBen.startFuBen();
					fuBenId = fuBen.getFubenId();
					mapUid = fuBen.getFubenPojo().getMapUid();
				} else if(pojo.getType() == IConst.FUBEN_TYPE_TEAM)
				{
				}
			}
			
			repMsg.setRetCode(1);
			repMsg.setMapUid(mapUid);
			fuBenManage.willEnterFuBenMaps.put(role.getId(), fuBenId);
			
			sp.setStatus(PlayerStatus.STATUS_TRANS_MAP);
			MessageSend.sendToGate(repMsg.build(), sp);
			
		} else if (gateInfo.type == GateInfo.GATE_TYPE_FUBEN) {
			SpaceClientEnterGateUpdateResponse.Builder repFuBenMsg = SpaceClientEnterGateUpdateResponse.newBuilder();
			// 获取挂载副本
			ArrayList<Integer> passed = sp.getPassFuBenIds();
			for (int mapUid : gateInfo.fuBenMapUid) {
				MapStatus.Builder mapStatus = MapStatus.newBuilder();
				mapStatus.setMapUid(mapUid);
				if (!passed.contains(mapUid))// 未通关
					mapStatus.setIsPassed(0);
				else
					mapStatus.setIsPassed(1);
				
				mapStatus.setIsLock(1);
				repFuBenMsg.addMapList(mapStatus);
			}
			
			MessageSend.sendToGate(repFuBenMsg.build(), sp);
		}
	}
	
}
