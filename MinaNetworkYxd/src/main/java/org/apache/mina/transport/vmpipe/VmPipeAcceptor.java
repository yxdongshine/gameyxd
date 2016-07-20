/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.vmpipe;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.service.AbstractIoAcceptor;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IdleStatusChecker;
import org.apache.mina.core.session.IdleStatusChecker.NotifyingTask;
import org.apache.mina.core.session.IoSession;

public final class VmPipeAcceptor extends AbstractIoAcceptor
{
  private IdleStatusChecker idleChecker;
  static final Map<VmPipeAddress, VmPipe> boundHandlers = new HashMap();

  public VmPipeAcceptor()
  {
    this(null);
  }

  public VmPipeAcceptor(Executor executor)
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

  public VmPipeAddress getLocalAddress()
  {
    return ((VmPipeAddress)super.getLocalAddress());
  }

  public VmPipeAddress getDefaultLocalAddress()
  {
    return ((VmPipeAddress)super.getDefaultLocalAddress());
  }

  public void setDefaultLocalAddress(VmPipeAddress localAddress)
  {
    super.setDefaultLocalAddress(localAddress);
  }

  protected void dispose0()
    throws Exception
  {
    this.idleChecker.getNotifyingTask().cancel();
    unbind();
  }

  protected Set<SocketAddress> bindInternal(List<? extends SocketAddress> localAddresses) throws IOException
  {
    Set newLocalAddresses = new HashSet();

    synchronized (boundHandlers)
    {
      VmPipeAddress newLocalAddress;
      for (SocketAddress a : localAddresses) {
        VmPipeAddress localAddress = (VmPipeAddress)a;
        if ((localAddress == null) || (localAddress.getPort() == 0)) {
          localAddress = null;
          for (int i = 10000; i < 2147483647; ++i) {
            newLocalAddress = new VmPipeAddress(i);
            if ((!(boundHandlers.containsKey(newLocalAddress))) && (!(newLocalAddresses.contains(newLocalAddress)))) {
              localAddress = newLocalAddress;
              break;
            }
          }

          if (localAddress == null)
            throw new IOException("No port available.");
        } else {
          if (localAddress.getPort() < 0)
            throw new IOException("Bind port number must be 0 or above.");
          if (boundHandlers.containsKey(localAddress)) {
            throw new IOException("Address already bound: " + localAddress);
          }
        }
        newLocalAddresses.add(localAddress);
      }

      for (Iterator iterator = newLocalAddresses.iterator(); iterator.hasNext();) {
		Object a =  iterator.next();
        VmPipeAddress localAddress = (VmPipeAddress)a;
        if (!(boundHandlers.containsKey(localAddress))) {
          boundHandlers.put(localAddress, new VmPipe(this, localAddress, getHandler(), getListeners()));
        } else {
            for (Iterator iterator1 = newLocalAddresses.iterator(); iterator1.hasNext();) {
        		Object a2 =  iterator1.next();
                boundHandlers.remove(a2);
          }
          throw new IOException("Duplicate local address: " + a);
        }
      }
    }

    return newLocalAddresses;
  }

  protected void unbind0(List<? extends SocketAddress> localAddresses)
  {
    synchronized (boundHandlers) {
      for (SocketAddress a : localAddresses)
        boundHandlers.remove(a);
    }
  }

  public IoSession newSession(SocketAddress remoteAddress, SocketAddress localAddress)
  {
    throw new UnsupportedOperationException();
  }

  void doFinishSessionInitialization(IoSession session, IoFuture future) {
    initSession(session, future, null);
  }
}