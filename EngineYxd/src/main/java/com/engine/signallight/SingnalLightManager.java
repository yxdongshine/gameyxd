/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engine.signallight;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Gust
 */
public class SingnalLightManager extends Thread {
	private static Log log = LogFactory.getLog(SingnalLightManager.class);
	static final HashMap<String, String> lights = new LinkedHashMap<String, String>();
	static private SingnalLightManager manager = new SingnalLightManager();
	static long PERIOD_PRINT = 30 * 1000;
	static boolean debug = true;
	
	static public SingnalLightManager getInstance() {
		return manager;
	}
	
	SingnalLightManager() {
		
		setDaemon(true);
		this.start();
		setName("SingnalLight");
	}
	
	@Override
	public void run() {
		long start = 0, now;
		start = System.currentTimeMillis();
		while (true) {
			now = System.currentTimeMillis();
			if (now - start > PERIOD_PRINT) {
				log.debug(toString());
				String result = getResult();
				if (result != null && !"".equals(result)) {
					log.info("singnal:" + result);
				}
				start = now;
			} else {
				try {
					sleep(100);
				} catch (InterruptedException ex) {
					Logger.getLogger(SingnalLightManager.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
	
	static public void setOn(String name) {
		if (!debug) {
			return;
		}
		synchronized (lights) {
			lights.put(name, null);
		}
	}
	
	static public void setOn(String name, String memo) {
		if (!debug) {
			return;
		}
		synchronized (lights) {
			lights.put(name, memo);
		}
	}
	
	static public void setOff(String name) {
		if (!debug) {
			return;
		}
		synchronized (lights) {
			lights.remove(name);
		}
	}
	
	static public String getResult() {
		synchronized (lights) {
			StringBuilder sb = new StringBuilder();
			if (lights.size() > 0) {
				sb.append("\n--------------lights--------------\n").append(new Date(System.currentTimeMillis())).append("\n");
				for (String name : lights.keySet()) {
					String o = lights.get(name);
					sb.append(name);
					if (o != null) {
						sb.append(":").append(o);
					}
					sb.append("\n");
				}
				sb.append("-------------------------------------\n");
			}
			return sb.toString();
		}
	}
}
