/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina;

import com.lx.server.mina.codec.ProtocolCodecFactoryImpl;
import com.lx.server.mina.codec.ProxySet;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.handler.INetApdapterLisener;
import com.lx.server.mina.handler.MinaHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaNetClient
  implements IClient
{
  private IoConnector connector;
  protected MinaHandlerAdapter adapter;
  public static Map<String, ProxyXmlBean> proxyList;
  protected ProtocolCodecFactoryImpl codecFactory;
  public static Map<String, Integer> protocolMap;
  protected Log log = LogFactory.getLog(super.getClass());
  private final ExecutorService FILTER_EXECUTOR = new OrderedThreadPoolExecutor(1, 1000);

  public void addLisener(INetApdapterLisener net)
  {
    if (this.adapter != null)
      this.adapter.setProxyDispatch(net);
    else
      System.exit(1);
  }

  protected void initProxy(String fileName) {
    ProxySet.proxy_initialize(fileName);
    proxyList = ProxySet.getProxyList();
    protocolMap = ProxySet.getProtocolmap();
    this.adapter = new MinaHandlerAdapter(proxyList);
  }

  protected void initialize(String ip, int port, String fileName) throws Exception {
    try {
      initProxy(fileName);
      this.connector = new NioSocketConnector();
      this.codecFactory = new ProtocolCodecFactoryImpl(protocolMap);
      this.connector.setConnectTimeoutMillis(0L);
      this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(this.codecFactory));
      this.connector.getFilterChain().addLast("threadpool", new ExecutorFilter(this.FILTER_EXECUTOR));
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

  public void close() {
    this.connector.dispose();
  }

  public IoSession connect(InetSocketAddress socketAddress)
    throws Exception
  {
    ConnectFuture connectf = this.connector.connect(socketAddress);
    IoSession session = connectf.awaitUninterruptibly().getSession();
    return session;
  }
}