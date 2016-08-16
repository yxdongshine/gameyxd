package com.lx.game.item.util;

import com.lx.game.res.item.ItemStaticConfig;

public class MathUtil {
	
	public static int displacementNumber(int baseNumber, int number) {
		number = 1 << number;
		baseNumber |= number;
		return baseNumber;
	}
	
	/**
	 * 
	 * isBagType:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param type
	 */
	public static boolean isBagType(int type) {
		boolean isLegel = false;
		if (ItemStaticConfig.TOOL_TYPE_EQUIPMENT <= type && ItemStaticConfig.TOOL_TYPE_SPECIAL >= type) {
			isLegel = true;
		}
		
		return isLegel;
	}
	
	/**
	 * 是否超过组队设置的间隔时间
	 * isPassCDTime:(). <br/> 
	 * TODO().<br/> 
	 * 
	 * @author yxd 
	 * @param cdTime
	 * @param beforeTime
	 * @return
	 */
	public static boolean isPassCDTime(int cdTime,long beforeTime){
		boolean isPass=false;
		if(beforeTime+cdTime*1000<System.currentTimeMillis()){
			isPass =true;
		}
		return isPass;
	}
	
	/**
	 * 拆分字符串 splitNumber:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param str
	 * @return
	 */
	public static int[] splitNumber(String str) {
		String[] subStrArr = str.trim().split("\\*");
		int[] strArr = new int[subStrArr.length];
		for (int i = 0; i < subStrArr.length; i++) {
			strArr[i] = Integer.parseInt(subStrArr[i].trim());
		}
		return strArr;
	}
	
}
