
package com.engine.timer;  

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;

import com.engine.utils.log.LogUtils;

/** 
 * ClassName:TimerCallback <br/> 
 * Function: (定时器回调). <br/> 
 * Date:     2015-9-6 下午4:54:17 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
public class TimerCallback {
	
	protected Log log = LogUtils.getLog(TimerCallback.class);
	/** 回调时间 **/  
	protected long startTimer;
	/** 回调间隔 **/  
	protected long internal;
	/** 回调结束时间 **/  
	protected long endTimer;
	
	/** 回调对象 **/  
	private Object object;
	/** 回调方法 **/  
	private String methodName;
	/** 回调参数 **/  
	private Object[] params;
	/** 回调参数类型 **/  
	private Class[] paramTypes;
	public Object getObject() {
    	return object;
    }
	public void setObject(Object object) {
    	this.object = object;
    }
	public String getMethodName() {
    	return methodName;
    }
	public void setMethodName(String methodName) {
    	this.methodName = methodName;
    }
	public Object[] getParams() {
    	return params;
    }
	public void setParams(Object[] params) {
    	this.params = params;
    }
	public Class[] getParamTypes() {
    	return paramTypes;
    }
	public void setParamTypes(Class[] paramTypes) {
    	this.paramTypes = paramTypes;
    }
	
	public long getStartTimer() {
    	return startTimer;
    }
	public void setStartTimer(long startTimer) {
    	this.startTimer = startTimer;
    }
	public long getInternal() {
    	return internal;
    }
	public void setInternal(long internal) {
    	this.internal = internal;
    }
	public long getEndTimer() {
    	return endTimer;
    }
	public void setEndTimer(long endTimer) {
    	this.endTimer = endTimer;
    }
	public TimerCallback(Object obj, String methodName,Object... args)
	{
		this.object = obj;
		this.methodName = methodName;
		this.params = args;
		this.contractParamTypes(this.params);
	}
	
	/** 
	 * 获取参数类型
	 * contractParamTypes:(). <br/> 
	 */  
	private void contractParamTypes(Object[] params)
	{
		this.paramTypes = new Class[params.length];
		for(int i = 0; i < params.length; i++)
		{
			this.paramTypes[i] = params[i].getClass();
		}
	}
	
	/** 
	 * 执行回调
	 * callback:(). <br/> 
	 */  
	public void invoke() throws Exception{
		Method method = this.object.getClass().getMethod(this.getMethodName(), this.getParamTypes());
		if(method == null)
		{
			return;
		}
		
		method.invoke(this.getObject(), this.getParams());
		doNext();
	}
	
	/** 
	 * 重置计时器
	 * doNext:(). <br/> 
	 */  
	private void doNext()
	{
		this.startTimer += this.internal * 1000;
	}
}
  