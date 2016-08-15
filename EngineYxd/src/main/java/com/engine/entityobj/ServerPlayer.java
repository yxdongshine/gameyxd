package com.engine.entityobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.engine.dbdao.EntityDAO;
import com.engine.domain.Role;
import com.engine.domain.TaskData;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.space.AbsMapObject;
import com.engine.interfaces.IFightListener;
import com.engine.interfaces.ISkill;
import com.loncent.protocol.game.battle.Battle.BattleDoActionRequest;
import com.loncent.protocol.game.battle.Battle.BattleHurtRequest;
import com.lx.game.item.Bag;
import com.lx.game.task.TaskModular;
import com.lx.game.team.TeamModular;
import com.lx.nserver.txt.CareerPojo;
import com.lx.nserver.txt.RoleInitPojo;

/**
 * ClassName:ServerPlayer <br/>
 * Function: TODO (角色类). <br/>
 * Reason: TODO (这个类先写在这儿，有时间要整理下). <br/>
 * Date: 2015-7-3 上午9:04:06 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerPlayer extends AbsMapObject implements IFighter {
	
	private Role role;
	
	/** 职业配置文件 **/
	private CareerPojo careerPojo;
	
	/** 角色初始数据 **/
	private RoleInitPojo roleInitPojo;
	
	/** 客户端sessionId **/
	private long clientSessionId;
	
	/** 网关服务类型id **/
	private int gateServerTypeId;
	/** 角色属性 **/
	private Attribute attribute;
	
	/** 可使用的属性点 **/
	private int usePoint;
	
	/** 记录5个基础属性,(以后洗点要用) **/
	private int recordBasePoint[] = new int[5];
	
	/** 队伍名 **/
	private String teamName;
	
	/** 钱的同步锁 **/
	private Integer synMoney = new Integer(1);
	
	/** 金砖同步锁 **/
	private Integer synrRechargeGold = new Integer(2);
	
	/** 绑定元宝绑定锁 **/
	private Integer synBindGold = new Integer(3);
	
	/** 同步经验值 **/
	private Integer synExp = new Integer(4);
	
	/**
	 * 玩家背包
	 */
	private Bag bag;
	
	/**
	 * 玩家任务模块
	 */
	private TaskModular taskModular;
	
	/**
	 * 玩家小队模块
	 */
	private TeamModular teamModular;
	
	/****/
	private int status;
	
	/**
	 * 上次使用药瓶时间
	 */
	private long lastUseBattleTime = 0;
	
	/**
	 * 上次升级药瓶的前的配置模板编号
	 */
	private long lastBattleId;
	
	/** 目标 **/
	protected IFighter targetFighter;
	protected IFightListener fightLister;
	
	/** 是否已经死亡 **/
	protected boolean bDie;
	/**自动复活时间**/  
	protected long autoReliveTime;
	
