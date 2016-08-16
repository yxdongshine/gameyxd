
package com.lx.logical.space;  

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.domain.Role;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.space.Space.SpaceClientResumeMonsterResponse;
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
 * ClassName:SpaceClientResumeMonsterRequest <br/> 
 * Function: TODO (). <br/> 
 * Date:     2015-9-10 下午2:56:03 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
@Component
@Head(CmdType.C_S_SPACE_CLIENT_RESUME_MONSTER_REQUEST_VALUE)
public class SpaceClientResumeMonsterRequest extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
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
	    	monster.getAction().setAction(MonsterAction.LIVE);
	    }
	    
	    SpaceClientResumeMonsterResponse.Builder repMsg = SpaceClientResumeMonsterResponse.newBuilder();
	    repMsg.setRetCode(1);
	    MessageSend.sendToGate(repMsg.build(), sp);	    
    }
	
}
  