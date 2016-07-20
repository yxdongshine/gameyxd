/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util;

public abstract class LazyInitializer<V>
{
  private V value;

  public abstract V init();

  public V get()
  {
    if (this.value == null) {
      this.value = init();
    }

    return this.value;
  }
}