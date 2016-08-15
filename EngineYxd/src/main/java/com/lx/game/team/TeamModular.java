
package com.lx.game.team;  


/** 
 * ClassName:TeamModular <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午4:35:18 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class TeamModular {
	
	
	/**
	 * 
	 * Creates a new instance of TeamModular. 
	 *
	 */
	public TeamModular() {
	    // TODO Auto-generated constructor stub
		
		
    }
	
	
	/**
     * 当前队伍
     */
    private long teamId;
    
    /*
     * 上次邀请时间
     */
    private long inviteTime = 0L;
    
    
    /**
     * 获取当前小队的人员数
     * getSumTeamer:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @return
     */
    public int getSumTeamer(){
    	return getTeam().getObjects().size();
    }
    
    
    /**
     * 小队中下一个成员顺序号
     * nextTeamerOrder:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @return
     */
    public int nextTeamerOrder(){
    	int nextOrder=0;
    	nextOrder=this.getTeam().getObjects().size()+1;
    	if(nextOrder>TeamStaticConifg.TEAM_INVITE_MAX_NUMBER){
    		nextOrder=TeamStaticConifg.TEAM_INVITE_MAX_NUMBER;
    	}
    	return nextOrder;
    }
    
    
    /**
     * 是否看满员了 小队
     * isFullStarffed:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @return
     */
    public boolean isFullStarffed(){
    	 boolean isFull=false;
    	 if(getSumTeamer()>=TeamStaticConifg.TEAM_INVITE_MAX_NUMBER){//满员
    		 isFull=true;
    	 }
    	 return isFull;
    }
    
    
    /**
     * 
     * removeRequestRoleId:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param requestRoleId
     * @return
     */
    public boolean removeRequestRoleId(long requestRoleId){
    	boolean isRemove=false;
    	for (int i = 0; i < getTeam().getRequestList().size(); i++) {
	        if(getTeam().getRequestList().get(i)==requestRoleId){
	        	getTeam().getRequestList().remove(i);
	        	isRemove=true;
	        	break;
	        }
        }
    	return isRemove;
    }
    
    
    /**
     * 
     * hasRoleInTeam:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param requestRoleId
     * @return
     */
    public boolean hasRoleInTeam(long requestRoleId){
    	boolean hasRoleInTeam=false;
    	for (int i = 0; i < getTeam().getRequestList().size(); i++) {
	        if(getTeam().getRequestList().get(i)==requestRoleId){
	        	hasRoleInTeam=true;
	        	break;
	        }
        }
    	return hasRoleInTeam;
    }
    
   /**
    * 获取所在的team
    * getTeam:(). <br/> 
    * TODO().<br/> 
    * 
    * @author yxd 
    * @return
    */
	public Team getTeam(){
			return TeamManager.getTeam(getTeamId());
		}
    
    /**
     * 是否是队长
     * isTeamLeader:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param roleId
     * @return
     */
    public int isTeamLeader(long roleId){
    	int order=0;
    	if(getTeam()!=null){
    		for (int i = 0; i < getTeam().getObjects().size(); i++) {
    	        if(getTeam().getObjects().get(i).getRoleId()==roleId){
                   order=getTeam().getObjects().get(i).getOrder();
    	        }
            }
    	}
    	return order;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	public long getTeamId() {
    	return teamId;
    }


	public void setTeamId(long teamId) {
    	this.teamId = teamId;
    }


	public long getInviteTime() {
    	return inviteTime;
    }
	public void setInviteTime(long inviteTime) {
    	this.inviteTime = inviteTime;
    }
    
}
  