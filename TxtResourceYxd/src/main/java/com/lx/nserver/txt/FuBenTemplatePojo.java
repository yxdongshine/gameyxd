package com.lx.nserver.txt;

import com.lib.utils.IConst;
import com.lib.utils.ToolUtils;
import com.read.tool.txt.*;
/**
*副本配置表
**/
public class FuBenTemplatePojo{

	public FuBenTemplatePojo(){
	}

	/**地图唯一的ID**/
	@TxtKey
	@TxtInt
	private int mapUid;

	/**场景ID**/
	@TxtInt
	private int scoTid;

	/**地图名**/
	@TxtString
	private String name;

	/**U3D名称**/
	@TxtString
	private String unity;

	/**地图图标**/
	@TxtString
	private String icon;

	/**副本类型**/
	@TxtInt
	private int type;

	/**地图难度等级**/
	@TxtInt
	private int level;

	/**进入地图最低等级**/
	@TxtInt
	private int enterLevel;

	/**副本时间**/
	@TxtInt
	private int totalTime;

	/**消耗体力**/
	@TxtInt
	private int costManual;

	/**付费扫荡金币**/
	@TxtInt
	private int costGold;

	/**进入次数限制**/
	@TxtInt
	private int enterLimit;

	/**死亡复活**/
	@TxtInt
	private int canRevive;

	/**免费复活次数限制**/
	@TxtInt
	private int freeReviveLimit;

	/**原地复活无敌时间**/
	@TxtInt
	private int invincibleTime;

	/**副本可否扫荡**/
	@TxtInt
	private int canSweep;

	/**获得道具**/
	@TxtString
	private String itemReward;
	
	/** 获得道具列表 **/
	private int[][] itemRewards = new int[0][];
	
	/** 获得经验 **/
	@TxtInt
	private int expReward;

	/**获得金币**/
	@TxtInt
	private int goldReward;

	/**获得银币**/
	@TxtInt
	private int sliverReward;

	/**获得铜币**/
	@TxtInt
	private int copperReward;

	/**评分**/
	@TxtString
	private String star;
	/** 评分列表 **/
	private int[][] stars = new int[0][];
	
	/** 最少玩家 **/
	@TxtInt
	private int minPlayer;

	/**最多玩家**/
	@TxtInt
	private int maxPlayer;

	/**开宝箱花费**/
	@TxtString
	private String openBoxCost;
	/** 开宝箱花费 **/
	private int[][] openBoxCosts = new int[0][];
	
	public int getMapUid() {
		return mapUid;
	}

	public void setMapUid(int _mapUid){
		mapUid= _mapUid;
	}

	public int getScoTid(){
		return scoTid;
	}

	public void setScoTid(int _scoTid){
		scoTid= _scoTid;
	}

	public String getName(){
		return name;
	}

	public void setName(String _name){
		name= _name;
	}

	public String getUnity(){
		return unity;
	}

	public void setUnity(String _unity){
		unity= _unity;
	}

	public String getIcon(){
		return icon;
	}

	public void setIcon(String _icon){
		icon= _icon;
	}

	public int getType(){
		return type;
	}

	public void setType(int _type){
		type= _type;
	}

	public int getLevel(){
		return level;
	}

	public void setLevel(int _level){
		level= _level;
	}

	public int getEnterLevel(){
		return enterLevel;
	}

	public void setEnterLevel(int _enterLevel){
		enterLevel= _enterLevel;
	}

	public int getTotalTime(){
		return totalTime;
	}

	public void setTotalTime(int _totalTime){
		totalTime= _totalTime;
	}

	public int getCostManual(){
		return costManual;
	}

	public void setCostManual(int _costManual){
		costManual= _costManual;
	}

	public int getCostGold(){
		return costGold;
	}

	public void setCostGold(int _costGold){
		costGold= _costGold;
	}

	public int getEnterLimit(){
		return enterLimit;
	}

	public void setEnterLimit(int _enterLimit){
		enterLimit= _enterLimit;
	}

	public int getCanRevive(){
		return canRevive;
	}

	public void setCanRevive(int _canRevive){
		canRevive= _canRevive;
	}

	public int getFreeReviveLimit(){
		return freeReviveLimit;
	}

	public void setFreeReviveLimit(int _freeReviveLimit){
		freeReviveLimit= _freeReviveLimit;
	}

	public int getInvincibleTime(){
		return invincibleTime;
	}

	public void setInvincibleTime(int _invincibleTime){
		invincibleTime= _invincibleTime;
	}

	public int getCanSweep(){
		return canSweep;
	}

	public void setCanSweep(int _canSweep){
		canSweep= _canSweep;
	}

	public String getItemReward(){
		return itemReward;
	}
	
	public void setItemReward(String _itemReward) {
		itemReward = _itemReward;
		if (!ToolUtils.isStringNull(_itemReward)) {
			this.itemRewards = ToolUtils.splitStr(_itemReward, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getItemRewards() {
		return this.itemRewards;
	}
	
	public int getExpReward() {
		return expReward;
	}

	public void setExpReward(int _expReward){
		expReward= _expReward;
	}

	public int getGoldReward(){
		return goldReward;
	}

	public void setGoldReward(int _goldReward){
		goldReward= _goldReward;
	}

	public int getSliverReward(){
		return sliverReward;
	}

	public void setSliverReward(int _sliverReward){
		sliverReward= _sliverReward;
	}

	public int getCopperReward(){
		return copperReward;
	}

	public void setCopperReward(int _copperReward){
		copperReward= _copperReward;
	}

	public String getStar(){
		return star;
	}
	
	public void setStar(String _star) {
		star = _star;
		if (!ToolUtils.isStringNull(_star)) {
			this.stars = ToolUtils.splitStr(_star, IConst.WELL, IConst.STAR);
		}
	}
	
	public int[][] getStars() {
		return this.stars;
	}
	
	public int getMinPlayer() {
		return minPlayer;
	}

	public void setMinPlayer(int _minPlayer){
		minPlayer= _minPlayer;
	}

	public int getMaxPlayer(){
		return maxPlayer;
	}

	public void setMaxPlayer(int _maxPlayer){
		maxPlayer= _maxPlayer;
	}

	public String getOpenBoxCost(){
		return openBoxCost;
	}
	
	public int[][] getOpenBoxCosts() {
		return this.openBoxCosts;
	}
	
	public void setOpenBoxCost(String _openBoxCost) {
		openBoxCost = _openBoxCost;
		if (!ToolUtils.isStringNull(_openBoxCost)) {
			this.openBoxCosts = ToolUtils.splitStr(_openBoxCost, IConst.WELL, IConst.STAR);
		}
	}
	
}