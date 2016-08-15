package com.lx.game.res.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lx.game.item.BagConfigLoad;
import com.lx.game.item.resConfig.EquipQuality;
import com.lx.game.item.resConfig.MainAttributeGroup;
import com.lx.game.item.resConfig.Socket;
import com.lx.nserver.model.EquipQualityMode;
import com.lx.nserver.model.EquipmentConfigMode;
import com.lx.nserver.model.EquipmentLevelGrouthMode;
import com.lx.nserver.model.EquipmentPropertyModel;
import com.lx.nserver.model.FunctionMode;
import com.lx.nserver.model.MainattributeMode;
import com.lx.nserver.model.PropertyModel;
import com.lx.nserver.model.SocketMode;
import com.lx.nserver.txt.EquipQualityPojo;
import com.lx.nserver.txt.EquipmentLevelGrouthPojo;
import com.lx.nserver.txt.EquipmentPojo;
import com.lx.nserver.txt.FunctionPojo;
import com.lx.nserver.txt.MainattributePojo;
import com.lx.nserver.txt.PropertyPojo;
import com.lx.nserver.txt.SocketPojo;

/**
 * ClassName:ItemConfigLoad <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-6-30 上午9:34:50 <br/>
 * 
 * @author yxd
 * @version
 * @see
 */
@Component
public class ItemConfigLoad {
	
	@Autowired
	PropertyModel propertyModel;
	@Autowired
	EquipmentPropertyModel equipmentPropertyModel;
	@Autowired
	FunctionMode functionMode;
	@Autowired
	SocketMode socketMode;
	@Autowired
	EquipmentConfigMode equipmentConfigMode;
	@Autowired
	EquipQualityMode equipQualityMode;
	@Autowired
	MainattributeMode mainattributeMode;
	@Autowired
	EquipmentLevelGrouthMode equipmentLevelGrouthMode;
	@Autowired
	BagConfigLoad bagConfigLoad;
	
	CopyOnWriteArrayList<PropertyPojo> propertyPojoAL;
	CopyOnWriteArrayList<EquipmentPojo> equipmentPojoAL;
	// 根据分类加载物品类型
	static ConcurrentHashMap<Integer, List<PropertyPojoGame>> propertyPojoHM;
	
	/**
	 * 键值对缓存道具类型
	 */
	static HashMap<Integer, Property> propertyHashMap;
	
	/**
	 * 使用功能配置
	 */
	static HashMap<Integer, FunctionPojo> functionHashMap;
	
	/**
	 * 孔配置
	 */
	static HashMap<String, Socket> socketHashMap;
	
	/**
	 * 装备质量配置
	 */
	static ArrayList<EquipQuality> equipQualityAL;
	
	/**
	 * 加载主属性
	 */
	static HashMap<Integer, MainAttributeGroup> mainattributeHashMap;
	
	/**
	 * 单例
	 */
	public ItemConfigLoad() {
		propertyPojoAL = new CopyOnWriteArrayList<PropertyPojo>();
		equipmentPojoAL = new CopyOnWriteArrayList<EquipmentPojo>();
		propertyHashMap = new HashMap<Integer, Property>();
		functionHashMap = new HashMap<Integer, FunctionPojo>();
		socketHashMap = new HashMap<String, Socket>();
		equipQualityAL = new ArrayList<EquipQuality>();
		mainattributeHashMap = new HashMap<Integer, MainAttributeGroup>();
		propertyPojoHM = new ConcurrentHashMap<Integer, List<PropertyPojoGame>>();
	}
	
