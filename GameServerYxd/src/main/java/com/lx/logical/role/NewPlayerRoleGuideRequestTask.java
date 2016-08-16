package com.lx.logical.role;

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.login.LoginGameServer.NewPlayerRoleGuideRequest;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.util.MathUtil;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:NewPlayerRoleGuideRequestTask <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-29 下午4:16:02 <br/>
 * 
 * @author yxd wq wq wq
 * @see
 */
@Component
@Head(CmdType.C_S_NEW_ROLEPALYER_GUIDE_REQUEST_VALUE)
public class NewPlayerRoleGuideRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		if (serverPlayer != null) {
			NewPlayerRoleGuideRequest builder = NewPlayerRoleGuideRequest.parseFrom(msg.getBody());
			serverPlayer.getRole().setGuideInfo(MathUtil.displacementNumber(serverPlayer.getRole().getGuideInfo(), builder.getGuideIndex()));
			// 临时保存 测试
			dao.saveOrUpdate(serverPlayer.getRole());
		}
	}
	
}
