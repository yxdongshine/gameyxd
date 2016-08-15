package com.engine.service;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.lib.utils.ServerRandomUtils;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName:ConnectorManage <br/>
 * Function: TODO (服务器客户端连接管理 器). <br/>
 * Reason: TODO (). <br/>
 * Date: 2015-7-1 上午11:22:45 <br/>
 * 
 * @author lyh
 * @version
 * @see
 */

/**
 * ClassName: ConnectorManage <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-8-7 下午4:13:32 <br/>
 * 
 * @author yxd
 * @version
 */
public class ConnectorManage {
	
	/** 连接管理器,sessionid,IConnect **/
	private final static Map<Long, IConnect> connectMap = new ConcurrentHashMap<Long, IConnect>();
	
	/** 连接管理器,sessionid,IConnect **/
	private final static Map<Long, IConnect> minaConnectMap = new ConcurrentHashMap<Long, IConnect>();
	
	/** 服务器类型,同id的连接列表 **/
	private final static Map<Integer, List<IConnect>> listsConnectMap = new ConcurrentHashMap<Integer, List<IConnect>>();
	
	/** 服务器类型Id,同id的连接列表 **/
	private final static Map<Integer, List<IConnect>> listServerTypeConnectMap = new ConcurrentHashMap<Integer, List<IConnect>>();
	
	/**
	 * putConnect:(). <br/>
	 * TODO().<br/>
	 * 把连接加入连接管理 器
	 * 
	 * @author lyh
	 * @param con
	 */
	public static void putConnect(IConnect con) {
		connectMap.put(con.getId(), con);
	}
	
	public static Map<Long, IConnect> getMinaconnectmap() {
		return minaConnectMap;
	}
	
	/**
	 * getConnect:(). <br/>
	 * TODO().<br/>
	 * 获取连接
	 * 
	 * @author lyh
	 * @return
	 */
	public static IConnect getConnect(long sessionId) {
		return connectMap.get(sessionId);
	}
	
	/**
	 * removeConnect:(). <br/>
	 * TODO().<br/>
	 * 从连接管理器删除
	 * 
	 * @author lyh
	 * @param sessionId
	 * @return
	 */
	public static IConnect removeConnect(long sessionId) {
		return connectMap.remove(sessionId);
	}
	
	/**
	 * putMinaConnect:(). <br/>
	 * TODO().<br/>
	 * 把连接加入连接管理 器
	 * 
	 * @author lyh
	 * @param con
	 */
	public static void putMinaConnect(IConnect con) {
		minaConnectMap.put(con.getId(), con);
	}
	
	/**
	 * getMinaConnect:(). <br/>
	 * TODO().<br/>
	 * 获取连接
	 * 
	 * @author lyh
	 * @return
	 */
	public static IConnect getMinaConnect(long sessionId) {
		return minaConnectMap.get(sessionId);
	}
	
	/**
	 * removeMinaConnect:(). <br/>
	 * TODO().<br/>
	 * 从连接管理器删除
	 * 
	 * @author lyh
	 * @param sessionId
	 * @return
	 */
	public static IConnect removeMinaConnect(long sessionId) {
		return minaConnectMap.remove(sessionId);
	}
	
	/**
	 * putConnectToList:(). <br/>
	 * TODO().<br/>
	 * 加入到连接列表
	 * 
	 * @author lyh
	 * @param con
	 */
	public static void putConnectToList(IConnect con) {
		
		List<IConnect> cList = listsConnectMap.get(con.getRemoteSType());
		if (cList == null) {
			cList = new Vector<IConnect>();
			listsConnectMap.put(con.getRemoteSType(), cList);
		}
		if (hasConnectFromList(cList, con.getId()) == null) {
			cList.add(con);
		}
	}
	
	/**
	 * getConnectFromList:(). <br/>
	 * TODO().<br/>
	 * 获得连接从连接列表
	 * 
	 * @author lyh
	 * @param serverType
	 * @param sessionId
	 * @return
	 */
	public static IConnect getConnectFromList(int serverType, long sessionId) {
		List<IConnect> cList = listsConnectMap.get(serverType);
		if (cList != null) {
			IConnect con = hasConnectFromList(cList, sessionId);
			if (con != null) {
				return con;
			}
		}
		return null;
	}
	
