/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.mina.core.buffer.IoBuffer;

public abstract class AbstractProtocolEncoderOutput
  implements ProtocolEncoderOutput
{
  private final Queue<Object> messageQueue = new ConcurrentLinkedQueue();

  private boolean buffersOnly = true;

  public Queue<Object> getMessageQueue()
  {
    return this.messageQueue;
  }

  public void write(Object encodedMessage) {
    if (encodedMessage instanceof IoBuffer) {
      IoBuffer buf = (IoBuffer)encodedMessage;
      if (buf.hasRemaining()) {
        this.messageQueue.offer(buf); return;
      }
      throw new IllegalArgumentException("buf is empty. Forgot to call flip()?");
    }

    this.messageQueue.offer(encodedMessage);
    this.buffersOnly = false;
  }

  public void mergeAll()
  {
    if (!(this.buffersOnly)) {
      throw new IllegalStateException("the encoded message list contains a non-buffer.");
    }

    int size = this.messageQueue.size();

    if (size < 2)
    {
      return;
    }

    int sum = 0;
    for (Iterator localIterator = this.messageQueue.iterator(); localIterator.hasNext(); ) { Object b = localIterator.next();
      sum += ((IoBuffer)b).remaining();
    }

    IoBuffer newBuf = IoBuffer.allocate(sum);
    while (true)
    {
      IoBuffer buf = (IoBuffer)this.messageQueue.poll();
      if (buf == null) {
        break;
      }

      newBuf.put(buf);
    }

    newBuf.flip();
    this.messageQueue.add(newBuf);
  }
}