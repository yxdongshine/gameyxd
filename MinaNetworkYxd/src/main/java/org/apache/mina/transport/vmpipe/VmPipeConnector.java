/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.vmpipe;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChainBuilder;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.DefaultConnectFuture;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.AbstractIoConnector;
import org.apache.mina.core.service.IoServiceListenerSupport;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatusChecker;
import org.apache.mina.core.session.IdleStatusChecker.NotifyingTask;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.session.IoSessionInitializer;
import org.apache.mina.util.ExceptionMonitor;

public final class VmPipeConnector extends AbstractIoConnector
{
  private IdleStatusChecker idleChecker;
  private static final Set<VmPipeAddress> TAKEN_LOCAL_ADDRESSES = new HashSet();

  private static int nextLocalPort = -1;

  private static final IoFutureListener<IoFuture> LOCAL_ADDRESS_RECLAIMER = new LocalAddressReclaimer();

  public VmPipeConnector()
  {
    this(null);
  }

  public VmPipeConnector(Executor executor)
  {
    super(new DefaultVmPipeSessionConfig(), executor);
    this.idleChecker = new IdleStatusChecker();

    executeWorker(this.idleChecker.getNotifyingTask(), "idleStatusChecker");
  }

  public TransportMetadata getTransportMetadata() {
    return VmPipeSession.METADATA;
  }

  public VmPipeSessionConfig getSessionConfig()
  {
    return ((VmPipeSessionConfig)this.sessionConfig);
  }

  protected ConnectFuture connect0(SocketAddress remoteAddress, SocketAddress localAddress, IoSessionInitializer<? extends ConnectFuture> sessionInitializer)
  {
    VmPipe entry = (VmPipe)VmPipeAcceptor.boundHandlers.get(remoteAddress);
    if (entry == null) {
      return DefaultConnectFuture.newFailedFuture(new IOException("Endpoint unavailable: " + remoteAddress));
    }

    DefaultConnectFuture future = new DefaultConnectFuture();
    VmPipeAddress actualLocalAddress;
    try
    {
      actualLocalAddress = nextLocalAddress();
    } catch (IOException e) {
      return DefaultConnectFuture.newFailedFuture(e);
    }
    VmPipeSession localSession = new VmPipeSession(this, getListeners(), actualLocalAddress, getHandler(), entry);

    initSession(localSession, future, sessionInitializer);

    localSession.getCloseFuture().addListener(LOCAL_ADDRESS_RECLAIMER);
    try
    {
      IoFilterChain filterChain = localSession.getFilterChain();
      getFilterChainBuilder().buildFilterChain(filterChain);

      getListeners().fireSessionCreated(localSession);
      this.idleChecker.addSession(localSession);
    } catch (Exception e) {
      future.setException(e);
      return future;
    }

    VmPipeSession remoteSession = localSession.getRemoteSession();
    ((VmPipeAcceptor)remoteSession.getService()).doFinishSessionInitialization(remoteSession, null);
    try {
      IoFilterChain filterChain = remoteSession.getFilterChain();
      entry.getAcceptor().getFilterChainBuilder().buildFilterChain(filterChain);

      entry.getListeners().fireSessionCreated(remoteSession);
      this.idleChecker.addSession(remoteSession);
    } catch (Exception e) {
      ExceptionMonitor.getInstance().exceptionCaught(e);
      remoteSession.close(true);
    }

    ((VmPipeFilterChain)localSession.getFilterChain()).start();
    ((VmPipeFilterChain)remoteSession.getFilterChain()).start();

    return future;
  }

  protected void dispose0()
    throws Exception
  {
    this.idleChecker.getNotifyingTask().cancel();
  }

  private static VmPipeAddress nextLocalAddress()
    throws IOException
  {
    synchronized (TAKEN_LOCAL_ADDRESSES) {
      if (nextLocalPort >= 0) {
        nextLocalPort = -1;
      }
      for (int i = 0; i < 2147483647; ++i) {
        VmPipeAddress answer = new VmPipeAddress(nextLocalPort--);
        if (!(TAKEN_LOCAL_ADDRESSES.contains(answer))) {
          TAKEN_LOCAL_ADDRESSES.add(answer);
          return answer;
        }
      }
    }

    throw new IOException("Can't assign a local VM pipe port.");
  }

  private static class LocalAddressReclaimer implements IoFutureListener<IoFuture> {
    public void operationComplete(IoFuture future) {
      synchronized (VmPipeConnector.TAKEN_LOCAL_ADDRESSES) {
        VmPipeConnector.TAKEN_LOCAL_ADDRESSES.remove(future.getSession().getLocalAddress());
      }
    }
  }
}