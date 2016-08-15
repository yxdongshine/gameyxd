package com.lx.nserver.txt;
import com.read.tool.txt.*;
/**
*组队配置表
**/
public class TeamConfigPojo{

	public TeamConfigPojo(){
	}

	/**队伍配置编号**/
	@TxtKey(name = "teamNo")
	@TxtInt(name = "teamNo")
	private int teamNo;

	/**邀请cd时间**/
	@TxtInt(name = "teamInviteTime")
	private int teamInviteTime;

	/**队伍最小成员数**/
	@TxtInt(name = "minNumber")
	private int minNumber;

	/**队伍最大成员数**/
	@TxtInt(name = "maxNumber")
	private int maxNumber;

	/**最大申请人数量**/
	@TxtInt(name = "maxInviteNumber")
	private int maxInviteNumber;

	/**队长图标**/
	@TxtInt(name = "laederPic")
	private int laederPic;

	/**投票有效时间数**/
	@TxtInt(name = "voteTime")
	private int voteTime;



	public int getTeamNo(){
		return teamNo;
	}

	public void setTeamNo(int _teamNo){
		teamNo= _teamNo;
	}

	public int getTeamInviteTime(){
		return teamInviteTime;
	}

	public void setTeamInviteTime(int _teamInviteTime){
		teamInviteTime= _teamInviteTime;
	}

	public int getMinNumber(){
		return minNumber;
	}

	public void setMinNumber(int _minNumber){
		minNumber= _minNumber;
	}

	public int getMaxNumber(){
		return maxNumber;
	}

	public void setMaxNumber(int _maxNumber){
		maxNumber= _maxNumber;
	}

	public int getMaxInviteNumber(){
		return maxInviteNumber;
	}

	public void setMaxInviteNumber(int _maxInviteNumber){
		maxInviteNumber= _maxInviteNumber;
	}

	public int getLaederPic(){
		return laederPic;
	}

	public void setLaederPic(int _laederPic){
		laederPic= _laederPic;
	}

	public int getVoteTime(){
		return voteTime;
	}

	public void setVoteTime(int _voteTime){
		voteTime= _voteTime;
	}



}