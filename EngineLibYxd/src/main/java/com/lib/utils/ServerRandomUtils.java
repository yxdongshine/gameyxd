/** 
 * Project Name:DragonBallWorldServerHappy 
 * File Name:ServerRandom.java 
 * Package Name:com.sj.world.utils 
 * Date:2013-9-14上午10:50:15 
 * Copyright (c) 2013, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.lib.utils;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * ClassName:ServerRandom <br/>
 * Function: TODO (随机数实体类全是静态方法). <br/>
 * Reason: TODO (). <br/>
 * Date: 2013-9-14 上午10:50:15 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */
public class ServerRandomUtils {
	private static Random random = new Random();
	
	public static int nextInt(int val) {
	
		return nextInt(val,true);
	}
	
	public static int nextInt(int val,boolean isAbs) {
		int r = ((random.nextInt() + random.nextInt()) % val);
		if (isAbs){
			return Math.abs(r);
		}
		return r;
	}
	
	
	public static float randomFloat(float val) {
		// random.setSeed(System.currentTimeMillis());
		float r = ((random.nextFloat()) * val);
		return r;
	}
	
	public static int randomNum(int min, int max) {
		if (min < 0) {
			int i = nextInt(max + 1 + Math.abs(min));
			return min + i;
		} else {
			int i = nextInt(max + 1 - min);
			return min + i;
		}
	}
	
	// public static float randomFloatNum(int min, int max) {
	// float i = new Random().nextFloat() % max;
	// return i % max;
	//
	// }
	
	/**
	 * randomNums:(). <br/>
	 * TODO(得到区间内n个不重复随机数).<br/>
	 * 
	 * @author yst
	 * @param min 最小值（包含）
	 * @param max 最大值（包含）
	 * @param count 数量
	 * @return
	 */
	public static List<Integer> randomNums(int min, int max, int count) {
		if (max - min + 1 > count) {
			count = max - min + 1;
		}
		Set<Integer> nums = new LinkedHashSet<Integer>();
		while (nums.size() < count) {
			nums.add(randomNum(min, max));
		}
		List<Integer> list = new ArrayList<Integer>(nums);
		return list;
	}
}
