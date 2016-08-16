package com.lx.gate.heart;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import com.engine.service.ConnectorManage;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:HeartManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-8-7 下午4:06:19 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class HeartManage {
	
	public void heartTick() {
		ExecutorService singPool = Executors.newSingleThreadExecutor();
		singPool.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (true) {
					for (Iterator iterator = ConnectorManage.getMinaconnectmap().values().iterator(); iterator.hasNext();) {
						IConnect iConnect = (IConnect) iterator.next();
						if ((System.currentTimeMillis() - iConnect.getPing()) / 1000 > 120) {// 大于2分钟
							iConnect.close();
						}
					}
				}
			}
		});
	}
}
