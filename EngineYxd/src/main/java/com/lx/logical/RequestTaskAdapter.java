package com.lx.logical;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.dbdao.EntityDAO;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.game.player.Role.UpdateAttrResponse;
import com.loncent.protocol.stauscode.StatusCode.StatusCodeResponse;

@Component
public class RequestTaskAdapter {
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	public EntityDAO dao;
	
	public RequestTaskAdapter() {
		
	}
	
	/**
	 * sendPopUpTip:(). <br/>
	 * TODO().<br/>
	 * 发送弹出框信息
	 * 
	 * @author lyh
	 * @param net
	 * @param statusCodeId
	 */
	public static StatusCodeResponse createPopUpTip(int statusCodeId) {
		StatusCodeResponse scr = StatusCodeResponse.newBuilder().setPopstr(statusCodeId).build();
		return scr;
	}
	
	/**
	 * getServerPlayerByteSessionId:(). <br/>
	 * TODO().<br/>
	 * 用sessionId得到玩家
	 * 
	 * @author lyh
	 * @param sessionId
	 * @return
	 */
	public ServerPlayer getServerPlayerByteSessionId(long sessionId) {
		return GlogalContainer.getSessionServerPlayerMap().get(sessionId);
	}
	

	
	// UpdateAttrToRoleResponse
}
