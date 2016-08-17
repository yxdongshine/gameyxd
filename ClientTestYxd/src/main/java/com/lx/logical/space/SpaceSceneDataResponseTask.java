package com.lx.logical.space;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceClientSceneDataResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SpaceSceneDataResponseTask <br/>
 * Function: TODO (). <br/>
 * Date: 2015-7-30 上午9:10:22 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_SPACE_CLIENT_SCENE_DATA_RESPONSE_VALUE)
public class SpaceSceneDataResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		
		SpaceClientSceneDataResponse repMsg = SpaceClientSceneDataResponse.parseFrom(msg.getBody());
		
		log.info("success");
	}
	
}
