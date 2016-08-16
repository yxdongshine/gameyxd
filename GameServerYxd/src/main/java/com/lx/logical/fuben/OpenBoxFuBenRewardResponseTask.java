package com.lx.logical.fuben;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.fuben.FuBen.OpenBoxFuBenRewardRequest;
import com.loncent.protocol.game.fuben.FuBen.OpenBoxFuBenRewardResponse;
import com.loncent.protocol.game.fuben.FuBen.OpenBoxFuBenRewardResponse.RewardItem;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:OpenBoxFuBenRewardResponseTask <br/>
 * Function: (开副本宝箱). <br/>
 * Date: 2015-8-25 下午3:34:35 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_OPEN_BOX_FUBEN_REWARD_REQUEST_VALUE)
public class OpenBoxFuBenRewardResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private ItemLogicManage itemMamange;
	@Autowired
	private FuBenManage fuBenManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
		
		OpenBoxFuBenRewardRequest reqMsg = OpenBoxFuBenRewardRequest.parseFrom(msg.getBody());
		int count = reqMsg.getOpenCount();
		
		FuBen fuBen = fuBenManage.getFuBen(role.getFuBenId());
		
		int curCount = fuBen.getRoleOpenCount(role.getId());
		// TODO 判断条件
		if (curCount > 4) {
			log.error("out of count");
			return;
		}
		
		// 扣除花费
		fuBen.costOpenBox(sp, count);
		fuBen.addOpenBoxCount(role.getId(), count);
		// 发放奖励
		Map<Integer, Integer> itemMaps = fuBen.getBoxReward(role.getId(), count);
		
		OpenBoxFuBenRewardResponse.Builder repMsg = OpenBoxFuBenRewardResponse.newBuilder();
		for (Map.Entry<Integer, Integer> entry : itemMaps.entrySet()) {
			RewardItem.Builder build = RewardItem.newBuilder();
			build.setItemId(entry.getKey());
			build.setNum(entry.getValue());
			repMsg.addItemRewards(build);
			// 发放奖励
			itemMamange.putItemInBagByPropertyId(entry.getKey(), entry.getValue(), sp);
		}
		
		MessageSend.sendToGate(repMsg.build(), sp);
	}
	
}
