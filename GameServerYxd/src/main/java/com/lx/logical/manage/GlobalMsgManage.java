
package com.lx.logical.manage;  

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.engine.entityobj.ServerPlayer;
import com.engine.entityobj.space.IMapObject;
import com.engine.utils.log.LogUtils;
import com.loncent.protocol.PublicData.AttributeData;
import com.loncent.protocol.PublicData.PbAoiEntityType;
import com.loncent.protocol.game.player.Role.UpdateAttrResponse;
import com.lx.game.send.MessageSend;

/** 
 * ClassName:GlobalMsgManage <br/> 
 * Function: TODO (全局消息封装类). <br/> 
 * Reason:   TODO (). <br/> 
 * Date:     2015-8-27 上午9:33:02 <br/> 
 * @author   lyh 
 * @version   
 * @see       
 */
@Component
public class GlobalMsgManage {
	private static Log log = LogUtils.getLog(GlobalMsgManage.class);
	/**
	 * createAttributeData:(). <br/>
	 * TODO().<br/>
	 * 创建协议属性数据
	 * 
	 * @author lyh
	 * @param attrType
	 * @param val
	 * @return
	 */
	public static AttributeData createAttributeData(int attrType, int val) {
		AttributeData data = AttributeData.newBuilder().setAttrType(attrType).setAttrVal(val).build();
		return data;
	}
	
	/** 
	 * sendUpdateAttrResponse:(). <br/> 
	 * TODO().<br/> 
	 * 发送属性包括怪物，角色宠物
	 * @author lyh 
	 * @param attributeDataList 
	 */  
	public static void sendUpdateAttrResponse(IMapObject obj,List<AttributeData> attributeDataList,ServerPlayer sp){
		if (obj == null || attributeDataList == null || attributeDataList.size() <= 0 || sp == null){
			log.error("没有发送属性:::");
			return;
		}
		UpdateAttrResponse.Builder attr = UpdateAttrResponse.newBuilder();
		attr.addAllData(attributeDataList);
		attr.setRoleId(obj.getId());
		attr.setType(PbAoiEntityType.valueOf(obj.getType()));
 		MessageSend.sendToGate(attr.build(), sp);
	}
	
	/** 
	 * sendAttrToAllViewObj:(). <br/> 
	 * TODO().<br/> 
	 * 通知其他人你更新了属性
	 * @author lyh 
	 * @param obj 被发送者
	 * @param attributeDataList
	 * @param sp
	 * @param includeMySelf 
	 */  
	public static void sendAttrToAllViewObj(IMapObject obj,List<AttributeData> attributeDataList,boolean includeMySelf){
		if (obj == null || attributeDataList == null || attributeDataList.size() <= 0){
			log.error("没有发送属性:::");
			return;
		}
		for (Map.Entry<Long, IMapObject> entry : obj.getPlayerViewMap().entrySet()){
			IMapObject mapObj = entry.getValue();
			//if (mapObj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER){
				sendUpdateAttrResponse(obj,attributeDataList,(ServerPlayer)mapObj);
			//}
			
		}
		
		//如果被发送者是角色也要发给自己
		if (obj.getType() == IMapObject.MAP_OBJECT_TYPE_PLAYER && includeMySelf){
			sendUpdateAttrResponse(obj,attributeDataList,(ServerPlayer)obj);
		}
		
		
	}
}
  