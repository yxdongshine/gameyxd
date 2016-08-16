package com.lx.game.fight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.utils.log.LogUtils;
import com.lib.utils.IConst;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.game.player.Role.AttrType;
import com.loncent.protocol.game.player.Role.RoleDeadResponse;
import com.lx.game.entity.DropItem;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.Monster;
import com.lx.game.send.MessageSend;
import com.lx.logical.manage.FuBenManage;
import com.lx.logical.manage.GlobalMsgManage;
import com.lx.logical.manage.ItemLogicManage;
import com.lx.logical.manage.TaskManage;
import com.lx.nserver.txt.MonsterPojo;

/**
 * ClassName:FightDrop <br/>
 * Function: TODO (处理战斗结果). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-25 下午5:32:53 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class FightResult {
	private static Log log = LogUtils.getLog(FightResult.class);
	@Autowired
	private ItemLogicManage itemLogicManage;
	@Autowired
	private TaskManage taskManage;
	@Autowired
	private FuBenManage fuBenManage;
	
	/**
	 * fighterDead:(). <br/>
	 * TODO().<br/>
	 * 怪物死亡
	 * 
	 * @author lyh
	 * @param deadObj
	 */
	public void fighterDead(IFighter attacker, IFighter deadObj) {
		if (deadObj.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {// 怪物死亡
			Monster deadMonster = ((Monster) deadObj);
			// 通知副本怪物死亡
			fuBenManage.onMonsterDead(deadMonster);
			//判断是否是做任务
			taskManage.taskOfKillMonster(deadMonster.getMonsterPojo().getId(), (ServerPlayer)attacker);
			if (attacker.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				processFightDrop((ServerPlayer) attacker, deadMonster);// 处理掉落
			}
			SpaceInfo info = deadMonster.getSpace().getSpaceInfo();
			//怪物死亡
			//判断是不是副本
			if (info.fubenPojo != null &&info.fubenPojo.getType() != IConst.FUBEN_TYPE_WORLD){
				deadMonster.getSpace().getMonsterMaps().remove(deadMonster.getId());
			}else {
				deadMonster.getAction().setDeathAction();
			}
			
		} else if (deadObj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {// 角色死亡
			ServerPlayer deadSp = (ServerPlayer) deadObj;
			sendRoleDeadResponse(deadSp);
			deadSp.setAutoReliveTime(System.currentTimeMillis() + 30000);//30秒
		}
		
	}
	
	/**
	 * processFightDrop:(). <br/>
	 * TODO().<br/>
	 * 处理怪物掉落
	 * 
	 * @author lyh
	 * @param deadObj
	 */
	public void processFightDrop(ServerPlayer attacker, Monster deadObj) {
		// 加入游戏币
		// 加入经验值
		List<AttributeData> attrDataList = new ArrayList<AttributeData>();
		MonsterPojo pojo = deadObj.getMonsterPojo();
		int money = deadObj.getMonsterPojo().getRewardGold();
		// attacker.addMoney(money);
		//
		// attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.MONEY_VALUE,attacker.getRole().getMoney()));
		int exp = deadObj.getMonsterPojo().getRewardExp();
		attacker.addExp(exp);
		
		attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.CUR_EXP_VALUE, attacker.getRole().getCurExp()));
		GlobalMsgManage.sendUpdateAttrResponse(attacker, attrDataList, attacker);
		
		// 处理掉落游戏币
		itemLogicManage.createDropItem((GameSpace) attacker.getSpace(), 0, money, attacker.getId(), getDropPos((GameSpace) attacker.getSpace(), deadObj), true, DropItem.DROP_TYPE_MONEY);
		// 处理必掉道具
		if (pojo.getRewardItem() > 0) {
			itemLogicManage.createDropItem((GameSpace) attacker.getSpace(), pojo.getRewardItem(), 1, attacker.getId(), getDropPos((GameSpace) attacker.getSpace(), deadObj), true, DropItem.DROP_TYPE_ITME);
		}
		
		// 处理有机率掉道具
		if (pojo.getRewardItems().length > 0) {
			int r = ServerRandomUtils.nextInt(1000);
			int tmp = 0;
			for (int i = 0; i < pojo.getRewardItems().length; i++) {
				if (pojo.getRewardItems()[i].length >= 3) {
					int itemConfigId = pojo.getRewardItems()[i][0];
					if (itemConfigId <= 0) {
						continue;
					}
					int num = pojo.getRewardItems()[i][1];
					if (num <= 0) {
						num = 1;
					}
					int rate = pojo.getRewardItems()[i][2];
					if (r > tmp && r <= tmp + rate) {
						itemLogicManage.createDropItem((GameSpace) attacker.getSpace(), itemConfigId, num, attacker.getId(), getDropPos((GameSpace) attacker.getSpace(), deadObj), true, DropItem.DROP_TYPE_ITME);
						break;
					} else {
						tmp += rate;
					}
				}
				
			}
		}
	}
	
	/**
	 * getDropPos:(). <br/>
	 * TODO().<br/>
	 * 随机周围三格
	 * 
	 * @author lyh
	 * @param deadObj
	 * @return
	 */
	public Position3D getDropPos(GameSpace gameSpace, Monster deadObj) {
		Position3D pos = deadObj.getPosition3D();
		SpaceInfo info = gameSpace.getSpaceInfo();
		for (int i = 0; i < 10; i++) {
			int x = ServerRandomUtils.nextInt(100, false);
			int z = ServerRandomUtils.nextInt(100, false);
			int col = info.getCol(pos.getX()) + x;
			int row = info.getRow(pos.getZ()) + z;
			
			if (info.isCanWalk(col, 0, row)) {
				return new Position3D(info.getGridX(col), pos.getY(), info.getGridZ(row));
			}
			
		}
		return pos;
	}
	
	/**
	 * sendRoleDeadResponse:(). <br/>
	 * TODO().<br/>
	 * 角色死亡通知
	 * 
	 * @author lyh
	 * @param roleId
	 * @param sp
	 */
	public void sendRoleDeadResponse(ServerPlayer sp) {
		RoleDeadResponse resp = RoleDeadResponse.newBuilder().setRoleId(sp.getId()).build();
		MessageSend.sendToGate(resp, sp);
		for (Map.Entry<Long, IMapObject> entry : sp.getPlayerViewMap().entrySet()) {
			ServerPlayer viewPlayer = (ServerPlayer) entry.getValue();
			if (viewPlayer != null) {
				MessageSend.sendToGate(resp, viewPlayer);
			}
		}
		
	}
}
