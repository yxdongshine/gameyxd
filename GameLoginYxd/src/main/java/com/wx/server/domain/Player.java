package com.wx.server.domain;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;


/**
 * ClassName:Player <br/>
 * Function: TODO (角色). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-9 下午4:23:59 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Entity
@Table(name = "loncent_player",indexes={@Index(name = "account_name",columnList="accountName")})
public class Player extends DbEntity {
	
	/****/
	private static final long serialVersionUID = -3267935359170487666L;
	
	/** 账号 gf**/
	
	private String accountName;// 账号
	/** 密码 **/
	private String password;// 密码
	/** 邮箱地址 **/
	private String mailAddress;// 邮箱地址
	/** 渠道号 **/
	private String channleId;// 渠道号
	/** 游戏编号 **/
	private String appId;// 游戏编号
	/** 版本号 **/
	private String version;// 版本号
	/** 国际移动电话设备识别码 **/
	private String phoneType;// 机型,游戏机型
	/** 国际移动电话设备识别码 **/
	private String imei;// 国际移动电话设备识别码
	/** 电话号码 **/
	private String phoneNum;// 电话号码
	/** 上次登录的游戏服务器id,或者推荐服务器id **/
	private int lastServerId;
	
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getMailAddress() {
		return mailAddress;
	}
	
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
	
	public String getChannleId() {
		return channleId;
	}
	
	public void setChannleId(String channleId) {
		this.channleId = channleId;
	}
	
	public String getAppId() {
		return appId;
	}
	
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getPhoneType() {
		return phoneType;
	}
	
	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}
	
	public String getImei() {
		return imei;
	}
	
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}
	
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public int getLastServerId() {
		return lastServerId;
	}
	
	public void setLastServerId(int lastServerId) {
		this.lastServerId = lastServerId;
	}
	
}
