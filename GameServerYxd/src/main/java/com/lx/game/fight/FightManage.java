package com.lx.game.fight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.ServerSkill;
import com.engine.entityobj.space.IArea;
import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.IFightListener;
import com.engine.interfaces.ISkill;
import com.engine.utils.ErrorCode;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.PublicData.PbPosition3D;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateMsg;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateResponse;
import com.loncent.protocol.game.battle.Battle.PbHitType;
import com.loncent.protocol.game.player.Role.AttrType;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.Monster;
import com.lx.game.send.MessageSend;
import com.lx.logical.RequestTaskAdapter;
import com.lx.logical.manage.GlobalMsgManage;
import com.lx.nserver.model.SkillTemplateModel;
import com.lx.nserver.txt.SkillTemplatePojo;
import com.lx.nserver.txt.SkillUnitPojo;
import com.sun.xml.internal.ws.wsdl.writer.document.Message;

/**
 * ClassName:FightManage <br/>
 * Function: TODO (战斗管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-8 下午6:16:19 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class FightManage extends RequestTaskAdapter implements IFightListener {
	@Autowired
	private FightCalculate fightCalculate;
	
	@Autowired
	private FightAoe fightAoe;
	
	@Autowired
	private FightResult fightResult;
	
	@Autowired
	private SkillTemplateModel skillTemplateModel;
	
	/**
	 * 对点进行攻击 返回0=没有攻击 返回1=有攻击
	 */
	@Override
	public int playerFight(IFighter attacker, int skillId, int unitId, List<IFighter> targetFighters) {
		// TODO Auto-generated method stub
		long curTime = System.currentTimeMillis();
		ServerPlayer sp = (ServerPlayer) attacker;
		// ISkill sSkill = sp.getSkillById(skillId);
		// SkillTemplatePojo stPojo = sSkill.getSkillTemplatePojo();
		SkillTemplatePojo stPojo = skillTemplateModel.get(skillId);
		if (stPojo == null) {
			return 0;
		}
		SkillUnitPojo suPojo = stPojo.getSkillUnitPojoById(unitId);
		
		// 判断是否在攻击距离内
		// 判断技能攻击范围(如果当前距离与目标距离不在攻击范围,就执行追击)
		if (canFight(attacker, curTime, stPojo, suPojo)) {
			BattleHurtUpdateResponse.Builder hurtBuilder = createBattleHurtUpdateResponse();
			hurtBuilder.setAttackerId(attacker.getId());
			hurtBuilder.setAttackerType(PbAoiEntityType.valueOf(attacker.getType()));
			hurtBuilder.setSkillUnitId(unitId);
			hurtBuilder.setSkillId(skillId);
			for (IFighter targeter : targetFighters) {
				this.fight(targeter, attacker, suPojo, hurtBuilder);
			}
			
			this.sendHurtMsg(hurtBuilder.build(), attacker);
			this.sendUpdateHp(targetFighters);
			
		} else {
			log.error("不在攻击范围内---------------");
		}
		return 0;
	}
	
	/**
	 * fight:(). <br/>
	 * TODO().<br/>
	 * 战斗
	 * 
	 * @author lyh
	 * @param targeter
	 * @param attacker
	 * @param hurtPojo
	 * @param hurtBuilder
	 * @return
	 */
	public int fight(IFighter targeter, IFighter attacker, SkillUnitPojo hurtPojo, BattleHurtUpdateResponse.Builder hurtBuilder) {
		try {
			// 伤害消息体
			BattleHurtUpdateMsg.Builder hurtMsg = createBattleHurtUpdateMsg();
			hurtMsg.setBeHurtId(targeter.getId());
			hurtMsg.setType(PbAoiEntityType.valueOf(targeter.getType()));
			int hurt = fightCalculate.hurt(attacker, hurtPojo, targeter, hurtMsg);
			if (attacker.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
				hurt = 0;
			}
			
			// 最后设置值
			hurtMsg.setHurtValue(hurt);
			// 暂不扣血
			// targeter.addHp(-hurt);
			targeter.addHp(-hurt);
			// System.err.println("hurt::"+hurt+"hp::"+targeter.getHp());
			hurtBuilder.addHurtList(hurtMsg.build());
			// 加入怒气
			if (attacker.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				ServerPlayer sp = (ServerPlayer) attacker;
				sp.addAnger(hurtPojo.getAngerVal());
				List<AttributeData> attrDataList = new ArrayList<AttributeData>();
				attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.ANGER_VALUE, sp.getRole().getAnger()));
				GlobalMsgManage.sendUpdateAttrResponse(sp, attrDataList, sp);
			}
			
			// Position3D hitback = fightCalculate.calHitBack(attacker, hurtPojo.getHurtMove(), targeter);
			// if (hitback != null) {
			// targeter.setX(hitback.getX());
			// targeter.setY(hitback.getY());
			// targeter.setZ(hitback.getZ());
			//
			// PbPosition3D pbPos = PbPosition3D.newBuilder().setX(hitback.getX()).setY(hitback.getY()).setZ(hitback.getZ()).build();
			// hurtMsg.setAttackedMovePos(pbPos);
			// }
			
			// 判断是否
			if (targeter.getHp() <= 0) {
				targeter.die(true);
				fightResult.fighterDead(attacker, targeter);
			} else {
				// 没有被打死是怪物
				if (targeter.getType() == IMapObject.MAP_OBJECT_TYPE_MONSTER) {
					// 加入仇恨
					Monster monster = (Monster) targeter;
					monster.getNpcHatred().addHatred(attacker, hurtPojo.getHatredVal(), true);
					monster.getAction().getAtkAction().doReadyAttack();
				}
				
				// 处理buff
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("战斗有错误::");
		}
		return 0;
	}
	
	long fighTime = System.currentTimeMillis();
	
	@Override
	public int monsterFight(IFighter attacker, IFighter defencer, ISkill skill) {
		// TODO Auto-generated method stub
		
		SkillTemplatePojo stPojo = skill.getSkillTemplatePojo();
		for (SkillUnitPojo hurtPojo : stPojo.getSkillUnitPojo()) {
			BattleHurtUpdateResponse.Builder hurtBuilder = createBattleHurtUpdateResponse();
			hurtBuilder.setAttackerId(attacker.getId());
			hurtBuilder.setAttackerType(PbAoiEntityType.valueOf(attacker.getType()));
			hurtBuilder.setSkillUnitId(hurtPojo.getId());
			hurtBuilder.setSkillId(stPojo.getId());
			
			// 选 择目标
			List<IFighter> targetList = fightAoe.selectTarget(attacker, defencer, hurtPojo, stPojo);
			if (targetList.size() <= 0) {
				targetList.add(defencer);
			}
			
			for (IFighter attacted : targetList) {
				if (!attacted.isDie()) {
					fight(attacted, attacker, hurtPojo, hurtBuilder);
				}
			}
			
			// 如果有移位加移位
			if (hurtPojo.getAttakerMove() > 0) {
				Position3D hitback = fightCalculate.calHitMove(attacker, hurtPojo.getAttakerMove(), defencer);
				if (hitback != null) {
					attacker.setX(hitback.getX());
					attacker.setY(hitback.getY());
					attacker.setZ(hitback.getZ());
					System.err.println(attacker.getName() + "战斗::" + hitback.getX() + ":::" + hitback.getZ());
					
					PbPosition3D pbPos = PbPosition3D.newBuilder().setX(hitback.getX()).setY(hitback.getY()).setZ(hitback.getZ()).build();
					hurtBuilder.setAttackerMovePos(pbPos);
				}
			}
			this.sendHurtMsg(hurtBuilder.build(), attacker);
			this.sendUpdateHp(targetList);
			
			// System.err.println("当前打斗时间::"+(System.currentTimeMillis() - fighTime));
			fighTime = System.currentTimeMillis();
		}
		return 0;
	}
	
	/**
	 * sendHurtMsg:(). <br/>
	 * TODO().<br/>
	 * 发送伤害消息
	 * 
	 * @author lyh
	 * @param msg
	 * @param sender
	 */
	public void sendHurtMsg(BattleHurtUpdateResponse msg, IFighter sender) {
		for (Map.Entry<Long, IMapObject> entry : sender.getPlayerViewMap().entrySet()) {
			IMapObject obj = entry.getValue();
			if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				MessageSend.sendToGate(msg, (ServerPlayer) obj);
			}
		}
		
		if (sender.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
			MessageSend.sendToGate(msg, (ServerPlayer) sender);
		}
	}
	
	/**
	 * sendUpdateHp:(). <br/>
	 * TODO().<br/>
	 * 更新hp
	 * 
	 * @author lyh
	 * @param list
	 */
	public void sendUpdateHp(List<IFighter> list) {
		for (IFighter fighter : list) {
			List<AttributeData> dataList = new ArrayList<AttributeData>();
			dataList.add(GlobalMsgManage.createAttributeData(AttrType.HP_VALUE, fighter.getHp()));
			GlobalMsgManage.sendAttrToAllViewObj(fighter, dataList, true);
		}
	}
	
	/**
	 * canFight:(). <br/>
	 * TODO().<br/>
	 * 角色是否能战斗
	 * 
	 * @author lyh
	 * @param src
	 * @param curTime
	 * @param skillId
	 * @return
	 */
	private boolean canFight(IMapObject src, long curTime, SkillTemplatePojo stPojo, SkillUnitPojo suPojo) {
		// 判断等级角色是否拥有这个技能
		// 判断是否已过CD时间
		ServerPlayer sp = (ServerPlayer) src;
		// 不在冷却时间内
		// Long endTime = sp.getColdTimeMap().get(IFighter.SKILL_COLD_TYPE);
		// if (endTime != null && endTime > curTime) {
		// MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_15), sp);
		// return false;
		// }
		
		// 不在动画时间内
//		Long aniEndTime = sp.getColdTimeMap().get(IFighter.SKILL_ANI_TYPE);
//		if (aniEndTime != null && aniEndTime > curTime) {
//			MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_1C), sp);
//			return false;
//		}
		
		// 判断有无此技能
		if (suPojo == null || stPojo == null) {
			MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_16), sp);
			return false;
		}
		
		// 定身或者睡眠中
		// 还要判断攻击者等级和技能的学习等级是否相符
