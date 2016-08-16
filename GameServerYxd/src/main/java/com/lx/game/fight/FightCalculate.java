package com.lx.game.fight;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.engine.entityobj.IFighter;
import com.engine.entityobj.Position3D;
import com.engine.interfaces.ISkill;
import com.engine.utils.GameUtils;
import com.engine.utils.log.LogUtils;
import com.lib.utils.ServerRandomUtils;
import com.loncent.protocol.game.battle.Battle.BattleHurtUpdateMsg;
import com.loncent.protocol.game.battle.Battle.PbHitType;
import com.lx.nserver.txt.SkillUnitPojo;

/**
 * ClassName:FightCalculate <br/>
 * Function: TODO (占斗技能). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-25 下午3:16:08 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class FightCalculate {
	private static Log log = LogUtils.getLog(FightCalculate.class);
	/**
	 * hurt:(). <br/>
	 * TODO().<br/>
	 * 计算最基本的伤害
	 * 
	 * @author lyh
	 * @param attacker
	 * @param suPojo
	 * @return
	 */
	public int hurt(IFighter attacker, SkillUnitPojo suPojo, IFighter defence, BattleHurtUpdateMsg.Builder hurtMsg) {
		int hurt = ServerRandomUtils.randomNum(suPojo.getHurtMin(), suPojo.getHurtMax());
		// 伤害计算
		this.getHurtVal(attacker, defence, suPojo, hurt);
		int hitRate = suPojo.getHitRate();
		if (suPojo.getHitRate() <= 0) {
			hitRate = 20;
		}
		int r = ServerRandomUtils.nextInt(100);
		hurtMsg.setHitType(hitRate >= r ? PbHitType.HIT : PbHitType.MISS);
		if (hurtMsg.getHitType() == PbHitType.MISS) {
			return 0;
		}
		// 暴击计算
	
		return hurt == 0 ? 1 : hurt;
	}
	
	/**
	 * getHurtVal:(). <br/>
	 * TODO().<br/>
	 * 计算伤害 0：无；1：物理；2、法术；3、火；4、冰；5、毒；6、电；7：治疗
	 * 
	 * @author lyh
	 * @param attack
	 * @param defence
	 * @param suPojo
	 * @return
	 */
	public int getHurtVal(IFighter attack, IFighter defence, SkillUnitPojo suPojo, int _hurt) {
		int hurt = 0;
		if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_EXTERNAL_FORCE_ATTACK) {// 外攻
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_INNER_FORCE_ATTACK) {// 内攻
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_LUNAR_ATTACK) {// 太阴攻击
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_NETHER_ATTACK) {// 幽冥攻击
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_PLOUGH_ATTACK) {// 天罡攻击
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_SCORCHING_SUN_ATTACK) {// 烈阳攻击
			hurt = 10;
		} else if (suPojo.getDamageType() == ISkill.DAMAGE_TYPE_ADD_HP) {// 加血
			// 如果类型为7，最后一位表示类型，1表示直接加值，2表示加百分比
			hurt = 10;
		} else {
			// 其他
		}
		
		return hurt;
		
	}
	
	/** 
	 * calHitBack:(). <br/> 
	 * TODO().<br/> 
	 * 计算击退和击飞距离
	 * @author lyh 
	 * @param attacker
	 * @param suPojo
	 * @param defence
	 * @param hurtMsg 
	 */  
	public Position3D calHitBack(IFighter attacker, int dis, IFighter defence){
		//先算出攻击者与被攻击者的角度
		if (dis > 0){
			Position3D pos3d =  GameUtils.findPoint(attacker.getPosition3D(), defence.getPosition3D(), dis, attacker.getSpace().getSpaceInfo());
			return pos3d;
		}else {
			return null;
		}
	}
	
	/** 
	 * calHitMove:(). <br/> 
	 * TODO().<br/> 
	 * 计算攻击者位移
	 * @author lyh 
	 * @param attacker
	 * @param dis
	 * @param defence
	 * @return 
	 */  
	public Position3D calHitMove(IFighter attacker, int dis, IFighter defence){
		if (dis > 0){
			Position3D pos3d =  GameUtils.findPoint1(attacker.getPosition3D(), defence.getPosition3D(), dis, attacker.getSpace().getSpaceInfo());
			return pos3d;
		}else {
			return null;
		}
	}
}
