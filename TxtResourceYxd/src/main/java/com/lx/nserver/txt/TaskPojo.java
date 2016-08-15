package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*任务配置表
**/
public class TaskPojo{

	public TaskPojo(){
	}

	/**任务编号**/
	@TxtKey(name = "id")
	@TxtInt(name = "id")
	private int id;

	/**任务名称**/
	@TxtString(name = "name")
	private String name;

	/**接任务文本**/
	@TxtString(name = "acceptContent")
	private String acceptContent;

	/**交任务文本**/
	@TxtString(name = "handContent")
	private String handContent;

	/**任务追踪文本1**/
	@TxtString(name = "trackContent1")
	private String trackContent1;

	/**任务追踪文本2**/
	@TxtString(name = "trackContent2")
	private String trackContent2;

	/**任务类型**/
	@TxtString(name = "taskType")
	private String taskType;

	/**后续任务**/
	@TxtInt(name = "afterTask")
	private int afterTask;

	/**发布NPC**/
	@TxtInt(name = "publishTask")
	private int publishTask;

	/**完成NPC**/
	@TxtInt(name = "finishNPC")
	private int finishNPC;

	/**获得道具**/
	@TxtString(name = "getProps")
	private String getProps;

	/**道具数量**/
	@TxtString(name = "getPropsNum")
	private String getPropsNum;

	/**目标关卡**/
	@TxtString(name = "targetPass")
	private String targetPass;

	/**关卡难度**/
	@TxtInt(name = "")
	private int passDegree;

	/**目标怪物**/
	@TxtInt(name = "")
	private int targetMonster;

	/**目标数量**/
	@TxtInt(name = "")
	private int targetMonsterCount;

	/**目标道具**/
	@TxtInt(name = "")
	private int targetProps;

	/**收集数量**/
	@TxtInt(name = "")
	private int targetPropsCount;

	/**奖励经验**/
	@TxtInt(name = "")
	private int rewardEXPrewardCurr;

	/**奖励金钱**/
	@TxtInt(name = "")
	private int rewardCurrency;

	/**货币类型**/
	@TxtInt(name = "")
	private int currencyType;

	/**奖励道具**/
	@TxtString(name = "")
	private String rewardProps;

	/**奖励道具数量**/
	@TxtString(name = "")
	private String rewardPropsCount;

	/**任务暴露等级**/
	@TxtInt(name = "")
	private int taskDispalyLevle;

	/**任务接受等级**/
	@TxtInt(name = "")
	private int taskAcpteetLevle;

	/**开启时间**/
	@TxtString(name = "")
	private String startTime;

	/**持续时间**/
	@TxtInt(name = "")
	private int continueTime;

	/**限定职业**/
	@TxtInt(name = "")
	private int limitCareer;

	/**可否放弃**/
	@TxtInt(name = "")
	private int canGiveUp;

	/**每天可否重复**/
	@TxtInt(name = "")
	private int canRepeat;

	/**重置时间**/
	@TxtString(name = "")
	private String clearTime;

	/**更新周期**/
	@TxtInt(name = "")
	private int updateCycle;



	public int getId(){
		return id;
	}

	public void setId(int _id){
		id= _id;
	}

	public String getName(){
		return name;
	}

	public void setName(String _name){
		name= _name;
	}

	public String getAcceptContent(){
		return acceptContent;
	}

	public void setAcceptContent(String _acceptContent){
		acceptContent= _acceptContent;
	}

	public String getHandContent(){
		return handContent;
	}

	public void setHandContent(String _handContent){
		handContent= _handContent;
	}

	public String getTrackContent1(){
		return trackContent1;
	}

	public void setTrackContent1(String _trackContent1){
		trackContent1= _trackContent1;
	}

	public String getTrackContent2(){
		return trackContent2;
	}

	public void setTrackContent2(String _trackContent2){
		trackContent2= _trackContent2;
	}

	public String getTaskType(){
		return taskType;
	}

	public void setTaskType(String _taskType){
		taskType= _taskType;
	}

	public int getAfterTask(){
		return afterTask;
	}

