package com.wx.server.logical.login;

import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.LoginServerRequest;
import com.loncent.protocol.login.LoginServer.LoginServerResponse;
import com.lx.server.mina.session.IConnect;
import com.wx.server.container.GlobalContainer;
import com.wx.server.domain.Player;
import com.wx.server.domain.ServerList;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.logical.manage.LoginManage;
import com.wx.server.msgloader.Head;
import com.wx.server.utils.IContents;
import com.wx.server.utils.RegUtils;
import com.wx.server.utils.ServerUUID;
import com.wx.server.utils.StatusCode;
import com.wx.server.utils.ToolUtils;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理登录逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_L_LOGIN_SERVER_REQUEST_VALUE)
public class LoginServerResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		LoginServerRequest login = LoginServerRequest.parseFrom(msg.getBody());
		
		ServerList pServer = LoginManage.serverListMap.get("" + login.getServerId());
		if (pServer == null) {
			log.error("no:::server::" + login.getServerId());
			return;
		}
		
		if (pServer.getStatus() == IContents.SERVER_CODE_4) {// 维护状态
			log.error("no:::server::" + login.getServerId());
			sendPopUpTip(session, StatusCode.STATUS_7);
			sendLoginServerResponse(session, null, 0, 0, pServer);
			return;
		}
		
		byte result = 1;
		if (ToolUtils.isStringNull(login.getAccountName()) || !RegUtils.validPassword(login.getPassword()) || ToolUtils.isStringNull(login.getPassword())) {
			sendPopUpTip(session, StatusCode.STATUS_2);
			result = 0;
		}
		
		Player player = null;
		// 系统判断输入的用户名和密码是否匹配，否：下方显示信息【用户名或者密码错误
		if (result > 0) {
			List<Player> playerList = entityDao.findByProperty(Player.class, "accountName", login.getAccountName());
			if (playerList.size() > 0) {
				player = playerList.get(0);
				if (player.getAccountName().equals(login.getAccountName()) && player.getPassword().equals(login.getPassword())) {
					
					player.setLastServerId(login.getServerId());
					entityDao.updateFinal(player);
					
				} else {
					result = 0;
					sendPopUpTip(session, StatusCode.STATUS_4);
					player = null;
				}
			} else {
				sendPopUpTip(session, StatusCode.STATUS_6);
				result = 0;
			}
		} else {
			log.error("no player");
		}
		long uuid = 0;
		if (player != null) {
			// 保存token
			uuid = ServerUUID.createVerifyCode();
			GlobalContainer.tokenMap.put(uuid, uuid);
		} 
		
		sendLoginServerResponse(session, player, result, uuid, pServer);  

		// 关掉连接
		if (result > 0) {
			((IoSession) session.getAttachment()).close(false);
			log.error("登陆服连接已关！！！！");
		}
	}
	
	private void sendLoginServerResponse(IConnect session, Player player, int result, long token, ServerList curServer) {
		LoginServerResponse.Builder lsrb = LoginServerResponse.newBuilder();
		lsrb.setStatus(result);
		if (player != null) {
			ServerList pServer = curServer;
			lsrb.setToken(token);
			lsrb.setIp(pServer.getIp());
			lsrb.setPort(pServer.getPort());
		}
		session.send(lsrb.build());
	}
	
}
