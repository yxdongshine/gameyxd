package com.engine.timer;

/**
 * ClassName:ITimer <br/>
 * Function: TODO (定时器接口). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午4:53:58 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public interface ITimer {
	public long getTime();
	
	public long getEventId();
	
	public int getCount();
	
	/** 
	 * getProtocolId:(). <br/> 
	 * TODO().<br/> 
	 * 协议id(这个必须要)
	 * @author lyh 
	 * @return 
	 */  
	public int getProtocolId();
}
