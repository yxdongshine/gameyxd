/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina;

import com.lx.server.executor.OrderedQueuePoolExecutor;
import com.lx.server.executor.TasksQueue;
import com.lx.server.mina.codec.InnerCodecFactoryImpl;
import com.lx.server.mina.codec.ProxySet;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.handler.INetApdapterLisener;
import com.lx.server.mina.handler.InnerHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class InnerNetClient
  implements IClient
{
  private IoConnector connector;
  protected InnerHandlerAdapter adapter;
  public static Map<String, ProxyXmlBean> proxyList;
  protected InnerCodecFactoryImpl codecFactory;
  public static Map<String, Integer> protocolMap;
  protected Log log = LogFactory.getLog(super.getClass());

  protected OrderedQueuePoolExecutor queuePool = new OrderedQueuePoolExecutor("ClientReceiveQueuePool", 5);

  public void addLisener(INetApdapterLisener net) {
    if (this.adapter != null)
      this.adapter.setProxyDispatch(net);
    else
      System.exit(1);
  }

  protected void initProxy(String fileName) {
    ProxySet.proxy_initialize(fileName);
    proxyList = ProxySet.getProxyList();
    protocolMap = ProxySet.getProtocolmap();
    this.adapter = new InnerHandlerAdapter(proxyList, this.queuePool);
  }

  protected void initialize(String ip, int port, String fileName) throws Exception {
    try {
      initProxy(fileName);
      this.connector = new NioSocketConnector();
      this.codecFactory = new InnerCodecFactoryImpl(protocolMap);
      this.connector.setConnectTimeoutMillis(0L);
      this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(this.codecFactory));

      this.connector.setHandler(this.adapter);
    }
    catch (RuntimeIoException e) {
      e.printStackTrace();
      throw new Exception(e.toString());
    }
  }

  public IoSession connect(String ip, int port)
    throws Exception
  {
    InetSocketAddress _socketAddress = new InetSocketAddress(ip, port);

    IoSession session = connect(_socketAddress);
    return session;
  }

  public IoSession connect(InetSocketAddress socketAddress) throws Exception {
    ConnectFuture connectf = this.connector.connect(socketAddress);
    IoSession session = connectf.awaitUninterruptibly().getSession();
    return session;
  }

  public void close() {
    this.connector.dispose();
    if (this.queuePool != null)
      this.queuePool.shutdown();
  }

  public TasksQueue removeQueuePoolFromMap(Long key)
  {
    if (this.queuePool != null) {
      return this.queuePool.removeFromPool(key);
    }
    return null;
  }
}