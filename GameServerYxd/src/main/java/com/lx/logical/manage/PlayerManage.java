package com.lx.logical.manage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.container.GlogalContainer;
import com.engine.dbdao.EntityDAO;
import com.engine.domain.ItemData;
import com.engine.domain.Role;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.lx.game.entity.MapObjectMessage;
import com.lx.game.item.Bag;
import com.lx.game.item.EquitItem;
import com.lx.game.item.ItemManage;
import com.lx.game.res.item.EquipmentPojoGame;
import com.lx.game.res.item.Item;
import com.lx.nserver.model.AttributeModel;
import com.lx.nserver.model.CareerModel;
import com.lx.nserver.model.RoleInitModel;
import com.lx.nserver.txt.AttributePojo;
import com.lx.nserver.txt.CareerPojo;

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
public class PlayerManage {
	@Autowired
	private CareerModel careerModel;
	
	@Autowired
	private AttributeModel attributeModel;
	
	@Autowired
	private RoleInitModel roleInitModel;
	@Autowired
	EntityDAO dao;
	
	/**
	 * createServerPlayer:(). <br/>
	 * TODO().<br/>
	 * 创建新玩家
	 * 
	 * @author lyh
	 * @param msg
	 * @param role
	 * @return
	 */
	public ServerPlayer createServerPlayer(InnerMessage msg, Role role, boolean bCreate) {
		ServerPlayer sp = new ServerPlayer();
		sp.setClientSessionId(msg.getClientSessionId());
		sp.setGateServerTypeId(msg.getGateTypeId());
		sp.setRole(role);
		sp.setId(role.getId());
		sp.setName(role.getRoleName());
		sp.setDir(role.getDir());
		sp.setEnable(true);
		sp.setPosition3D(role.getX(), role.getY(), role.getZ());
		sp.setType(IMapObject.MAP_OBJECT_TYPE_PLAYER);
		CareerPojo pojo = careerModel.get(role.getCareerConfigId());
		sp.setCareerPojo(pojo);
		sp.setRoleInitPojo(roleInitModel.get(1));
		sp.setUsePoint(role.getRoleLevel() * sp.getRoleInitPojo().getUpPoint());
		MapObjectMessage mom = new MapObjectMessage();
		sp.setMapObjectMessage(mom);
		mom.init(sp);
		
		// 初始化属性
		initAttribute(sp, role.getCareerConfigId());
		// 加入职业初属性
		this.initPoint(role, pojo, sp.getAttribute());
		// 初始化背包结构
		this.initBag(sp);
		// 初始化床上装备
		initEquip(sp);
		// 加入等级初始属性
		// for(int i = 0; i < role.getRoleLevel(); i++){
		this.addLevelBaseAttributePoint(sp, pojo, sp.getAttribute(), role.getRoleLevel());
		// }
		// 更新等级相关
		this.updateLevelAttr(sp.getAttribute(), role.getRoleLevel());
		if (bCreate) {// 初始HP和MP
			role.setHp(sp.getAttribute().getAttribute(Attribute.MAX_HP));
			role.setMp(sp.getAttribute().getAttribute(Attribute.MAX_MP));
		}
		GlogalContainer.getRolesMap().put(role.getId(), sp);
		GlogalContainer.getSessionServerPlayerMap().put(msg.getClientSessionId(), sp);
		return sp;
	}
	
	/**
	 * 初始化背包结构 initBag:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 */
	public void initBag(ServerPlayer sp) {
		// 初始化背包信息
		Bag bag = sp.getBag();
		if (bag != null) {
			bag.createBag(sp);
		} else {
			bag = new Bag();// 背包容器
			sp.setBag(bag);// 对象设置背包
			bag.createBag(sp);
		}
	}
	
	/**
	 * 初始化装备并穿上 initEquip:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void initEquip(ServerPlayer sp) {
		// CareerPojo cp=sp.getCareerPojo();
		// if(cp!=null&&cp.getInitEquipId()!=null){
		// String[] initEquipIdStr=cp.getInitEquipId().split("\\*");
		// if(initEquipIdStr!=null&&initEquipIdStr.length>0){
		// for (int i = 0; i < initEquipIdStr.length; i++) {
		// int propConfigId=Integer.parseInt(initEquipIdStr[i]);
		// Item item=ItemManage.createItem(propConfigId);
		// if(item!=null){//穿上装备
		// // 获取装备原型
		// EquipmentPojoGame equipProperty = (EquipmentPojoGame) item.getProperty();
		// if (equipProperty != null) {
		// // 获取装备位
		// int slot = equipProperty.getBind();
		// ItemData itemData=buildItemData((EquitItem)item, sp.getRole());
		// item.setItemData(itemData);
		// // 装备栏数据
		// sp.getBag().getEquipData()[slot - 1] = item;
		// //保存数据库
		// dao.saveOrUpdate(itemData);
		// // 生效
		// if (item instanceof EquitItem) {
		// EquitItem equitItem = (EquitItem) item;
		// equitItem.effect(sp);// 装备生效
		//
		// }
		//
		// }
		// }
		// }
		// }
		// }
	}
	
	/**
	 * 
	 * buildItemData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param equitItem
	 * @return
	 */
	public ItemData buildItemData(EquitItem equitItem, Role role) {
		ItemData itemTemporaryData = new ItemData();
		itemTemporaryData.setId(equitItem.getId());
		itemTemporaryData.setConfigId(equitItem.getProperty().getId());
		itemTemporaryData.setItemType(equitItem.getProperty().getBagClass());
		itemTemporaryData.setNumber(1);
		itemTemporaryData.setQuality(equitItem.getProperty().getQuality());
		itemTemporaryData.setIndexInBag(-1);
		itemTemporaryData.setRoleId(role.getId());
		// 设置装备的固有属性
		itemTemporaryData.setPos(equitItem.getProperty().getBind());// 装备绑定位
		itemTemporaryData.setScore(equitItem.getScore());// 初始化分数
		itemTemporaryData.setSocket(equitItem.getBornSocket());// 初始化孔
		return itemTemporaryData;
	}
	
