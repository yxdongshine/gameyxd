package com.lx.nserver.txt;

import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.read.tool.txt.*;

/**
 * 技能伤害单元
 **/
public class SkillUnitPojo {
	
	public SkillUnitPojo() {
	}
	
	/** 技能单元ID **/
	@TxtKey
	@TxtInt
	private int id;
	
	/** 类型 0：无；1：物理；2、法术；3、火；4、冰；5、毒；6、电；7：治疗 **/
	@TxtInt
	private int damageType;
	/** 命中率 **/
	@TxtInt
	private int hitRate;
	/** 伤害最小值 如果类型为7，最后一位表示类型，1表示直接加值，2表示加百分比 **/
	@TxtInt
	private int hurtMin;
	
	/** 伤害最大值 **/
	@TxtInt
	private int hurtMax;
	
	/** 攻击范围形状 1单体，2矩形，3扇形，4圆形 **/
	@TxtInt
	private int attkAoe;
	
	/** 仇恨值 **/
	@TxtInt
	private int hatredVal;
	
	/** 攻击距离 **/
	@TxtInt
	private int attackDistance;
	
	/** 是否为远程 **/
	@TxtInt
	private int isFarSkill;
	
	/** 伤害范围大小 **/
	@TxtString
	private String hurtFun;
	
	private int[] hurtScope = new int[2];
	
	/** buff列表及几率 **/
	@TxtString
	private String buffList;
	
	/** 吟唱时间 **/
	@TxtInt
	private int singTime;
	
	/** 是否可打断吟唱 **/
	@TxtInt
	private int singIsBreak;
	
	/** 持续施法时间 **/
	@TxtInt
	private int releaseTime;
	
	/** 是否可打断施放 **/
	@TxtInt
	private int releaseIsBreak;
	
	/** 伤害次数 **/
	@TxtInt
	private int hurtCount;
	
	/** 增加的怒气 **/
	@TxtInt
	private int angerVal;

	/**伤害成长值**/
	@TxtInt
	private int groupUpAttack;

	/**投掷物**/
	@TxtString
	private String  throwItemName;

	/**投掷物是否追踪**/
	@TxtInt
	private int isFollow;

	/**追踪半径**/
	@TxtInt
	private int followRadius;

	/**投掷物飞行速度**/
	@TxtInt
	private int throwSpeed;
	/**施法位移**/
	@TxtInt
	private int attakerMove;

	/**受击移位**/
	@TxtInt
	private int hurtMove;

	public int getId() {
		return id;
	}
	
	public void setId(int _id) {
		id = _id;
	}
	
	public int getDamageType() {
		return damageType;
	}
	
	public void setDamageType(int _damageType) {
		damageType = _damageType;
	}
	
	public int getHitRate() {
		return hitRate;
	}
	
	public void setHitRate(int _hitRate) {
		hitRate = _hitRate;
	}
	
	public int getHurtMin() {
		return hurtMin;
	}
	
	public void setHurtMin(int _hurtMin) {
		hurtMin = _hurtMin;
	}
	
	public int getHurtMax() {
		return hurtMax;
	}
	
	public void setHurtMax(int _hurtMax) {
		hurtMax = _hurtMax;
	}
	
	public int getAttkAoe() {
		return attkAoe;
	}
	
	public void setAttkAoe(int _attkAoe) {
		attkAoe = _attkAoe;
	}
	
	public int getHatredVal() {
		return hatredVal;
	}
	
	public void setHatredVal(int _hatredVal) {
		hatredVal = _hatredVal;
	}
	
	public int getAttackDistance() {
		return attackDistance;
	}
	
	public void setAttackDistance(int _attackDistance) {
		attackDistance = _attackDistance;
	}
	
	public int getIsFarSkill() {
		return isFarSkill;
	}
	
	public void setIsFarSkill(int _isFarSkill) {
		isFarSkill = _isFarSkill;
	}
	
	public String getHurtFun() {
		return hurtFun;
	}
	
	public void setHurtFun(String _hurtFun) {
		hurtFun = _hurtFun;
		if (!ToolUtils.isStringNull(hurtFun)) {
			int[] scope = ToolUtils.splitStringToInt(hurtFun, IConst.WELL);
			for (int i = 0; i < scope.length; i++) {
				hurtScope[i] = scope[i];
			}
		}
	}
	
	public String getBuffList() {
		return buffList;
	}
	
	public void setBuffList(String _buffList) {
		buffList = _buffList;
	}
	
	public int getSingTime() {
		return singTime;
	}
	
	public void setSingTime(int _singTime) {
		singTime = _singTime;
	}
	
	public int getSingIsBreak() {
		return singIsBreak;
	}
	
	public void setSingIsBreak(int _singIsBreak) {
		singIsBreak = _singIsBreak;
	}
	
	public int getReleaseTime() {
		return releaseTime;
	}
	
	public void setReleaseTime(int _releaseTime) {
		releaseTime = _releaseTime;
	}
	
	public int getReleaseIsBreak() {
		return releaseIsBreak;
	}
	
	public void setReleaseIsBreak(int _releaseIsBreak) {
		releaseIsBreak = _releaseIsBreak;
	}
	
	public int getHurtCount() {
		return hurtCount;
	}
	
	public void setHurtCount(int _hurtCount) {
		hurtCount = _hurtCount;
	}
	
	public int getAngerVal() {
		return angerVal;
	}
	
	public void setAngerVal(int _angerVal) {
		angerVal = _angerVal;
	}
	public int getGroupUpAttack(){
		return groupUpAttack;
	}

	public void setGroupUpAttack(int _groupUpAttack){
		groupUpAttack= _groupUpAttack;
	}

	public String getThrowItemName(){
		return  throwItemName;
	}

	public void setThrowItemName(String _throwItemName){
		 throwItemName= _throwItemName;
	}

	public int getIsFollow(){
		return isFollow;
	}

	public void setIsFollow(int _isFollow){
		isFollow= _isFollow;
	}

	public int getFollowRadius(){
		return followRadius;
	}

	public void setFollowRadius(int _followRadius){
		followRadius= _followRadius;
	}

	public int getThrowSpeed(){
		return throwSpeed;
	}

	public void setThrowSpeed(int _throwSpeed){
		throwSpeed= _throwSpeed;
	}
	
	public int[] getHurtScope() {
		return hurtScope;
	}
	
	public void setHurtScope(int[] hurtScope) {
		this.hurtScope = hurtScope;
	}
	

	public int getAttakerMove(){
		return attakerMove;
	}

	public void setAttakerMove(int _attakerMove){
		attakerMove= _attakerMove;
	}

	public int getHurtMove(){
		return hurtMove;
	}

	public void setHurtMove(int _hurtMove){
		hurtMove= _hurtMove;
	}

}