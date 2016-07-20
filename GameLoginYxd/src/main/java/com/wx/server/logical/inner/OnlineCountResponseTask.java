package com.wx.server.logical.inner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.cmd.InnerCommand.CmdInnerType;
import com.loncent.protocol.inner.OnLineCount.OnLineCountRequest;
import com.lx.server.mina.session.IConnect;
import com.wx.server.domain.ServerList;
import com.wx.server.excel.model.ServerCodeModel;
import com.wx.server.excel.pojo.ServerCodePojo;
import com.wx.server.logical.GameMessage;
import com.wx.server.logical.ServerLoginAdapter;
import com.wx.server.logical.manage.LoginManage;
import com.wx.server.msgloader.Head;

/**
 * ClassName:LoginServerResponse <br/>
 * Function: TODO (处理服务器状态逻辑). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:06:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdInnerType.S_L_ONLIN_COUNT_REQUEST_VALUE)
public class OnlineCountResponseTask extends ServerLoginAdapter implements GameMessage<MinaMessage> {
	
	@Autowired
	private ServerCodeModel pServerCodeModel;
	
	/***
	 * 畅通1 拥挤2 火爆3 维护4
	 */
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		
		OnLineCountRequest check = OnLineCountRequest.parseFrom(msg.getBody());
		ServerCodePojo pojo = pServerCodeModel.findServerCodePojoByCount(check.getOnlineCount());
		if (pojo != null) {
			ServerList sl = LoginManage.serverListMap.get(check.getServerId());
			if (sl != null) {
				LoginManage.setServerStatus(sl, pojo.getId());
			}
		}
	}
}
