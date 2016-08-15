package com.engine.thread;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.msgloader.CommondClassLoader;
import com.engine.timer.AbstractTimerEvent;
import com.engine.timer.TimerThreadManage;
import com.lx.logical.GameMessage;

/**
 * ClassName:TimerThread <br/>
 * Function: TODO (定时器线程). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-11 下午5:06:22 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
@Component
public class TimerThread extends TimerThreadManage implements Runnable {
	private Log log = LogFactory.getLog(TimerThread.class);
	private boolean running = false;
	private long INTERNAL = 300;// 毫秒
	/** 处理事件线程池 **/
	private ExecutorService executorThreadPool = Executors.newCachedThreadPool();
	private Thread thread = new Thread(this, "TimeThread");
	@Autowired
	private CommondClassLoader commond;
	public void startThread() {
		running = true;
		thread.start();
	}
	
	public void stopThread() {
		running = false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (running) {
			long begin = System.currentTimeMillis();
			for (Map.Entry<Long, AbstractTimerEvent> entry : timerEventLists.entrySet()) {
				
				long curTime = System.currentTimeMillis();
				try {
					AbstractTimerEvent event = entry.getValue();
					if (event != null && curTime >= event.getTime() && event.getActive()) {
						final AbstractTimerEvent removeEvent = event;
						event.disActive();
						if (removeEvent.getCount() == 1 || removeEvent.getCount() == 0){
							timerEventLists.remove(removeEvent.getEventId());
						}
						removeEvent.calculateCount();
						executorThreadPool.execute(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								@SuppressWarnings("unchecked")
                                GameMessage<AbstractTimerEvent> task = (GameMessage<AbstractTimerEvent>)commond.get(removeEvent.getProtocolId());
								if (task != null){
									try {
	                                    task.doMessage(removeEvent, null);
                                    } catch (Exception e) {
                                    	timerEventLists.remove(removeEvent);
	                                    e.printStackTrace();
	                                	log.error("处理定时器线程有问题::", e);
                                    }
								}
								removeEvent.event();
							}
						});
					}
				} catch (Exception e) {
					log.error("时间线程内有错误", e);
					timerEventLists.remove(entry.getValue());
				}
			}
			long end = INTERNAL - (System.currentTimeMillis() - begin);
			if (end <= 5) {
				end = 5;
			}
			try {
				Thread.sleep(end);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error("时间线程有错误", e);
			}
		}
	}
	
}
