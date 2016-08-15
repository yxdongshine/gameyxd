package com.engine.service;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;

import com.engine.config.xml.ConnctorInfo;
import com.engine.config.xml.ConnctorInfo.Conn;
import com.engine.config.xml.ServerInfo;
import com.engine.config.xml.ServerInfoManage;
import com.engine.utils.log.LogUtils;
import com.lib.utils.TheadUtils;
import com.lx.server.mina.session.IConnect;

/**
 * ClassName: AbsConnecorProxy <br/>
 * Function: TODO (). <br/>
 * Reason: TODO (). <br/>
 * date: 2015-7-1 上午9:47:29 <br/>
 * 根据配置连接相应的服务器
 * 
 * @author lyh
 * @version
 */
public abstract class AbsConnecorProxy implements Runnable, IConnectorProxy {
	
	protected Log log = LogUtils.getLog(this.getClass());
	/** 自检线程开关 */
	protected boolean active = false;
	
	/** 需要始终维持连接的列表 */
	protected List<IConnect> connectList = new ArrayList<IConnect>();
	
	/** 解析各个连接地址 */
	public void parseConnects(ConnctorInfo connInfo, int sid) {
		for (Conn conn : connInfo.conns) {
			@SuppressWarnings("unused")
			IConnect ic = null;
			if (conn.toId > 0) {
				// 使用id连接
				ServerInfo sInfo = ServerInfoManage.getSConfig(conn.toId);
				if (sInfo == null) {
					log.error("null serverConfig  id=" + conn.toId);
				} else {
					ic = genarateConnect(sInfo, conn, sid);
				}
			} else if (conn.toGroup != null) {
				// 使用组名连接
				ServerInfo[] sInfos = ServerInfoManage.getSConfigByGroup(conn.toGroup);
				if (sInfos.length == 0) {
					log.error("null serverConfig  group=" + conn.toGroup);
				} else {
					for (ServerInfo sInfo : sInfos) {
						ic = genarateConnect(sInfo, conn, sid);
					}
				}
			}
		}
	}
	
	public InetSocketAddress getConnectByType(int type) {
		for (IConnect conn : connectList) {
			if (conn.getRemoteSType() == type) {
				return conn.getRemouteAdress();
			}
		}
		return null;
	}
	
	/**
	 * 根据配置生产一个连接
	 * 
	 * @param sid
	 */
	private IConnect genarateConnect(ServerInfo remoteS, Conn conn, int sid) {
		IConnect con = createConnect();
		int port = remoteS.port;
		InetSocketAddress saddr = null;
		if (conn.connectMina) {
			saddr = new InetSocketAddress(remoteS.host, remoteS.minaPort);
		} else {
			saddr = new InetSocketAddress(remoteS.host, port);
		}
		con.setRemoteSType(remoteS.type);
		con.setRemouteAdress(saddr);
		con.setRemoteSTypeid(remoteS.serverTypeId);
		con.setRemoteGroup(remoteS.group);
		con.setRemoteName(remoteS.name);
		log.info("生成新服务器连接--->" + con.getRemouteAdress() + " " + con.getRemoteName() + " " + con.getRemoteSTypeid());
		connectList.add(con);
		return con;
	}
	
	public abstract IConnect createConnect();
	
	/** 开启自检线程 */
	public void start() {
		this.active = true;
		new Thread(this).start();
	}
	
	/** 关闭自检线程 */
	public void stop() {
		this.active = false;
	}
	
	public void run() {
		while (active) {
			doConnect();
			TheadUtils.sleep(9000);
		}
	}
	
	protected abstract void doConnect();
	
}
