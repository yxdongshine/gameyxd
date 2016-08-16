package com.lx.logical.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.config.xml.map.SpaceInfo;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.AbsMonster;
import com.engine.entityobj.Npc;
import com.engine.entityobj.NpcHatred;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.ServerSkill;
import com.engine.entityobj.space.IMapObject;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.game.monster.Monster.InitMonstersResponse;
import com.loncent.protocol.game.monster.Monster.InitNpcResponse;
import com.loncent.protocol.game.monster.Monster.MonsterData;
import com.loncent.protocol.game.monster.Monster.NpcData;
import com.lx.game.entity.GameSpace;
import com.lx.game.entity.MapObjectMessage;
import com.lx.game.entity.Monster;
import com.lx.game.entity.listener.IDropItemListener;
import com.lx.game.fight.FightManage;
import com.lx.game.monster.action.MonsterAction;
import com.lx.game.send.MessageSend;
import com.lx.logical.RequestTaskAdapter;
import com.lx.nserver.model.MonsterGroupModel;
import com.lx.nserver.model.MonsterModel;
import com.lx.nserver.model.SkillTemplateModel;
import com.lx.nserver.txt.MonsterGroupPojo;
import com.lx.nserver.txt.MonsterPojo;
import com.lx.nserver.txt.SkillmodelPojo;

