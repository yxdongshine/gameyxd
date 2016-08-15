package com.lx.game.item.function;

import com.engine.entityobj.ServerPlayer;
import com.lx.game.item.util.Const;
import com.lx.nserver.txt.FunctionPojo;
import com.read.tool.txt.TxtInt;

/**
 * ClassName:RecoverFunction <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 下午3:16:11 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
public class RecoverFunction extends Function {
	
	/** 血值 **/
	@TxtInt
	private int valueh;
	
	/** 血值百分比 **/
	@TxtInt
	private int percenth;
	
	/** 魔值 **/
	@TxtInt
	private int valuem;
	
	/** 魔值百分比 **/
	@TxtInt
	private int percentm;
	
	@Override
	public boolean canActive(ServerPlayer me, ServerPlayer targets) {
		// TODO Auto-generated method stub
		if ((System.currentTimeMillis() - me.getLastUseBattleTime()) / 1000 > 3) {// 大于3秒钟
			me.setLastUseBattleTime(System.currentTimeMillis());
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean active(ServerPlayer me, ServerPlayer targets) {
		// TODO Auto-generated method stub
		// 计算血魔值
		me.addHp(me.getRole().getHp() * (1 + percenth / Const.DENOMINATOR) + valueh);
		// 计算魔
		me.addMp(me.getRole().getMp() * (1 + percentm / Const.DENOMINATOR) + valuem);
		return true;
	}
	
	@Override
	protected void parse(FunctionPojo function) {
		// TODO Auto-generated method stub
		this.valueh = function.getValueh();
		this.valuem = function.getValuem();
		this.percenth = function.getPercenth();
		this.percentm = function.getPercentm();
	}
}
