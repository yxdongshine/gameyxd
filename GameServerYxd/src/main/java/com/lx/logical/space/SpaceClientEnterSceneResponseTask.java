package com.lx.logical.space;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.lib.utils.IConst;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceClientEnterSceneResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.BuffManage;
import com.lx.logical.manage.FuBenManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SpaceClientEnterSceneRequestTask <br/>
 * Function: (客户端请求进入场景). <br/>
 * Date: 2015-7-27 上午10:58:00 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_ENTER_SCENE_REQUEST_VALUE)
public class SpaceClientEnterSceneResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	private static Log log = LogFactory.getLog(SpaceClientEnterSceneResponseTask.class);
	
	@Autowired
	private FuBenManage fuBenManage;
	@Autowired
	private BuffManage buffManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		long oldFuBenId = sp.getFuBenId();
		long newFuBenId = fuBenManage.willEnterFuBenMaps.get(role.getId());
		
		FuBen fuBen = fuBenManage.getFuBen(newFuBenId);
		FuBen oldFuBen = fuBenManage.getFuBen(oldFuBenId);
		if (fuBen == null || oldFuBen == null)
			return;
		
		Position3D pos = new Position3D();
		
		if (oldFuBen.getFubenPojo().getType() != IConst.FUBEN_TYPE_WORLD 
			&& fuBen.getFubenPojo().getType() == IConst.FUBEN_TYPE_WORLD) {
			// 从组队副本或者单人副本返回世界副本
			fuBenManage.revertRoleInfo(role, oldFuBenId);
		} else if (newFuBenId == oldFuBenId) {
			// 上线
			pos.setX(role.getX());
			pos.setY(role.getY());
			pos.setZ(role.getZ());
		} else if(oldFuBen.getFubenPojo().getType() == IConst.FUBEN_TYPE_WORLD
			&& fuBen.getFubenPojo().getType() != IConst.FUBEN_TYPE_WORLD) {
			// 从世界副本进入组队副本或者单人副本
			fuBenManage.recordRoleInfo(role, newFuBenId);
			pos = fuBen.getSpace().getSpaceInfo().birthPlace;
			role.setX(pos.getX());
			role.setY(pos.getY());
			role.setZ(pos.getZ());
		}else 
		{
			pos = fuBen.getSpace().getSpaceInfo().birthPlace;
			role.setX(pos.getX());
			role.setY(pos.getY());
			role.setZ(pos.getZ());
		}
		
		fuBenManage.roleExitFuBen(sp);
		fuBenManage.transRoleToFuBen(sp, newFuBenId);
		
		SpaceClientEnterSceneResponse.Builder repMsg = SpaceClientEnterSceneResponse.newBuilder();
		repMsg.setRetCode(0);
		// 获取出生点
		PbPosition3D.Builder pbPos = PbPosition3D.newBuilder();
		pbPos.setX(role.getX());
		pbPos.setY(role.getY());
		pbPos.setZ(role.getZ());
		
		repMsg.setBirthPlace(pbPos);
		
		// TODO测试buff
		//buffManage.addBuff(sp, PbAoiEntityType.Role_VALUE, 1);
		
		MessageSend.sendToGate(repMsg.build(), sp);
	}
	
}
