/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina;

import com.lx.server.mina.codec.ProtocolCodecFactoryImpl;
import com.lx.server.mina.codec.ProxySet;
import com.lx.server.mina.codec.ProxyXmlBean;
import com.lx.server.mina.handler.INetApdapterLisener;
import com.lx.server.mina.handler.MinaHandlerAdapter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaNetSever
{
  protected Log log = LogFactory.getLog(super.getClass());
  protected MinaHandlerAdapter adapter;
  private boolean singleThread = false;
  public static Map<String, ProxyXmlBean> proxyList;
  public static Map<String, Integer> protocolMap;
  protected ProtocolCodecFactoryImpl codcFactory;
  private NioSocketAcceptor acceptor;
  private final ExecutorService FILTER_EXECUTOR = new OrderedThreadPoolExecutor(10, 1000);

  public MinaHandlerAdapter getAdapter() {
    return this.adapter;
  }

  public ProtocolCodecFactoryImpl getCodcFactory() {
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
    this.log.error("======Э���ʼ��======");
    ProxySet.proxy_initialize(fileName);
    proxyList = ProxySet.getProxyList();
    protocolMap = ProxySet.getProtocolmap();
    this.adapter = new MinaHandlerAdapter(proxyList);
    this.log.error("======Э���ʼ���ɹ�======");
  }

  protected void initialize(int port, String fileName) throws Exception {
    try {
      if (!(bindPort(port))) {
        throw new Exception("======����˿�:" + port + "��ռ��  ======");
      }

      initProxy(fileName);

      NioSocketAcceptor acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);

      DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

      this.codcFactory = new ProtocolCodecFactoryImpl(protocolMap);
      chain.addLast("codec", new ProtocolCodecFilter(this.codcFactory));

      if (!(this.singleThread))
        chain.addLast("threadPool", new ExecutorFilter(this.FILTER_EXECUTOR));
      else {
        chain.addLast("threadPool", new ExecutorFilter(Executors.newSingleThreadExecutor()));
      }

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
      this.log.error("======����˿�:" + port + "�ɹ�======");
    } catch (IOException e) {
      this.log.error("======���紴��ʧ��======");
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

  protected void stop() {
    if (this.acceptor != null) {
      this.acceptor.unbind();
      this.acceptor.dispose();
      this.acceptor = null;
    }

    if (this.FILTER_EXECUTOR != null) {
      this.FILTER_EXECUTOR.shutdown();
      try {
        this.FILTER_EXECUTOR.awaitTermination(5000L, TimeUnit.MILLISECONDS);
      }
      catch (InterruptedException e) {
        this.log.error("ͣ���׳����쳣", e);
      }
    }
  }

  static class NamedThreadFactory implements ThreadFactory {
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public NamedThreadFactory(ThreadGroup group, String name) {
      this.group = group;
      this.namePrefix = group.getName() + ":" + name;
    }

    public Thread newThread(Runnable r) {
      Thread thread = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0L);
      return thread;
    }
  }
}