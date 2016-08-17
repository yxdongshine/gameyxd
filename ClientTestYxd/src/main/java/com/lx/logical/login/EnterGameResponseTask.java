package com.lx.logical.login;

import org.springframework.stereotype.Component;

import com.engine.msgloader.Head;
import com.loncent.protocol.BaseMessage.MinaMessage;
import com.loncent.protocol.cmd.Command.CmdType;
import com.loncent.protocol.game.item.Item.AddItemToBagResponse;
import com.loncent.protocol.game.item.Item.BuyItemRequest;
import com.loncent.protocol.game.item.Item.DelItemFromBagResponse;
import com.loncent.protocol.game.item.Item.ExpandGridRequest;
import com.loncent.protocol.game.item.Item.ItemData;
import com.loncent.protocol.game.item.Item.LookUpItemRequest;
import com.loncent.protocol.game.item.Item.OpenRoleBagReqest;
import com.loncent.protocol.game.item.Item.PutOnEquipToBodyResquest;
import com.loncent.protocol.game.item.Item.RemoveEquipFromBodyRequest;
import com.loncent.protocol.game.item.Item.SaleItemRequest;
import com.loncent.protocol.game.item.Item.UpdateItemToBagResponse;
import com.loncent.protocol.game.item.Item.UseItemRequest;
import com.loncent.protocol.game.login.LoginGameServer.EnterGameResponse;
import com.loncent.protocol.game.space.Space.SpaceClientSceneDataRequest;
import com.loncent.protocol.game.space.Space.SpaceClientSceneDataRequest.Builder;
import com.lx.logical.GameMessage;
import com.lx.logical.RequestTaskAdapter;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:EnterGameResponseTask <br/>
 * Function: TODO (成功进入游戏消息). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午6:22:40 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
@Head(CmdType.S_C_ENTER_GMAE_RESPONSE_VALUE)
public class EnterGameResponseTask extends RequestTaskAdapter implements GameMessage<MinaMessage> {
	
	@Override
	public void doMessage(MinaMessage msg, IConnect session) throws Exception {
		// TODO Auto-generated method stub
		EnterGameResponse resp = EnterGameResponse.parseFrom(msg.getBody());
		log.error("可以进入游戏了&&&&&&&&&&&&&&&&&&&&&&" + resp.getResult());
		//
		// buyItemTest(session,10001);
		// buyItemTest(session,60000);
		// 添加道具到背包
		// AddItemToBagResponseTest(session, 60000, 5L);
		// 更新背包数据
		// UpdateItemToBagResponseTest(session, 5L, 3);
		// 测试删除道具
		// DelItemFromBagResponseTest(session, 12L);
		// 测试卖出道具
		// SaleItemRequestTest(session, 5L, 1);
		// 测试使用道具
		// UseItemRequestTest(session, 5L, 1);
		// 增加背包格子
		// ExpandGridRequestTest(session, 1);
		// 查看道具信息
		// LookUpItemRequestTest(session, 5L);
		// 脱下装备
		// RemoveEquipFromBodyRequestTest(session, 5L);
		// 穿上装备
		// PutOnEquipToBodyResquestTest(session, 5L);
		// 测试打开背包数据
		// OpenRoleBagReqestTest(session);
		
		// 测试请求场景数据
		sceneDataRequest(session);
	}
	
	// 测试请求场景数据
	private void sceneDataRequest(IConnect session) {
		Builder req = SpaceClientSceneDataRequest.newBuilder();
		session.send(req.build());
	}
	
	// 测试购买道具
	public void buyItemTest(IConnect session, int itemTypeId) {
		BuyItemRequest.Builder builder = BuyItemRequest.newBuilder();
		builder.setItemTypeId(itemTypeId);
		builder.setItemNum(2);
		session.send(builder.build());
	}
	
	// 添加道具到背包
	public void AddItemToBagResponseTest(IConnect session, int itemConfigid, long itemdataId) {
		AddItemToBagResponse.Builder builder = AddItemToBagResponse.newBuilder();
		ItemData.Builder itemProtoBuilder = com.loncent.protocol.game.item.Item.ItemData.newBuilder();
		itemProtoBuilder.setItemId(itemdataId);
		itemProtoBuilder.setItemNum(1);
		itemProtoBuilder.setItemTypeId(itemConfigid);
		builder.setItemData(itemProtoBuilder.build());
		session.send(builder.build());
	}
	
	/**
	 * 更新背包中的数量 UpdateItemToBagResponseTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 * @param number
	 */
	public void UpdateItemToBagResponseTest(IConnect session, long itemdataId, int number) {
		UpdateItemToBagResponse.Builder builder = UpdateItemToBagResponse.newBuilder();
		builder.setItemId(itemdataId);
		builder.setItemNum(number);
		session.send(builder.build());
	}
	
	/**
	 * 
	 * DelItemFromBagResponseTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 */
	public void DelItemFromBagResponseTest(IConnect session, long itemdataId) {
		DelItemFromBagResponse.Builder builder = DelItemFromBagResponse.newBuilder();
		builder.setItemId(itemdataId);
		session.send(builder.build());
	}
	
	/**
	 * 卖出道具 SaleItemRequestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 * @param number
	 */
	public void SaleItemRequestTest(IConnect session, long itemdataId, int number) {
		SaleItemRequest.Builder builder = SaleItemRequest.newBuilder();
		builder.setItemId(itemdataId);
		builder.setItemNumber(number);
		session.send(builder.build());
	}
	
	/**
	 * 使用道具 UseItemRequestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 * @param number
	 */
	public void UseItemRequestTest(IConnect session, long itemdataId, int number) {
		UseItemRequest.Builder builder = UseItemRequest.newBuilder();
		builder.setItemId(itemdataId);
		builder.setItemNum(number);
		session.send(builder.build());
	}
	
	/**
	 * 增加格子 ExpandGridRequestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param number
	 */
	public void ExpandGridRequestTest(IConnect session, int number) {
		ExpandGridRequest.Builder builder = ExpandGridRequest.newBuilder();
		builder.setBagType(number);
		session.send(builder.build());
	}
	
	/**
	 * 
	 * LookUpItemRequestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 */
	public void LookUpItemRequestTest(IConnect session, long itemdataId) {
		LookUpItemRequest.Builder builder = LookUpItemRequest.newBuilder();
		builder.setItemId(itemdataId);
		session.send(builder.build());
	}
	
	/**
	 * 
	 * PutOnEquipToBodyResquestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 */
	public void PutOnEquipToBodyResquestTest(IConnect session, long itemdataId) {
		PutOnEquipToBodyResquest.Builder builder = PutOnEquipToBodyResquest.newBuilder();
		builder.setItemId(itemdataId);
		session.send(builder.build());
	}
	
	/**
	 * 脱下装备 RemoveEquipFromBodyRequestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 * @param itemdataId
	 */
	public void RemoveEquipFromBodyRequestTest(IConnect session, long itemdataId) {
		RemoveEquipFromBodyRequest.Builder builder = RemoveEquipFromBodyRequest.newBuilder();
		builder.setItemId(itemdataId);
		session.send(builder.build());
	}
	
	/**
	 * 打开背包数据 OpenRoleBagReqestTest:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param session
	 */
	public void OpenRoleBagReqestTest(IConnect session) {
		OpenRoleBagReqest.Builder builder = OpenRoleBagReqest.newBuilder();
		session.send(builder.build());
	}
}
