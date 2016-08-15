package com.engine.shutdown;

import java.util.concurrent.CopyOnWriteArrayList;

import com.engine.close.Stopable;

/**
 * ClassName:ShutThread <br/>
 * Function: TODO (退出线程). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-17 下午4:46:09 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ShutDownThread extends Thread {
	private static CopyOnWriteArrayList<Stopable> shutDownLists = new CopyOnWriteArrayList<Stopable>();
	
	public ShutDownThread() {
		super("ExitThread");
		
	}
	
	public void run() {
		for (Stopable ca : shutDownLists) {
			// 关闭
			ca.stop();
		}
	}
	
	/**
	 * registerCloseable:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author lyh
	 * @param ca
	 */
	public static void registerCloseableToShutDown(Stopable ca) {
		if (!shutDownLists.contains(ca)) {
			shutDownLists.add(ca);
		}
		
	}
}
