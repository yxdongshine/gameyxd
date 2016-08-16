package com.lx.logical.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.engine.domain.Role;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IArea;
import com.engine.entityobj.space.IMapObject;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.player.Role.AttrType;
import com.loncent.protocol.game.player.Role.EnterGameRoleInfoResponse;
import com.loncent.protocol.game.player.Role.RoleReliveResponse;
import com.loncent.protocol.inner.InnerToMessage.GameWorldUpdatePlayerRequest;
import com.lx.game.send.MessageSend;
import com.lx.nserver.model.RoleLevelUpModel;
import com.lx.nserver.txt.RoleLevelUpPojo;

/**
 * ClassName:ServerPlayerManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (角色管理类). <br/>
 * Date: 2015-7-9 下午5:35:20 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class GamePlayerManage extends PlayerManage {
	/**入口复活**/  
	public static final int RELIVE_BIRTH_PLACE = 1;
	/**原地复活**/  
	public static final int RELIVE_PLACE = 2;
	/**复仇**/  
	public static final int RELIVE_REVENGE = 3;

		
	@Autowired
	private RoleLevelUpModel roleLevelUpModel;
	@Autowired
	private TaskManage taskManage;
	
	/**
	 * sendEnterGameRoleInfoResponse:(). <br/>
	 * TODO().<br/>
	 * 发送角色属性数据
	 * 
	 * @author lyh
	 */
	public void sendEnterGameRoleInfoResponse(ServerPlayer sp) {
		Role role = sp.getRole();
		Attribute attr = sp.getAttribute();
		EnterGameRoleInfoResponse.Builder resp = EnterGameRoleInfoResponse.newBuilder();
		resp.setRoleName(role.getRoleName());// ;//角色名字
		resp.setCareerConfigId(role.getCareerConfigId());// 角色职业配置文件
		resp.setRoleLevel(role.getRoleLevel());// 角色等级
		resp.setCurExp(role.getCurExp());// 当前经验
		RoleLevelUpPojo pojo = roleLevelUpModel.get(role.getRoleLevel());
		resp.setMaxExp(pojo.getLevelUpExp());// 当前等级最大经验
		resp.setHp(role.getHp());// 精血
		resp.setMaxHp(attr.getAttribute(Attribute.MAX_HP));
		resp.setMp(role.getMp());// 精力
		resp.setMaxMp(attr.getAttribute(Attribute.MAX_MP));
		resp.setMoney(role.getMoney());// 白银
		resp.setBindGold(role.getBindGold());// //元宝
		resp.setRechargeGold(role.getRechargeGold());// 金砖
		resp.setVipLevel(role.getVipLevel());// vip等级
		resp.setChineseZodiacId(role.getChineseZodiacId());// 生肖
		resp.setCurVits(role.getVits());
		resp.setMaxVits(sp.getRoleInitPojo().getMaxVits());// 角色最大体力
		resp.setTitleConfigId(role.getTitleConfigId());
		resp.setGuildId(role.getGuildId());// 公会id
		resp.setGuildName(role.getGuildName() == null ? "" : role.getGuildName());// //公会名称
		resp.setX(role.getX());
		resp.setY(role.getY());
		resp.setZ(role.getZ());
		resp.setDir(role.getDir());
		resp.setMapId(role.getFuBenId());
		resp.setMapUid(role.getMapUid());
		MessageSend.sendToGate(resp.build(), sp);
	}
	
	/**
	 * addExp:(). <br/>
	 * TODO().<br/>
	 * 给玩家加入经验值
	 * 
	 * @author lyh
	 * @param sp
	 */
	public void addExp(ServerPlayer sp, int exp) {
		Role role = sp.getRole();
		synchronized (sp.getSynExp()) {
			int roleExp = sp.addExp(exp);
			int oldLevel = role.getRoleLevel();
			boolean bLevelUp = false;
			// 判断是否可以升级
			for (int i = 0; i < 20; i++) {
				RoleLevelUpPojo pojo = roleLevelUpModel.get(role.getRoleLevel());
				if (roleExp >= pojo.getLevelUpExp()) {
					if (pojo.getIsLeveUp() == 1) {// 可以升级
						bLevelUp = true;
						sp.addExp(-pojo.getLevelUpExp());// 升级减经验值
						role.setRoleLevel(role.getRoleLevel() + 1);
						// 发送升级后的任务列表
						// 发送可见或者可接的任务列表
						MessageSend.sendToGate(taskManage.buildRoleCanAcceptTaskResponse(sp), sp);
					} else {
						// 把最大值赋给当前经验
						role.setCurExp(pojo.getLevelUpExp());
						break;
					}
				} else {
					
					break;
				}
			}
			
			List<AttributeData> dataList = new ArrayList<AttributeData>();
			dataList.add(createAttributeData(AttrType.CUR_EXP_VALUE, role.getCurExp()));
			// 有升级
			if (bLevelUp) {// 处理升经后的影响
				// 增加属性点
				RoleLevelUpPojo lvUpPojo = roleLevelUpModel.get(role.getRoleLevel());
				Attribute attr = sp.getAttribute();
				sp.setUsePoint(sp.getUsePoint() + (role.getRoleLevel() - oldLevel) * sp.getRoleInitPojo().getUpPoint());
				// 改变属性值
				this.addLevelBaseAttributePoint(sp, sp.getCareerPojo(), sp.getAttribute(), role.getRoleLevel() - oldLevel);
				// 更新角色等级相关的属性
				updateLevelAttr(sp.getAttribute(), role.getRoleLevel());
				
				role.setHp(attr.getAttribute(Attribute.MAX_HP));
				role.setMp(attr.getAttribute(Attribute.MAX_MP));
				// 要更新最大经验值,当前血,当前MP,最大HP,最大MP，角色等级
				dataList.add(createAttributeData(AttrType.ROLE_LEVEL_VALUE, role.getRoleLevel()));
				dataList.add(createAttributeData(AttrType.MAX_EXP_VALUE, lvUpPojo.getLevelUpExp()));
				dataList.add(createAttributeData(AttrType.HP_VALUE, role.getHp()));
				dataList.add(createAttributeData(AttrType.MP_VALUE, role.getMp()));
				dataList.add(createAttributeData(AttrType.MAX_HP_VALUE, attr.getAttribute(Attribute.MAX_HP)));
				dataList.add(createAttributeData(AttrType.MAX_MP_VALUE, attr.getAttribute(Attribute.MAX_MP)));
			}
			
			GlobalMsgManage.sendUpdateAttrResponse(sp, dataList, sp);
			
		}
	}
	
	/**
	 * createAttributeData:(). <br/>
	 * TODO().<br/>
	 * 创建角色属性协议数据
	 * 
	 * @author lyh
	 * @param roleAttrType
	 * @param val
	 * @return
	 */
	public AttributeData createAttributeData(int roleAttrType, int val) {
		AttributeData data = AttributeData.newBuilder().setAttrType(roleAttrType).setAttrVal(val).build();
		return data;
	}
	

	
	/**
	 * addMoney:(). <br/>
	 * TODO().<br/>
	 * 添加游戏币
	 * 
	 * @author lyh
	 * @param money
	 */
	public void addMoney(ServerPlayer sp, int money) {
		synchronized (sp.getSynMoney()) {
			sp.addMoney(money);
		}
	}
	
	/**
	 * addBindGold:(). <br/>
	 * TODO().<br/>
	 * 添加绑定金币
	 * 
	 * @author lyh
	 * @param bindGold
	 */
	public void addBindGold(ServerPlayer sp, int bindGold) {
		synchronized (sp.getSynBindGold()) {
			sp.addBindGold(bindGold);
		}
	}
	
	/**
	 * addRechargeGold:(). <br/>
	 * TODO().<br/>
	 * 添加充值的金币
	 * 
	 * @author lyh
	 * @param gold
	 */
	public void addRechargeGold(ServerPlayer sp, int gold) {
		synchronized (sp.getSynrRechargeGold()) {
			sp.addRechargeGold(gold);
		}
	}
	
	/**
	 * sendGameWorldUpdatePlayerRequest:(). <br/>
	 * TODO().<br/>
	 * 发送更新角以到世界服务器
	 * 
	 * @author lyh
	 * @param role
	 */
	public void sendGameWorldUpdatePlayerRequest(ServerPlayer sp) {
		GameWorldUpdatePlayerRequest.Builder req = GameWorldUpdatePlayerRequest.newBuilder();
		req.setRoleInfo(JSON.toJSONString(sp.getRole()));
		MessageSend.sendToWorld(req.build(), sp);
	}
	
	
	/** 
	 * playerRelive:(). <br/> 
	 * TODO().<br/> 
	 * 角色复活()
	 * @author lyh 
	 * @param sp 
	 */  
	public void playerRelive(ServerPlayer sp,int reliveState){
		// 血,魔加满
		sp.setHp(sp.getAttribute().getAttribute(Attribute.MAX_HP));
		sp.setMp(sp.getAttribute().getAttribute(Attribute.MAX_MP));
		
		List<AttributeData> data = new ArrayList<AttributeData>();
		data.add(GlobalMsgManage.createAttributeData(AttrType.HP_VALUE, sp.getHp()));
		data.add(GlobalMsgManage.createAttributeData(AttrType.MP_VALUE, sp.getMp()));
		GlobalMsgManage.sendAttrToAllViewObj(sp, data, true);
		
		
		sp.die(false);
		if (reliveState == GamePlayerManage.RELIVE_BIRTH_PLACE){//1=入口复活,
			Position3D pos = sp.getSpace().getSpaceInfo().birthPlace;
			//离开旧的区域
			IArea oldArea = sp.getSpace().getAoi().getIAreaByXZ(sp.getPosition3D().getX(), sp.getPosition3D().getZ());
			sp.setX(pos.getX());
			sp.setY(pos.getY());
			sp.setZ(pos.getZ());
		
			IArea newArea = sp.getSpace().getAoi().getIAreaByXZ(sp.getPosition3D().getX(), sp.getPosition3D().getZ());
			if (newArea != oldArea){
				oldArea.removeMapObject(sp);
				newArea.addMapObject(sp);
				sp.setArea(newArea);
			}
			sp.getSpace().doMapObjectLeftView(sp);
			sp.getSpace().doMapObjectAddView(sp, sp.getArea().getNineArea());
			
			
		}else if (reliveState == GamePlayerManage.RELIVE_PLACE){//2=原地复活
			
		}else if (reliveState == GamePlayerManage.RELIVE_REVENGE){//3=复仇
			
		}
		
		sendRoleReliveResponse(sp);

	}
	
	/** 
	 * sendRoleReliveResponse:(). <br/> 
	 * TODO().<br/> 
	 * 发送复活消息
	 * @author lyh 
	 * @param serverPlayer 
	 */  
	public void sendRoleReliveResponse(ServerPlayer serverPlayer){
		PbPosition3D.Builder pos = PbPosition3D.newBuilder();
		pos.setX(serverPlayer.getPosition3D().getX());
		pos.setY(serverPlayer.getPosition3D().getY());
		pos.setZ(serverPlayer.getPosition3D().getZ());
		RoleReliveResponse resp = RoleReliveResponse.newBuilder().setRoleId(serverPlayer.getId()).setRelivePos(pos.build()).build();
		MessageSend.sendToGate(resp, serverPlayer);
		for (Map.Entry<Long,IMapObject> entry : serverPlayer.getPlayerViewMap().entrySet()){
			ServerPlayer sp = (ServerPlayer)entry.getValue();
			MessageSend.sendToGate(resp, sp);
		}
	}
}
