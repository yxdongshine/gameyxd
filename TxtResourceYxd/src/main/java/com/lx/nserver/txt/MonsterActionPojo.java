package com.lx.nserver.txt;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.lib.utils.ToolUtils;
import com.lx.nserver.monster.action.AtkActions;
import com.lx.nserver.monster.action.HateAction;
import com.lx.nserver.monster.action.IdleAction;
import com.lx.nserver.monster.action.RageAction;
import com.lx.nserver.monster.action.WalkAction;
import com.read.tool.txt.TxtInt;
import com.read.tool.txt.TxtKey;
import com.read.tool.txt.TxtString;

/**
 * 怪物行为
 **/
public class MonsterActionPojo {
	
	public MonsterActionPojo() {
	}
	
	/** 怪物id **/
	@TxtKey
	@TxtInt
	private int monsterId;
	
	/** 待机动作 **/
	@TxtString
	private String idleAction;
	private IdleAction pIdleAction;
	
	/** 漫游动作 **/
	@TxtString
	private String walkAction;
	private WalkAction pWalkAction;
	
	/** 多个攻击行为(列表) **/
	@TxtString
	private String atkActions;
	private List<AtkActions> atkActionList = new ArrayList<AtkActions>();
	
	/** 仇恨行为 **/
	@TxtString
	private String hateAction;
	private HateAction pHateAction;
	/** 暴怒行为 **/
	@TxtString
	private String rageAction;
	private RageAction pRageAction;
	
	public int getMonsterId() {
		return monsterId;
	}
	
	public void setMonsterId(int _monsterId) {
		monsterId = _monsterId;
	}
	
	public String getIdleAction() {
		return idleAction;
	}
	
	public void setIdleAction(String _idleAction) {
		idleAction = _idleAction;
		if (!ToolUtils.isStringNull(idleAction)) {
			pIdleAction = JSON.parseObject(idleAction, IdleAction.class);
		}
	}
	
	public String getWalkAction() {
		return walkAction;
	}
	
	public void setWalkAction(String _walkAction) {
		walkAction = _walkAction;
		if (!ToolUtils.isStringNull(walkAction)) {
			pWalkAction = JSON.parseObject(walkAction, WalkAction.class);
		}
	}
	
	public String getAtkActions() {
		return atkActions;
	}
	
	public void setAtkActions(String _atkActions) {
		atkActions = _atkActions;
		if (!ToolUtils.isStringNull(atkActions)) {
			atkActionList = JSON.parseArray(atkActions, AtkActions.class);
		}
	}
	
	public String getHateAction() {
		return hateAction;
	}
	
	public void setHateAction(String _hateAction) {
		hateAction = _hateAction;
		if (!ToolUtils.isStringNull(hateAction)) {
			pHateAction = JSON.parseObject(hateAction, HateAction.class);
		}
	}
	
	public String getRageAction() {
		return rageAction;
	}
	
	public void setRageAction(String _rageAction) {
		rageAction = _rageAction;
		if (!ToolUtils.isStringNull(rageAction)) {
			pRageAction = JSON.parseObject(rageAction, RageAction.class);
		}
	}
	
	public IdleAction getpIdleAction() {
		return pIdleAction;
	}
	
	public void setpIdleAction(IdleAction pIdleAction) {
		this.pIdleAction = pIdleAction;
	}
	
	public WalkAction getpWalkAction() {
		return pWalkAction;
	}
	
	public void setpWalkAction(WalkAction pWalkAction) {
		this.pWalkAction = pWalkAction;
	}
	
	public List<AtkActions> getAtkActionList() {
		return atkActionList;
	}
	
	public void setAtkActionList(List<AtkActions> atkActionList) {
		this.atkActionList = atkActionList;
	}
	
	public HateAction getpHateAction() {
		return pHateAction;
	}
	
	public void setpHateAction(HateAction pHateAction) {
		this.pHateAction = pHateAction;
	}
	
	public RageAction getpRageAction() {
		return pRageAction;
	}
	
	public void setpRageAction(RageAction pRageAction) {
		this.pRageAction = pRageAction;
	}
	
}