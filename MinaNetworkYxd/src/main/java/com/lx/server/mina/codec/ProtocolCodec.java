/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;
import com.loncent.protocol.BaseMessage;
import com.loncent.protocol.BaseMessage.InnerMessage;
import com.loncent.protocol.BaseMessage.InnerMessage.Builder;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolCodec
  implements ICodec<String, Integer>
{
  private static final Logger log = LoggerFactory.getLogger(ProtocolCodec.class);

  public IoBuffer encode(Message msg, Map<String, Integer> map) {
    Integer head = (Integer)map.get(msg.getDescriptorForType().getName());
    if (head != null)
    {
      BaseMessage.InnerMessage enMsg = BaseMessage.InnerMessage.newBuilder().setCommand(head.intValue()).setBody(msg.toByteString()).build();
      byte[] data = enMsg.toByteArray();
      IoBuffer buffer = IoBuffer.allocate(data.length + 4);
      buffer.putInt(data.length);
      buffer.put(data);
      buffer.flip();
      return buffer;
    }
    log.info("not have head��" + msg.getDescriptorForType().getName());

    return null;
  }

  public BaseMessage.InnerMessage decode(byte[] data) throws Exception {
    return BaseMessage.InnerMessage.parseFrom(data);
  }
}