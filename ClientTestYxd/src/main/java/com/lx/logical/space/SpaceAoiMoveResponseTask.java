package com.lx.logical.space;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceAoiMoveResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SpaceAoiMoveResponseTask <br/>
 * Function: (AOI移动消息). <br/>
 * Date: 2015-7-16 上午10:46:12 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_SPACE_AOI_MOVE_RESPONSE_VALUE)
public class SpaceAoiMoveResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		
		SpaceAoiMoveResponse repMsg = SpaceAoiMoveResponse.parseFrom(msg.getBody());
		log.info("move. entityId=" + repMsg.getId() + " type=" + repMsg.getType() + " from(" + repMsg.getFromX() + "," + repMsg.getFromY() + "," + repMsg.getFromZ() + ")" + " to (" + repMsg.getToX() + "," + repMsg.getToY() + "," + repMsg.getToZ() + ")");
	}
	
}
