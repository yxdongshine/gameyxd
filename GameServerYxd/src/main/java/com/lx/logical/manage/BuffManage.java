package com.lx.logical.manage;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.dbdao.EntityDAO;
import com.engine.domain.BuffData;
import com.engine.entityattribute.Attribute;
import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.AbsMapObject;
import com.engine.entityobj.space.IMapObject;
import com.engine.thread.TimerCallbackThread;
import com.lib.utils.ServerUUID;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.game.pbbuff.Buff.BuffActionUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffStatusUpdateResponse;
import com.loncent.protocol.game.pbbuff.Buff.BuffStatusUpdateResponse.BuffInfo;
import com.lx.game.entity.Buff;
import com.lx.game.entity.Monster;
import com.lx.game.send.MessageSend;
import com.lx.nserver.model.BuffModel;
import com.lx.nserver.model.BuffTypeModel;
import com.lx.nserver.txt.BuffPojo;
import com.lx.nserver.txt.BuffTypePojo;

/**
 * ClassName:BuffManage <br/>
 * Function: (Buff管理类). <br/>
 * Date: 2015-9-1 上午11:39:33 <br/>
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class BuffManage {
	
	@Autowired
	private BuffModel buffConfs;
	@Autowired
	private BuffTypeModel buffTypeConfs;
	@Autowired
	EntityDAO dbDao;
	
	Log log = LogFactory.getLog(this.getClass());
	
	/** buff信息 **/
	private Map<Long, Vector<Buff>> targetBuffs = new ConcurrentHashMap<Long, Vector<Buff>>();
	
	/**
	 * 加载buff数据 loadRoleBuffFromDb:(). <br/>
	 */
	public void loadRoleBuffFromDb(ServerPlayer sp) {
		List<BuffData> datas = dbDao.findByProperty(BuffData.class, "roleId", sp.getRole().getId());
		if (datas.size() <= 0)
			return;
		
		Vector<Buff> buffVec = this.getTargetBuffs(sp.getRole().getId());
		// 判断buff是否可保存
		BuffStatusUpdateResponse.Builder msg = BuffStatusUpdateResponse.newBuilder();
		BuffData data = datas.get(0);
		if (data.getBuffInfos().length() <= 0)
			return;
		String[] buffStrs = data.getBuffInfos().split(";");
		for (int i = 0; i < buffStrs.length; i++) {
			String buffStr = buffStrs[i];
			String[] buffInfoStr = buffStr.split(",");
			int buffId = Integer.parseInt(buffInfoStr[0]);
			long endTimer = Long.parseLong(buffInfoStr[1]);
			long curTimer = System.currentTimeMillis();
			if (endTimer < curTimer)
				continue;// 超时
				
			BuffPojo pojo = this.buffConfs.get(buffId);
			BuffTypePojo typePojo = this.buffTypeConfs.get(pojo.getBuffType());
			if (typePojo.getCanSave() != 1)
				continue; // 不可保存
				
			Buff buff = new Buff();
			buff.setBuffManange(this);
			buff.setBuffPojo(pojo);
			buff.setBuffTypePojo(typePojo);
			buff.setTargetObj(sp);
			buff.setTargetType(PbAoiEntityType.Role_VALUE);
			buff.setStartTime(curTimer);
			buff.setEndTime(endTimer);
			buffVec.add(buff);
			
			BuffInfo.Builder info = BuffInfo.newBuilder();
			info.setBuffId(buff.getBuffPojo().getBuffId());
			info.setLeftTime((int) (buff.getEndTime() - System.currentTimeMillis()));
			msg.addNewBuffList(info);
		}
		
		msg.setTargetId(sp.getRole().getId());
		msg.setTargetType(PbAoiEntityType.Role);
		
		sendBuffStatusToClient(msg, sp, PbAoiEntityType.Role_VALUE);
	}
	
	/**
	 * 添加buff addBuff:(). <br/>
	 */
	public Buff addBuff(Object targetObj, int targetType, int buffId) {
		long targetId = 0;
		if (targetType == PbAoiEntityType.Role_VALUE) {
			ServerPlayer sp = (ServerPlayer) targetObj;
			targetId = sp.getRole().getId();
		} else if (targetType == PbAoiEntityType.Monster_VALUE) {
			Monster monster = (Monster) targetObj;
			targetId = monster.getId();
		}
		
		boolean isImmune = this.checkTargetIsImmune(targetId, buffId);
		if (isImmune) // 免疫
		{
			BuffActionUpdateResponse.Builder actionMsg = BuffActionUpdateResponse.newBuilder();
			actionMsg.setActionId(1);
			actionMsg.setTargetId(targetId);
			actionMsg.setTargetType(PbAoiEntityType.valueOf(targetType));
			this.sendBuffActionToClient(actionMsg, targetObj, targetType);
			return null;
		}
		
		BuffPojo pojo = buffConfs.get(buffId);
		if (pojo == null) {
			log.error("cant find buff. buffId = " + buffId);
			return null;
		}
		
		BuffTypePojo typePojo = buffTypeConfs.get(pojo.getBuffType());
		if (typePojo == null) {
			log.error("cant find buff type. buffTypeId = " + pojo.getBuffType());
			return null;
		}
		Buff buff = null;
		boolean hasBuffId = this.checkRoleHasBuffId(targetId, buffId);
		Buff typeBuffInfo = this.checkTargetHasBuffTypeId(targetId, pojo.getBuffType());
		if (typeBuffInfo != null)// 同类型高等级覆盖低等级
		{
			if (typeBuffInfo.getBuffPojo().getBuffLevel() < pojo.getBuffLevel()) {
				// 移除高等级
				this.removeBuff(targetObj, targetType, typeBuffInfo.getBuffPojo().getBuffId());
				buff = this.addBuffInner(targetObj, targetType, buffId);
			}
		} else if (hasBuffId && (typePojo.getCanAdd() == 1))// 已有buff且可叠加
		{
			buff = this.getTargetBuff(targetId, buffId);
			buff.setEndTime(buff.getEndTime() + buff.getBuffTypePojo().getWorkTime() * 1000);
		} else {
			buff = this.addBuffInner(targetObj, targetType, buffId);
		}
		
		// 清除buff列表
		for (int i = 0; i < typePojo.getClearBuffTypeList().length; i++) {
			int clearBuffId = typePojo.getClearBuffTypeList()[0][i];
			this.removeBuff(targetObj, targetType, clearBuffId);
		}
		
		return buff;
	}
	
	/**
	 * 内部接口 addBuff:(). <br/>
	 */
	private Buff addBuffInner(Object targetObj, int targetType, int buffId) {
		BuffPojo pojo = buffConfs.get(buffId);
		BuffTypePojo typePojo = buffTypeConfs.get(pojo.getBuffType());
		Buff buff = new Buff();
		
		buff.setTargetObj(targetObj);
		buff.setTargetType(targetType);
		buff.setBuffManange(this);
		buff.setBuffPojo(pojo);
		buff.setBuffTypePojo(typePojo);
		buff.setStartTime(System.currentTimeMillis());
		buff.setEndTime(buff.getStartTime() + typePojo.getWorkTime() * 1000);
		Vector<Buff> buffVec = this.getTargetBuffs(buff.getTargetId());
		buffVec.add(buff);
		
		BuffStatusUpdateResponse.Builder msg = BuffStatusUpdateResponse.newBuilder();
		BuffInfo.Builder info = BuffInfo.newBuilder();
		info.setBuffId(buffId);
		info.setLeftTime(buff.getBuffTypePojo().getWorkTime());
		msg.addNewBuffList(info);
		msg.setTargetId(buff.getTargetId());
		msg.setTargetType(PbAoiEntityType.valueOf(buff.getTargetType()));
		this.sendBuffStatusToClient(msg, targetObj, targetType);
		
		return buff;
	}
	
	/**
	 * 移除buff removeBuff:(). <br/>
	 */
	public void removeBuff(Object targetObj, int targetType, int buffId) {
		long targetId = 0;
		Attribute attr = null;
		if (targetType == PbAoiEntityType.Role_VALUE) {
			ServerPlayer sp = (ServerPlayer) targetObj;
			targetId = sp.getRole().getId();
			attr = sp.getAttribute();
			
		} else if (targetType == PbAoiEntityType.Monster_VALUE) {
			Monster monster = (Monster) targetObj;
			targetId = monster.getId();
			attr = monster.getAttribute();
		}
		
		Buff buff = this.getTargetBuff(targetId, buffId);
		if (buff == null)
			return;
		Vector<Buff> buffVec = this.getTargetBuffs(targetId);
		buffVec.remove(buff);
		
		// 作用属性置0
		int[][] workAttrType = buff.getBuffTypePojo().getWorkAttrValues();
		for(int i = 0; i < workAttrType.length; i += 2)
		{
			int workAttr = workAttrType[0][i];
			attr.addBuffAttribute(workAttr, 0);
		}
		
		TimerCallbackThread.removeTimerCallback(buff.getTimerId());
		
		BuffStatusUpdateResponse.Builder msg = BuffStatusUpdateResponse.newBuilder();
		BuffInfo.Builder info = BuffInfo.newBuilder();
		info.setBuffId(buffId);
		info.setLeftTime((int) (System.currentTimeMillis() - buff.getEndTime()));
		msg.addOutBuffList(info);
		msg.setTargetId(targetId);
		msg.setTargetType(PbAoiEntityType.valueOf(targetType));
		this.sendBuffStatusToClient(msg, targetObj, targetType);
	}
	
	/**
	 * 获取角色buff getRoleBuffs:(). <br/>
	 */
	public Vector<Buff> getTargetBuffs(long targetId) {
		if (this.targetBuffs.containsKey(targetId)) {
			return this.targetBuffs.get(targetId);
		} else {
			Vector<Buff> buffVec = new Vector<Buff>();
			this.targetBuffs.put(targetId, buffVec);
			return buffVec;
		}
	}
	
	/**
	 * 获取角色指定buff getRoleBuff:(). <br/>
	 */
	public Buff getTargetBuff(long targetId, int buffId) {
		Vector<Buff> buffVec = this.getTargetBuffs(targetId);
		for (java.util.Iterator<Buff> it = buffVec.iterator(); it.hasNext();) {
			Buff buff = it.next();
			if (buff.getBuffPojo().getBuffId() == buffId)
				return buff;
		}
		return null;
	}
	
	/**
	 * 检查是否具有buffId checkRoleHasBuffId:(). <br/>
	 */
	public boolean checkRoleHasBuffId(long roleId, int buffId) {
		Vector<Buff> buffVec = this.getTargetBuffs(roleId);
		for (java.util.Iterator<Buff> it = buffVec.iterator(); it.hasNext();) {
			Buff buff = it.next();
			if (buff.getBuffPojo().getBuffId() == buffId)
				return true;
		}
		return false;
	}
	
	/**
	 * 角色是否对buffId免疫 checkIsImmuneBuff:(). <br/>
	 */
	public boolean checkTargetIsImmune(long roleId, int buffId) {
		Vector<Buff> buffVec = this.getTargetBuffs(roleId);
		for (java.util.Iterator<Buff> it = buffVec.iterator(); it.hasNext();) {
			Buff buff = it.next();
			int[][] immuneList = buff.getBuffTypePojo().getImmuneBuffTypeList();
			for (int i = 0; i < immuneList.length; i++) {
				if (immuneList[0][i] == buffId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 检查角色是否具有buffTypeId checkRoleHasBuffTypeId:(). <br/>
	 */
	public Buff checkTargetHasBuffTypeId(long roleId, int buffTypeId) {
		Vector<Buff> buffVec = this.getTargetBuffs(roleId);
		for (java.util.Iterator<Buff> it = buffVec.iterator(); it.hasNext();) {
			Buff buff = it.next();
			if (buff.getBuffTypePojo().getBuffTypeId() == buffTypeId)
				return buff;
		}
		return null;
	}
	
	/**
	 * 保存buff信息到db saveBuffToDb:(). <br/>
	 */
	public void saveBuffToDb(ServerPlayer sp) {
		long roleId = sp.getRole().getId();
		Vector<Buff> buffVec = this.getTargetBuffs(roleId);
		if (buffVec.size() <= 0)
			return;
				
		List<BuffData> list = dbDao.findByProperty(BuffData.class, "roleId", roleId);
		if (list.size() == 0) {
			BuffData data = new BuffData();
			data.setId(ServerUUID.getUUID());
			data.setRoleId(roleId);
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < buffVec.size(); i++) {
				Buff buff = buffVec.get(i);
				str.append(buff.getBuffPojo().getBuffId() + "," + buff.getEndTime() + ";");
			}
			data.setBuffInfos(str.toString());
			dbDao.save(data);
		} else {
			BuffData data = list.get(0);
			StringBuffer str = new StringBuffer();
			for (int i = 0; i < buffVec.size(); i++) {
				Buff buff = buffVec.get(i);
				str.append(buff.getBuffPojo().getBuffId() + "," + buff.getEndTime() + ";");
			}
			
			data.setBuffInfos(str.toString());
			dbDao.updateFinal(data);
		}
		
		// 停止所有buff
		for (int i = 0; i < buffVec.size(); i++) {
			Buff buff = buffVec.get(i);
			this.removeBuff(sp, PbAoiEntityType.Role_VALUE, buff.getBuffPojo().getBuffId());
		}
	}
	
	/**
	 * 发送buff状态到客户端 sendBuffStatusToClient:(). <br/>
	 */
	public void sendBuffStatusToClient(BuffStatusUpdateResponse.Builder builder, Object targetObj, int targetType) {
		if (targetType == PbAoiEntityType.Role_VALUE) {
			ServerPlayer sp = (ServerPlayer) targetObj;
			MessageSend.sendToGate(builder.build(), sp);
			
		}
		
		// 广播
		AbsMapObject map = (AbsMapObject) targetObj;
		for (Entry<Long, IMapObject> obj : map.getViewMap().entrySet()) {
			IMapObject mb = obj.getValue();
			if (mb.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				mb.getMapObjectMessage().buffStatus(builder);
			}
		}
	}
	
	/**
	 * 发送buff动作到客户端 sendBuffActionToClient:(). <br/>
	 */
	public void sendBuffActionToClient(BuffActionUpdateResponse.Builder builder, Object targetObj, int targetType) {
		if (targetType == PbAoiEntityType.Role_VALUE) {
			ServerPlayer sp = (ServerPlayer) targetObj;
			MessageSend.sendToGate(builder.build(), sp);
		}
		
		// 广播
		AbsMapObject map = (AbsMapObject) targetObj;
		for (Entry<Long, IMapObject> obj : map.getViewMap().entrySet()) {
			IMapObject mb = obj.getValue();
			if (mb.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER) {
				mb.getMapObjectMessage().buffAction(builder);
			}
		}
	}
}
