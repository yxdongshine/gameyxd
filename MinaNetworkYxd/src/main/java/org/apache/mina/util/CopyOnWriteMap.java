/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CopyOnWriteMap<K, V>
  implements Map<K, V>, Cloneable
{
  private volatile Map<K, V> internalMap;

  public CopyOnWriteMap()
  {
    this.internalMap = new HashMap();
  }

  public CopyOnWriteMap(int initialCapacity)
  {
    this.internalMap = new HashMap(initialCapacity);
  }

  public CopyOnWriteMap(Map<K, V> data)
  {
    this.internalMap = new HashMap(data);
  }

  public V put(K key, V value)
  {
    synchronized (this) {
      Map newMap = new HashMap(this.internalMap);
      Object val = newMap.put(key, value);
      this.internalMap = newMap;
      return (V) val;
    }
  }

  public V remove(Object key)
  {
    synchronized (this) {
      Map newMap = new HashMap(this.internalMap);
      Object val = newMap.remove(key);
      this.internalMap = newMap;
      return (V) val;
    }
  }

  public void putAll(Map<? extends K, ? extends V> newData)
  {
    synchronized (this) {
      Map newMap = new HashMap(this.internalMap);
      newMap.putAll(newData);
      this.internalMap = newMap;
    }
  }

  public void clear()
  {
    synchronized (this) {
      this.internalMap = new HashMap();
    }
  }

  public int size()
  {
    return this.internalMap.size();
  }

  public boolean isEmpty()
  {
    return this.internalMap.isEmpty();
  }

  public boolean containsKey(Object key)
  {
    return this.internalMap.containsKey(key);
  }

  public boolean containsValue(Object value)
  {
    return this.internalMap.containsValue(value);
  }

  public V get(Object key)
  {
    return this.internalMap.get(key);
  }

  public Set<K> keySet()
  {
    return this.internalMap.keySet();
  }

  public Collection<V> values()
  {
    return this.internalMap.values();
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    return this.internalMap.entrySet();
  }

  public Object clone()
  {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    }
  }
}