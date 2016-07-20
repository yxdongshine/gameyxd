package com.wx.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/** 
 * ClassName:DbEntity <br/> 
 * Function: TODO (). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2014-6-3 下午3:31:26 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
/**
 * ClassName: DbEntity <br/>
 * Function: TODO (所有数据库数的的基类). <br/>
 * Reason: TODO (). <br/>
 * date: 2014-6-3 下午3:31:26 <br/>
 * 
 * @author lyh
 * @version
 */
@MappedSuperclass
public class DbEntity implements Serializable, Cloneable {
	
	/****/
	private static final long serialVersionUID = 1L;
	
	private long id;
	
	@Id
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		DbEntity db = null;
		try {
			db = (DbEntity) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return db;
	}
	
}
