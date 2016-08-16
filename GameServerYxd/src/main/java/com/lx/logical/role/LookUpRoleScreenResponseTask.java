package com.lx.logical.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.domain.ItemData;
import com.engine.entityobj.ServerPlayer;
import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.player.Role.LookUpRoleScreenRequest;
import com.loncent.protocol.game.player.Role.LookUpRoleScreenResponse;
import com.lx.game.container.GameGlogalContainer;
import com.lx.game.item.Bag;
import com.lx.game.item.EquitItem;
import com.lx.game.res.item.Item;
import com.lx.game.send.MessageSend;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.RoleManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:LookUpRoleScreenResponseTask <br/>
 * Function: TODO (查看角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-16 下午3:08:57 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.C_S_LOOK_UP_ROLE_SCREEN_REQUEST_VALUE)
public class LookUpRoleScreenResponseTask extends RequestTaskAdapter implements GameMessage<InnerMessage> {
	
	@Autowired
	private RoleManage roleManage;
	@Override
	public void doMessage(InnerMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		// 根据回话取出当前游戏玩家对象
		ServerPlayer serverPlayer = GameGlogalContainer.getSessionServerPlayerMap().get(msg.getClientSessionId());
		LookUpRoleScreenRequest request = LookUpRoleScreenRequest.parseFrom(msg.getBody());
		ServerPlayer sp = GlogalContainer.getRolesMap().get(request.getRoleId());
		if (sp != null) {
			LookUpRoleScreenResponse.Builder resp = LookUpRoleScreenResponse.newBuilder();
			resp.setCareerConfigId(sp.getRole().getCareerConfigId());
			resp.setMapId(sp.getRole().getMapUid());
			resp.setRoleId(request.getRoleId());
			resp.setRoleLevel(sp.getRole().getRoleLevel());
			resp.setRoleName(sp.getRole().getRoleName());
			resp.setGuildName((sp.getRole().getGuildName() == null) ? "" : sp.getRole().getGuildName());
			resp.setTeamId(sp.getTeamId());
			resp.setOrderOfTeam(sp.getRoleTeamOrder());
			//设置属性
			resp.setRoleAttr(roleManage.buildOpenRoleAttrScreenResponse(sp));
			// 还要装备显示没有做
			this.getPlayerEquipBar(resp, sp);
			MessageSend.sendToGate(resp.build(), serverPlayer);
		} else {
			log.error("角色不在线!!");
		}
	}
	
	public LookUpRoleScreenResponse.Builder getPlayerEquipBar(LookUpRoleScreenResponse.Builder resp, ServerPlayer sp) {
		// 获取玩家装备栏装备信息
		Bag bag = sp.getBag();
		if (bag != null) {
			// 装备栏列表
			for (int i = 0; i < bag.getEquipData().length; i++) {
				Item item = bag.getEquipData()[i];
				if (item != null && item.getItemData() != null) {
					ItemData itemData = item.getItemData();
					if (itemData != null) {
						resp.addEquipBarItemData(buildItemData(item).build());
					}
				}
				
			}
		}
		return resp;
	}
	
	/**
	 * 
	 * buildItemData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param item
	 * @return
	 */
	public com.loncent.protocol.game.player.Role.ItemDataOfRole.Builder buildItemData(Item item) {
		com.loncent.protocol.game.player.Role.ItemDataOfRole.Builder responseBuliderItemData = com.loncent.protocol.game.player.Role.ItemDataOfRole.newBuilder();
		if (item != null && item.getItemData() != null) {
			ItemData itemData = item.getItemData();
			responseBuliderItemData.setItemId(itemData.getId());
			responseBuliderItemData.setItemNum(itemData.getNumber());
			responseBuliderItemData.setItemType(itemData.getItemType());
			responseBuliderItemData.setItemTypeId(itemData.getConfigId());
			responseBuliderItemData.setQuality(itemData.getQuality());
			responseBuliderItemData.setIndexInBag(itemData.getIndexInBag());
			if (item instanceof EquitItem) {
				responseBuliderItemData.setPos(itemData.getPos());
				responseBuliderItemData.setScore(itemData.getScore());
				responseBuliderItemData.setSocket(itemData.getSocket());
			}
			
		}
		return responseBuliderItemData;
	}
	
}
