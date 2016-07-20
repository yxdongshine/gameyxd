/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.transport.socket.nio;

import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import org.apache.mina.core.filterchain.DefaultIoFilterChain;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.service.IoProcessor;
import org.apache.mina.core.service.IoService;
import org.apache.mina.core.session.AbstractIoSession;

public abstract class NioSession extends AbstractIoSession
{
  protected final IoProcessor<NioSession> processor;
  protected final Channel channel;
  protected SelectionKey key;
  private final IoFilterChain filterChain;

  protected NioSession(IoProcessor<NioSession> processor, IoService service, Channel channel)
  {
    super(service);
    this.channel = channel;
    this.processor = processor;
    this.filterChain = new DefaultIoFilterChain(this);
  }

  abstract ByteChannel getChannel();

  public IoFilterChain getFilterChain()
  {
    return this.filterChain;
  }

  SelectionKey getSelectionKey()
  {
    return this.key;
  }

  void setSelectionKey(SelectionKey key)
  {
    this.key = key;
  }

  public IoProcessor<NioSession> getProcessor()
  {
    return this.processor;
  }
}