	/**
	 * getConnectFromList:(). <br/>
	 * TODO().<br/>
	 * 服务类型得到 连接
	 * 
	 * @author lyh
	 * @param serverType
	 * @return
	 */
	public static IConnect getConnectFromList(int serverType) {
		List<IConnect> cList = listsConnectMap.get(serverType);
		if (cList != null) {
			try {
				IConnect con = null;
				if (cList.size() > 1) {
					con = cList.get(ServerRandomUtils.nextInt(cList.size()));
				} else if (cList.size() > 0) {
					con = cList.get(0);
				}
				
				if (con != null) {
					return con;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * hasConnectFromList:(). <br/>
	 * TODO().<br/>
	 * 连接是否存在,在容器里
	 * 
	 * @author lyh
	 * @param list
	 * @param con
	 * @return
	 */
	public static IConnect hasConnectFromList(List<IConnect> list, long sessionId) {
		for (IConnect c : list) {
			if (c.getId() == sessionId) {
				return c;
			}
		}
		return null;
	}
	
	/**
	 * removeConnectFromList:(). <br/>
	 * TODO().<br/>
	 * 删除连接从列表
	 * 
	 * @author lyh
	 * @param serverTypeId
	 * @param sessionId
	 * @return
	 */
	public static IConnect removeConnectFromList(int serverType, long sessionId) {
		
		List<IConnect> cList = listsConnectMap.get(serverType);
		if (cList != null) {
			IConnect con = hasConnectFromList(cList, sessionId);
			if (con != null) {
				cList.remove(con);
				if (cList.size() == 0) {
					listsConnectMap.remove(cList);
				}
				return con;
			}
		}
		return null;
	}
	
	/**
	 * putConnectToListTypeId:(). <br/>
	 * TODO().<br/>
	 * 加入到连接列表
	 * 
	 * @author lyh
	 * @param con
	 */
	public static void putConnectToListTypeId(IConnect con) {
		
		List<IConnect> cList = listServerTypeConnectMap.get(con.getRemoteSTypeid());
		if (cList == null) {
			cList = new Vector<IConnect>();
			listServerTypeConnectMap.put(con.getRemoteSTypeid(), cList);
		}
		if (hasConnectFromList(cList, con.getId()) == null) {
			cList.add(con);
		}
	}
	
	/**
	 * getConnectFromListTypeId:(). <br/>
	 * TODO().<br/>
	 * 获得连接从连接列表
	 * 
	 * @author lyh
	 * @param serverType
	 * @param sessionId
	 * @return
	 */
	public static IConnect getConnectFromListTypeId(int serverTypeId, long sessionId) {
		List<IConnect> cList = listServerTypeConnectMap.get(serverTypeId);
		if (cList != null) {
			IConnect con = hasConnectFromList(cList, sessionId);
			if (con != null) {
				return con;
			}
		}
		return null;
	}
	
	/**
	 * getConnectFromListTypeId:(). <br/>
	 * TODO().<br/>
	 * 服务类型得到 连接
	 * 
	 * @author lyh
	 * @param serverType
	 * @return
	 */
	public static IConnect getConnectFromListTypeId(int serverTypeId) {
		List<IConnect> cList = listServerTypeConnectMap.get(serverTypeId);
		if (cList != null) {
			try {
				IConnect con = null;
				if (cList.size() > 1) {
					
					con = cList.get(ServerRandomUtils.nextInt(cList.size()));
				} else if (cList.size() > 0) {
					con = cList.get(0);
				}
				
				if (con != null) {
					return con;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * removeConnectFromList:(). <br/>
	 * TODO().<br/>
	 * 删除连接从列表
	 * 
	 * @author lyh
	 * @param serverTypeId
	 * @param sessionId
	 * @return
	 */
	public static IConnect removeConnectFromListTypeId(int serverTypeTypId, long sessionId) {
		
		List<IConnect> cList = listServerTypeConnectMap.get(serverTypeTypId);
		if (cList != null) {
			IConnect con = hasConnectFromList(cList, sessionId);
			if (con != null) {
				cList.remove(con);
				if (cList.size() == 0) {
					listServerTypeConnectMap.remove(cList);
				}
				return con;
			}
		}
		return null;
	}
}
