/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.demux;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public abstract interface MessageEncoder<T>
{
  public abstract void encode(IoSession paramIoSession, T paramT, ProtocolEncoderOutput paramProtocolEncoderOutput)
    throws Exception;
}