/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.core.service;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.session.IoSessionConfig;

public abstract class AbstractIoAcceptor extends AbstractIoService
  implements IoAcceptor
{
  private final List<SocketAddress> defaultLocalAddresses = new ArrayList();

  private final List<SocketAddress> unmodifiableDefaultLocalAddresses = Collections.unmodifiableList(this.defaultLocalAddresses);

  private final Set<SocketAddress> boundAddresses = new HashSet();

  private boolean disconnectOnUnbind = true;

  protected final Object bindLock = new Object();

  protected AbstractIoAcceptor(IoSessionConfig sessionConfig, Executor executor)
  {
    super(sessionConfig, executor);
    this.defaultLocalAddresses.add(null);
  }

  public SocketAddress getLocalAddress()
  {
    Set localAddresses = getLocalAddresses();
    if (localAddresses.isEmpty()) {
      return null;
    }

    return ((SocketAddress)localAddresses.iterator().next());
  }

  public final Set<SocketAddress> getLocalAddresses()
  {
    Set localAddresses = new HashSet();

    synchronized (this.boundAddresses) {
      localAddresses.addAll(this.boundAddresses);
    }

    return localAddresses;
  }

  public SocketAddress getDefaultLocalAddress()
  {
    if (this.defaultLocalAddresses.isEmpty()) {
      return null;
    }
    return ((SocketAddress)this.defaultLocalAddresses.iterator().next());
  }

  public final void setDefaultLocalAddress(SocketAddress localAddress)
  {
    setDefaultLocalAddresses(localAddress, new SocketAddress[0]);
  }

  public final List<SocketAddress> getDefaultLocalAddresses()
  {
    return this.unmodifiableDefaultLocalAddresses;
  }

  public final void setDefaultLocalAddresses(List<? extends SocketAddress> localAddresses)
  {
    if (localAddresses == null) {
      throw new IllegalArgumentException("localAddresses");
    }
    setDefaultLocalAddresses(localAddresses);
  }

  public final void setDefaultLocalAddresses(Iterable<? extends SocketAddress> localAddresses)
  {
    if (localAddresses == null) {
      throw new IllegalArgumentException("localAddresses");
    }

    synchronized (this.bindLock) {
      synchronized (this.boundAddresses) {
        if (!(this.boundAddresses.isEmpty())) {
          throw new IllegalStateException("localAddress can't be set while the acceptor is bound.");
        }

        Collection newLocalAddresses = new ArrayList();

        for (SocketAddress a : localAddresses) {
          checkAddressType(a);
          newLocalAddresses.add(a);
        }

        if (newLocalAddresses.isEmpty()) {
          throw new IllegalArgumentException("empty localAddresses");
        }

        this.defaultLocalAddresses.clear();
        this.defaultLocalAddresses.addAll(newLocalAddresses);
      }
    }
  }

  public final void setDefaultLocalAddresses(SocketAddress firstLocalAddress, SocketAddress[] otherLocalAddresses)
  {
    if (otherLocalAddresses == null) {
      otherLocalAddresses = new SocketAddress[0];
    }

    Collection newLocalAddresses = new ArrayList(otherLocalAddresses.length + 1);

    newLocalAddresses.add(firstLocalAddress);
    for (SocketAddress a : otherLocalAddresses) {
      newLocalAddresses.add(a);
    }

    setDefaultLocalAddresses(newLocalAddresses);
  }

  public final boolean isCloseOnDeactivation()
  {
    return this.disconnectOnUnbind;
  }

  public final void setCloseOnDeactivation(boolean disconnectClientsOnUnbind)
  {
    this.disconnectOnUnbind = disconnectClientsOnUnbind;
  }

  public final void bind()
    throws IOException
  {
    bind(getDefaultLocalAddresses());
  }

  public final void bind(SocketAddress localAddress)
    throws IOException
  {
    if (localAddress == null) {
      throw new IllegalArgumentException("localAddress");
    }

    List localAddresses = new ArrayList(1);
    localAddresses.add(localAddress);
    bind(localAddresses);
  }

  public final void bind(SocketAddress[] addresses)
    throws IOException
  {
    if ((addresses == null) || (addresses.length == 0)) {
      bind(getDefaultLocalAddresses());
      return;
    }

    List localAddresses = new ArrayList(2);

    for (SocketAddress address : addresses) {
      localAddresses.add(address);
    }

    bind(localAddresses);
  }

  public final void bind(SocketAddress firstLocalAddress, SocketAddress[] addresses)
    throws IOException
  {
    if (firstLocalAddress == null) {
      bind(getDefaultLocalAddresses());
    }

    if ((addresses == null) || (addresses.length == 0)) {
      bind(getDefaultLocalAddresses());
      return;
    }

    List localAddresses = new ArrayList(2);
    localAddresses.add(firstLocalAddress);

    for (SocketAddress address : addresses) {
      localAddresses.add(address);
    }

    bind(localAddresses);
  }

  public final void bind(Iterable<? extends SocketAddress> localAddresses)
    throws IOException
  {
    if (isDisposing()) {
      throw new IllegalStateException("Already disposed.");
    }

    if (localAddresses == null) {
      throw new IllegalArgumentException("localAddresses");
    }

    List localAddressesCopy = new ArrayList();

    for (SocketAddress a : localAddresses) {
      checkAddressType(a);
      localAddressesCopy.add(a);
    }

    if (localAddressesCopy.isEmpty()) {
      throw new IllegalArgumentException("localAddresses is empty.");
    }

    boolean activate = false;
    synchronized (this.bindLock) {
      synchronized (this.boundAddresses) {
        if (this.boundAddresses.isEmpty()) {
          activate = true;
        }
      }

      if (getHandler() == null) {
        throw new IllegalStateException("handler is not set.");
      }
      try
      {
        Set addresses = bindInternal(localAddressesCopy);

        synchronized (this.boundAddresses) {
          this.boundAddresses.addAll(addresses);
        }
      } catch (IOException e) {
        throw e;
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeIoException("Failed to bind to: " + getLocalAddresses(), e);
      }
    }

    if (activate)
      getListeners().fireServiceActivated();
  }

  public final void unbind()
  {
    unbind(getLocalAddresses());
  }

  public final void unbind(SocketAddress localAddress)
  {
    if (localAddress == null) {
      throw new IllegalArgumentException("localAddress");
    }

    List localAddresses = new ArrayList(1);
    localAddresses.add(localAddress);
    unbind(localAddresses);
  }

  public final void unbind(SocketAddress firstLocalAddress, SocketAddress[] otherLocalAddresses)
  {
    if (firstLocalAddress == null) {
      throw new IllegalArgumentException("firstLocalAddress");
    }
    if (otherLocalAddresses == null) {
      throw new IllegalArgumentException("otherLocalAddresses");
    }

    List localAddresses = new ArrayList();
    localAddresses.add(firstLocalAddress);
    Collections.addAll(localAddresses, otherLocalAddresses);
    unbind(localAddresses);
  }

  public final void unbind(Iterable<? extends SocketAddress> localAddresses)
  {
    if (localAddresses == null) {
      throw new IllegalArgumentException("localAddresses");
    }

    boolean deactivate = false;
    synchronized (this.bindLock) {
      synchronized (this.boundAddresses) {
        if (this.boundAddresses.isEmpty()) {
           return;
        }

        List localAddressesCopy = new ArrayList();
        int specifiedAddressCount = 0;

        for (SocketAddress a : localAddresses) {
          ++specifiedAddressCount;

          if ((a != null) && (this.boundAddresses.contains(a))) {
            localAddressesCopy.add(a);
          }
        }

        if (specifiedAddressCount == 0) {
          throw new IllegalArgumentException("localAddresses is empty.");
        }

        if (!(localAddressesCopy.isEmpty())) {
          try {
            unbind0(localAddressesCopy);
          } catch (RuntimeException e) {
            throw e;
          } catch (Exception e) {
            throw new RuntimeIoException("Failed to unbind from: " + getLocalAddresses(), e);
          }

          this.boundAddresses.removeAll(localAddressesCopy);

          if (this.boundAddresses.isEmpty()) {
            deactivate = true;
          }
        }
      }
    }

    if (deactivate)
      getListeners().fireServiceDeactivated();
  }

  protected abstract Set<SocketAddress> bindInternal(List<? extends SocketAddress> paramList)
    throws Exception;

  protected abstract void unbind0(List<? extends SocketAddress> paramList)
    throws Exception;

  public String toString()
  {
    TransportMetadata m = getTransportMetadata();
    return '(' + m.getProviderName() + ' ' + m.getName() + " acceptor: " + ((isActive()) ? "localAddress(es): " + getLocalAddresses() + ", managedSessionCount: " + getManagedSessionCount() : "not bound") + ')';
  }

  private void checkAddressType(SocketAddress a) {
    if ((a != null) && (!(getTransportMetadata().getAddressType().isAssignableFrom(a.getClass()))))
      throw new IllegalArgumentException("localAddress type: " + a.getClass().getSimpleName() + " (expected: " + getTransportMetadata().getAddressType().getSimpleName() + ")");
  }

  public static class AcceptorOperationFuture extends AbstractIoService.ServiceOperationFuture
  {
    private final List<SocketAddress> localAddresses;

    public AcceptorOperationFuture(List<? extends SocketAddress> localAddresses) {
      this.localAddresses = new ArrayList(localAddresses);
    }

    public final List<SocketAddress> getLocalAddresses() {
      return Collections.unmodifiableList(this.localAddresses);
    }

    public String toString()
    {
      StringBuilder sb = new StringBuilder();

      sb.append("Acceptor operation : ");

      if (this.localAddresses != null) {
        boolean isFirst = true;

        for (SocketAddress address : this.localAddresses) {
          if (isFirst)
            isFirst = false;
          else {
            sb.append(", ");
          }

          sb.append(address);
        }
      }
      return sb.toString();
    }
  }
}