package com.engine.entityobj;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.space.AbsMapObject;
import com.engine.entityobj.space.IMapObject;
import com.engine.entityobj.space.AbsSpace;
import com.engine.entityobj.space.IMapObjectMessage;
import com.engine.interfaces.IFightListener;
import com.engine.interfaces.ISkill;
import com.engine.interfaces.ITickable;
import com.lx.nserver.txt.MonsterPojo;

/**
 * ClassName:Monster <br/>
 * Function: TODO (怪物对象数据层). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-23 上午9:15:03 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class AbsMonster extends AbsMapObject implements IFighter, ITickable {
	
	/** 怪物属性 **/
	protected Attribute attribute;
	/** 怪物初始坐标坐标 **/
	protected Position3D initPoint3d;
	
	/** 怪物配置文件 **/
	protected MonsterPojo monsterPojo;
	
	/** 广播监听类 **/
	protected IMapObjectMessage messageSend;
	
	/** 对象已有技能的容器 **/
	protected Map<Integer, ISkill> skillMap = new ConcurrentHashMap<Integer, ISkill>();
	
	/**冷却时间容器**/  
	protected Map<Integer,Long> coldTimeMap = new ConcurrentHashMap<Integer,Long>();
	


	/** 血 **/
	protected int hp;
	
	/** 最大的hp **/
	protected int maxHp;
	
	/** 目标 **/
	protected IFighter targetFighter;
	protected IFightListener fightLister;
	
	/** 是否已经死亡 **/
	protected boolean bDie;
	
	/** 怪物状态 **/
	protected int status;
	
	/** 仇恨列表 **/
	protected NpcHatred npcHatred;
	
	/** 怪物当前使用的技能 **/
	protected ISkill useSkill;
	
	/** 怪物组ID **/  
	protected int groupId;
	
	// /**每次攻击等待时间**/
	// protected long waitAttackTime = System.currentTimeMillis();
	
	public MonsterPojo getMonsterPojo() {
		return monsterPojo;
	}
	
	public void setMonsterPojo(MonsterPojo monsterPojo) {
		this.monsterPojo = monsterPojo;
	}
	
	@Override
	public Attribute getAttribute() {
		// TODO Auto-generated method stub
		return attribute;
	}
	
	@Override
	public void setAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		attribute = attr;
	}
	
	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return monsterPojo == null ? 0 : monsterPojo.getLevel();
	}
	
	/**
	 * getMonsterType:(). <br/>
	 * TODO().<br/>
	 * 怪物类型
	 * 
	 * @author lyh
	 * @return
	 */
	public int getMonsterType() {
		return monsterPojo.getType();
	}
	
	public Position3D getInitPoint3d() {
		return initPoint3d;
	}
	
	public void setInitPoint3d(Position3D initPoint3d) {
		this.initPoint3d = new Position3D(initPoint3d);
	}
	
	@Override
	public IMapObjectMessage getMapObjectMessage() {
		// TODO Auto-generated method stub
		return messageSend;
	}
	
	@Override
	public void setMapObjectMessage(IMapObjectMessage listener) {
		// TODO Auto-generated method stub
		messageSend = listener;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public NpcHatred getNpcHatred() {
		return npcHatred;
	}
	
	public void setNpcHatred(NpcHatred npcHatred) {
		this.npcHatred = npcHatred;
	}
	
	/**
	 * isAtkType:(). <br/>
	 * TODO().<br/>
	 * 是否是主动怪
	 * 
	 * @author lyh
	 * @return
	 */
	public boolean isAtkType() {
		return monsterPojo.getAtkType() == 1;
	}
	
	@Override
	public int getHp() {
		// TODO Auto-generated method stub
		return hp;
	}
	
	@Override
	public void setMaxHp(int hp) {
		// TODO Auto-generated method stub
		this.maxHp = hp;
	}
	
	@Override
	public int getMaxHp() {
		// TODO Auto-generated method stub
		return maxHp;
	}
	
	@Override
	public void setHp(int hp) {
		// TODO Auto-generated method stub
		this.hp = hp;
	}
	
	@Override
	public void addHp(int vHp) {
		// TODO Auto-generated method stub
		hp += vHp;
	}
	
	@Override
	public int getMp() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getMaxMp() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void setMp(int mp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setMaxMp(int mp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addMp(int vHp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean isDie() {
		// TODO Auto-generated method stub
		return bDie;
	}
	
	@Override
	public IFighter getTargetFighter() {
		// TODO Auto-generated method stub
		return targetFighter;
	}
	
	@Override
	public void setTargetFighter(IFighter fighter) {
		// TODO Auto-generated method stub
		targetFighter = fighter;
	}
	
	@Override
	public ISkill getSkillById(int skillId) {
		// TODO Auto-generated method stub
		return skillMap.get(skillId);
	}
	
	@Override
	public Map<Integer, ISkill> getSkillMap() {
		// TODO Auto-generated method stub
		return skillMap;
	}
	
	@Override
	public ISkill getUseSkill() {
		// TODO Auto-generated method stub
		return useSkill;
	}
	
	public void setUseSkill(ISkill useSkill) {
		this.useSkill = useSkill;
	}
	
	@Override
	public void tick(long time) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setIFightListener(IFightListener listener) {
		// TODO Auto-generated method stub
		this.fightLister = listener;
	}
	
	@Override
	public IFightListener getIFightListener() {
		// TODO Auto-generated method stub
		return fightLister;
	}
	
	
	@Override
	public void die(boolean bDie) {
		// TODO Auto-generated method stub
		this.bDie = bDie;
	}
	
	@Override
    public Map<Integer, Long> getColdTimeMap() {
	    // TODO Auto-generated method stub
	    return coldTimeMap;
    }

	public int getGroupId() {
    	return groupId;
    }

	public void setGroupId(int groupId) {
    	this.groupId = groupId;
    }
	
	
}
