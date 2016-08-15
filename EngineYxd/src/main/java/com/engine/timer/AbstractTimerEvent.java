package com.engine.timer;

import org.apache.commons.logging.Log;

import com.engine.utils.log.LogUtils;

/**
 * ClassName:AbstractTimeEvent <br/>
 * Function: TODO (时间事件). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午4:43:49 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class AbstractTimerEvent implements ITimer {
	
	protected Log log = LogUtils.getLog(AbstractTimerEvent.class);
	/** 事件id **/
	protected long eventId;
	
	/**协议id**/  
	protected int protocolId;
	
	protected long timer;
	
	/**次数**/  
	protected int count;
	
	/**是否为激活状态**/  
	protected boolean active = true;
	
	/**角色id**/  
	protected long roleId;
	
	public AbstractTimerEvent(long eventId, int protocolId,long timer,int count,long _roleId) {
		this.eventId = eventId;
		this.timer = timer;
		this.count = count;
		this.protocolId = protocolId;
		roleId = _roleId;
		TimerThreadManage.addTimeEvent(this);
	}
	
	public AbstractTimerEvent(long eventId,int protocolId, long timer,long _roleId) {
		this(eventId,protocolId,timer,1,_roleId);
	}
	
	public AbstractTimerEvent(long eventId,int protocolId, long timer) {
		this(eventId,protocolId,timer,1,0);
	}
	
	public AbstractTimerEvent(long eventId,int protocolId, long timer,int count) {
		this(eventId,protocolId,timer,count,0);
	}
	
	@Override
	public long getTime() {
		// TODO Auto-generated method stub
		return this.timer;
	}
	
	public long addTime(long addTime) {
		return this.timer += addTime;
	}

	public void calculateCount(){
		if (count == -1){
			return;
		}
		count--;
	}

    public void event() {
	    // TODO Auto-generated method stub
		if (count == -1 || count > 0){
			active = true;
			doNetxTime();
		}
    }
    
	
	/** 
	 * doNetxTime:(). <br/> 
	 * TODO().<br/> 
	 * 加入下一个时间节点
	 * @author lyh  
	 */  
	public abstract void doNetxTime();
	
	public boolean getActive(){
		return active;
	}
	
	public void disActive(){
		active = false;
	}

	@Override
    public long getEventId() {
	    // TODO Auto-generated method stub
	    return eventId;
    }

	@Override
    public int getCount() {
	    // TODO Auto-generated method stub
	    return count;
    }

	public long getRoleId() {
    	return roleId;
    }

	@Override
    public int getProtocolId() {
	    // TODO Auto-generated method stub
	    return protocolId;
    }
}
