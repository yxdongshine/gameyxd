package com.lx.logical.item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.AbsDropItem;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.AbsSpace;
import com.engine.msgloader.Head;
import com.engine.utils.ErrorCode;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.PickUpItemRequest;
import com.loncent.protocol.game.item.Item.PickUpItemResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.entity.DropItem;
import com.lx.game.item.ItemManage;
import com.lx.game.item.PackItem;
import com.lx.game.res.item.Item;
import com.lx.game.res.item.ItemStaticConfig;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:PickUpItemRequest <br/>
 * Function: TODO (请求捡起道具). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 下午5:56:30 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_PICK_UP_ITEM_REQUEST_VALUE)
public class PickUpItemRequestTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	@Autowired
	private ItemLogicManage itemLogicManage;
	
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		PickUpItemRequest pickUpItemBuilder = PickUpItemRequest.parseFrom(msg.getBody());
		long itemDataId = pickUpItemBuilder.getItemId();//
		
		// 根据回话取出游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		long roleId = 0;
		if (serverPlayer != null) {
			AbsSpace gs = serverPlayer.getSpace();
			AbsDropItem dItem = gs.getDropItemMaps().get(itemDataId);
			if (dItem != null && dItem.getRoleId() == 0){
				//判断是否在保护时间
				if (dItem.getProtectTime() > System.currentTimeMillis()){
					if (dItem.getOwnerId() != serverPlayer.getId()){
						return;
					}
				}
				
				//一定要同步
				synchronized (dItem) {
					dItem.setRoleId(serverPlayer.getId());
					return;
                }
			}
			
			//后面的暂时不要
			
			roleId = serverPlayer.getRole().getId();
			int count = 1;
			Item item = ItemManage.createItem(10001);
			List<PackItem> resultLongAL = new ArrayList<PackItem>();
			resultLongAL = serverPlayer.getBag().putItemInBag(item, count, roleId, dao);
			if (resultLongAL.size() >= 1 && item != null) {
				if (resultLongAL.get(0).getAddType() == -1) {
					MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_E), serverPlayer);
				} else {
					for (int i = 1; i < resultLongAL.size(); i++) {
						if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_UPDATE) {// 发送更新消息
							// 发送更新道具消息
							MessageSend.sendToGate(itemLogicManage.sendUpdateItemToBagResponse(resultLongAL.get(i).getItem().getItemData().getId(), resultLongAL.get(i).getUpdateNumber(), item.getProperty().getBagClass()), serverPlayer);
						} else if (resultLongAL.get(i).getAddType() == ItemStaticConfig.BAG_ADD_TYPE_ADD) {
							item = resultLongAL.get(i).getItem();
							// 发送放置物品到背包消息
							MessageSend.sendToGate(itemLogicManage.sendAddItemToBagResponse(item), serverPlayer);
						}
					}
				}
			}
			
		}
		
	}
	
}
