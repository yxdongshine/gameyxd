package com.wx.server.logical.inner;

import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.CheckToken.CheckTokenRequest;
import com.loncent.protocol.inner.CheckToken.CheckTokenResponse;
import com.lx.server.mina.session.IConnect;
import com.wx.server.container.GlobalContainer;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.msgloader.Head;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理Token逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.S_L_CHECK_TOKEN_REQUEST_VALUE)
public class CheckTokenResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		CheckTokenRequest check = CheckTokenRequest.parseFrom(msg.getBody());
		Long token = GlobalContainer.tokenMap.remove(check.getToken());
		
		int result = 1;
		if (token == null || token.longValue() != check.getToken()) {
			result = 0;
		}
		sendCheckTokenResponse(session, result, check.getAccountName(), check.getToken());
	}
	
	private void sendCheckTokenResponse(IConnect session, int result, String userName, long token) {
		CheckTokenResponse.Builder lsrb = CheckTokenResponse.newBuilder();
		lsrb.setAccountName(userName);
		lsrb.setResult(result);
		lsrb.setToken(token);
		session.send(lsrb.build());
	}
}
