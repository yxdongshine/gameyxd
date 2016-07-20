/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.google.protobuf.Message;
import com.loncent.protocol.BaseMessage;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaProtocolCodec
  implements ICodec<String, Integer>
{
  private static final Logger log = LoggerFactory.getLogger(MinaProtocolCodec.class);

  public IoBuffer encode(Message msg, Map<String, Integer> map) {
    Integer head = (Integer)map.get(msg.getDescriptorForType().getName());
    if (head != null) {
      BaseMessage.MinaMessage enMsg = BaseMessage.MinaMessage.newBuilder().setCommand(head.intValue()).setBody(msg.toByteString()).build();
      byte[] data = enMsg.toByteArray();
      IoBuffer buffer = IoBuffer.allocate(data.length + 4);
      buffer.putInt(data.length);
      buffer.put(data);
      buffer.flip();
      log.info("send-*--local to other:::" + Integer.toHexString(head.intValue()));
      return buffer;
    }

    log.info("not have head��" + msg.getDescriptorForType().getName());

    return null;
  }

  public BaseMessage.MinaMessage decode(byte[] data) throws Exception {
    return BaseMessage.MinaMessage.parseFrom(data);
  }
}