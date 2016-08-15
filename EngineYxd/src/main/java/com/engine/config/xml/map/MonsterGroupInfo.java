package com.engine.config.xml.map;

import java.util.ArrayList;

/**
 * ClassName:MonsterInfo <br/>
 * Function: (怪物组配置信息). <br/>
 * Date: 2015-7-14 下午3:47:49 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class MonsterGroupInfo {
	/** x坐标 **/
	public float x;
	
	/** y坐标 **/
	public float y;
	
	/** z坐标 **/
	public float z;
	
	/** 怪物列表 **/
	public ArrayList<Integer> monsterList = new ArrayList<Integer>();
	
	/** 刷新次数 **/
	public int count;
	
	/** 是否激活 **/  
	public int isActive;
}
