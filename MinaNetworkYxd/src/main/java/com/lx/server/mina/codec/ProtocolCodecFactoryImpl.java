/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ProtocolCodecFactoryImpl
  implements ProtocolCodecFactory
{
  private final MinaProtocolEncoder encoder;
  private final MinaProtocolDecoder decoder;
  private MinaProtocolCodec codec = new MinaProtocolCodec();

  public ProtocolCodecFactoryImpl(Map<String, Integer> map)
  {
    this.encoder = new MinaProtocolEncoder(this.codec, map);
    this.decoder = new MinaProtocolDecoder(this.codec);
  }

  public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    return this.decoder;
  }

  public ProtocolEncoder getEncoder(IoSession session) throws Exception {
    return this.encoder;
  }
}