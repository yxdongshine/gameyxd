package com.lx.game.item.resConfig;

import com.lx.nserver.txt.EquipQualityPojo;

/**
 * 装备品质
 * 
 * @author ming
 * 
 */
public class EquipQuality {
	
	/**
	 * 品质颜色
	 */
	private String color;
	/**
	 * 品质名称
	 */
	private String name;
	/**
	 * 品质等级
	 */
	private int value;
	/**
	 * 装备初始分数
	 */
	private int standard;
	/**
	 * 装备分数最小值
	 */
	private int min;
	/**
	 * 装备分数最大值
	 */
	private int max;
	/**
	 * 装备分数最大值
	 */
	private int showColor[];
	
	/**
	 * 载入配置文件
	 * 
	 * @param e
	 */
	public EquipQuality(EquipQualityPojo equipQuality) {
		
		color = equipQuality.getColor();
		value = equipQuality.getValue();
		name = equipQuality.getName();
		standard = equipQuality.getStandard();
		min = equipQuality.getMin();
		max = equipQuality.getMax();
		
	}
	
	public String getColor() {
		return color;
	}
	
	public int getShowColor(int score) {
		for (int i = 0; i < showColor.length; i++) {
			if (score <= showColor[i]) {
				return i;
			}
		}
		return 0;
	}
	
	public int getValue() {
		return value;
	}
	
	public int getStandard() {
		return standard;
	}
	
	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}
	
	public String getName() {
		return name;
	}
	
}
