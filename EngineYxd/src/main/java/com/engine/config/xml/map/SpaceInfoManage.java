package com.engine.config.xml.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.engine.entityobj.Position3D;
import com.engine.res.XmlPath;
import com.lib.utils.XmlUtils;
import com.lx.nserver.model.FuBenTemplateModel;

/**
 * ClassName:SpaceInfoManage <br/>
 * Function: (空间信息读取类). <br/>
 * Date: 2015-7-14 下午3:19:28 <br/>
 * 地图的配置信息xml格式
 * 
 * @author jack
 * @version
 * @see
 */
@Component
public class SpaceInfoManage {
	
	@Autowired
	private FuBenTemplateModel fubenTemplateModel;
	
	/** 空间列表 **/
	public HashMap<Integer, SpaceInfo> spaceList;
	
	/** 日志 **/
	private static Log log = LogFactory.getLog(SpaceInfoManage.class);
	
	/**
	 * init:(加载地图数据). <br/>
	 */
	public void init() {
		log.info("========初始化地图=======地图路径是" + XmlPath.MAP_PATH);
		spaceList = new HashMap<Integer, SpaceInfo>();
		// 遍历文件夹
		ArrayList<String> fileList = getFileList(XmlPath.MAP_PATH);
		for (Iterator<String> it = fileList.iterator(); it.hasNext();) {
			String filePath = it.next();
			loadMap(filePath);
			// log.info("========初始化" + XmlPath.MAP_PATH + filePath + "=======");
		}
		log.info("========初始化地图完成=======");
	}
	
