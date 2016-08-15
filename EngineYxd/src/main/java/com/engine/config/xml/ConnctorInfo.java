package com.engine.config.xml;

import java.util.List;

import org.dom4j.Element;

import com.lib.utils.XmlUtils;

/**
 * ClassName: ConnctorInfo <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-6-30 下午8:08:21 <br/>
 * 连接配置信息
 * 
 * @author lyh
 * @version
 */
public class ConnctorInfo {
	public Conn[] conns;
	
	/** * 依据xml初始化 */
	@SuppressWarnings("unchecked")
	public void init(Element elem) {
		List<Element> elems = elem.elements("conn");
		conns = new Conn[elems.size()];
		for (int i = 0; i < elems.size(); i++) {
			Element cElem = elems.get(i);
			Conn conn = new Conn();
			conn.init(cElem);
			conns[i] = conn;
		}
	}
	
	/** 基本连接信息 */
	public class Conn {
		
		/** 根据服务id去连接 */
		public int toId;
		
		/** 根据服务组名字去连接 */
		public String toGroup;
		
		/** 是否连接mina服务 **/
		public boolean connectMina = false;
		
		public void init(Element elem) {
			toId = XmlUtils.getAttributeAsInt(elem, "toId");
			toGroup = XmlUtils.getAttributeAsString(elem, "toGroup");
			connectMina = XmlUtils.getAttributeAsBool(elem, "minaCon");
		}
	}
	
}
