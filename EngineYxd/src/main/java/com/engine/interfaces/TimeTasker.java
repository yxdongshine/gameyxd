package com.engine.interfaces;

import java.util.concurrent.ScheduledFuture;

/**
 * ClassName:TimeTasker <br/>
 * Function: TODO (时间任务接口). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-22 下午5:11:18 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public abstract class TimeTasker implements Runnable {
	ScheduledFuture<?> future = null;
	
	public void run() {
		onTime(System.currentTimeMillis());
	}
	
	public void setScheduledFuture(ScheduledFuture<?> sf) {
		future = sf;
	}
	
	public void stop() {
		if (future != null) {
			future.cancel(true);
		}
	}
	
	public abstract void onTime(long time);
}
