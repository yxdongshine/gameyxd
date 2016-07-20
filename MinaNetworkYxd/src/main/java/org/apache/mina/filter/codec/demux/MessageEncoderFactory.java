/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.filter.codec.demux;

public abstract interface MessageEncoderFactory<T>
{
  public abstract MessageEncoder<T> getEncoder()
    throws Exception;
}