/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina;

import com.lx.server.executor.OrderedQueuePoolExecutor;
import com.lx.server.executor.TasksQueue;
import com.lx.server.mina.codec.InnerCodecFactoryImpl;
import com.lx.server.mina.codec.ProxySet;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.handler.INetApdapterLisener;
import com.lx.server.mina.handler.InnerHandlerAdapter;
import com.lx.server.mina.handler.MinaHandlerAdapter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class InnerNetSever
{
  protected Log log = LogFactory.getLog(super.getClass());
  protected InnerHandlerAdapter adapter;
  private boolean singleThread = false;
  public static Map<String, ProxyXmlBean> proxyList;
  public static Map<String, Integer> protocolMap;
  protected InnerCodecFactoryImpl codcFactory;
  private NioSocketAcceptor acceptor;
  protected OrderedQueuePoolExecutor queuePool = new OrderedQueuePoolExecutor("ServerReceiveQueuePool", 10);

  public MinaHandlerAdapter getAdapter()
  {
    return this.adapter;
  }

  public InnerCodecFactoryImpl getCodcFactory() {
    return this.codcFactory;
  }

  public void addLisener(INetApdapterLisener net) {
    if (this.adapter != null) {
      this.adapter.setProxyDispatch(net);
    } else {
      this.log.error("addLisener is not NetApdapterLisener");
      System.exit(1);
    }
  }

  public void setSingleThread(boolean singleThread) {
    this.singleThread = singleThread;
  }

  protected void initProxy(String fileName) {
    this.log.error("======协议初始化======");
    ProxySet.proxy_initialize(fileName);
    proxyList = ProxySet.getProxyList();
    protocolMap = ProxySet.getProtocolmap();
    this.adapter = new InnerHandlerAdapter(proxyList, this.queuePool);
    this.log.error("======协议初始化成功======");
  }

  protected void initialize(int port, String fileName) throws Exception {
    try {
      if (!(bindPort(port))) {
        throw new Exception("======监听端口:" + port + "被占用  ======");
      }

      initProxy(fileName);

      NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);

      DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

      this.codcFactory = new InnerCodecFactoryImpl(protocolMap);
      chain.addLast("codec", new ProtocolCodecFilter(this.codcFactory));

      acceptor.setHandler(this.adapter);
      acceptor.getSessionConfig().setSoLinger(0);

      acceptor.getSessionConfig().setReuseAddress(true);
      acceptor.setReuseAddress(true);
      acceptor.getSessionConfig().setReceiveBufferSize(8192);
      acceptor.getSessionConfig().setSendBufferSize(32768);
      acceptor.getSessionConfig().setReadBufferSize(8192);
      acceptor.getSessionConfig().setWriteTimeout(20000);
      acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60000);

      acceptor.getSessionConfig().setTcpNoDelay(true);

      acceptor.bind(new InetSocketAddress(port));
      IoBuffer.setUseDirectBuffer(false);
      IoBuffer.setAllocator(new SimpleBufferAllocator());
      this.log.error("======监听端口:" + port + "成功======");
    } catch (IOException e) {
      this.log.error("======网络创建失败======");
      this.log.error(e.toString());
      throw new Exception(e.toString());
    }
  }

  private static boolean bindPort(int port) {
    try {
      Socket s = new Socket();
      s.bind(new InetSocketAddress(port));
      s.close();
      return true;
    } catch (Exception localException) {
    }
    return false;
  }

  public TasksQueue removeQueuePoolFromMap(Long key)
  {
    if (this.queuePool != null) {
      return this.queuePool.removeFromPool(key);
    }
    return null;
  }

  protected void stop() {
    if (this.acceptor != null) {
      this.acceptor.unbind();
      this.acceptor.dispose();
      this.acceptor = null;
      if (this.queuePool != null)
        this.queuePool.shutdown();
    }
  }

  static class NamedThreadFactory
    implements ThreadFactory
  {
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public NamedThreadFactory(ThreadGroup group, String name)
    {
      this.group = group;
      this.namePrefix = group.getName() + ":" + name;
    }

    public Thread newThread(Runnable r) {
      Thread thread = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
      return thread;
    }
  }
}