/**
 * ClassName:MonsterManage <br/>
 * Function: TODO (怪物管理类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-24 下午2:11:22 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class MonsterManage extends RequestTaskAdapter {
	
	@Autowired
	private MonsterGroupModel monsterGroupModel;
	
	@Autowired
	private MonsterModel monsterModel;
	
	@Autowired
	private FightManage fightManage;
	
	@Autowired
	private SkillTemplateModel skillTemplateModel;
	
	/**
	 * createMonster:(). <br/>
	 * TODO().<br/>
	 * 创建怪物
	 * 
	 * @author lyh
	 * @param monsterConfigId
	 * @return
	 */
	public Monster createMonster(int monsterConfigId, float x, float y, float z, GameSpace space) {
		Monster monster = new Monster();
		MonsterPojo pojo = monsterModel.get(monsterConfigId);
		if (pojo != null) {
			monster.setId(space.generateMonsterId());
			monster.setMonsterPojo(pojo);
			monster.setName(pojo.getName());
			try {
				Position3D pos;
				if(pojo.getType() == 1)//普通怪随机位置
					pos = initMonsteRandomRange(x, y, z, 3, space.getSpaceInfo());
				else
					pos = new Position3D(x,y,z);
				
				monster.setPosition3D(pos.getX(), pos.getY(), pos.getZ());
				monster.setInitPoint3d(monster.getPosition3D());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			monster.setType(IMapObject.MAP_OBJECT_TYPE_MONSTER);
			monster.setSpace(space);
			monster.setNpcHatred(new NpcHatred(monster));
			Attribute attr = new Attribute();
			addMonsterAttribute(pojo, attr);
			monster.setAttribute(attr);
			monster.setHp(pojo.getCurHp());
			monster.setMaxHp(pojo.getMaxHp());
			// 怪物加入地图
			space.addToMap(monster);
			MapObjectMessage sendlistener = new MapObjectMessage();
			sendlistener.init(monster);
			monster.setMapObjectMessage(sendlistener);
			monster.setIFightListener(fightManage);
			// 怪物加入地图怪物容器
			space.getMonsterMaps().put(monster.getId(), monster);
			// 怪物加入视野
			space.doMapObjectAddView(monster, monster.getArea().getNineArea());
		
			// log.info("y = " + monster.getPosition3D().getY());
			monster.setAction(new MonsterAction(monster));
			// 加载怪物技能
			loadSkill(monster);
			
		} else {
			log.error("没有创建怪物:::" + monsterConfigId);
		}
		return monster;
	}
	
	/**
	 * loadSkill:(). <br/>
	 * TODO().<br/>
	 * 加载怪物技能
	 * 
	 * @author lyh
	 * @param monster
	 */
	public void loadSkill(Monster monster) {
		// 加载怪物技能
		int index = 0;
		for (int i : monster.getMonsterPojo().getSkills()) {
			ServerSkill sSkill = new ServerSkill();
			sSkill.setSkillTemplatePojo(skillTemplateModel.get(i));
			monster.getSkillMap().put(sSkill.getSkillTemplatePojo().getId(), sSkill);
			if (sSkill.getSkillTemplatePojo() == null) {
				log.error("加载怪物技能 为空:::" + i);
			}
			if (index == 0) {
				monster.setUseSkill(sSkill);
			}
			index++;
		}
	}
	
	
	
	/**
	 * initMonsteRandomRange:(). <br/>
	 * TODO().<br/>
	 * 初始化怪物坐标(随机的)
	 * 
	 * @author lyh
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	private Position3D initMonsteRandomRange(float x, float y, float z, int maxRange, SpaceInfo sInfo) {
		try {
			float dx, dz;
			for (int i = 0; i < 10; i++) {
				float xRange = ServerRandomUtils.randomFloat(maxRange);
				float zRange = ServerRandomUtils.randomFloat(maxRange);
				if ((int) xRange % 2 == 0) {
					xRange = -xRange;
				}
				
				if ((int) zRange % 2 == 0) {
					zRange = -zRange;
				}
				
				dx = x + xRange;
				dz = z + zRange;
				// 判断 是否超过追击距离内
				boolean dh = sInfo.isCanWalk(dx, y, dz);
				if (dh) {
					// 转到chr再转回来,进行规一化,防止卡在障碍
					return new Position3D(dx, y, dz);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new Position3D(x, y, z);
	}
	
	/**
	 * createMonsters:(). <br/>
	 * TODO().<br/>
	 * 创建怪物组
	 * 
	 * @author lyh
	 * @param monsterGroupConfigId
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public List<AbsMonster> createMonsters(int monsterGroupConfigId, float x, float y, float z, GameSpace space) {
		List<AbsMonster> list = new ArrayList<AbsMonster>();
		MonsterGroupPojo pojo = monsterGroupModel.get(monsterGroupConfigId);
		if (pojo != null) {
			for (int i = 0; i < pojo.getMonsterId().length; i++) {
				for (int j = 0; j < pojo.getMonsterId()[i][1]; j++) {
					AbsMonster monster = createMonster(pojo.getMonsterId()[i][0], x, y, z, space);
					monster.setGroupId(monsterGroupConfigId);
					list.add(monster);
					//return list;
				}  
			}
			
		} else {
			log.error("没有创建怪物组:" + monsterGroupConfigId);
		}
		return list;
	}
	
	/**
	 * addMonsterAttribute:(). <br/>
	 * TODO().<br/>
	 * 加入怪物属性(85个)
	 * 
	 * @author lyh
	 * @param pojo
	 * @param attr
	 */
	public void addMonsterAttribute(MonsterPojo pojo, Attribute attr) {
		attr.addBaseAttribute(Attribute.AIR, pojo.getAir());
		attr.updateAttribute(Attribute.AIR);
		attr.addBaseAttribute(Attribute.AGILITY, pojo.getAgility());
		attr.updateAttribute(Attribute.AGILITY);
		attr.addBaseAttribute(Attribute.INNER_FORCE, pojo.getInnerForce());
		attr.updateAttribute(Attribute.INNER_FORCE);
		attr.addBaseAttribute(Attribute.CONTROL, pojo.getControl());
		attr.updateAttribute(Attribute.CONTROL);
		attr.addBaseAttribute(Attribute.TENACITY, pojo.getTenacity());
		attr.updateAttribute(Attribute.TENACITY);
		attr.addBaseAttribute(Attribute.EXTERNAL_FORCE_ATTACK, pojo.getExternalForceAttack());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_ATTACK);
		attr.addBaseAttribute(Attribute.INNER_FORCE_ATTACK, pojo.getInnerForceAttack());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_ATTACK);
		attr.addBaseAttribute(Attribute.SCORCHING_SUN_ATTACK, pojo.getScorchingSunAttack());
		attr.updateAttribute(Attribute.SCORCHING_SUN_ATTACK);
		attr.addBaseAttribute(Attribute.PLOUGH_ATTACK, pojo.getPloughAttack());
		attr.updateAttribute(Attribute.PLOUGH_ATTACK);
		attr.addBaseAttribute(Attribute.NETHER_ATTACK, pojo.getNetherAttack());
		attr.updateAttribute(Attribute.NETHER_ATTACK);
		attr.addBaseAttribute(Attribute.LUNAR_ATTACK, pojo.getLunarAttack());
		attr.updateAttribute(Attribute.LUNAR_ATTACK);
		attr.addBaseAttribute(Attribute.MELEE_HIT_RATE, pojo.getMeleeHitRate());
		attr.updateAttribute(Attribute.MELEE_HIT_RATE);
		attr.addBaseAttribute(Attribute.REMOTE_HIT_RATE, pojo.getRemoteHitRate());
		attr.updateAttribute(Attribute.REMOTE_HIT_RATE);
		attr.addBaseAttribute(Attribute.MELEE_DODGE_RATE, pojo.getMeleeDodgeRate());
		attr.updateAttribute(Attribute.MELEE_DODGE_RATE);
		attr.addBaseAttribute(Attribute.REMOTE_DODGE_RATE, pojo.getRemoteDodgeRate());
		attr.updateAttribute(Attribute.REMOTE_DODGE_RATE);
		attr.addBaseAttribute(Attribute.MAX_HP, pojo.getMaxHp());
		attr.updateAttribute(Attribute.MAX_HP);
		attr.addBaseAttribute(Attribute.CUR_HP, pojo.getCurHp());
		attr.updateAttribute(Attribute.CUR_HP);
		attr.addBaseAttribute(Attribute.MAX_MP, pojo.getMaxMp());
		attr.updateAttribute(Attribute.MAX_MP);
		attr.addBaseAttribute(Attribute.CUR_MP, pojo.getCurMp());
		attr.updateAttribute(Attribute.CUR_MP);
		attr.addBaseAttribute(Attribute.DEFENCE, pojo.getDefence());
		attr.updateAttribute(Attribute.DEFENCE);
		attr.addBaseAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_REDUCE, pojo.getExternalForceDamageReduce());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.INNER_FORCE_DAMAGE_REDUCE, pojo.getInnerForceDamageReduce());
		attr.updateAttribute(Attribute.INNER_FORCE_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.SCORCHING_SUN_DAMAGE_REDUCE, pojo.getScorechingSunDamageReduce());
		attr.updateAttribute(Attribute.SCORCHING_SUN_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.PLOUGH_DAMAGE_REDUCE, pojo.getPloughDamageReduce());
		attr.updateAttribute(Attribute.PLOUGH_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.NETHER_DAMAGE_REDUCE, pojo.getNetherDamageReduce());
		attr.updateAttribute(Attribute.NETHER_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.LUNAR_DAMAGE_REDUCE, pojo.getLunarDamageReduce());
		attr.updateAttribute(Attribute.LUNAR_DAMAGE_REDUCE);
		attr.addBaseAttribute(Attribute.KNOWING_RATE, pojo.getKnowingRate());
		attr.updateAttribute(Attribute.KNOWING_RATE);
		attr.addBaseAttribute(Attribute.PARRY_RATE, pojo.getParryRate());
		attr.updateAttribute(Attribute.PARRY_RATE);
		attr.addBaseAttribute(Attribute.ATTACK_SPEED, pojo.getAttackSpeed());
		attr.updateAttribute(Attribute.ATTACK_SPEED);
		attr.addBaseAttribute(Attribute.ATTACK_SCOPE, pojo.getAttackScope());
		attr.updateAttribute(Attribute.ATTACK_SCOPE);
		attr.addBaseAttribute(Attribute.SPEED, pojo.getSpeed());
		attr.updateAttribute(Attribute.SPEED);
		attr.addBaseAttribute(Attribute.ALL_HURT_LOWER, pojo.getAllHurtLower());
		attr.updateAttribute(Attribute.ALL_HURT_LOWER);
		attr.addBaseAttribute(Attribute.EXP_BASE, pojo.getExpBase());
		attr.updateAttribute(Attribute.EXP_BASE);
		attr.addBaseAttribute(Attribute.TARGET_ROLE_COEFFICIENT, pojo.getTargetRoleCoefficient());
		attr.updateAttribute(Attribute.TARGET_ROLE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.TARGET_MONSTER_COEFFICIENT, pojo.getTargetMonsterCoefficient());
		attr.updateAttribute(Attribute.TARGET_MONSTER_COEFFICIENT);
		attr.addBaseAttribute(Attribute.TARGET_PET_COEFFICIENT, pojo.getTargetPetCoefficient());
		attr.updateAttribute(Attribute.TARGET_PET_COEFFICIENT);
		attr.addBaseAttribute(Attribute.TARGET_CALL_MONSTER_COEFFICIENT, pojo.getTargetCallMonsterCoefficient());
		attr.updateAttribute(Attribute.TARGET_CALL_MONSTER_COEFFICIENT);
		attr.addBaseAttribute(Attribute.COEFFICIENT_FROM_ROLE, pojo.getCoefficientFromRole());
		attr.updateAttribute(Attribute.COEFFICIENT_FROM_ROLE);
		attr.addBaseAttribute(Attribute.COEFFICIENT_FROM_MONSTER, pojo.getCoefficientFromMonster());
		attr.updateAttribute(Attribute.COEFFICIENT_FROM_MONSTER);
		attr.addBaseAttribute(Attribute.COEFFICIENT_FROM_PET, pojo.getCoefficientFromPet());
		attr.updateAttribute(Attribute.COEFFICIENT_FROM_PET);
		attr.addBaseAttribute(Attribute.COEFFICIENT_FROM_CALL_MONSTER, pojo.getCofficientFromCallMonster());
		attr.updateAttribute(Attribute.COEFFICIENT_FROM_CALL_MONSTER);
		attr.addBaseAttribute(Attribute.HP_RECOVER_SPEED, pojo.getHpRecoverSpeed());
		attr.updateAttribute(Attribute.HP_RECOVER_SPEED);
		attr.addBaseAttribute(Attribute.MP_RECOVER_SPEED, pojo.getMpRecoverSpeed());
		attr.updateAttribute(Attribute.MP_RECOVER_SPEED);
		attr.addBaseAttribute(Attribute.SPRIT_SPEED, pojo.getSpritSpeed());
		attr.updateAttribute(Attribute.SPRIT_SPEED);
		attr.addBaseAttribute(Attribute.SPRIT_PIERCE_RATE, pojo.getSpritPierceRate());
		attr.updateAttribute(Attribute.SPRIT_PIERCE_RATE);
		attr.addBaseAttribute(Attribute.DIZZINESS_RATE, pojo.getDizzinessRate());
		attr.updateAttribute(Attribute.DIZZINESS_RATE);
		attr.addBaseAttribute(Attribute.DIZZINESS_RESISTANCE, pojo.getDizzinessResistance());
		attr.updateAttribute(Attribute.DIZZINESS_RESISTANCE);
		attr.addBaseAttribute(Attribute.HP_STEAL, pojo.getHpSteal());
		attr.updateAttribute(Attribute.HP_STEAL);
		attr.addBaseAttribute(Attribute.MP_STEAL, pojo.getMpSteal());
		attr.updateAttribute(Attribute.MP_STEAL);
		attr.addBaseAttribute(Attribute.FROZEN_RATE, pojo.getFrozenRate());
		attr.updateAttribute(Attribute.FROZEN_RATE);
		attr.addBaseAttribute(Attribute.FROZEN_RESISTANCE, pojo.getFrozenResistance());
		attr.updateAttribute(Attribute.FROZEN_RESISTANCE);
		attr.addBaseAttribute(Attribute.HP_STEAL_RESISTANCE, pojo.getHpStealResistance());
		attr.updateAttribute(Attribute.HP_STEAL_RESISTANCE);
		attr.addBaseAttribute(Attribute.MP_STEAL_RESISTANCE, pojo.getMpStealResistance());
		attr.updateAttribute(Attribute.MP_STEAL_RESISTANCE);
		attr.addBaseAttribute(Attribute.HP_BURN_RESISTANCE, pojo.getHpBurnResistance());
		attr.updateAttribute(Attribute.HP_BURN_RESISTANCE);
		attr.addBaseAttribute(Attribute.MP_BURN_RESISTANCE, pojo.getMpBurnResistance());
		attr.updateAttribute(Attribute.MP_BURN_RESISTANCE);
		attr.addBaseAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_OBSORB, pojo.getExternalForceDamageObsorb());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.INNER_FORCE_DAMAGE_OBSORB, pojo.getInnerForceDamageObsorb());
		attr.updateAttribute(Attribute.INNER_FORCE_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.SCORCHING_SUN_DAMAGE_OBSORB, pojo.getScorchingSunDamageObsorb());
		attr.updateAttribute(Attribute.SCORCHING_SUN_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.PLOUGH_DAMAGE_OBSORB, pojo.getPloughDamageObsorb());
		attr.updateAttribute(Attribute.PLOUGH_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.NETHER_DAMAGE_OBSORB, pojo.getNetherDamageObsorb());
		attr.updateAttribute(Attribute.NETHER_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.LUNAR_DAMAGE_OBSORB, pojo.getLunarDamageObsorb());
		attr.updateAttribute(Attribute.LUNAR_DAMAGE_OBSORB);
		attr.addBaseAttribute(Attribute.EXTERNAL_FORCE_REBIND, pojo.getExternalForceRebind());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_REBIND);
		attr.addBaseAttribute(Attribute.INNER_FORCE_REBIND, pojo.getInnerForceRebind());
		attr.updateAttribute(Attribute.INNER_FORCE_REBIND);
		attr.addBaseAttribute(Attribute.SCORCHING_SUN_REBIND, pojo.getScorchingSunRebind());
		attr.updateAttribute(Attribute.SCORCHING_SUN_REBIND);
		attr.addBaseAttribute(Attribute.PLOUGH_REBIND, pojo.getPloughRebind());
		attr.updateAttribute(Attribute.PLOUGH_REBIND);
		attr.addBaseAttribute(Attribute.NETHER_REBIND, pojo.getNetherRebind());
		attr.updateAttribute(Attribute.NETHER_REBIND);
		attr.addBaseAttribute(Attribute.LUNAR_REBIND, pojo.getLunarRebind());
		attr.updateAttribute(Attribute.LUNAR_REBIND);
		attr.addBaseAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_COEFFICIENT, pojo.getExternalForceDamageCoefficient());
		attr.updateAttribute(Attribute.EXTERNAL_FORCE_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.INNER_FORCE_DAMAGE_COEFFICIENT, pojo.getInnerForceDamageCoefficient());
		attr.updateAttribute(Attribute.INNER_FORCE_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.SCORCHING_SUN_DAMAGE_COEFFICIENT, pojo.getScorchingSunDamageCoefficient());
		attr.updateAttribute(Attribute.SCORCHING_SUN_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.PLOUGH_DAMAGE_COEFFICIENT, pojo.getPloughDamageCoefficent());
		attr.updateAttribute(Attribute.PLOUGH_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.NETHER_DAMAGE_COEFFICIENT, pojo.getNetherDamageCoefficient());
		attr.updateAttribute(Attribute.NETHER_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.LUNAR_DAMAGE_COEFFICIENT, pojo.getLunarDamageCoefficient());
		attr.updateAttribute(Attribute.LUNAR_DAMAGE_COEFFICIENT);
		attr.addBaseAttribute(Attribute.KNOWING_HURT_RATE, pojo.getKnowingHurtHurtRate());
		attr.updateAttribute(Attribute.KNOWING_HURT_RATE);
		attr.addBaseAttribute(Attribute.PARRY_HURT_RATE, pojo.getParryHurtRate());
		attr.updateAttribute(Attribute.PARRY_HURT_RATE);
		attr.addBaseAttribute(Attribute.ALL_HURT_ADD, pojo.getAllHurtAdd());
		attr.updateAttribute(Attribute.ALL_HURT_ADD);
		attr.addBaseAttribute(Attribute.HEAVY_ATTACK, pojo.getHeavyAttack());
		attr.updateAttribute(Attribute.HEAVY_ATTACK);
		attr.addBaseAttribute(Attribute.WATCH_BOX_DEF, pojo.getWatchBosDef());
		attr.updateAttribute(Attribute.WATCH_BOX_DEF);
		attr.addBaseAttribute(Attribute.NO_MOVE_RATE, pojo.getNoMoveRate());
		attr.updateAttribute(Attribute.NO_MOVE_RATE);
		attr.addBaseAttribute(Attribute.NO_MOVE_RESISTANCE, pojo.getNoMoveResistance());
		attr.updateAttribute(Attribute.NO_MOVE_RESISTANCE);
		attr.addBaseAttribute(Attribute.TOUGHNESS_RATE, pojo.getToughnessRate());
		attr.updateAttribute(Attribute.TOUGHNESS_RATE);
		attr.addBaseAttribute(Attribute.TOUGHNESS_EFFECT, pojo.getToughnessEffect());
		attr.updateAttribute(Attribute.TOUGHNESS_EFFECT);
		attr.addBaseAttribute(Attribute.DISRUPTING, pojo.getDisrupting());
		attr.updateAttribute(Attribute.DISRUPTING);
		attr.addBaseAttribute(Attribute.DISRUPTING_EFFECT, pojo.getDisruptingEffect());
		attr.updateAttribute(Attribute.DISRUPTING_EFFECT);
		attr.addBaseAttribute(Attribute.EXTRA_SKILL_POINT, pojo.getExtraSkillPoint());
		attr.updateAttribute(Attribute.EXTRA_SKILL_POINT);
	}
	
	/**
	 * sendInitMonstersResponse:(). <br/>
	 * TODO().<br/>
	 * 怪物的基本数据
	 * 
	 * @author lyh
	 */
	public void sendInitMonstersResponse(ConcurrentHashMap<Long, AbsMonster> monsters, ServerPlayer sp) {
		InitMonstersResponse.Builder builder = InitMonstersResponse.newBuilder();
		for (Map.Entry<Long, AbsMonster> entry : monsters.entrySet()) {
			builder.addMonsterData(createMonsterData(entry.getValue()));
		}
		MessageSend.sendToGate(builder.build(), sp);
		log.error("已发送怪物进入消息-------------");
	}
	
	/**
	 * createMonsterData:(). <br/>
	 * TODO().<br/>
	 * 创建怪物数据
	 * 
	 * @author lyh
	 * @param monster
	 * @return
	 */
	public MonsterData createMonsterData(AbsMonster monster) {
		MonsterData.Builder data = MonsterData.newBuilder();
		data.setHp(monster.getHp());
		data.setMaxHp(monster.getMaxHp());
		data.setMonsterId(monster.getId());
		data.setSpeed(monster.getAttribute().getAttribute(Attribute.SPEED));
		data.setX(monster.getPosition3D().getX());
		data.setY(monster.getPosition3D().getY());
		data.setZ(monster.getPosition3D().getZ());
		data.setFace(monster.getDir());
		data.setMonsterConfigId(monster.getMonsterPojo().getId());
		return data.build();
	}
	
	/**
	 * 
	 * sendInitNpcsResponse:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param npcs
	 * @param sp
	 */
	public void sendInitNpcsResponse(ConcurrentHashMap<Long, Npc> npcs, ServerPlayer sp) {
		InitNpcResponse.Builder builder = InitNpcResponse.newBuilder();
		for (Map.Entry<Long, Npc> entry : npcs.entrySet()) {
			builder.addNpcData(createNpcData(entry.getValue()));
		}
		MessageSend.sendToGate(builder.build(), sp);
		log.error("已发送NPC进入消息-------------");
	}
	
	/**
	 * 
	 * createNpcData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param npc
	 * @return
	 */
	public NpcData createNpcData(Npc npc) {
		NpcData.Builder data = NpcData.newBuilder();
		data.setNpcId(npc.getId());
		data.setX(npc.getPosition3D().getX());
		data.setY(npc.getPosition3D().getY());
		data.setZ(npc.getPosition3D().getZ());
		data.setFace(npc.getDir());
		data.setNpcConfigId(npc.getNpcConfigId());
		return data.build();
	}
}
