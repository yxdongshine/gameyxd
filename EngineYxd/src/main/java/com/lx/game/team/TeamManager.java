
package com.lx.game.team;  

import java.util.concurrent.ConcurrentHashMap;

/** 
 * ClassName:TeamManager <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午4:39:03 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class TeamManager {
   
	static ConcurrentHashMap<Long, Team> teamMap = new ConcurrentHashMap<Long, Team>();
    /**
     * 得到小队数据
     * getTeam:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param teamId
     * @return
     */
    public static Team getTeam(long teamId) {
    	return getTeamMap().get(teamId);
    }

    
    /**
     * 初始化数据库小队
     * buildTeamDataBase:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param teamData
     * @return
     
    public Team buildTeamDataBase(TeamData teamData){
    	Team team=null;
    	if(teamData!=null){//团队数据
    	    team =new Team(teamData.getId());//小队编号
    		List<Teamer> teamerList=JSONArray.parseArray(teamData.getTeamerInfo(),Teamer.class);
    		team.setObjects(teamerList);
    	}
    	return team;
    }
    */
    
    /**
     * 添加小队
     * addTeam:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param teamId
     * @param team
     */
    public static  void addTeam(Long teamId, Team team) {
        //存入数据库
        insertTeamer(team);
    }

    /**
     * 存入数据库小队信息
     * insertTeamer:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param team
     */
    public static void insertTeamer(Team team){
    	/*JSONArray jsonarray=new JSONArray();
    	for (int i = 0; i < team.getObjects().size(); i++) {
	        Teamer teamer=team.getObjects().get(i);
	        jsonarray.add(teamer);
        }
    	//保存字符串儿
    	TeamData teamData=new TeamData();
    	teamData.setId(team.getTeamId());
    	teamData.setLeaderId(team.getTeamLeader().getRoleId());
    	teamData.setTeamerInfo(jsonarray.toJSONString());
    	dao.saveOrUpdate(teamData);*/
    	
    	getTeamMap().put(team.getTeamId(), team);
    }
    
    /**
     * 删除小队
     * removeTeam:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @param teamId
     */
    public static void removeTeam(long teamId) {
    	/*TeamData teamData=new TeamData();
    	teamData.setId(teamId);
    	dao.delete(teamData);*/
    	
    	getTeamMap().remove(teamId);
    }


    
	public static ConcurrentHashMap<Long, Team> getTeamMap() {
    	return teamMap;
    }


	public static void setTeamMap(ConcurrentHashMap<Long, Team> teamMap) {
    	TeamManager.teamMap = teamMap;
    }
    
    
    
    
}
  