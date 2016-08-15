package com.lib.utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * ClassName:LockUtils <br/>
 * Function: TODO (加锁工具类). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-16 下午6:15:32 <br/>
 * 最大上限1000超过上限1000删除第一个
 * 
 * @author lyh
 * @version
 * @see
 */
public class LockUtils {
	private static HashMap<Object, LockObject> lockMaps = new HashMap<Object, LockObject>();
	private static ConcurrentLinkedQueue<LockObject> linkLists = new ConcurrentLinkedQueue<LockObject>();
	private static int MAX_NUM = 1000;
	
	/**
	 * getLockObject:(). <br/>
	 * TODO().<br/>
	 * 获得锁对象
	 * 
	 * @author lyh
	 * @param obj
	 * @return
	 */
	public static LockObject getLockObject(Object obj) {
		LockObject lockObj = null;
		synchronized (lockMaps) {
			lockObj = lockMaps.get(obj);
			if (lockObj == null) {
				if (linkLists.size() > MAX_NUM) {
					Object tmp = linkLists.poll();
					lockMaps.remove(tmp);
				}
				lockObj = new LockObject(obj);
				lockMaps.put(obj, lockObj);
				linkLists.add(lockObj);
				lockObj = lockMaps.get(obj);
			}
		}
		return lockObj;
	}
	
}
