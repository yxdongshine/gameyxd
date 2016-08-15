package com.engine.entityobj;

import java.util.Map;

import com.engine.entityobj.space.IMapObject;
import com.engine.interfaces.ISkill;

/**
 * ClassName:IFighter <br/>
 * Function: TODO (战斗对象接口). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-23 上午11:24:31 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IFighter extends IMapObject {
	
	public static final int COLD_TYPE_NONE = 0;
	 /**技能冷却**/  
	public static final int SKILL_COLD_TYPE = 1;
	 /**技能动画播放类型**/  
	public static final int SKILL_ANI_TYPE = 2;
	
	/**
	 * getHp:(). <br/>
	 * TODO().<br/>
	 * 获得血
	 * 
	 * @author lyh
	 * @return
	 */
	public int getHp();
	
	/**
	 * maxHp:(). <br/>
	 * TODO().<br/>
	 * 最大血
	 * 
	 * @author lyh
	 * @return
	 */
	public int getMaxHp();
	
	/**
	 * setMaxHp:(). <br/>
	 * TODO().<br/>
	 * 最大血量
	 * 
	 * @author lyh
	 * @return
	 */
	public void setMaxHp(int hp);
	
	/**
	 * setHp:(). <br/>
	 * TODO().<br/>
	 * 设置血
	 * 
	 * @author lyh
	 * @param hp
	 */
	public void setHp(int hp);
	
	/**
	 * addHp:(). <br/>
	 * TODO().<br/>
	 * 加血
	 * 
	 * @author lyh
	 * @param vHp
	 */
	public void addHp(int vHp);
	
	/**
	 * getMp:(). <br/>
	 * TODO().<br/>
	 * 获得魔力
	 * 
	 * @author lyh
	 * @return
	 */
	public int getMp();
	
	/**
	 * getMp:(). <br/>
	 * TODO().<br/>
	 * 最大魔力
	 * 
	 * @author lyh
	 * @return
	 */
	public int getMaxMp();
	
	/**
	 * setMp:(). <br/>
	 * TODO().<br/>
	 * 设置魔力
	 * 
	 * @author lyh
	 * @param mp
	 */
	public void setMp(int mp);
	
	public void setMaxMp(int mp);
	
	/**
	 * addMp:(). <br/>
	 * TODO().<br/>
	 * 增加魔力
	 * 
	 * @author lyh
	 * @param vHp
	 */
	public void addMp(int vHp);
	
	/**
	 * isDie:(). <br/>
	 * TODO().<br/>
	 * 判断 是否已经死了
	 * 
	 * @author lyh
	 * @return
	 */
	public boolean isDie();
	
	/**
	 * getTargetFighter:(). <br/>
	 * TODO().<br/>
	 * 获得攻击目标
	 * 
	 * @author lyh
	 * @return
	 */
	public IFighter getTargetFighter();
	
	/**
	 * setTargetFighter:(). <br/>
	 * TODO().<br/>
	 * 攻击目标
	 * 
	 * @author lyh
	 * @param fighter
	 */
	public void setTargetFighter(IFighter fighter);
	
	/**
	 * getSkillById:(). <br/>
	 * TODO().<br/>
	 * 用技能id找到技能
	 * 
	 * @author lyh
	 * @param skillConfigId
	 * @return
	 */
	public ISkill getSkillById(int skillId);
	
	/**
	 * getSkillMap:(). <br/>
	 * TODO().<br/>
	 * 技能容器
	 * 
	 * @author lyh
	 * @return
	 */
	public Map<Integer, ISkill> getSkillMap();
	
	/**
	 * getUseSkill:(). <br/>
	 * TODO().<br/>
	 * 得到当前使用的技能
	 * 
	 * @author lyh
	 * @return
	 */
	public ISkill getUseSkill();
	
	/**
	 * die:(). <br/>
	 * TODO().<br/>
	 * 设置死亡
	 * 
	 * @author lyh
	 * @param bDie
	 */
	public void die(boolean bDie);
	
	
	/** 
	 * getColdTimeMap:(). <br/> 
	 * TODO().<br/> 
	 * 《冷却类型,冷却结束时间》
	 * @author lyh 
	 * @return 
	 */  
	public Map<Integer,Long> getColdTimeMap();
}