//	/** 每次攻击等待时间 **/
//	protected long waitAttackTime = System.currentTimeMillis();
	
	/** 角色技能列表 **/
	private Map<Integer, ISkill> skillList = new HashMap<Integer, ISkill>();
	
	/** 角色按钮映射 key 按钮位置 value 技能 **/
	private Map<Integer, ISkill> skillBtnMap = new HashMap<Integer, ISkill>();
	
	/** 战斗队列 **/
	protected ConcurrentLinkedQueue<BattleHurtRequest> fightAttackQueue = new ConcurrentLinkedQueue<BattleHurtRequest>();
	
	/**冷却时间容器**/  
	protected Map<Integer,Long> coldTimeMap = new ConcurrentHashMap<Integer,Long>();
	
	public Map<Integer, ISkill> getSkillBtnMap() {
		return skillBtnMap;
	}
	
	public void setSkillBtnMap(Map<Integer, ISkill> skillBtnMap) {
		this.skillBtnMap = skillBtnMap;
	}
	
	/**
	 * 
	 * offIineSaveBagData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void offIineSaveBagData(EntityDAO dao) {
		this.getBag().saveBagData(dao);
		// 装备容量
		this.getRole().setEquitGridNumber(this.getBag().getSubBags()[0].capacity);
		// 普通道具容量
		this.getRole().setCommonGridNumber(this.getBag().getSubBags()[1].capacity);
		
		// log.info("角色：" + getRole().getId() + "  背包数据保存完成");
	}
	
	/**
	 * 
	 * saveTaskData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param sp
	 */
	public void saveTaskData(EntityDAO dao) {
		for (Iterator iterator = this.getTaskModular().getTaskHashmap().values().iterator(); iterator.hasNext();) {
			TaskData taskData = (TaskData) iterator.next();
			dao.saveOrUpdate(taskData);
		}
		
	}
	
	
	/**
	 * 获取小队编号
	 * getTeamId:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @return
	 */
	public long getTeamId(){
		long teamId=0;
		if(getTeamModular()!=null){
			teamId=getTeamModular().getTeamId();
		}
		return teamId;
	}
	
	/**
	 * 获取队长的顺序
	 * getRoleTeamOrder:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @return
	 */
	public int getRoleTeamOrder(){
		int order=0;
		if(getTeamModular()!=null){
			order=getTeamModular().isTeamLeader(getRole().getId());
		}
		return order;
	}
	
	
	/**
	 * 扣款是否成功
	 * 
	 * 
	 * isDebitSuccess:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param currencyType 0金币，1充值元宝，2 绑定元宝
	 * @param number
	 * @param price
	 * @return
	 */
	public boolean isDebitSuccess(int currencyType, int number, int price) {
		boolean isDebitSuccess = false;
		switch (currencyType) {
			case 0: // 金币
				if (getRole().getMoney() - (number * price) >= 0) {// 能扣
					getRole().setMoney(getRole().getMoney() - (number * price));
					isDebitSuccess = true;
				} else {
					isDebitSuccess = false;
				}
				break;
			case 1: // 充值元宝
				if (getRole().getRechargeGold() - (number * price) >= 0) {// 能扣
					getRole().setRechargeGold(getRole().getRechargeGold() - (number * price));
					isDebitSuccess = true;
				} else {
					isDebitSuccess = false;
				}
				break;
			case 2: // 绑定元宝
				if (getRole().getBindGold() - (number * price) >= 0) {// 能扣
					getRole().setBindGold(getRole().getBindGold() - (number * price));
					isDebitSuccess = true;
				} else {
					isDebitSuccess = false;
				}
				break;
			default:
				break;
		}
		return isDebitSuccess;
	}
	
	/**
	 * 给用户添加货币 addCurrency:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param currencyType
	 * @param number
	 * @param price
	 */
	public void addCurrency(int currencyType, int number, int price) {
		switch (currencyType) {
			case 0: // 金币
				getRole().setMoney(getRole().getMoney() + number * price);
				break;
			case 1: // 充值元宝
				getRole().setRechargeGold(getRole().getRechargeGold() + number * price);
				break;
			case 2: // 绑定元宝
				getRole().setBindGold(getRole().getBindGold() + number * price);
				break;
			default:
				break;
		}
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
	 * 改变经验值
	 * 
	 * 增加就传入正值 减少就传入负值 changceExp:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param number
	 */
	public void changceExp(int number) {
		getRole().setCurExp(getRole().getCurExp() + number);
	}
	
	public long getClientSessionId() {
		return clientSessionId;
	}
	
	public void setClientSessionId(long clientSessionId) {
		this.clientSessionId = clientSessionId;
		// System.err.println(this + "玩家的session：：" + clientSessionId);
	}
	
	public int getGateServerTypeId() {
		return gateServerTypeId;
	}
	
	public void setGateServerTypeId(int gateServerTypeId) {
		this.gateServerTypeId = gateServerTypeId;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public CareerPojo getCareerPojo() {
		return careerPojo;
	}
	
	public void setCareerPojo(CareerPojo careerPojo) {
		this.careerPojo = careerPojo;
	}
	
	public Bag getBag() {
		return bag;
	}
	
	public void setBag(Bag bag) {
		this.bag = bag;
	}
	
	public Attribute getAttribute() {
		return attribute;
	}
	
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	public int getUsePoint() {
		return usePoint;
	}
	
	public void setUsePoint(int usePoint) {
		this.usePoint = usePoint;
	}
	
	public int[] getRecordBasePoint() {
		return recordBasePoint;
	}
	
	public void setRecordBasePoint(int[] recordBasePoint) {
		this.recordBasePoint = recordBasePoint;
	}
	
	public RoleInitPojo getRoleInitPojo() {
		return roleInitPojo;
	}
	
	public void setRoleInitPojo(RoleInitPojo roleInitPojo) {
		this.roleInitPojo = roleInitPojo;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	/**
	 * addMoney:(). <br/>
	 * TODO().<br/>
	 * 添加游戏币(铜币)
	 * 
	 * @author lyh
	 * @param money
	 */
	public int addMoney(int money) {
		role.setMoney(role.getMoney() + money);
		if (role.getMoney() < 0) {
			role.setMoney(0);
		}
		return role.getMoney();
	}
	
	/**
	 * addBindGold:(). <br/>
	 * TODO().<br/>
	 * 添加绑定金币(银元宝)
	 * 
	 * @author lyh
	 * @param bindGold
	 */
	public int addBindGold(int bindGold) {
		role.setBindGold(role.getBindGold() + bindGold);
		if (role.getBindGold() < 0) {
			role.setBindGold(0);
		}
		return role.getBindGold();
	}
	
	/**
	 * addRechargeGold:(). <br/>
	 * TODO().<br/>
	 * 添加充值的金币(金元宝)
	 * 
	 * @author lyh
	 * @param gold
	 */
	public int addRechargeGold(int gold) {
		role.setRechargeGold(role.getRechargeGold() + gold);
		if (role.getRechargeGold() < 0) {
			role.setRechargeGold(0);
		}
		return role.getRechargeGold();
	}
	
	/**
	 * addHp:(). <br/>
	 * TODO().<br/>
	 * 加血
	 * 
	 * @author lyh
	 * @param hp
	 * @return
	 */
	// public int addHp(int hp) {
	// role.setHp(role.getHp() + hp);
	// if (role.getHp() < 0) {
	// role.setHp(0);
	// }
	// return role.getHp();
	// }
	
	/**
	 * addMp:(). <br/>
	 * TODO().<br/>
	 * 加魔
	 * 
	 * @author lyh
	 * @param mp
	 * @return
	 */
	// public int addMp(int mp) {
	// role.setMp(role.getMp() + mp);
	// if (role.getMp() < 0) {
	// role.setMp(0);
	// }
	// return role.getMp();
	// }
	
	/**
	 * addExp:(). <br/>
	 * TODO().<br/>
	 * 添加经验值
	 * 
	 * @author lyh
	 * @param exp
	 */
	public int addExp(int exp) {
		role.setCurExp(role.getCurExp() + exp);
		if (role.getCurExp() < 0) {
			role.setCurExp(0);
		}
		return role.getCurExp();
	}
	
	public Integer getSynMoney() {
		return synMoney;
	}
	
	public void setSynMoney(Integer synMoney) {
		this.synMoney = synMoney;
	}
	
	public Integer getSynrRechargeGold() {
		return synrRechargeGold;
	}
	
	public void setSynrRechargeGold(Integer synrRechargeGold) {
		this.synrRechargeGold = synrRechargeGold;
	}
	
	public Integer getSynBindGold() {
		return synBindGold;
	}
	
	public void setSynBindGold(Integer synBindGold) {
		this.synBindGold = synBindGold;
	}
	
	public Integer getSynExp() {
		return synExp;
	}
	
	public void setSynExp(Integer synExp) {
		this.synExp = synExp;
	}
	
	public long getFuBenId() {
		return role.getFuBenId();
	}
	
	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return role.getRoleLevel();
	}
	
	@Override
	public boolean isEnable() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void setEnable(boolean bUse) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setDir(float dir) {
		// TODO Auto-generated method stub
		super.setDir(dir);
		role.setDir(dir);
	}
	
	public TaskModular getTaskModular() {
		return taskModular;
	}
	
	public void setTaskModular(TaskModular taskModular) {
		this.taskModular = taskModular;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public void setY(float y) {
		// TODO Auto-generated method stub
		super.setY(y);
		this.role.setY(y);
	}
	
	@Override
	public void setX(float x) {
		// TODO Auto-generated method stub
		super.setX(x);
		this.role.setX(x);
	}
	
	@Override
	public void setZ(float z) {
		// TODO Auto-generated method stub
		super.setZ(z);
		this.role.setZ(z);
	}
	
	public long getLastUseBattleTime() {
		return lastUseBattleTime;
	}
	
	public void setLastUseBattleTime(long lastUseBattleTime) {
		this.lastUseBattleTime = lastUseBattleTime;
	}
	
	public long getLastBattleId() {
		return lastBattleId;
	}
	
	public void setLastBattleId(long lastBattleId) {
		this.lastBattleId = lastBattleId;
	}
	
	@Override
	public int getHp() {
		// TODO Auto-generated method stub
		return role.getHp();
	}
	
	@Override
	public int getMaxHp() {
		// TODO Auto-generated method stub
		return attribute.getAttribute(Attribute.MAX_HP);
	}
	
	@Override
	public void setMaxHp(int hp) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setHp(int hp) {
		// TODO Auto-generated method stub
		role.setHp(hp);
	}
	
	@Override
	public int getMp() {
		// TODO Auto-generated method stub
		return role.getMp();
	}
	
	@Override
	public int getMaxMp() {
		// TODO Auto-generated method stub
		return attribute.getAttribute(Attribute.MAX_MP);
	}
	
	@Override
	public void setMp(int mp) {
		// TODO Auto-generated method stub
		role.setMp(mp);
	}
	
	@Override
	public void setMaxMp(int mp) {
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
	public void addHp(int vHp) {
		// TODO Auto-generated method stub
		role.setHp(role.getHp() + vHp);
		if (role.getHp() < 0) {
			role.setHp(0);
		}
		// 同时也要更新属性值的hp
	}
	
	@Override
	public void addMp(int vHp) {
		// TODO Auto-generated method stub
		role.setMp(role.getMp() + vHp);
		if (role.getMp() < 0) {
			role.setMp(0);
		}
		// 同时也要更新属性值的mp
	}
	
	/**
	 * addAnger:(). <br/>
	 * TODO().<br/>
	 * 添加怒气
	 * 
	 * @author lyh
	 * @param anger
	 */
	public void addAnger(int anger) {
		role.setAnger(role.getAnger() + anger);
		if (role.getAnger() < 0) {
			role.setAnger(0);
		}
	}
	
	@Override
	public ISkill getSkillById(int skillId) {
		// TODO Auto-generated method stub
		return skillList.get(skillId);
	}
	
	@Override
	public Map<Integer, ISkill> getSkillMap() {
		// TODO Auto-generated method stub
		return skillList;
	}
	
	@Override
	public ISkill getUseSkill() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setIFightListener(IFightListener listener) {
		// TODO Auto-generated method stub
		fightLister = listener;
	}
	
	public ConcurrentLinkedQueue<BattleHurtRequest> getFightAttackQueue() {
		return fightAttackQueue;
	}

	
	/**
	 * 获取已通关副本列表 getPassFuBenIds:(). <br/>
	 */
	public ArrayList<Integer> getPassFuBenIds() {
		ArrayList<Integer> passed = new ArrayList<Integer>();
		String passStr = this.getRole().getPassedFuBenUids();
		if (passStr == null)
			return passed;
		String[] picePassStr = passStr.split(";");
		for (String pice : picePassStr) {
			passed.add(Integer.parseInt(pice));
		}
		return passed;
	}
	
//	public long getWaitAttackTime() {
//		return waitAttackTime;
//	}
//	
//	public void setWaitAttackTime(long waitAttackTime) {
//		this.waitAttackTime = waitAttackTime;
//	}
	
	@Override
	public void die(boolean bDie) {
		// TODO Auto-generated method stub
		this.bDie = bDie;
	}
	
	/**
	 * 获得计数 getCount:(). <br/>
	 */
	public int getCount(int countId) {
		JSONObject json = JSON.parseObject(this.getRole().getCounterStr());
		Object o = json.get(countId);
		Integer count = Integer.parseInt(o.toString());
		return count;
	}
	
	/**
	 * 扣除体力 costVits:(). <br/>
	 */
	public void costVits(int costVits) {
		int curVits = this.getRole().getVits();
		if (costVits > curVits)
			return;
		this.getRole().setVits(curVits - costVits);
	}
	
	/**
	 * 增加计数器的次数 addCounter:(). <br/>
	 */
	public void addCounter(int counterId, int count) {
		JSONObject json = JSON.parseObject(this.getRole().getCounterStr());
		int curCount = this.getCount(counterId);
		json.remove(counterId);
		curCount += count;
		json.put(counterId + "", curCount);
		this.getRole().setCounterStr(json.toJSONString());
	}

	@Override
    public IFightListener getIFightListener() {
	    // TODO Auto-generated method stub
	    return fightLister;
    }

    @Override
    public Map<Integer, Long> getColdTimeMap() {
	    // TODO Auto-generated method stub
	    return coldTimeMap;
    }

	public long getAutoReliveTime() {
    	return autoReliveTime;
    }

	public void setAutoReliveTime(long autoReliveTime) {
    	this.autoReliveTime = autoReliveTime;
    }
    /**
     * 获取小队模块
     * getTeamModular:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @return
     */
	public TeamModular getTeamModular() {
    	return teamModular;
    }

	/**
	 * 设置小队模块
	 * setTeamModular:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param teamModular
	 */
	public void setTeamModular(TeamModular teamModular) {
    	this.teamModular = teamModular;
    }

	
	
	
}
