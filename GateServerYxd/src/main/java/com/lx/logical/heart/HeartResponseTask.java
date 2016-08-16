package com.lx.logical.heart;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.sys.System.HeartRequest;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:HeartResponseTask <br/>
 * Function: TODO (处理客户端发上来的心跳包信息). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午9:44:59 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_HEART_REQUEST_VALUE)
public class HeartResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		HeartRequest request = HeartRequest.parseFrom(msg.getBody());
		if ((System.currentTimeMillis() - session.getPing()) / 1000 > 120) {// 大于2分钟
			session.close();
		} else {
			session.ping(System.currentTimeMillis());
		}
	}
	
}
