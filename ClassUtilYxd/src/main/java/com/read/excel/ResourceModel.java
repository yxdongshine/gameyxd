/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.excel;

public abstract interface ResourceModel<T>
{
  public abstract void load()
    throws Exception;

  public abstract void print();

  public abstract T get(int paramInt);

  public abstract T get(String paramString);

  public abstract T[] asArray();
}