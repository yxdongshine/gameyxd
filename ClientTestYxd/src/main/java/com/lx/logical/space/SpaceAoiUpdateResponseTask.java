package com.lx.logical.space;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.PublicData.PbAoiEntity;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceAoiUpdateResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:SpaceAoiUpdateResponseTask <br/>
 * Function: (场景更新,可见性更新). <br/>
 * Date: 2015-7-15 下午6:07:01 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_SPACE_AOI_UPDATE_RESPONSE_VALUE)
public class SpaceAoiUpdateResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	private Log log = LogFactory.getLog(SpaceAoiUpdateResponseTask.class);
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		
		SpaceAoiUpdateResponse repMsg = SpaceAoiUpdateResponse.parseFrom(msg.getBody());
		for (Iterator<PbAoiEntity> it = repMsg.getNewListList().iterator(); it.hasNext();) {
			PbAoiEntity newEntity = it.next();
			log.info("newEntityId:" + newEntity.getId());
		}
		
	}
	
}