	public void setAfterTask(int _afterTask){
		afterTask= _afterTask;
	}

	public int getPublishTask(){
		return publishTask;
	}

	public void setPublishTask(int _publishTask){
		publishTask= _publishTask;
	}

	public int getFinishNPC(){
		return finishNPC;
	}

	public void setFinishNPC(int _finishNPC){
		finishNPC= _finishNPC;
	}

	public String getGetProps(){
		return getProps;
	}

	public void setGetProps(String _getProps){
		getProps= _getProps;
	}

	public String getGetPropsNum(){
		return getPropsNum;
	}

	public void setGetPropsNum(String _getPropsNum){
		getPropsNum= _getPropsNum;
	}

	public String getTargetPass(){
		return targetPass;
	}

	public void setTargetPass(String _targetPass){
		targetPass= _targetPass;
	}

	public int getPassDegree(){
		return passDegree;
	}

	public void setPassDegree(int _passDegree){
		passDegree= _passDegree;
	}

	public int getTargetMonster(){
		return targetMonster;
	}

	public void setTargetMonster(int _targetMonster){
		targetMonster= _targetMonster;
	}

	public int getTargetMonsterCount(){
		return targetMonsterCount;
	}

	public void setTargetMonsterCount(int _targetMonsterCount){
		targetMonsterCount= _targetMonsterCount;
	}

	public int getTargetProps(){
		return targetProps;
	}

	public void setTargetProps(int _targetProps){
		targetProps= _targetProps;
	}

	public int getTargetPropsCount(){
		return targetPropsCount;
	}

	public void setTargetPropsCount(int _targetPropsCount){
		targetPropsCount= _targetPropsCount;
	}

	public int getRewardEXPrewardCurr(){
		return rewardEXPrewardCurr;
	}

	public void setRewardEXPrewardCurr(int _rewardEXPrewardCurr){
		rewardEXPrewardCurr= _rewardEXPrewardCurr;
	}

	public int getRewardCurrency(){
		return rewardCurrency;
	}

	public void setRewardCurrency(int _rewardCurrency){
		rewardCurrency= _rewardCurrency;
	}

	public int getCurrencyType(){
		return currencyType;
	}

	public void setCurrencyType(int _currencyType){
		currencyType= _currencyType;
	}

	public String getRewardProps(){
		return rewardProps;
	}

	public void setRewardProps(String _rewardProps){
		rewardProps= _rewardProps;
	}

	public String getRewardPropsCount(){
		return rewardPropsCount;
	}

	public void setRewardPropsCount(String _rewardPropsCount){
		rewardPropsCount= _rewardPropsCount;
	}

	public int getTaskDispalyLevle(){
		return taskDispalyLevle;
	}

	public void setTaskDispalyLevle(int _taskDispalyLevle){
		taskDispalyLevle= _taskDispalyLevle;
	}

	public int getTaskAcpteetLevle(){
		return taskAcpteetLevle;
	}

	public void setTaskAcpteetLevle(int _taskAcpteetLevle){
		taskAcpteetLevle= _taskAcpteetLevle;
	}

	public String getStartTime(){
		return startTime;
	}

	public void setStartTime(String _startTime){
		startTime= _startTime;
	}

	public int getContinueTime(){
		return continueTime;
	}

	public void setContinueTime(int _continueTime){
		continueTime= _continueTime;
	}

	public int getLimitCareer(){
		return limitCareer;
	}

	public void setLimitCareer(int _limitCareer){
		limitCareer= _limitCareer;
	}

	public int getCanGiveUp(){
		return canGiveUp;
	}

	public void setCanGiveUp(int _canGiveUp){
		canGiveUp= _canGiveUp;
	}

	public int getCanRepeat(){
		return canRepeat;
	}

	public void setCanRepeat(int _canRepeat){
		canRepeat= _canRepeat;
	}

	public String getClearTime(){
		return clearTime;
	}

	public void setClearTime(String _clearTime){
		clearTime= _clearTime;
	}

	public int getUpdateCycle(){
		return updateCycle;
	}

	public void setUpdateCycle(int _updateCycle){
		updateCycle= _updateCycle;
	}



}