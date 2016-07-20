/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class InnerCodecFactoryImpl
  implements ProtocolCodecFactory
{
  private final InnerProtocolEncoder encoder;
  private final InnerProtocolDecoder decoder;
  private ProtocolCodec codec = new ProtocolCodec();

  public InnerCodecFactoryImpl(Map<String, Integer> map)
  {
    this.encoder = new InnerProtocolEncoder(this.codec, map);
    this.decoder = new InnerProtocolDecoder(this.codec);
  }

  public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    return this.decoder;
  }

  public ProtocolEncoder getEncoder(IoSession session) throws Exception {
    return this.encoder;
  }
}