	/**
	 * 加载孔列表 loadSocketData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void loadSocketData() {
		ArrayList<SocketPojo> socketNoAL = new ArrayList<>();
		ArrayList<SocketPojo> socket0AL = new ArrayList<>();
		ArrayList<SocketPojo> socket1AL = new ArrayList<>();
		for (int i = 0; i < socketMode.getReslList().size(); i++) {
			SocketPojo socket = socketMode.getReslList().get(i);
			if (socket != null) {
				if (socket.getSocketType().equals("nosocket")) {
					socketNoAL.add(socket);
				} else if (socket.getSocketType().equals("socket0")) {
					socket0AL.add(socket);
				} else if (socket.getSocketType().equals("socket1")) {
					socket1AL.add(socket);
				}
			}
		}
		
		Socket Socketno = new Socket(socketNoAL);
		Socket Socket0 = new Socket(socket0AL);
		Socket Socket1 = new Socket(socket1AL);
		
		socketHashMap.put(Socketno.getId(), Socketno);
		socketHashMap.put(Socket0.getId(), Socket0);
		socketHashMap.put(Socket1.getId(), Socket1);
	}
	
	/**
	 * 加载装备配置表 loadEquipConfigData:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void loadEquipConfigData() {
		/* for (int i = 0; i < equipmentConfigMode.getReslList().size(); i++) { equipmentConfigPojo equipmentConfig= equipmentConfigMode.getReslList().get(i); if(equipmentConfig!=null){
		 * 
		 * } } */
		for (int i = 0; i < equipQualityMode.getReslList().size(); i++) {
			EquipQualityPojo equipQualityPo = equipQualityMode.getReslList().get(i);
			if (equipQualityPo != null) {
				EquipQuality equipQuality = new EquipQuality(equipQualityPo);
				equipQualityAL.add(equipQuality);
			}
		}
		
	}
	
	/**
	 * 加载主属性数据 loadMainAttributeDate:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void loadMainAttributeDate() {
		// 加载属性成长数据
		HashMap<Integer, EquipmentLevelGrouthPojo> equipmentLevelGrouthHashMap = new HashMap<Integer, EquipmentLevelGrouthPojo>();
		
		for (int i = 0; i < equipmentLevelGrouthMode.getReslList().size(); i++) {
			EquipmentLevelGrouthPojo equipmentLevelGrouthP = equipmentLevelGrouthMode.getReslList().get(i);
			if (equipmentLevelGrouthP != null) {
				equipmentLevelGrouthHashMap.put(equipmentLevelGrouthP.getId(), equipmentLevelGrouthP);
			}
		}
		
		// 加载主属性
		for (int i = 0; i < mainattributeMode.getReslList().size(); i++) {
			MainattributePojo mainattribute = mainattributeMode.getReslList().get(i);
			if (mainattribute != null) {
				String equipmentLevelGrouthStr = mainattribute.getEquipmentId();
				if (equipmentLevelGrouthStr != null && equipmentLevelGrouthStr.trim().length() > 0) {
					String[] equipmentLevelGrouthStrArr = equipmentLevelGrouthStr.split("\\*");
					if (equipmentLevelGrouthStrArr != null && equipmentLevelGrouthStrArr.length > 0) {
						ArrayList<EquipmentLevelGrouthPojo> equipLGal = new ArrayList<EquipmentLevelGrouthPojo>();
						for (int j = 0; j < equipmentLevelGrouthStrArr.length; j++) {
							EquipmentLevelGrouthPojo equipmentLevelGrouthPojo = equipmentLevelGrouthHashMap.get(Integer.parseInt(equipmentLevelGrouthStrArr[j]));
							if (equipmentLevelGrouthPojo != null) {
								equipLGal.add(equipmentLevelGrouthPojo);
							}
						}
						MainAttributeGroup mainAttributeGroup = new MainAttributeGroup(equipLGal);
						mainattributeHashMap.put(mainattribute.getId(), mainAttributeGroup);
					}
					
				}
			}
		}
	}
	
	/**
	 * //加载分类材料物品 loadPropertyByType:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void loadPropertyByType() {
		List<PropertyPojoGame> propertyPojoList;
		for (int i = 0; i < propertyModel.getReslList().size(); i++) {
			PropertyPojo propertyPojo = propertyModel.getReslList().get(i);
			if (propertyPojo != null) {
				if (propertyPojoHM.containsKey(propertyPojo.getItemType())) {// 如果含有
					propertyPojoHM.get(propertyPojo.getItemType()).add(new PropertyPojoGame(propertyPojo));
				} else {
					propertyPojoList = new ArrayList<PropertyPojoGame>();
					propertyPojoList.add(new PropertyPojoGame(propertyPojo));
					propertyPojoHM.put(propertyPojo.getItemType(), propertyPojoList);
				}
			}
		}
	}
	
	/**
	 * // * 加载物品数据 load:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 */
	public void load() {
		
		// 加载普通道具配置
		propertyPojoAL = (CopyOnWriteArrayList<PropertyPojo>) propertyModel.getReslList();
		for (int i = 0; i < propertyPojoAL.size(); i++) {
			PropertyPojo property = propertyPojoAL.get(i);
			PropertyPojoGame propertyPojoGame = new PropertyPojoGame(property);
			propertyHashMap.put(propertyPojoGame.getId(), propertyPojoGame);
			
		}
		
		// 加载装备配置
		equipmentPojoAL = (CopyOnWriteArrayList<EquipmentPojo>) equipmentPropertyModel.getReslList();
		for (int i = 0; i < equipmentPojoAL.size(); i++) {
			EquipmentPojo equipmentProperty = equipmentPojoAL.get(i);
			EquipmentPojoGame equipmentGame = new EquipmentPojoGame(equipmentProperty);
			propertyHashMap.put(equipmentGame.getId(), equipmentGame);
		}
		// 加载功能使用配置表
		for (int i = 0; i < functionMode.getReslList().size(); i++) {
			FunctionPojo functionPojo = functionMode.getReslList().get(i);
			if (functionPojo != null) {
				functionHashMap.put(functionPojo.getId(), functionPojo);
			}
		}
		
		// 加载孔位
		this.loadSocketData();
		// 加载装备质量
		this.loadEquipConfigData();
		// 加载装备主属性
		loadMainAttributeDate();
		// 初始化背包数据
		bagConfigLoad.load();
		// 加载分类材料物品
		loadPropertyByType();
	}
	
	/**
	 * 根据道具编号 获取道具原型 getPrototype:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @param propertyId
	 * @return
	 */
	public static Property getPrototype(int propertyId) {
		return propertyHashMap.get(propertyId);
	}
	
	/**
	 * Function 配置资源化 getFunctionHashMap:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public static HashMap<Integer, FunctionPojo> getFunctionHashMap() {
		return functionHashMap;
	}
	
	public void setFunctionHashMap(HashMap<Integer, FunctionPojo> functionHashMap) {
		this.functionHashMap = functionHashMap;
	}
	
	/**
	 * 获取孔配置 getSocketHashMap:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public static HashMap<String, Socket> getSocketHashMap() {
		return socketHashMap;
	}
	
	/**
	 * 获取装备质量数据 getEquipQualityAL:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public static ArrayList<EquipQuality> getEquipQualityAL() {
		return equipQualityAL;
	}
	
	/**
	 * 获取主属性 getMainattributeHashMap:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public static HashMap<Integer, MainAttributeGroup> getMainattributeHashMap() {
		return mainattributeHashMap;
	}
	
	/**
	 * 获取道具属性配置表 getPropertyHashMap:(). <br/>
	 * TODO().<br/>
	 * 
	 * @author yxd
	 * @return
	 */
	public static HashMap<Integer, Property> getPropertyHashMap() {
		return propertyHashMap;
	}
	
	public static ConcurrentHashMap<Integer, List<PropertyPojoGame>> getPropertyPojoHM() {
		return propertyPojoHM;
	}
	
}