//		List<AttributeData> attrDataList = new ArrayList<AttributeData>();
//		// 判断消耗
//		int costType = stPojo.getCost() % 10;
//		int costVal = stPojo.getCost() / 10;
//		
//		if (costType == ServerSkill.COST_MP) {
//			if (sp.getMp() < costVal) {
//				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_17), sp);
//				return false;
//			} else {
//				sp.addMp(-costVal);
//				attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.MP_VALUE, sp.getMp()));
//				GlobalMsgManage.sendUpdateAttrResponse(sp, attrDataList, sp);
//			}
//		} else if (costType == ServerSkill.COST_HP) {
//			if (sp.getHp() < costVal) {
//				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_18), sp);
//				return false;
//			} else {
//				sp.addHp(-costVal);
//				attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.HP_VALUE, sp.getHp()));
//				GlobalMsgManage.sendAttrToAllViewObj(sp, attrDataList, true);
//			}
//			
//		} else if (costType == ServerSkill.COST_ANGER) {
//			if (sp.getRole().getAnger() < costVal) {
//				MessageSend.sendToGate(createPopUpTip(ErrorCode.STATUS_19), sp);
//				return false;
//			} else {
//				sp.addAnger(-costVal);
//				attrDataList.add(GlobalMsgManage.createAttributeData(AttrType.ANGER_VALUE, sp.getRole().getAnger()));
//				GlobalMsgManage.sendUpdateAttrResponse(sp, attrDataList, sp);
//			}
//			
//		}
//		
		sp.getColdTimeMap().put(IFighter.SKILL_COLD_TYPE, System.currentTimeMillis() + stPojo.getCdTime());
		sp.getColdTimeMap().put(IFighter.SKILL_ANI_TYPE, System.currentTimeMillis() + stPojo.getAnimationTime());
		
		return true;
	}
	
	/**
	 * createBattleHurtUpdateResponse:(). <br/>
	 * TODO().<br/>
	 * 创建战斗伤害类
	 * 
	 * @author lyh
	 * @return
	 */
	public BattleHurtUpdateResponse.Builder createBattleHurtUpdateResponse() {
		return BattleHurtUpdateResponse.newBuilder();
	}
	
	// S_C_BATTLE_HURT_UPDATE_RESPONSE = 0x20404;//服务器更新伤害
	// Messagee BattleHurtUpdateResponse{
	// repeated BattleHurtUpdateMsg hurtList =1; //受伤列表
	// optional fixed64 attackerId = 2;//攻击者ID
	// optional PbAoiEntityType attackerType = 3 [default = None];//攻击者类型
	// optional int32 skillUnitId = 4;// 动作ID
	// }
	
	/**
	 * createBattleHurtUpdateMsg:(). <br/>
	 * TODO().<br/>
	 * 创建每一个伤害数据
	 * 
	 * @author lyh
	 * @return
	 */
	public BattleHurtUpdateMsg.Builder createBattleHurtUpdateMsg() {
		
		BattleHurtUpdateMsg.Builder msg = BattleHurtUpdateMsg.newBuilder();
		// msg.setBeHurtId(hurtTargetId);
		// msg.setSkillId(skillId);
		// msg.setSkillUnitId(skillUnitId);
		// msg.setType(hurtTargetType);
		// msg.setHitType(hitType);
		return msg;
	}
	
	//
	// message BattleHurtUpdateMsg{
	// optional fixed64 beHurtId = 1; //被击者ID
	// optional int32 skillId = 2;//被skillId击中
	// optional int32 skillUnitId = 3;// 动作ID
	// optional PbHitType hitType = 4; //受击类型
	// optional int32 hurtValue = 5;//被伤害数值
	// optional PbAoiEntityType type = 6 [default = None];//类型
	// }
	
}
