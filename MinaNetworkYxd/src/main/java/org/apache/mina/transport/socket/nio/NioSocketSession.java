/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket.nio;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.file.FileRegion;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.service.DefaultTransportMetadata;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.AbstractSocketSessionConfig;
import org.apache.mina.transport.socket.SocketSessionConfig;

class NioSocketSession extends NioSession
{
  static final TransportMetadata METADATA = new DefaultTransportMetadata("nio", "socket", false, true, InetSocketAddress.class, SocketSessionConfig.class, new Class[] { IoBuffer.class, FileRegion.class });

  private Socket getSocket() {
    return ((SocketChannel)this.channel).socket();
  }

  public NioSocketSession(IoService service, IoProcessor<NioSession> processor, SocketChannel channel)
  {
    super(processor, service, channel);
    this.config = new SessionConfigImpl();
    this.config.setAll(service.getSessionConfig());
  }

  public TransportMetadata getTransportMetadata() {
    return METADATA;
  }

  public SocketSessionConfig getConfig()
  {
    return ((SocketSessionConfig)this.config);
  }

  SocketChannel getChannel()
  {
    return ((SocketChannel)this.channel);
  }

  public InetSocketAddress getRemoteAddress()
  {
    if (this.channel == null) {
      return null;
    }

    Socket socket = getSocket();

    if (socket == null) {
      return null;
    }

    return ((InetSocketAddress)socket.getRemoteSocketAddress());
  }

  public InetSocketAddress getLocalAddress()
  {
    if (this.channel == null) {
      return null;
    }

    Socket socket = getSocket();

    if (socket == null) {
      return null;
    }

    return ((InetSocketAddress)socket.getLocalSocketAddress());
  }

  public InetSocketAddress getServiceAddress()
  {
    return ((InetSocketAddress)super.getServiceAddress());
  }

  public final boolean isSecured()
  {
    IoFilterChain chain = getFilterChain();

    IoFilter sslFilter = chain.get(SslFilter.class);

    if (sslFilter != null)
    {
      return ((SslFilter)sslFilter).isSslStarted(this);
    }
    return false;
  }

  private class SessionConfigImpl extends AbstractSocketSessionConfig
  {
    public boolean isKeepAlive()
    {
      try
      {
        return NioSocketSession.this.getSocket().getKeepAlive();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setKeepAlive(boolean on) {
      try {
        NioSocketSession.this.getSocket().setKeepAlive(on);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public boolean isOobInline() {
      try {
        return NioSocketSession.this.getSocket().getOOBInline();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setOobInline(boolean on) {
      try {
        NioSocketSession.this.getSocket().setOOBInline(on);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public boolean isReuseAddress() {
      try {
        return NioSocketSession.this.getSocket().getReuseAddress();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setReuseAddress(boolean on) {
      try {
        NioSocketSession.this.getSocket().setReuseAddress(on);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public int getSoLinger() {
      try {
        return NioSocketSession.this.getSocket().getSoLinger();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setSoLinger(int linger) {
      try {
        if (linger < 0) {
          NioSocketSession.this.getSocket().setSoLinger(false, 0); return;
        }
        NioSocketSession.this.getSocket().setSoLinger(true, linger);
      }
      catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public boolean isTcpNoDelay() {
      if (!(NioSocketSession.this.isConnected())) {
        return false;
      }
      try
      {
        return NioSocketSession.this.getSocket().getTcpNoDelay();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setTcpNoDelay(boolean on) {
      try {
        NioSocketSession.this.getSocket().setTcpNoDelay(on);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public int getTrafficClass()
    {
      try
      {
        return NioSocketSession.this.getSocket().getTrafficClass();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setTrafficClass(int tc)
    {
      try
      {
        NioSocketSession.this.getSocket().setTrafficClass(tc);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public int getSendBufferSize() {
      try {
        return NioSocketSession.this.getSocket().getSendBufferSize();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setSendBufferSize(int size) {
      try {
        NioSocketSession.this.getSocket().setSendBufferSize(size);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public int getReceiveBufferSize() {
      try {
        return NioSocketSession.this.getSocket().getReceiveBufferSize();
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }

    public void setReceiveBufferSize(int size) {
      try {
        NioSocketSession.this.getSocket().setReceiveBufferSize(size);
      } catch (SocketException e) {
        throw new RuntimeIoException(e);
      }
    }
  }
}