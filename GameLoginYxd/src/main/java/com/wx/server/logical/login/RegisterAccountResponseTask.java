package com.wx.server.logical.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.login.LoginServer.RegisterAccountRequest;
import com.loncent.protocol.login.LoginServer.RegisterAccountResponse;
import com.lx.server.mina.session.IConnect;
import com.wx.server.dbdao.EntityDAO;
import com.wx.server.dbdao.EntityDAOInterface;
import com.wx.server.domain.Player;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.msgloader.Head;
import com.wx.server.utils.RegUtils;
import com.wx.server.utils.ServerUUID;
import com.wx.server.utils.StatusCode;
import com.wx.server.utils.ToolUtils;

/**
 * ClassName:HelloTask <br/>
 * Function: TODO (处理注册逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-8 下午2:30:51 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

@Component
@Head(CmdType.C_L_REGISTER_ACCOUNT_REQUEST_VALUE)
public class RegisterAccountResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Autowired(required=true)
	private EntityDAOInterface entityDAOInterface;
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		RegisterAccountRequest register = RegisterAccountRequest.parseFrom(msg.getBody());
		// 判断 是否已用过这个帐号
		int result = 1;
		// 判断密码是否达到要求
		if (!RegUtils.validPassword(register.getPassword()) || ToolUtils.isStringNull(register.getPassword()) || ToolUtils.isStringNull(register.getAccountName())) {
			this.sendPopUpTip(session, StatusCode.STATUS_2);
			result = 0;
		}
		
		if (result == 1) {
			List<Player> playerList = entityDAOInterface.findByProperty(Player.class, "accountName", register.getAccountName());
			if (playerList.size() > 0) {
				this.sendPopUpTip(session, StatusCode.STATUS_3);
				result = 0;
			}
		}
		
		Player player = null;
		// 创建角色
		if (result == 1) {
			player = createPlayer(register);
		}
		sendRegisterAccountResponse(session, result, player);
	}
	
	/**
	 * sendRegisterAccountResponse:(). <br/>
	 * TODO().<br/>
	 * 发送注册结果
	 * 
	 * @author lyh
	 * @param net
	 * @param result
	 */
	private void sendRegisterAccountResponse(IConnect net, int result, Player player) {
		RegisterAccountResponse.Builder rar = RegisterAccountResponse.newBuilder();
		rar.setStatus(result);
		if (player != null) {
			rar.setAccountName(player.getAccountName());
			rar.setPassword(player.getPassword());
		}
		net.send(rar.build());
	}
	
	/**
	 * createPlayer:(). <br/>
	 * TODO().<br/>
	 * 创建角色
	 * 
	 * @author lyh
	 * @param register
	 */
	private Player createPlayer(RegisterAccountRequest register) {
		Player player = new Player();
		player.setId(ServerUUID.createVerifyCode());
		player.setAccountName(register.getAccountName());
		player.setPassword(register.getPassword());
		player.setAppId(register.getAppId());
		player.setChannleId(register.getChannleId());
		player.setImei(register.getImei());
		player.setMailAddress(register.getMailAddress());
		player.setPhoneNum(register.getPhoneNum());
		player.setPhoneType(register.getPhoneType());
		player.setVersion(player.getVersion());
		entityDAOInterface.save(player);
		return player;
	}
	
}
