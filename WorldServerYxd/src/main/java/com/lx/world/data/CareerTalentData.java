package com.lx.world.data;

import java.util.concurrent.CopyOnWriteArrayList;

import com.engine.domain.Role;

/**
 * ClassName:LoginInfoData <br/>
 * Function: TODO (职业资质信息). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-8 上午10:18:43 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class CareerTalentData {
	
	/** 职业配置文件id **/
	private int careerConfigId;
	
	/** 气力 **/
	private int air;
	/** 内力 **/
	private int innerForce;
	/*** 敏捷 */
	private int agility;
	/** 掌控 **/
	private int control;
	/** 坚韧 **/
	private int tenacity;
	/** 防御 **/
	private int defence;
	
	/** 用户名 **/
	private String userName;
	
	public int getCareerConfigId() {
		return careerConfigId;
	}
	
	public void setCareerConfigId(int careerConfigId) {
		this.careerConfigId = careerConfigId;
	}
	
	public int getAir() {
		return air;
	}
	
	public void setAir(int air) {
		this.air = air;
	}
	
	public int getInnerForce() {
		return innerForce;
	}
	
	public void setInnerForce(int innerForce) {
		this.innerForce = innerForce;
	}
	
	public int getAgility() {
		return agility;
	}
	
	public void setAgility(int agility) {
		this.agility = agility;
	}
	
	public int getControl() {
		return control;
	}
	
	public void setControl(int control) {
		this.control = control;
	}
	
	public int getTenacity() {
		return tenacity;
	}
	
	public void setTenacity(int tenacity) {
		this.tenacity = tenacity;
	}
	
	public int getDefence() {
		return defence;
	}
	
	public void setDefence(int defence) {
		this.defence = defence;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
