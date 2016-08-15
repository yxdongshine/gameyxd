package com.engine.interfaces;

import java.util.List;

import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.entityobj.space.IMapObject;

/**
 * ClassName:IFightListener <br/>
 * Function: TODO (战斗监听类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-8 下午6:02:32 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface IFightListener {
	
	/**
	 * fight:(). <br/>
	 * TODO().<br/>
	 * 战斗
	 * 
	 * @author lyh
	 * @param src
	 * @param target
	 * @param skill
	 * @return 战斗结果
	 */
	public int monsterFight(IFighter attacker, IFighter defencer, ISkill skill);
	
	/**
	 * playerFight:(). <br/>
	 * TODO().<br/>
	 * 角色战斗
	 * 
	 * @author lyh
	 * @param src
	 * @param skillId
	 * @param unitId
	 * @return
	 */
	public int playerFight(IFighter src, int skillId, int unitId, List<IFighter> targetFighters);
}
