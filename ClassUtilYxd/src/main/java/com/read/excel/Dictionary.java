/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Dictionary<T>
{
  static final String ADD_ENTRY_KEYEXIST = "������Ŀʧ��:Ŀ��KEYֵ�Ѵ���!";
  static final String ADD_ENTRY_LINKEXIST = "������Ŀʧ��:Ŀ����Ǵ��Ѵ���!";
  static final String REMOVE_ENTRY_KEYNOTEXIST = "�Ƴ���Ŀʧ��:Ŀ��KEYֵ������!";
  private AtomicInteger entries = new AtomicInteger(0);
  private ConcurrentHashMap<Long, T> entryMap = new ConcurrentHashMap();
  private ConcurrentHashMap<String, Long> linkMap = new ConcurrentHashMap();

  private Queue<T> queue = new ConcurrentLinkedQueue();
  private boolean checkName = true;

  public boolean addEntry(int key, String mnemonic, T entry)
  {
    return addEntry(key, mnemonic, entry);
  }

  public boolean addEntry(long key, String mnemonic, T entry)
  {
    Long _key = Long.valueOf(key);
    if (this.entryMap.containsKey(_key)) {
      System.err.println("������Ŀʧ��:Ŀ��KEYֵ�Ѵ���! key=" + key + 
        ",mnemonic=" + mnemonic + ",T=" + entry);
      return false;
    }

    this.entryMap.putIfAbsent(_key, entry);
    this.queue.offer(entry);
    if (this.checkName) {
      this.linkMap.putIfAbsent(mnemonic, _key);
    }
    this.entries.incrementAndGet();
    return true;
  }

  public int countEntries()
  {
    return this.entries.intValue();
  }

  public Set<Map.Entry<Long, T>> entrySet()
  {
    return this.entryMap.entrySet();
  }

  public int getIntKey(String mnemonic)
  {
    return (int)getKey(mnemonic);
  }

  public long getKey(String mnemonic)
  {
    if (hasMnemonic(mnemonic)) {
      return ((Long)this.linkMap.get(mnemonic)).longValue();
    }
    return -1L;
  }

  public boolean hasIntKey(int key)
  {
    return hasKey(key);
  }

  public boolean hasKey(long key)
  {
    Long _key = Long.valueOf(key);
    return this.entryMap.containsKey(_key);
  }

  public boolean hasMnemonic(String mnemonic)
  {
    return this.linkMap.containsKey(mnemonic);
  }

  public boolean isCheckName()
  {
    return this.checkName;
  }

  public List<T> iterate()
  {
    ArrayList tmp = new ArrayList();
    tmp.addAll(this.entryMap.values());
    return tmp;
  }

  public Queue<T> getQueue()
  {
    return this.queue;
  }

  public Set<Long> keySet()
  {
    return this.entryMap.keySet();
  }

  public T lookupEntry(int key)
  {
    return lookupEntry(key);
  }

  public T lookupEntry(long key)
  {
    Long _key = Long.valueOf(key);
    return this.entryMap.get(_key);
  }

  public T lookupEntry(String mnemonic)
  {
    if (hasMnemonic(mnemonic)) {
      Long key = (Long)this.linkMap.get(mnemonic);
      return lookupEntry(key.longValue());
    }
    return null;
  }

  public Set<String> mnemonicSet()
  {
    return this.linkMap.keySet();
  }

  public boolean removeEntry(int key)
  {
    return removeEntry(key);
  }

  public boolean removeEntry(long key)
  {
    Long _key = Long.valueOf(key);
    if (!(this.entryMap.containsKey(_key))) {
      System.err.println("�Ƴ���Ŀʧ��:Ŀ��KEYֵ������!");
      return false;
    }
    if ((this.entryMap.remove(_key) != null) && 
      (this.linkMap.containsValue(_key))) {
      for (Map.Entry entry : this.linkMap.entrySet()) {
        String linkKey = (String)entry.getKey();
        Long keyRef = (Long)entry.getValue();
        if (keyRef.longValue() == _key.longValue()) {
          this.linkMap.remove(linkKey);
          this.entries.decrementAndGet();

          return true;
        }
      }
    }

    return false;
  }

  public void reset()
  {
    this.entries.set(0);
    this.entryMap.clear();
    this.linkMap.clear();
    this.queue.clear();
  }

  public void setCheckName(boolean checkName)
  {
    this.checkName = checkName;
  }

  public void update(long index, T value)
  {
    if (hasKey(index))
      this.entryMap.replace(Long.valueOf(index), value);
  }

  public void update(String mnemonic, T value)
  {
    if (this.linkMap.containsKey(mnemonic)) {
      Long key = (Long)this.linkMap.get(mnemonic);
      update(key.longValue(), value);
    }
  }
}