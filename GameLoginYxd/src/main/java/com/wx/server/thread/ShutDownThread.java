package com.wx.server.thread;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * ClassName:ShutDownThread <br/>
 * Function: TODO (服务器关闭线程). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-10 下午2:25:59 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ShutDownThread extends Thread {
	
	private static CopyOnWriteArraySet<Closeable> closeList = new CopyOnWriteArraySet<Closeable>();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (Closeable co : closeList) {
			co.close();
		}
	}
	
	/**
	 * addCloseable:(). <br/>
	 * TODO().<br/>
	 * 加入到关闭钩子
	 * 
	 * @author lyh
	 * @param close
	 */
	public static void addCloseable(Closeable close) {
		closeList.add(close);
	}
	
}