	/**
	 * initAttribute:(). <br/>
	 * TODO().<br/>
	 * 初始化属性
	 * 
	 * @author lyh
	 * @param sp
	 * @param careerId
	 */
	public void initAttribute(ServerPlayer sp, int careerId) {
		List<AttributePojo> list = attributeModel.findAttributeMapByCareerId(careerId);
		
		if (list != null) {
			Attribute attr = new Attribute();
			addBaseAttributes(list, attr);
			sp.setAttribute(attr);
		}
		
	}
	
	/**
	 * initPoint:(). <br/>
	 * TODO().<br/>
	 * 初始属性点
	 * 
	 * @author lyh
	 * @param pojo
	 * @param attrbute
	 */
	public void initPoint(Role role, CareerPojo pojo, Attribute attribute) {
		int[] initAttr = { role.getAir() + pojo.getInitAir(), role.getAgility() + pojo.getInitAgility(), role.getInnerForce() + pojo.getInitInnerForce(), role.getControl() + pojo.getInitControl(), role.getTenacity() + pojo.getInitTenacity() };
		byte[] arr = new byte[] { Attribute.AIR, Attribute.AGILITY, Attribute.INNER_FORCE, Attribute.CONTROL, Attribute.TENACITY };
		for (byte index : arr) {
			this.addBaseAttribute(attribute, index, initAttr[index - 1]);// 这里加上资质系统的属性值计算
			attribute.refreshRelatedAttribute(index);
		}
	}
	
	/**
	 * addAttributes:(). <br/>
	 * TODO().<br/>
	 * 加入属性
	 * 
	 * @author lyh
	 * @param map
	 * @param attr
	 */
	private void addBaseAttributes(List<AttributePojo> list, Attribute attr) {
		for (AttributePojo pojo : list) {
			addBaseAttributes(attr, pojo);
		}
	}
	
	/**
	 * addAttributes:(). <br/>
	 * TODO().<br/>
	 * 加入单个属性
	 * 
	 * @author lyh
	 * @param attr
	 * @param pojo
	 */
	private void addBaseAttributes(Attribute attr, AttributePojo pojo) {
		attr.addBaseAttribute(pojo.getAttributeType(), pojo.getInitVal());
		attr.updateAttribute(pojo.getAttributeType());
	}
	
	/**
	 * addBaseAttribute:(). <br/>
	 * TODO().<br/>
	 * 加入基础属性
	 * 
	 * @author lyh
	 * @param attr
	 * @param attrType
	 * @param val
	 */
	public void addBaseAttribute(Attribute attr, int attrType, int val) {
		attr.addBaseAttribute(attrType, val);
		attr.updateAttribute(attrType);
	}
	
	/**
	 * addLevelBaseAttribute:(). <br/>
	 * TODO().<br/>
	 * 加入角色等级的基础属性
	 * 
	 * @author lyh
	 * @param attr
	 * @param attrType
	 * @param val
	 */
	public void addLevelBaseAttributePoint(ServerPlayer sp, CareerPojo pojo, Attribute attribute, int dl) {
		int[] initAttr = { pojo.getLvAir(), pojo.getLvAgility(), pojo.getLvInnerForce(), pojo.getLvControl(), pojo.getLvTenacity() };
		byte[] arr = new byte[] { Attribute.AIR, Attribute.AGILITY, Attribute.INNER_FORCE, Attribute.CONTROL, Attribute.TENACITY };
		for (byte index : arr) {
			if (sp.getUsePoint() < dl * initAttr[index - 1]) {
				return;
			}
			
			this.addBaseAttribute(attribute, index, dl * initAttr[index - 1]);
			sp.getRecordBasePoint()[index - 1] += dl * initAttr[index - 1];
			sp.setUsePoint(sp.getUsePoint() - dl * initAttr[index - 1]);
			attribute.refreshRelatedAttribute(index);
		}
	}
	
	/**
	 * updateLevelAttr:(). <br/>
	 * TODO().<br/>
	 * 更新等级相关的属性
	 * 
	 * @author lyh
	 * @param attr
	 * @param level
	 */
	public void updateLevelAttr(Attribute attr, int level) {
		// 刷新相关属性
		attr.updateAttribute(Attribute.MAX_HP);
		attr.updateAttribute(Attribute.MAX_MP);
		attr.updateAttribute(Attribute.DEFENCE);
	}
	
}
