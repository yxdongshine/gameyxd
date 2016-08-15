package com.engine.config.xml.map;

import java.util.ArrayList;

import com.engine.entityobj.Position3D;

/**
 * ClassName:GateInfo <br/>
 * Function: (传送门配置信息). <br/>
 * Date: 2015-7-14 下午3:48:11 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
public class GateInfo {
	
	/** 世界传送门 **/
	public static final int GATE_TYPE_WORLD = 1;
	/** 副本传送门 **/
	public static final int GATE_TYPE_FUBEN = 2;
	
	/** 模板ID **/
	public int tid;
	
	/** x坐标 **/
	public float x;
	
	/** y坐标 **/
	public float y;
	
	/** z坐标 **/
	public float z;
	
	/** 传送门类型 1-普通传送门 2副本传送门 **/
	public int type;
	
	/** 目标ID **/
	public int toTargetId;
	
	/** 目标位置 **/
	public Position3D toTargetPos;
	
	/** 副本挂载集合 **/
	public ArrayList<Integer> fuBenMapUid;
	
	public int openMonsterGroupId;
}
