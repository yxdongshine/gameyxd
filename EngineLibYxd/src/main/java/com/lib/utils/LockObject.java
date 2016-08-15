package com.lib.utils;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ClassName:LockObject <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-16 下午6:29:46 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class LockObject extends ReentrantLock {
	
	/****/
	private static final long serialVersionUID = -5947807012445608690L;
	private Object obj;
	
	LockObject(Object _obj) {
		this.obj = _obj;
	}
	
	public Object getObj() {
		return obj;
	}
	
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
