package com.wx.server.logical.login;

import java.util.List;

import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.ModifyPassWordRequest;
import com.loncent.protocol.login.LoginServer.ModifyPassWordResponse;
import com.lx.server.mina.session.IConnect;
import com.wx.server.domain.Player;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.msgloader.Head;
import com.wx.server.utils.RegUtils;
import com.wx.server.utils.StatusCode;
import com.wx.server.utils.ToolUtils;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理修改密码逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

@Component
@Head(CmdType.C_L_MODIFY_PASSWORD_REQUEST_VALUE)
public class ModifyPassWordResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		ModifyPassWordRequest mswr = ModifyPassWordRequest.parseFrom(msg.getBody());
		byte result = 1;
		if (ToolUtils.isStringNull(mswr.getAccountName()) || !RegUtils.validPassword(mswr.getOldPassword()) || !RegUtils.validPassword(mswr.getNewPassword()) || ToolUtils.isStringNull(mswr.getOldPassword()) || ToolUtils.isStringNull(mswr.getNewPassword())) {
			sendPopUpTip(session, StatusCode.STATUS_2);
			result = 0;
		}
		
		Player player = null;
		// 系统判断输入的用户名和密码是否匹配，否：下方显示信息【用户名或者密码错误
		if (result > 0) {
			List<Player> playerList = entityDAOInterface.findByProperty(Player.class, "accountName", mswr.getAccountName());
			if (playerList.size() > 0) {
				player = playerList.get(0);
				if (player.getAccountName().equals(mswr.getAccountName()) && player.getPassword().equals(mswr.getOldPassword())) {
					player.setPassword(mswr.getNewPassword());
					entityDAOInterface.updateFinal(player);
				} else {
					sendPopUpTip(session, StatusCode.STATUS_4);
					result = 0;
				}
			} else {
				sendPopUpTip(session, StatusCode.STATUS_6);
				result = 0;
			}
		}
		
		sendModifyPassWordResponse(session, result);
		if (result > 0) {
			sendPopUpTip(session, StatusCode.STATUS_5);
		}
	}
	
	private void sendModifyPassWordResponse(IConnect session, int result) {
		session.send(ModifyPassWordResponse.newBuilder().setResult(result).build());
	}
	
}
