/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractProtocolDecoderOutput
  implements ProtocolDecoderOutput
{
  private final Queue<Object> messageQueue = new LinkedList();

  public Queue<Object> getMessageQueue()
  {
    return this.messageQueue;
  }

  public void write(Object message) {
    if (message == null) {
      throw new IllegalArgumentException("message");
    }

    this.messageQueue.add(message);
  }
}