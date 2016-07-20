/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerProtocolEncoder extends ProtocolEncoderAdapter
{
  private ProtocolCodec _codec;
  private Map<String, Integer> map;
  private static final Logger log = LoggerFactory.getLogger(InnerProtocolEncoder.class);

  public InnerProtocolEncoder(ProtocolCodec codec, Map<String, Integer> _map) {
    this._codec = codec;
    this.map = _map;
  }

  public void encode(IoSession session, Object obj, ProtocolEncoderOutput out) throws Exception {
    if (obj instanceof Message) {
      Message msg = (Message)obj;
      IoBuffer io = this._codec.encode(msg, this.map);
      if (io != null)
      {
        out.write(io);
        log.info("send msg ok !!!!!!!!!!" + msg.getDescriptorForType().getName());
      }
      io = null;
    } else {
      out.write(obj);
      log.info("inner msg ok !!!!!!!!!!");
    }
  }
}