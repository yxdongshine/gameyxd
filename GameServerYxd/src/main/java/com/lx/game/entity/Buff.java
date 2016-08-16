
package com.lx.game.entity;  

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.engine.thread.TimerCallbackThread;
import com.engine.timer.TimerCallback;
import com.engine.utils.log.LogUtils;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.game.pbbuff.Buff.BuffStatusUpdateResponse;
import com.lx.logical.manage.BuffManage;
import com.lx.nserver.txt.BuffPojo;
import com.lx.nserver.txt.BuffTypePojo;

/** 
 * ClassName:Buff <br/> 
 * Function: (buff实体类). <br/> 
 * Date:     2015-9-1 下午2:19:43 <br/> 
 * @author   jack 
 * @version   
 * @see       
 */
public class Buff {
	/** buff **/  
	private BuffPojo buffPojo;
	/** buff类别 **/  
	private BuffTypePojo buffTypePojo;
	/** 开始时间 **/  
	private long startTime;
	/** 结束时间 **/  
	private long endTime;
	/** 作用目标 **/  
	private Object targetObj;
	/** 目标类型 **/  
	private int targetType;
	/** buff 定时器id **/  
	private long timerId;
	
	private Log log = LogUtils.getLog(Buff.class);
	
	private BuffManage buffManange;
	
	public void setBuffManange(BuffManage buffManange) {
    	this.buffManange = buffManange;
    }

	public Buff()
	{
		this.timerId  = TimerCallbackThread.addTimerCallback(new TimerCallback(this, "buffWork"));
	}

	public long getTimerId() {
    	return timerId;
    }

	public Object getTargetObj() {
    	return targetObj;
    }
	public void setTargetObj(Object targetObj) {
    	this.targetObj = targetObj;
    }
	public int getTargetType() {
    	return targetType;
    }
	public void setTargetType(int targetType) {
    	this.targetType = targetType;
    }
	public BuffPojo getBuffPojo() {
    	return buffPojo;
    }
	public void setBuffPojo(BuffPojo buffPojo) {
    	this.buffPojo = buffPojo;
    }
	public BuffTypePojo getBuffTypePojo() {
    	return buffTypePojo;
    }
	public void setBuffTypePojo(BuffTypePojo buffTypePojo) {
    	this.buffTypePojo = buffTypePojo;
    }
	public long getStartTime() {
    	return startTime;
    }
	public void setStartTime(long startTime) {
    	this.startTime = startTime + this.buffTypePojo.getWorkTick();
    	TimerCallback t = TimerCallbackThread.findTimerCallback(this.timerId);
    	t.setStartTimer(startTime);
    	t.setInternal(this.buffTypePojo.getWorkTick());
    }
	public long getEndTime() {
    	return endTime;
    }
	public void setEndTime(long endTime) {
    	this.endTime = endTime;
    	TimerCallback t=  TimerCallbackThread.findTimerCallback(this.timerId);
    	t.setEndTimer(this.endTime);
    }
	
	public long getTargetId()
	{
		long targetId = 0;
		if(this.targetType == PbAoiEntityType.Monster_VALUE)
		{
			Monster monster = (Monster) targetObj;
			targetId = monster.getId();
			
		} else if(this.targetType == PbAoiEntityType.Role_VALUE)
		{
			ServerPlayer sp = (ServerPlayer) targetObj;
			targetId = sp.getId();
		}
		return targetId;
	}
	
	/** 
	 * buff生效
	 * buffWork:(). <br/> 
	 */  
	public void buffWork()
	{
		//是否结束
		long curTime = System.currentTimeMillis();
		if(this.endTime < curTime)
		{
			buffManange.removeBuff( targetObj, targetType, this.buffPojo.getBuffId());
			return;
		}
		//test
		if(this.targetType == PbAoiEntityType.Role_VALUE)
		{
			ServerPlayer sp = (ServerPlayer)this.targetObj;
			//log.info("buffWork buffName=" + this.getBuffPojo().getBuffName() + " targetName=" + sp.getRole().getRoleName());
		}else if(this.targetType == PbAoiEntityType.Monster_VALUE)
		{
			Monster monster = (Monster)this.targetObj;
			//log.info("buffWork buffName=" + this.getBuffPojo().getBuffName() + " targetName=" + monster.getName());
		}
				
		Attribute attr = null;
		long targetId = 0;
		if(this.targetType == PbAoiEntityType.Monster_VALUE)
		{
			Monster monster = (Monster) targetObj;
			attr = monster.getAttribute();
			targetId = monster.getId();
			if(monster.isDie()) //死亡
				return;
			
		} else if(this.targetType == PbAoiEntityType.Role_VALUE)
		{
			ServerPlayer sp = (ServerPlayer) targetObj;
			attr = sp.getAttribute();
			targetId = sp.getId();
			if(sp.isDie()) //死亡
				return;
		}
		
		BuffStatusUpdateResponse.Builder msg = BuffStatusUpdateResponse.newBuilder();		
		int[][] workAttrType = buffTypePojo.getWorkAttrValues();
		for(int i = 0; i < workAttrType.length; i += 2)
		{
			int workAttr = workAttrType[0][i];
			int workType = workAttrType[0][i+1];
			double workValue = 0;
			if(workType ==1 )//数值
			{
				workValue = this.buffPojo.getWorkValue();
				attr.addBuffAttribute(workAttr, workValue);
			}else //百分比
			{
				int value = attr.getAttribute(workAttr);
				workValue = value * this.buffPojo.getWorkValue()/100.0;
				attr.addBuffAttribute(workAttr, workValue);
			}
			
			BuffStatusUpdateResponse.WorkBuffInfo.Builder info = BuffStatusUpdateResponse.WorkBuffInfo.newBuilder();
			info.setWorkAttr(workAttr);
			info.setWorkValue((int)workValue);
			msg.addWorkBuffList(info);
		}
		msg.setTargetId(targetId);
		msg.setTargetType(PbAoiEntityType.valueOf(targetType));
		
		buffManange.sendBuffStatusToClient(msg, targetObj, targetType);
	}
}
  