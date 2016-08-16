
package com.lx.logical.space;  

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceClientPauseMonsterResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.FuBen;
import com.lx.game.entity.Monster;
import com.lx.game.monster.action.MonsterAction;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.FuBenManage;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:SpaceClientPauseMonsterRequest <br/> 
 * Function: TODO (). <br/> 
 * Date:     2015-9-10 下午2:55:40 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_PAUSE_MONSTER_REQUEST_VALUE)
public class SpaceClientPauseMonsterRequest extends RequestTaskAdapter implements GameMessage<InnerMessage> {

	@Autowired
	private FuBenManage fuBenManage;
	@Override
    public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		ServerPlayer sp = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		Role role = sp.getRole();
	    long fuBenId = role.getFuBenId();
	    FuBen fuBen = fuBenManage.getFuBen(fuBenId);
	   
	    for(Map.Entry<Long, AbsMonster> entry : fuBen.getSpace().getMonsterMaps().entrySet())
	    {
	    	Monster monster = (Monster)entry.getValue();
	    	monster.getAction().setAction(MonsterAction.SUNK_SLEEP);
	    }
	    
	    SpaceClientPauseMonsterResponse.Builder repMsg = SpaceClientPauseMonsterResponse.newBuilder();
	    repMsg.setRetCode(1);
	    MessageSend.sendToGate(repMsg.build(), sp);
    }
}
  