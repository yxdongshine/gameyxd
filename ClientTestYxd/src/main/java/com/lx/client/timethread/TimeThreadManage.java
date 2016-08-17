package com.lx.client.timethread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.loncent.protocol.sys.System.HeartRequest;
import com.lx.logical.manage.PlayerManage;

/**
 * ClassName:TimeThreadManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-10 上午9:55:28 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class TimeThreadManage {
	
	@Autowired
	private PlayerManage playerManage;
	/** 时间线程池 **/
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(4);
	
	public void load() {
		// 处理心跳包
		scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HeartRequest request = HeartRequest.newBuilder().build();
				playerManage.getConnect().send(request);
			}
		}, 0, 60000L, TimeUnit.MILLISECONDS);
	}
}
