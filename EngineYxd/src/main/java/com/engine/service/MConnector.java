package com.engine.service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.engine.config.xml.ConnctorInfo;
import com.engine.config.xml.ServerInfo;
import com.lib.utils.ServerType;
import com.loncent.protocol.inner.InnerToMessage.RegisterRequest;
import com.lx.server.mina.IClient;
import com.lx.server.mina.InnerNetClient;
import com.lx.server.mina.session.IConnect;
import com.lx.server.mina.session.MinaConnect;
import com.lx.server.mina.utils.NetConst;

/**
 * @author chen 基于mina的服务器间连接管理器,维持与服务端的连接
 * @preserve
 */
public class MConnector extends AbsConnecorProxy {
	/** mina连接管理器 */
	protected IClient innerConnector;
	
	/** 自检线程开关 */
	protected boolean active = false;
	
	private ServerInfo sInfo;
	
	// public List<IoSession> game_wolrd_connPool = new ArrayList<IoSession>();
	//
	// public int now_pool_index = 0;
	
	public void init(ServerInfo sc, IClient connector) {
		if (sc.connInfo == null) {
			log.info("no connector config sname=" + sc.name);
			return;
		}
		sInfo = sc;
		ConnctorInfo connInfo = sc.connInfo;
		// // mina connector初始化
		// NioSocketConnector minaConnector = new NioSocketConnector();
		// IoHandler handler = new MResponseHandler();
		// minaConnector.setHandler(handler);
		// // 解码，编码器
		// minaConnector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MCodecFactory()));
		// // 写超时
		// minaConnector.getSessionConfig().setWriteTimeout(1000 * 20);
		// minaConnector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 240);
		// minaConnector.getSessionConfig().setTcpNoDelay(true);
		this.innerConnector = connector;
		// 解析各个连接
		parseConnects(connInfo, 0);
		// if (connInfo.duplicateConnId != null) {
		// for (int dupId : connInfo.duplicateConnId) {
		// parseConnects(connInfo, dupId);
		// }
		// }
		//
		// 开启自检线程
		start();
		// if (App.isGame()) {
		// for (int i = 0; i < 8; i++) {
		// InetSocketAddress addr = getConnectByType(ServerEnum.world.value());
		// ConnectFuture fu = minaConnector.connect(addr);
		// Log.system.fatal("创建与世界服的网络连接池");
		// fu.awaitUninterruptibly();
		// game_wolrd_connPool.add(fu.getSession());
		// Log.system.fatal("网络连接池 成功" + fu.getSession() + "数量 " + game_wolrd_connPool.size());
		// }
		//
		// }
	}
	
	// public void poolWorldsend(ByteBuffer data) {
	//
	// IoSession sess = game_wolrd_connPool.get(now_pool_index);
	// try {
	// sess.write(data);
	// } catch (Exception e) {
	// Log.system.error(e.getMessage(), e);
	// e.printStackTrace();
	// }
	// now_pool_index++;
	// if (now_pool_index >= game_wolrd_connPool.size()) {
	// now_pool_index = 0;
	// }
	// }
	
	protected void doConnect() {
		synchronized (connectList) {
			for (int i = 0; i < connectList.size(); i++) {
				final IConnect connect = connectList.get(i);
				if (connect.isConnect()) {
					continue;
				}
				
				// Log.system.fatal("<" + connect.getRemoteName()
				// + ">连接未建立,尝试连接--->" + connect.getRemouteAdress()
				// + " id=" + connect.getRemoteSid() + " sid="
				// + connect.getSId());
				InetSocketAddress addr = connect.getRemouteAdress();
				IoSession ioSession = null;
				try {
					ioSession = innerConnector.connect(addr);
				} catch (Exception e) {
					log.info(connect.getRemoteName() + ">连接未建立,尝试连接--->" + connect.getRemouteAdress() + " id=" + connect.getRemoteSTypeid());
				}
				if (ioSession == null) {
					continue;
				}
				connect.setId(ioSession.getId());
				
				connect.setConnected(ioSession.isConnected());
				ioSession.setAttribute(NetConst.NET_SESSION, connect);
				connect.setAttachment(ioSession);
				ConnectorManage.putConnect(connect);
				ConnectorManage.putConnectToList(connect);
				ConnectorManage.putConnectToListTypeId(connect);
				// 把自己的信息发送到服务器
				
				// optional int32 serverTypeId=1;//服务类型id
				// optional int32 serverType=2;//服务类型
				// optional string serverName=3;//服务器名称
				// optional string serverGroup=4;//服务器组
				// optional string serverHost=5;//服务器主机名(ip)
				// optional int32 serverPort=6;//服务器端口
				if (connect.getRemoteSType() != ServerType.LOGIN_SERVER && sInfo.type != ServerType.CLIENT) {
					RegisterRequest.Builder rrb = RegisterRequest.newBuilder();
					rrb.setServerTypeId(sInfo.serverTypeId);
					rrb.setServerType(sInfo.type);
					rrb.setServerName(sInfo.name);
					rrb.setServerGroup(sInfo.group);
					rrb.setServerPort(sInfo.port);
					rrb.setServerHost(sInfo.host);
					ioSession.write(rrb.build());
				}
				log.error("连接成功" + connect.getRemoteSTypeid() + addr.getHostName() + ":::" + addr.getPort());
				
			}
		}
		
	}
	
	public String getInfo() {
		StringBuffer sb = new StringBuffer();
		return sb.toString();
	}
	
	public IConnect createConnect() {
		return new MinaConnect();
	}
	
}
