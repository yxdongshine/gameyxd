package com.lx.logical.login;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.msgloader.Head;
import com.engine.properties.ServerGameConfig;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.game.login.LoginGameServer.RoleListResponse;
import com.loncent.protocol.game.login.LoginGameServer.RoleMessageData;
import com.loncent.protocol.inner.CheckToken.CheckTokenResponse;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.world.manage.LoginManage;
import com.lx.server.mina.session.IConnect;
import com.lx.world.container.WorldGlogalContainer;
import com.lx.world.data.CheckTokenData;
import com.lx.world.send.MessageSend;

/**
 * ClassName:CheckTokenResponseTask <br/>
 * Function: TODO (处理令牌验证逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午9:30:05 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.S_L_CHECK_TOKEN_RESPONSE_VALUE)
public class CheckTokenResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Autowired
	private LoginManage loginManage;
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		CheckTokenResponse resp = CheckTokenResponse.parseFrom(msg.getBody());
		
		CheckTokenData data = WorldGlogalContainer.getTokendatamap().remove(resp.getToken());
		
		if (data != null && resp.getResult() == 1) {
			
			// 判断版本号是否相等
			if (!data.getReq().getVersion().equals(ServerGameConfig.VERSION)) {
				MessageSend.sendToGate(loginManage.createRoleResult(0), data.getCon(), data.getInnerMessage().getClientSessionId(), data.getInnerMessage().getGateTypeId());
				// 还有一个提示
				return;
			}
			loginManage.processRoleList(data.getReq().getAccountName(), data.getInnerMessage(), data.getCon());
			
		} else {
			MessageSend.sendToGate(loginManage.createRoleResult(0), data.getCon(), data.getInnerMessage().getClientSessionId(), data.getInnerMessage().getGateTypeId());
			
			if (resp.getResult() == 0) {
				log.error("验证没有通过" + resp.getAccountName());
			} else {
				log.error("信息数据已删除");
			}
		}
	}
	
}
