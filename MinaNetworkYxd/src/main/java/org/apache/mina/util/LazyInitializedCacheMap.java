/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package org.apache.mina.util;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LazyInitializedCacheMap<K, V>
  implements Map<K, V>
{
  private ConcurrentMap<K, LazyInitializer<V>> cache;

  public LazyInitializedCacheMap()
  {
    this.cache = new ConcurrentHashMap();
  }

  public LazyInitializedCacheMap(ConcurrentHashMap<K, LazyInitializer<V>> map)
  {
    this.cache = map;
  }

  public V get(Object key)
  {
    LazyInitializer c = (LazyInitializer)this.cache.get(key);
    if (c != null) {
      return (V) c.get();
    }

    return null;
  }

  public V remove(Object key)
  {
    LazyInitializer c = (LazyInitializer)this.cache.remove(key);
    if (c != null) {
      return (V) c.get();
    }

    return null;
  }

  public V putIfAbsent(K key, LazyInitializer<V> value)
  {
    LazyInitializer v = (LazyInitializer)this.cache.get(key);
    if (v == null) {
      v = (LazyInitializer)this.cache.putIfAbsent(key, value);
      if (v == null) {
        return value.get();
      }
    }

    return (V) v.get();
  }

  public V put(K key, V value)
  {
    LazyInitializer c = (LazyInitializer)this.cache.put(key, new NoopInitializer());
    if (c != null) {
      return (V) c.get();
    }

    return null;
  }

  public boolean containsValue(Object value)
  {
    throw new UnsupportedOperationException();
  }

  public Collection<V> values()
  {
    throw new UnsupportedOperationException();
  }

  public Set<Map.Entry<K, V>> entrySet()
  {
    throw new UnsupportedOperationException();
  }

  public void putAll(Map<? extends K, ? extends V> m)
  {
    for (Map.Entry e : m.entrySet())
      this.cache.put((K) e.getKey(), new NoopInitializer());
  }

  public Collection<LazyInitializer<V>> getValues()
  {
    return this.cache.values();
  }

  public void clear()
  {
    this.cache.clear();
  }

  public boolean containsKey(Object key)
  {
    return this.cache.containsKey(key);
  }

  public boolean isEmpty()
  {
    return this.cache.isEmpty();
  }

  public Set<K> keySet()
  {
    return this.cache.keySet();
  }

  public int size()
  {
    return this.cache.size();
  }

  public class NoopInitializer extends LazyInitializer<V>
  {
    private V value;

    public NoopInitializer()
    {
      this.value = value;
    }

    public V init() {
      return this.value;
    }
  }
}