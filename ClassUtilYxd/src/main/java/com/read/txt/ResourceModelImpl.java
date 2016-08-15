/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package com.read.txt;

import com.read.tool.txt.TxtKey;
import com.read.tool.txt.TxtLoader;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ResourceModelImpl<T> extends Dictionary<T> implements
		ResourceModel<T> {
	protected Log log = LogFactory.getLog(super.getClass());
	private static final long serialVersionUID = 5716704764378187037L;
	protected String resFile;
	protected Class<T> _class;
	protected TxtParam xlsKey;
	private CopyOnWriteArrayList<T> reslList;

	public ResourceModelImpl() {
	}

	public ResourceModelImpl(String resFile, Class<T> _class) {
		this.resFile = resFile;
		this._class = _class;
		Field[] arrayOfField;
		int j = (arrayOfField = _class.getDeclaredFields()).length;
		for (int i = 0; i < j; ++i) {
			Field field = arrayOfField[i];

			Annotation[] anos = field.getDeclaredAnnotations();
			Annotation[] arrayOfAnnotation1;
			int l = (arrayOfAnnotation1 = anos).length;
			for (int k = 0; k < l; ++k) {
				Annotation annotation = arrayOfAnnotation1[k];
				TxtParam param = new TxtParam();
				param.setField(field);
				param.setName(field.getName());
				if (annotation instanceof TxtKey)
					this.xlsKey = param;
			}
		}
		try {
			load();
			printLog();
		} catch (Exception e) {
			e.printStackTrace();
			this.log.error(e, e);
		}
	}

	public abstract void printLog();

	public ResourceModelImpl(String resFile, Class<T> _class,
			boolean keyFromZero) throws Exception {
		load(keyFromZero);
	}

	public T[] asArray() {
		return asArray(this.xlsKey.getName());
	}

	protected T[] asArray(String field) {
		List list = sort(field);
		Object[] valueArrays = (Object[]) Array.newInstance(this._class,
				list.size());
		for (int i = 0; i < list.size(); ++i) {
			valueArrays[i] = list.get(i);
		}
		return (T[]) valueArrays;
	}

	public T get(int resID) {
		return lookupEntry(resID);
	}

	protected int getKeyValue(T value) {
		if (this.xlsKey == null)
			return 0;
		return getValue(value, this.xlsKey.getField().getName());
	}

	protected String getName(T value) {
		try {
			Method method = this._class.getMethod("getName", new Class[0]);
			String name = (String) method.invoke(value, new Object[0]);
			return name;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected int getValue(T value, String property) {
		try {
			String methodName = property;
			methodName = methodName.substring(0, 1).toUpperCase()
					+ methodName.substring(1, methodName.length());
			methodName = "get" + methodName;
			Method method = this._class.getMethod(methodName, new Class[0]);
			Integer key = (Integer) method.invoke(value, new Object[0]);
			return key.intValue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public void load() throws Exception {
		load(false);
	}

	@SuppressWarnings("unchecked")
	protected void load(boolean keyFromZero) throws Exception {
		reset();

		TxtLoader loader = new TxtLoader(this.resFile, this._class);
		Map stages = loader.load(keyFromZero);
		this.reslList = loader.getList();
		for (Iterator iterator = stages.entrySet().iterator(); iterator.hasNext();) {
			Entry entry = (Entry) iterator.next();
			Object value = entry.getValue();
			Integer key = Integer.valueOf(getKeyValue((T) value));
			String name = null;
			if (name == null) {
				name = key+"";
			}
			addEntry(key.intValue(), name+"", (T) value);
		}
	}

	public void print() {
		for (Object value : sort()) {
			System.out.println("����[" + this._class.getSimpleName() + "] "
					+ value);
		}
	}

	protected List<T> sort() {
		List result = iterate();
		Collections.sort(result, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		return result;
	}

	protected List<T> sort(String field) {
		List result = iterate();
		Collections.sort(result, null);
		return result;
	}

	protected List<T> sortDESC(String field) {
		List result = iterate();
		Collections.sort(result,null);
		return result;
	}

	public int length() {
		return countEntries();
	}

	public CopyOnWriteArrayList<T> getReslList() {
		return this.reslList;
	}

	public void setReslList(CopyOnWriteArrayList<T> reslList) {
		this.reslList = reslList;
	}
}