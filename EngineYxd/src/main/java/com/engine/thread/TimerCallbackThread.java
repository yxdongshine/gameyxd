
package com.engine.thread;  

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.engine.timer.TimerCallback;
import com.engine.timer.TimerCallbackThreadManage;

/** 
 * ClassName:TimerCallbackThread <br/> 
 * Function: (定时器回调线程). <br/> 
 * Date:     2015-9-6 下午5:12:15 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
@Component
public class TimerCallbackThread extends TimerCallbackThreadManage implements Runnable {
	private Log log = LogFactory.getLog(TimerCallbackThread.class);
	private boolean running = false;
	private long INTERNAL = 300;//毫秒
	private ExecutorService executorThreadPool = Executors.newCachedThreadPool();
	private Thread thread = new Thread(this, "TimerCallbackThread");
	
	public void startThread()
	{
		running = true;
		thread.start();
	}
	
	public void stopThread()
	{
		running = false;
	}
	
	@Override
    public void run() {
		while(running)
		{
			long begin = System.currentTimeMillis();
			for(Map.Entry<Long, TimerCallback> entry : timerCallbackMap.entrySet())
			{
				long curTime = System.currentTimeMillis();
				try{
					TimerCallback callback = entry.getValue();
					if(callback != null && curTime >= callback.getStartTimer() && curTime <= callback.getEndTimer())
					{
						final TimerCallback tCallback = callback;
						
						// 定时器超时
						if(tCallback.getStartTimer() >= tCallback.getEndTimer())
						{
							timerCallbackMap.remove(entry.getKey());
						}
						
						this.executorThreadPool.execute(new Runnable(){

							@Override
                            public void run() {
								try {
	                                tCallback.invoke();
                                } catch (Exception e) {
	                                e.printStackTrace();
                                }
                            }
							
						});
					}
				}catch(Exception e)
				{
					log.error("定时器回调线程内有错误", e);
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
				log.error("定时器回调线程有错误", e);
			}
		}
    }
	
}
  