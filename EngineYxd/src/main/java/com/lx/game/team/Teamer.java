
package com.lx.game.team;  
/** 
 * ClassName:Teamer <br/> 
 * Function: TODO (队员信息). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-31 下午6:11:18 <br/> 
 * @author   yxd 
 * @version   
 * @see       
 */
public class Teamer {
	/**
	 * 角色编号
	 */
	private long roleId;
	/**
	 * 进入队伍的先后顺序
	 */
	private byte order;
	
	
	public Teamer(long roleId,byte order) {
	    // TODO Auto-generated constructor stub
		this.order=order;
		this.roleId=roleId;
    }
	
	public Teamer() {
	    // TODO Auto-generated constructor stub
    }
	
	public long getRoleId() {
    	return roleId;
    }
	public void setRoleId(long roleId) {
    	this.roleId = roleId;
    }
	public byte getOrder() {
    	return order;
    }
	public void setOrder(byte order) {
    	this.order = order;
    }
	
	
	
}
  