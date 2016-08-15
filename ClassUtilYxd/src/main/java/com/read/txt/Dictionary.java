/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.txt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Dictionary<T> {
	static final String ADD_ENTRY_KEYEXIST = "������Ŀʧ��:Ŀ��KEYֵ�Ѵ���!";
	static final String ADD_ENTRY_LINKEXIST = "������Ŀʧ��:Ŀ����Ǵ��Ѵ���!";
	static final String REMOVE_ENTRY_KEYNOTEXIST = "�Ƴ���Ŀʧ��:Ŀ��KEYֵ������!";
	private AtomicInteger entries = new AtomicInteger(0);
	private ConcurrentHashMap<Integer, T> entryMap = new ConcurrentHashMap();

	private boolean checkName = true;

	public boolean addEntry(int key, String mnemonic, T entry) {
		int _key = key;
		if (this.entryMap.containsKey(Integer.valueOf(_key))) {
			System.err.println("������Ŀʧ��:Ŀ��KEYֵ�Ѵ���! key=" + key + ",mnemonic="
					+ mnemonic + ",T=" + entry);
			return false;
		}

		this.entryMap.putIfAbsent(Integer.valueOf(_key), entry);

		this.entries.incrementAndGet();
		return true;
	}

	public int countEntries() {
		return this.entries.intValue();
	}

	public Set<Map.Entry<Integer, T>> entrySet() {
		return this.entryMap.entrySet();
	}

	public boolean hasIntKey(int key) {
		return hasKey(key);
	}

	public boolean hasKey(long key) {
		Long _key = Long.valueOf(key);
		return this.entryMap.containsKey(_key);
	}

	public List<T> iterate() {
		ArrayList tmp = new ArrayList();
		tmp.addAll(this.entryMap.values());
		return tmp;
	}

	public Set<Integer> keySet() {
		return this.entryMap.keySet();
	}

	public T lookupEntry(int key) {
		return this.entryMap.get(Integer.valueOf(key));
	}

	public boolean removeEntry(int key) {
		int _key = key;
		if (!(this.entryMap.containsKey(Integer.valueOf(_key)))) {
			System.err.println("�Ƴ���Ŀʧ��:Ŀ��KEYֵ������!");
			return false;
		}

		return (this.entryMap.remove(Integer.valueOf(_key)) != null);
	}

	public void reset() {
		this.entries.set(0);
		this.entryMap.clear();
	}

	public void setCheckName(boolean checkName) {
		this.checkName = checkName;
	}

	public void update(int index, T value) {
		if (hasKey(index))
			this.entryMap.replace(Integer.valueOf(index), value);
	}
}