
package com.lx.logical.timer;  

import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.engine.timer.TimerEvent;
import com.loncent.protocol.game.item.Item.BottleInvaildResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemConfigLoad;
import com.lx.game.res.item.PropertyPojoGame;
import com.lx.game.send.MessageSend;
import com.lx.game.timer.TaskTimerEvent;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/** 
 * ClassName:TaskTimerEventTask <br/> 
 * Function: TODO (定时续费的逻辑处理). <br/> 
 * Reason:   TODO (没有做角 色不在线的情况). <br/> 
 * Date:     2015-9-2 下午2:17:17 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
@Component
@Head(TimerEvent.TIMER_EVENT_ITEM_RENEW)
public class TaskTimerEventTask extends RequestTaskAdapter implements GameMessage<TaskTimerEvent> {

	@Override
    public void doMessage(TaskTimerEvent msg, IConnect session) throws Exception {
	    // TODO Auto-generated method stub
		ServerPlayer sp = GameGlogalContainer.getRolesMap().get(msg.getRoleId());
		if (sp == null){
			log.error("没有角色直接去拿 数据库的数据");
			//暂时返回
			return;
		}
		
		PropertyPojoGame ppg = (PropertyPojoGame) ItemConfigLoad.getPropertyHashMap().get(sp.getLastBattleId());
		if (ppg != null) {
			Item item = sp.getBag().getBottleAndRevive()[0];
			item.setProperty(ppg);
			item.getItemData().setLevel(ppg.getLevel());
			item.getItemData().setNumber(ppg.getFolderableNum());
			item.getItemData().setConfigId(ppg.getId());
			item.getItemData().setEffectTime(0);// 生效时间失效
			item.getItemData().setRenewTimes(0);// 续费次数失效
			sp.getBag().getBottleAndRevive()[0] = item;
			// 更新数据库
			dao.updateFinal(item.getItemData());
			// 发送失效消息 
			MessageSend.sendToGate(buildItemData(item), sp);
		}
    }
	
	/**
	 * 构建失效物品信息 buildItemData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @return
	 */
	public BottleInvaildResponse buildItemData(Item item) {
		BottleInvaildResponse.Builder responBuilder = BottleInvaildResponse.newBuilder();
		responBuilder.setItemData(ItemLogicManage.buildItemData(item));
		return responBuilder.build();
	}
}
  