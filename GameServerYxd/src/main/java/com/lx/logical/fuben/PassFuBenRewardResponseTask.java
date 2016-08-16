package com.lx.logical.fuben;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.FuBenManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:PassFuBenRewardResponseTask <br/>
 * Function: (暂用，客户端告知通关). <br/>
 * Date: 2015-8-24 下午5:55:30 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_PASS_FUBEN_REWARD_REQUEST_VALUE)
public class PassFuBenRewardResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private FuBenManage fuBenManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		FuBen fuBen = fuBenManage.getFuBen(role.getFuBenId());
		fuBen.endFuBen();
	}
	
}
