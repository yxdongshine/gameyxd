/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lx.game.item.resConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lx.nserver.txt.EquipmentLevelGrouthPojo;

/**
 * 
 * ClassName: MainAttributeGroup <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-7-30 上午10:13:21 <br/>
 * 
 * @author yxd
 * @version
 */
public class MainAttributeGroup {
	
	private Map<Integer, MainAttribute> mainAttribute = new HashMap<Integer, MainAttribute>(); // key = level
	
	public MainAttributeGroup(ArrayList<EquipmentLevelGrouthPojo> equipLGal) {
		int level;
		for (int i = 0; i < equipLGal.size(); i++) {
			level = equipLGal.get(i).getLevel();
			mainAttribute.put(level, new MainAttribute(equipLGal.get(i)));
		}
	}
	
	public int[][] getMainAttribute(int level) {
		return new int[][] { mainAttribute.get(level).getAttribute() };
	}
	
	public int[] getMainAttribute(int level, byte currency) {
		return mainAttribute.get(level).getGrowth(currency);
	}
}

class MainAttribute {
	
	private int[][] growth = new int[2][];
	int[] attribute = new int[2];
	
	public MainAttribute(EquipmentLevelGrouthPojo equipmentLevelGrouth) {
		attribute[0] = equipmentLevelGrouth.getElgId(); // AttributesConfigLoad.getIndexById(equipmentLevelGrouth.getElgId());
		attribute[1] = equipmentLevelGrouth.getValue();
		String growth0Str = equipmentLevelGrouth.getGrowth0();
		if (growth0Str != null && growth0Str.trim().length() > 0) {
			String[] growth0StrArr = growth0Str.split("\\*");
			if (growth0StrArr != null && growth0StrArr.length > 0) {
				growth[0] = new int[growth0StrArr.length];
				for (int i = 0; i < growth0StrArr.length; i++) {
					growth[0][i] = Integer.parseInt(growth0StrArr[i]);
				}
			}
		}
		
		growth0Str = equipmentLevelGrouth.getGrowth1();
		if (growth0Str != null && growth0Str.trim().length() > 0) {
			String[] growth0StrArr = growth0Str.split("\\*");
			if (growth0StrArr != null && growth0StrArr.length > 0) {
				growth[1] = new int[growth0StrArr.length];
				for (int i = 0; i < growth0StrArr.length; i++) {
					growth[1][i] = Integer.parseInt(growth0StrArr[i]);
				}
			}
		}
		
	}
	
	public int[] getAttribute() {
		return attribute;
	}
	
	public int[] getGrowth(byte currey) {
		return growth[currey];
	}
}
