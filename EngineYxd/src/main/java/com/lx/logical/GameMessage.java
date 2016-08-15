package com.lx.logical;

import com.lx.server.mina.session.IConnect;

/**
 * ClassName: GmmeMessage <br/>
 * Function: TODO (消息处理接口). <br/>
 * Reason: TODO (). <br/>
 * date: 2013-8-6 下午2:09:36 <br/>
 * 
 * @author lyh
 * @version
 */
public interface GameMessage<T> extends ProtocolMessage {
	void doMessage(T msg, IConnect session) throws Exception;
	
}
