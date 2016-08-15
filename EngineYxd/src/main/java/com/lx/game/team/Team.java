
package com.lx.game.team;  

import java.util.ArrayList;
import java.util.List;

/** 
 * ClassName:Team <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午4:39:54 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class Team {
	
	/**
	 * 成员列表
	 */
	 private List<Teamer> objects;
	 /**
	  * 编号
	  */
	 private long TeamId;
	 
	 /**
	     * 收到的组队请求
	     */
	 private List<Long> requestList = new ArrayList<Long>();
	    
	 public Team(long TeamId) {
	        objects = new ArrayList<Teamer>();
	        this.setTeamId(TeamId);
	    }

	 
	 
	 
	 
	 
	 
	 
	 
	    /**
	     * 移交队长
	     * TransferLeader:(). <br/> 
	     * TODO().<br/> 
	     * 
	     * @author yxd 
	     * @param transferRoleId
	     * @return
	     */
	    public boolean TransferLeader(long transferRoleId){
	    	boolean transfer =true;
	    	Teamer leader= getTeamLeader();//老队长
	    	Teamer beLeader=getTeamer(transferRoleId);//新队长
	    	int sortBoundaryIndex=beLeader.getOrder();//排序分界线
	    	for (int i = sortBoundaryIndex+1; i <= objects.size(); i++) {
	    		byte order=objects.get(i).getOrder();
	    		order--;
	            objects.get(i).setOrder(order);
            }
	    	leader.setOrder((byte)objects.size());//老队长最后
	    	objects.remove(leader);
	    	objects.remove(beLeader);
	    	beLeader.setOrder((byte)TeamStaticConifg.TEAM_LEADER_ORDER);
	    	objects.add(leader);
	    	objects.add(beLeader);
	    	return transfer;
	    }
	 
	 
	    /**
	     * 
	     * removeTeamer:(). <br/> 
	     * TODO().<br/> 
	     * 
	     * @author yxd 
	     * @param beRemoveRoleId
	     * @return
	     */
		 public boolean removeTeamer(long beRemoveRoleId){
			 boolean hasRemove=true;
			 int sortBoundaryIndex=0;//排序分界线
			 for (int i = 0; i < this.objects.size(); i++) {
	            if(objects.get(i).getRoleId()==beRemoveRoleId){
	            	sortBoundaryIndex=objects.get(i).getOrder();
	            	//移除
	            	objects.remove(objects.get(i));
	            }
            }
			 
			 for (int i = 0; i < this.objects.size(); i++) {
				  if(objects.get(i).getOrder()>sortBoundaryIndex){//减一
					  byte order=objects.get(i).getOrder();
			    		order--;
			            objects.get(i).setOrder(order);
				  }
					  
            }
			 return hasRemove;
		 }
	 
	 
	 
    /**
     * 获取队长	 
     * getTeamLeader:(). <br/> 
     * TODO().<br/> 
     * 
     * @author yxd 
     * @return
     */
	public Teamer getTeamLeader(){
		Teamer teamer=null;
		for (int i = 0; i < this.getObjects().size(); i++) {
	        if(this.getObjects().get(i).getOrder()==TeamStaticConifg.TEAM_LEADER_ORDER){//队长
	        	teamer=this.getObjects().get(i);
	        	break;
	        }
        }
		
		return teamer;
	} 
	 
	/**
	 * 获取队员
	 * getTeamer:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param teamerRoleId
	 * @return
	 */
	public Teamer getTeamer(long teamerRoleId){
		Teamer teamer=null;
		for (int i = 0; i < this.getObjects().size(); i++) {
	        if(this.getObjects().get(i).getRoleId()==teamerRoleId){//队长
	        	teamer=this.getObjects().get(i);
	        	break;
	        }
        }
		return teamer;
	}
	 
	/**
	 * 
	 * hasRequestRole:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param requestRoleId
	 * @return
	 */
	public boolean hasRequestRole(long  requestRoleId){
		 boolean hasRequest=false;
		 for (int i = 0; i <getRequestList().size(); i++) {
		        if(this.getRequestList().get(i)==requestRoleId){//队长
		        	hasRequest=true;
		        	break;
		        }
	        }
		 return hasRequest;
	}
	
	
	public List<Teamer> getObjects() {
    	return objects;
    }

	public void setObjects(List<Teamer> objects) {
    	this.objects = objects;
    }


	public long getTeamId() {
    	return TeamId;
    }


	public void setTeamId(long teamId) {
    	TeamId = teamId;
    }
	

	public List<Long> getRequestList() {
    	return requestList;
    }
	public void setRequestList(List<Long> requestList) {
    	this.requestList = requestList;
    }
}
  