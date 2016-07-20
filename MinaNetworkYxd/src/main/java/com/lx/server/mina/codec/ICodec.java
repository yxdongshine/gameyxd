/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.lx.server.mina.codec;

import com.google.protobuf.Message;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;

public abstract interface ICodec<K, V>
{
  public abstract IoBuffer encode(Message paramMessage, Map<K, V> paramMap);

  public abstract Message decode(byte[] paramArrayOfByte)
    throws Exception;
}