	/**
	 * 获取目录下所有文件(不包括子目录) getFileList:(). <br/>
	 */
	private ArrayList<String> getFileList(String path) {
		ArrayList<String> fileList = new ArrayList<String>();
		
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				log.error("Dont support sub directory!!! path:" + files[i].getAbsolutePath());
				continue;
			} else {
				fileList.add(files[i].getName());
			}
		}
		return fileList;
	}
	
	/**
	 * 加载地图数据 loadMap:(). <br/>
	 */
	private void loadMap(String path) {
		Document doc = null;
		try {
			SpaceInfo spaceInfo = new SpaceInfo();
			doc = XmlUtils.readAsDocument(XmlPath.MAP_PATH + path);
			log.error("地图加载" + XmlPath.MAP_PATH + path);
			Element root = doc.getRootElement();
			// 空间信息
			loadSpaceInfo(root, spaceInfo);
			loadNpcs(root, spaceInfo);
			loadMonsters(root, spaceInfo);
			loadGates(root, spaceInfo);
			loadBirthPlace(root, spaceInfo);
			loadBlockInfo(root, spaceInfo);
			
			spaceList.put(spaceInfo.mapUid, spaceInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取空间信息 loadSpaceInfo:(). <br/>
	 */
	private void loadSpaceInfo(Element root, SpaceInfo spaceInfo) {
		Element spaceEle = root.element("space");
		spaceInfo.mapUid = XmlUtils.getAttributeAsInt(spaceEle, "mapUid");
		spaceInfo.totalLength = XmlUtils.getAttributeAsInt(spaceEle, "length");
		spaceInfo.totalWidth = XmlUtils.getAttributeAsInt(spaceEle, "width");
		spaceInfo.fubenPojo = fubenTemplateModel.get(spaceInfo.mapUid);
		log.info("fuben. name = " + spaceInfo.fubenPojo.getName());
	}
	
	/**
	 * 读取npc数据 loadNpcs:(). <br/>
	 */
	private void loadNpcs(Element root, SpaceInfo spaceInfo) {
		@SuppressWarnings("unchecked")
		List<Element> eles = root.elements("npc");
		for (Element ele : eles) {
			NpcInfo npcInfo = new NpcInfo();
			npcInfo.tid = XmlUtils.getAttributeAsInt(ele, "tid");
			npcInfo.x = XmlUtils.getAttributeAsFloat(ele, "x");
			npcInfo.y = XmlUtils.getAttributeAsFloat(ele, "y");
			npcInfo.z = XmlUtils.getAttributeAsFloat(ele, "z");
			npcInfo.face = XmlUtils.getAttributeAsFloat(ele, "face");
			npcInfo.name = XmlUtils.getAttributeAsString(ele, "name");
			spaceInfo.npcList.add(npcInfo);
		}
	}
	
	/**
	 * 读取怪物数据 loadMonsters:(). <br/>
	 */
	private void loadMonsters(Element root, SpaceInfo spaceInfo) {
		@SuppressWarnings("unchecked")
		List<Element> eles = root.elements("monsterAgent");
		for (Element ele : eles) {
			MonsterGroupInfo monsterInfo = new MonsterGroupInfo();
			monsterInfo.x = XmlUtils.getAttributeAsFloat(ele, "x");
			monsterInfo.y = XmlUtils.getAttributeAsFloat(ele, "y");
			monsterInfo.z = XmlUtils.getAttributeAsFloat(ele, "z");
			monsterInfo.count = XmlUtils.getAttributeAsInt(ele, "count");
			monsterInfo.isActive = XmlUtils.getAttributeAsInt(ele, "active");
			
			String monsterList = XmlUtils.getAttributeAsString(ele, "monsterGroupId");
			for (String monster : monsterList.split(";")) {
				monsterInfo.monsterList.add(Integer.parseInt(monster));
			}
			spaceInfo.monsterList.add(monsterInfo);
		}
	}
	
	/**
	 * 读取传送门数据 loadGates:(). <br/>
	 */
	@SuppressWarnings("unchecked")
	private void loadGates(Element root, SpaceInfo spaceInfo) {
		List<Element> eles = root.elements("gate");
		for (Element ele : eles) {
			GateInfo gateInfo = new GateInfo();
			gateInfo.tid = XmlUtils.getAttributeAsInt(ele, "tid");
			gateInfo.x = XmlUtils.getAttributeAsFloat(ele, "x");
			gateInfo.y = XmlUtils.getAttributeAsFloat(ele, "y");
			gateInfo.z = XmlUtils.getAttributeAsFloat(ele, "z");
			gateInfo.type = XmlUtils.getAttributeAsInt(ele, "type");
			if (gateInfo.type == GateInfo.GATE_TYPE_WORLD) {
				gateInfo.toTargetId = XmlUtils.getAttributeAsInt(ele, "toTargetId");
				
				float toX = XmlUtils.getAttributeAsFloat(ele, "toTargetX");
				float toY = XmlUtils.getAttributeAsFloat(ele, "toTargetY");
				float toZ = XmlUtils.getAttributeAsFloat(ele, "toTargetZ");
				gateInfo.openMonsterGroupId = XmlUtils.getAttributeAsInt(ele, "openMonsterGroupId");
				Position3D pos = new Position3D(toX, toY, toZ);
				gateInfo.toTargetPos = pos;
			} else if (gateInfo.type == GateInfo.GATE_TYPE_FUBEN) {
				String mapUidStr = XmlUtils.getAttributeAsString(ele, "mapUids");
				String[] piceMap = mapUidStr.split(";");
				ArrayList<Integer> mapUidList = new ArrayList<Integer>();
				for (int i = 0; i < piceMap.length; i++) {
					String mapUid = piceMap[i];
					mapUidList.add(Integer.parseInt(mapUid));
				}
				gateInfo.fuBenMapUid = mapUidList;
			}
			spaceInfo.gateList.put(gateInfo.tid, gateInfo);
		}
	}
	
	/**
	 * 加载阻挡信息 loadBlockInfo:(). <br/>
	 */
	private void loadBlockInfo(Element root, SpaceInfo spaceInfo) {
		Element blockEle = root.element("block");
		if (blockEle == null)
			return;
		
		int totalLength = XmlUtils.getAttributeAsInt(blockEle, "totalLength");
		int totalWidth = XmlUtils.getAttributeAsInt(blockEle, "totalWidth");
		int mapCellLength = XmlUtils.getAttributeAsInt(blockEle, "mapCellLength");
		int mapCellWidth = XmlUtils.getAttributeAsInt(blockEle, "mapCellWidth");
		spaceInfo.initBlock(totalWidth, totalLength, mapCellWidth, mapCellLength);
		
		String blockStr = XmlUtils.getAttributeAsString(blockEle, "blockInfo");
		String[] piceBlocks = blockStr.split(";");
		for (int i = 0; i < piceBlocks.length; i++) {
			String block = piceBlocks[i];
			String[] info = block.split(",");
			int row = Integer.parseInt(info[0]);
			int col = Integer.parseInt(info[1]);
			int type = Integer.parseInt(info[2]);
			float x = Float.parseFloat(info[3]);
			float y = Float.parseFloat(info[4]);
			float z = Float.parseFloat(info[5]);
			BlockData data = new BlockData(row, col, type, y);
			spaceInfo.blocks[row][col] = data;
		}
	}
	
	/**
	 * 加载出生点 loadBirthPlace:(). <br/>
	 */
	private void loadBirthPlace(Element root, SpaceInfo spaceInfo) {
		Element birthEle = root.element("birthplace");
		if (birthEle == null)
			return;
		spaceInfo.birthPlace.setX(XmlUtils.getAttributeAsFloat(birthEle, "x"));
		spaceInfo.birthPlace.setY(XmlUtils.getAttributeAsFloat(birthEle, "y"));
		spaceInfo.birthPlace.setZ(XmlUtils.getAttributeAsFloat(birthEle, "z"));
	}
	
	/**
	 * 获取空间模板数据 getSpaceInfo:(). <br/>
	 */
	public SpaceInfo getSpaceInfo(int tid) {
		if (!isValidSpaceInfo(tid))
			return null;
		return spaceList.get(tid);
	}
	
	/**
	 * tid是否合法 isValidSpaceInfo:(). <br/>
	 */
	public boolean isValidSpaceInfo(int tid) {
		if (!spaceList.containsKey(tid))
			return false;
		return true;
	}
	
	/***
	 * 是否可行走 canWalk:(). <br/>
	 */
	public boolean canWalk(int tid, Position3D pos) {
		SpaceInfo spaceInfo = this.getSpaceInfo(tid);
		if (spaceInfo == null)
			return false;
		return spaceInfo.isCanWalk(pos);
	}
}
