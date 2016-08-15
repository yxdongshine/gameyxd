
package com.lx.game.team;  

/** 
 * ClassName:TeamStaticConifg <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午4:46:33 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class TeamStaticConifg {
	
	public  static int num=0;
	
	 /**
     * 分配方式 自由拾取
     */
    public static final byte DISTRIBUTE_MODE_FREE = 1;
    /**
     * 分配方式 职业拾取
     */
    public static final byte DISTRIBUTE_MODE_CAREER = 2;
    
    /**
     * 常规组队
     */
    public static final byte TEAM_TYPE_COMMON=0;
    
    /**
     * 快速组队
     */
    public static final byte TEAM_TYPE_FAST=1;
    
    /**
     * 邀请间隔时间 秒
     */
    public static int TEAM_INVITE_TIME;
    /**
     * 队伍最小人数
     */
    public static int TEAM_INVITE_MIN_NUMBER;
    /**
     * 队伍最大人数
     */
    public static int TEAM_INVITE_MAX_NUMBER;
 
    /**
     * 队伍申请限制最大数
     */
    public static int TEAM_LIMIT_MAX_NUMBER;
    /**
     * 队长图标
     */
    public static int TEAM_LEADER_PIC;
    
    /**
     * 队长顺序
     */
    public static final int TEAM_LEADER_ORDER=1;
    
    /**
     * 顺序
     */
    public static final int TEAM_LEADER_ORDER_2=2;
    
    public static final int TEAM_LEADER_ORDER_3=3;

    public static final int TEAM_LEADER_ORDER_4=4;
    
    /**
     * 同意加入
     */
    public static final int TEAM_INVITE_AGREE=1;
    
    /**
     * 不同意加入
     */
    public static final int TEAM_INVITE_AGREE_NOT=0;
    
    /**
     * 投票有效时间数
     * 单位秒
     */
    public static  int TEAM_VOTE_TIME=0;

    
    
}
  