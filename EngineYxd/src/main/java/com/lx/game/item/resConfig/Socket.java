/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lx.game.item.resConfig;

import java.util.ArrayList;

import com.lx.nserver.txt.SocketPojo;

/**
 * 
 * ClassName: Socket <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-7-8 下午4:34:01 <br/>
 * 
 * @author yxd
 * @version
 */
public class Socket {
	private String id;
	private int count[];
	private int probability[];
	
	public Socket(ArrayList<SocketPojo> socketAL) {
		if (socketAL != null && socketAL.size() > 0) {
			this.id = socketAL.get(0).getSocketType();
			count = new int[socketAL.size()];
			probability = new int[socketAL.size()];
			for (int i = 0; i < socketAL.size(); i++) {
				count[i] = socketAL.get(i).getCount();
				probability[i] = socketAL.get(i).getProbability();
			}
		}
		
	}
	
	public int[] getProbability() {
		return probability;
	}
	
	public int getCount(int index) {
		return count[index];
	}
	
	public int getMaxCount() {
		if (count.length <= 0) {
			return 0;
		}
		return count[count.length - 1];
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}
