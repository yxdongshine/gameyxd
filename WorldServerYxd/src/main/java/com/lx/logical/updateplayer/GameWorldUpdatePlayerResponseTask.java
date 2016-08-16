package com.lx.logical.updateplayer;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.engine.domain.Role;
import com.engine.msgloader.Head;
import com.lib.utils.JsonObjectMapper;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.InnerToMessage.GameWorldUpdatePlayerRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:GameWorldUpdatePlayerResponse <br/>
 * Function: TODO (更新角色信息). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午11:27:39 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.GAME_WORLD_UPDATE_PLAYER_REQUEST_VALUE)
public class GameWorldUpdatePlayerResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		GameWorldUpdatePlayerRequest request = GameWorldUpdatePlayerRequest.parseFrom(msg.getBody());
		Role role = JSON.parseObject(request.getRoleInfo(), Role.class);
		// Role role = JsonObjectMapper.readMapperObjectByContent(request.getRoleInfo(), Role.class);
		
	}
	
}
